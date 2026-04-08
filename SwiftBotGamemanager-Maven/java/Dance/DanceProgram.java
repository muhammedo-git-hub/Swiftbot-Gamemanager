package Dance;

import swiftbot.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
 
/**
 * DanceProgram  (Main class)
 * CS1814 Software Implementation - Brunel University London
 *
 * Task 9: Dance
 *
 * Entry point and program orchestrator. Coordinates all other classes:
 *   InputValidator  - QR code parsing and hex validation   (FR2  - FR11)
 *   HexConverter    - Manual number system conversions     (FR12 - FR14)
 *   DanceExecutor   - SwiftBot movement and LED control    (FR15 - FR36)
 *   LogManager      - Session log file writing             (FR41 - FR44)
 *
 * This class handles:
 *   - SwiftBot API initialisation
 *   - Button registration (Y = continue, X = exit)
 *   - Main program loop  (FR1, FR29, FR37 - FR40)
 *   - QR code scanning   (FR1)
 *
 * Verified SwiftBot API (from SwiftBot-API-6_0_0.jar):
 *   new SwiftBotAPI()                                   - constructor
 *   enableButton(Button b, ButtonFunction f)            - button callback
 *   getQRImage()          -> BufferedImage              - camera capture
 *   decodeQRImage(img)    -> String                     - QR decode
 *   disableUnderlights()                                - all LEDs off
 *   stopMove()                                          - stop wheels
 *   disableAllButtons()                                 - unregister all
 *
 * Controls:
 *   Y button - scan another QR code / confirm continue
 *   X button - save sorted log file and terminate
 *
 * To compile and run on University Windows machine:
 *   javac -cp SwiftBot-API-6_0_0.jar *.java
 *   java  -cp .;SwiftBot-API-6_0_0.jar DanceProgram
 *
 * @author Mohammed Yahya Uddin (2520724)
 */
public class DanceProgram {
 
    // -------------------------------------------------------------------------
    // Shared state  (volatile for thread-safe button callbacks)
    // -------------------------------------------------------------------------
 
    /**
     * Reference to the SwiftBotAPI singleton enum instance.
     * Assigned via SwiftBotAPI.INSTANCE (never new SwiftBotAPI()).
     */
    private static SwiftBotAPI swiftBot;
 
    /**
     * All valid hex values entered across the entire session.
     * Collected here and passed to LogManager on exit (FR41).
     */
    private static final List<String> allHexList = new ArrayList<>();
 
    /** Set to true by the X button callback to signal program termination. */
    private static volatile boolean exitRequested = false;
 
    /** Set to true by the Y button callback to signal user confirmation. */
    private static volatile boolean continuePressed = false;
 
    /** Running count of completed dance routines, shown in log file. */
    private static int totalDancesExecuted = 0;
 
    // =========================================================================
    // ENTRY POINT
    // =========================================================================
 
    public static void main(String[] args) {
 
        // --- Initialise SwiftBot hardware -------------------------------------
        // SwiftBotAPI is an enum singleton - access via INSTANCE, never new SwiftBotAPI()
        try {
            swiftBot = SwiftBotAPI.INSTANCE;
        } catch (Exception e) {
            System.out.println("[ERROR] Could not access SwiftBot API: " + e.getMessage());
            System.out.println("        Check the SwiftBot is connected and the camera is enabled.");
            System.exit(1);
        }
 
        registerButtons(); // hook X (exit) and Y (continue)
        printWelcome();    // display instructions screen
 
        // ---- Main program loop -----------------------------------------------
        while (!exitRequested) {
 
            continuePressed = false;
 
            // FR1 - prompt user to scan a QR code or exit
            printDivider();
            System.out.println("  Press [Y] to scan a QR code   |   Press [X] to save & exit");
            printDivider();
 
            waitForYorX();
            if (exitRequested) break;
 
            // --- QR code scan (FR1) -------------------------------------------
            System.out.println("\n[SCANNING] Hold QR code steady in front of the camera...");
 
            String qrContent = null;
            try {
                BufferedImage qrImage = swiftBot.getQRImage();      // camera capture
                qrContent = swiftBot.decodeQRImage(qrImage);        // decode to text
            } catch (Exception e) {
                System.out.println("[ERROR] Could not read QR code: " + e.getMessage());
                System.out.println("        Please try again.");
                continue;
            }
 
            if (qrContent == null || qrContent.trim().isEmpty()) {
                System.out.println("[ERROR] QR code was empty or unreadable. Please try again.");
                continue;
            }
 
            System.out.println("[QR CODE SCANNED]: " + qrContent.trim());
 
            // --- Validate and parse (FR2 - FR11, delegates to InputValidator) -
            List<String> validHex = InputValidator.validateAndParse(qrContent.trim());
            if (validHex.isEmpty()) {
                System.out.println("[ERROR] No valid hex values found. Please scan a new QR code.");
                continue;
            }
 
            // --- Execute each dance routine in sequence (FR29) ----------------
            for (int i = 0; i < validHex.size(); i++) {
                String hex = validHex.get(i);
 
                printDivider();
                System.out.printf("  Processing Hex Value %d of %d: %s%n",
                        i + 1, validHex.size(), hex);
                printDivider();
 
                // Delegates to DanceExecutor for conversion, display, movement
                DanceExecutor.processDance(swiftBot, hex);
 
                allHexList.add(hex);   // FR41 - collect for log
                totalDancesExecuted++;
            }
 
            // Post-session summary (FR37)
            printDivider();
            System.out.println("  ALL DANCE ROUTINES COMPLETE");
            System.out.printf("  Total routines this session: %d%n", totalDancesExecuted);
            printDivider();
            System.out.println("  Press [Y] to scan another QR code");
            System.out.println("  Press [X] to save log and exit");
 
            waitForYorX();
        }
 
        // --- Program termination: save log and exit ---------------------------
        LogManager.saveLogAndExit(allHexList, totalDancesExecuted); // FR41-FR44
        shutdownSwiftBot();
    }
 
    // =========================================================================
    // BUTTON HANDLING   FR38, FR39, FR40
    // =========================================================================
 
    /**
     * Registers asynchronous button callbacks using SwiftBot's ButtonFunction
     * functional interface.
     *
     * Button.X -> sets exitRequested = true   (FR40 - press X to terminate)
     * Button.Y -> sets continuePressed = true (FR38 - press Y to continue)
     *
     * Callbacks run on a separate thread, hence volatile flags.
     */
    private static void registerButtons() {
        try {
            swiftBot.enableButton(Button.X, () -> exitRequested   = true); // FR40
            swiftBot.enableButton(Button.Y, () -> continuePressed = true); // FR38
        } catch (Exception e) {
            System.out.println("[WARN] Could not register buttons: " + e.getMessage());
        }
    }
 
    /**
     * Blocking wait until the user presses either Y (continue) or X (exit).
     * Polls every 100 ms to avoid busy-waiting on the CPU.
     */
    private static void waitForYorX() {
        continuePressed = false;
        while (!continuePressed && !exitRequested) {
            sleep(100);
        }
    }
 
    // =========================================================================
    // HARDWARE SHUTDOWN
    // =========================================================================
 
    /**
     * Safely shuts down all SwiftBot hardware before the program exits.
     * Wrapped in individual try-catch blocks so one failure does not
     * prevent the others from running.
     */
    private static void shutdownSwiftBot() {
        try { swiftBot.disableUnderlights(); } catch (Exception ignored) {}
        try { swiftBot.stopMove();           } catch (Exception ignored) {}
        try { swiftBot.disableAllButtons();  } catch (Exception ignored) {}
    }
 
    // =========================================================================
    // UTILITIES
    // =========================================================================
 
    /** Thread sleep wrapper - handles InterruptedException cleanly. */
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
 
    /** Prints a standard divider line for consistent UI formatting. */
    private static void printDivider() {
        System.out.println("########################################################");
    }
 
    // =========================================================================
    // WELCOME SCREEN   NFR45
    // =========================================================================
 
    /**
     * Displays the welcome screen with full usage instructions (NFR45).
     * Matches the improved UI design from Assignment 2.
     */
    private static void printWelcome() {
        System.out.println();
        printDivider();
        System.out.println("       *********SWIFTBOT DANCE PROGRAM*********         ");
        printDivider();
        System.out.println();
        System.out.println("  Welcome to the Swiftbot dance program!");
        System.out.println("  Let's make the Swiftbot dance!!!");
        System.out.println();
        System.out.println("  HOW TO USE:");
        System.out.println("    I)   Scan a QR code containing hex values (0-9, A-F).");
        System.out.println("    II)  The system validates and converts each value.");
        System.out.println("    III) SwiftBot sets its LED colour and performs the dance.");
        System.out.println("    IV)  Press [Y] to scan again or [X] to save log and exit.");
        System.out.println();
        System.out.println("  INPUT FORMAT:");
        System.out.println("    I)   1-2 hex digits per value (case insensitive)");
        System.out.println("    II)  Separate multiple values with '&'");
        System.out.println("    III) Maximum 5 values per QR code");
        System.out.println("    IV)  Example: 1F&2D&C8");
        System.out.println();
        System.out.println("  CONTROLS: [Y] Continue  |  [X] Save & Exit");
        printDivider();
        System.out.println("  Press [Y] to begin....");
        printDivider();
    }
}

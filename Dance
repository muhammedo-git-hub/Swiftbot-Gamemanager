import swiftbot.*;

/**
 * DanceExecutor
 * CS1814 Software Implementation - Brunel University London
 *
 * Handles the full dance pipeline for a single hex value (FR15 - FR36):
 *   - Calculate speed from octal equivalent        (FR15, FR16, FR17, FR18)
 *   - Calculate LED RGB colour from decimal        (FR19, FR20, FR21)
 *   - Determine movement duration from hex length  (FR26, FR27)
 *   - Print pre-movement display to console        (FR30, FR31, FR32, FR33, FR34, FR35)
 *   - Execute movements on SwiftBot hardware       (FR22, FR23, FR24, FR25, FR28, FR29)
 *   - Disable underlights after routine            (FR36)
 *
 * Verified SwiftBot API methods used:
 *   move(int leftVelocity, int rightVelocity, int movementTimeMs)  (III)V
 *   startMove(int leftVelocity, int rightVelocity)                 (II)V
 *   stopMove()                                                      ()V
 *   fillUnderlights(int[] rgb)                                      ([I)V
 *   disableUnderlights()                                            ()V
 *
 * @author Mohammed Yahya Uddin (2520724)
 */
public class DanceExecutor {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** SwiftBot hardware wheel speed maximum (FR18). */
    private static final int MAX_WHEEL_SPEED = 100;

    /**
     * Duration of a spin step in milliseconds (FR25).
     * Tuned for approximately 180 degrees at mid-range speed.
     * Adjust after physical calibration with your specific SwiftBot.
     */
    private static final int SPIN_DURATION_MS = 600;

    /** Brief pause between each movement step in milliseconds. */
    private static final int STEP_PAUSE_MS = 300;

    // Private constructor - utility class, not instantiated
    private DanceExecutor() {}

    // =========================================================================
    // Main pipeline entry point
    // =========================================================================

    /**
     * Orchestrates the full pipeline for one hex value:
     *   1. Convert using HexConverter (FR12, FR13, FR14)
     *   2. Calculate dance parameters (FR15-FR21)
     *   3. Print pre-movement display (FR30-FR35)
     *   4. Execute routine on hardware (FR22-FR29, FR36)
     *
     * @param swiftBot  initialised SwiftBotAPI instance
     * @param hex       validated upper-cased 1-2 char hex string
     */
    public static void processDance(SwiftBotAPI swiftBot, String hex) {

        // Step 1 - Convert hex to all number systems (delegates to HexConverter)
        int    decimal   = HexConverter.hexToDecimal(hex);         // FR12
        String octalStr  = HexConverter.decimalToOctal(decimal);   // FR14
        String binaryStr = HexConverter.decimalToBinary(decimal);  // FR13
        int    octalInt  = Integer.parseInt(octalStr);              // safe: we produced this string

        // Step 2a - Speed calculation (FR15, FR16, FR17, FR18)
        int speed;
        if (octalInt < 50) {
            speed = octalInt + 50;                  // FR16: below threshold, add 50
        } else {
            speed = octalInt;                       // FR17: use octal value directly
        }
        speed = Math.min(speed, MAX_WHEEL_SPEED);   // FR18: cap at hardware maximum

        // Step 2b - LED colour calculation (FR19, FR20, FR21)
        int red   = Math.min(decimal, 255);              // FR19: red   = decimal
        int green = Math.min((decimal % 80) * 3, 255);   // FR20: green = (dec % 80) * 3
        int blue  = Math.max(red, green);                 // FR21: blue  = max(red, green)

        // Step 2c - Movement duration (FR26, FR27)
        // 1-digit hex -> 1000 ms forward; 2-digit hex -> 500 ms forward
        int forwardMs = (hex.length() == 2) ? 500 : 1000;

        // Step 3 - Print pre-movement display to console (FR30-FR35)
        printPreMovementDisplay(hex, octalStr, decimal, binaryStr,
                speed, red, green, blue, forwardMs);

        // Step 4 - Execute on SwiftBot hardware (FR22-FR29, FR36)
        executeDance(swiftBot, binaryStr, speed, red, green, blue, forwardMs);

        System.out.println("\n  [DONE] Dance routine for " + hex + " complete!");
    }

    // =========================================================================
    // Pre-movement display   FR30 - FR35
    // =========================================================================

    /**
     * Prints all conversion results and dance parameters before movement begins.
     * Also lists each planned movement step from the binary sequence.
     *
     * Required by FR30 (hex), FR31 (octal), FR32 (decimal), FR33 (binary),
     * FR34 (speed), FR35 (LED colour).
     *
     * Matches spec example output exactly:
     *   F,  17, 15, 1111,     speed=67,  LED(red 15, green 45,  blue 45)
     *   3F, 77, 63, 111111,   speed=77,  LED(red 63, green 189, blue 189)
     *   C8, 310,200,11001000, speed=100, LED(red 200,green 120, blue 200)
     */
    private static void printPreMovementDisplay(String hex, String octal, int decimal,
            String binary, int speed, int red, int green, int blue, int forwardMs) {

        printDivider();
        System.out.println("  *********SWIFTBOT DANCE PROGRAM*********");
        printDivider();
        System.out.println("  CONVERSIONS:");
        System.out.printf("    Hexadecimal : %s%n", hex);      // FR30
        System.out.printf("    Octal       : %s%n", octal);    // FR31
        System.out.printf("    Decimal     : %d%n", decimal);  // FR32
        System.out.printf("    Binary      : %s%n", binary);   // FR33
        System.out.println();
        System.out.println("  DANCE PARAMETERS:");
        System.out.printf("    Wheel Speed : %d%n", speed);                          // FR34
        System.out.printf("    LED Colour  : (red %d, green %d, blue %d)%n",
                red, green, blue);                                                     // FR35
        System.out.println();
        System.out.println("  MOVEMENT SEQUENCE (binary read RIGHT to LEFT):");

        // List every planned step (FR22, FR23, FR24, FR25)
        int step = 1;
        for (int i = binary.length() - 1; i >= 0; i--) {
            char bit = binary.charAt(i);
            if (bit == '1') {
                System.out.printf("    Step %d [1] -> Forward %.1f sec%n",
                        step, forwardMs / 1000.0);  // FR24, FR26/FR27
            } else {
                System.out.printf("    Step %d [0] -> Spin%n", step);  // FR25
            }
            step++;
        }

        System.out.println();
        System.out.println("  GET JIGGY!!!!!");
        printDivider();
        System.out.println("  Starting routine in 3... 2... 1...");
        sleep(3000);
    }

    // =========================================================================
    // Dance execution   FR22 - FR29, FR36
    // =========================================================================

    /**
     * Physically executes the dance routine on the SwiftBot.
     *
     * LED colour is set first using fillUnderlights (sets all 6 LEDs at once).
     * Binary string is read right-to-left (FR23):
     *   bit = 1 -> move forward at speed for forwardMs  (FR24)
     *   bit = 0 -> spin in place for SPIN_DURATION_MS   (FR25)
     * Underlights are disabled after all steps complete  (FR36).
     *
     * Uses move(left, right, timeMs) - verified (III)V signature from JAR.
     * Spin is achieved by driving wheels in opposite directions.
     *
     * @param swiftBot   initialised SwiftBotAPI instance
     * @param binary     binary string of the hex value
     * @param speed      wheel speed (0-100)
     * @param red        LED red component (0-255)
     * @param green      LED green component (0-255)
     * @param blue       LED blue component (0-255)
     * @param forwardMs  forward movement duration in milliseconds
     */
    private static void executeDance(SwiftBotAPI swiftBot, String binary,
            int speed, int red, int green, int blue, int forwardMs) {

        // Set underlight colour for entire routine (FR35)
        // fillUnderlights sets all 6 underlights simultaneously - verified ([I)V
        int[] rgb = {red, green, blue};
        try {
            swiftBot.fillUnderlights(rgb);
        } catch (Exception e) {
            System.out.println("  [WARN] Could not set underlights: " + e.getMessage());
        }

        System.out.println("  [EXECUTING] Dance routine started...");

        // Read binary right-to-left (FR23) and execute each step
        int stepNum = 1;
        for (int i = binary.length() - 1; i >= 0; i--) {
            char bit = binary.charAt(i);

            if (bit == '1') {
                // FR24 - move forward: both wheels at same speed for forwardMs
                System.out.printf("  [STEP %d] Bit=1 -> Moving FORWARD for %.1f sec at speed %d%n",
                        stepNum, forwardMs / 1000.0, speed);
                try {
                    // move(leftVelocity, rightVelocity, movementTimeMs) - verified (III)V
                    swiftBot.move(speed, speed, forwardMs);
                } catch (Exception e) {
                    System.out.println("  [WARN] Forward move error: " + e.getMessage());
                }

            } else {
                // FR25 - spin: left wheel forward, right wheel backward
                System.out.printf("  [STEP %d] Bit=0 -> SPINNING for %dms at speed %d%n",
                        stepNum, SPIN_DURATION_MS, speed);
                try {
                    // Negative right velocity causes spin-in-place
                    swiftBot.move(speed, -speed, SPIN_DURATION_MS);
                } catch (Exception e) {
                    System.out.println("  [WARN] Spin error: " + e.getMessage());
                }
            }

            sleep(STEP_PAUSE_MS); // brief pause between steps
            stepNum++;
        }

        // FR36 - disable all underlights after routine completes
        try {
            swiftBot.disableUnderlights();
        } catch (Exception e) {
            System.out.println("  [WARN] Could not disable underlights: " + e.getMessage());
        }

        System.out.println("  [DONE] All steps executed. Underlights off.");
    }

    // =========================================================================
    // Helpers
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
}

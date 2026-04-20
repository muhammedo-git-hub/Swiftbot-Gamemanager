package DetectLights;

import swiftbot.SwiftBotAPI;
import java.util.Scanner;

/**
 * Entry point for the SwiftBot Assignment 3: Detect Lights.
 * This class coordinates the startup sequence: speed calibration, 
 * QR-code mode selection, and handing control over to the SwiftBotController.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * The main application loop.
     */
    public static void main(String[] args) {
        // Access the singleton instance of the SwiftBot API (v6)
        SwiftBotAPI swiftBot = SwiftBotAPI.INSTANCE;
        
        // Default movement speeds (as a percentage of max power, 1-100)
        int wanderSpeed = 25;
        int actionSpeed = 50;

        // Prompt user for custom speed settings (Additional Feature)
        System.out.print("Would you like to calibrate speeds? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.print("Wander speed (1-100): ");
            wanderSpeed = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Action speed (1-100): ");
            actionSpeed = Integer.parseInt(scanner.nextLine().trim());
        }

        boolean keepRunning = true;
        while (keepRunning) {
            ModeSelector selector = new ModeSelector(swiftBot);
            String modeName = selector.selectMode();

            SwiftBotController controller = new SwiftBotController(swiftBot, modeName, wanderSpeed, actionSpeed);
            controller.registerXButton();
            controller.run();

            if (controller.isXPressed()) {
                keepRunning = false;
            } else {
                System.out.print("Scan new mode? (y/n): ");
                keepRunning = scanner.nextLine().trim().equalsIgnoreCase("y");
            }
        }
    }
}

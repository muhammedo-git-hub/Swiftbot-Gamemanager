package MainMenu;

import java.lang.reflect.Method;
import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getChoice(scanner);

            switch (choice) {
                case 1: launchGame("Dance.DanceProgram"); break;
                case 2: launchGame("SearchForLight.SearchForLight"); break;
                case 3: launchGame("NoughtsAndCrosses.Noughts_and_Crosses"); break;
                case 4: launchGame("ZigZag.ZigZagProgram"); break;
                case 5: launchGame("MasterMind.Mastermind"); break;
                case 6: launchGame("DrawShape.MainProgram"); break;
                case 7: launchGame("Trafficlights.TrafficLightSystem"); break;
                case 0:
                    System.out.println("\nExiting program...");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please choose a valid number.");
            }

            if (running) {
                System.out.println("\nReturning to Main Menu...\n");
            }
        }
        scanner.close();
    }

    /**
     * Launches a game class's main method using Reflection.
     * This ensures MainMenu can compile even if individual games are broken.
     */
    private static void launchGame(String fullClassName) {
        try {
            System.out.println("\n[SYSTEM] Attempting to launch: " + fullClassName + "...");
            
            // Dynamically load the class
            Class<?> gameClass = Class.forName(fullClassName);
            
            // Find the 'public static void main(String[] args)' method
            Method mainMethod = gameClass.getMethod("main", String[].class);
            
            // Execute the method (passing null/empty args)
            mainMethod.invoke(null, (Object) new String[0]);
            
        } catch (ClassNotFoundException e) {
            System.err.println("\n[ERROR] Game file not found or not compiled: " + fullClassName);
        } catch (NoSuchMethodException e) {
            System.err.println("\n[ERROR] Could not find 'main' method in: " + fullClassName);
        } catch (Exception e) {
            System.err.println("\n[ERROR] Game crashed or failed to start: " + e.getCause());
        }
        // No matter what happens, we return here to continue the MainMenu loop.
    }

    private static void displayMenu() {
        System.out.println("==========================================================");
        System.out.println("                TASK CONTROL PROGRAM");
        System.out.println("==========================================================");
        System.out.println("                    MAIN MENU");
        System.out.println("----------------------------------------------------------");
        System.out.println("Select a task to execute: By pressing a Number on the Keyboard");
        System.out.println();
        System.out.println("    1. Dance");
        System.out.println();
        System.out.println("    2. Search for Light");
        System.out.println();
        System.out.println("    3. Noughts and Crosses");
        System.out.println();
        System.out.println("    4. ZigZag");
        System.out.println();
        System.out.println("    5. MasterMind");
        System.out.println();
        System.out.println("    6. Draw Shape");
        System.out.println();
        System.out.println("    7. Traffic Lights");
        System.out.println();
        System.out.println();
        System.out.println("    0. Exit Program");
        System.out.println("----------------------------------------------------------");
        System.out.print("Input: ");
    }

    private static int getChoice(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }
}

package MainMenu;

import java.util.Scanner;

// Confirmed imports
import Dance.DanceProgram;
import DrawShape.MainProgram;
import MasterMind.Mastermind;
import ZigZag.ZigZagProgram;
import NoughtsAndCrosses.Noughts_and_Crosses;

// TODO: Replace these placeholder imports when confirmed/fixed
// import SearchForLight.YourMainClassHere;
// import Trafficlights.YourMainClassHere;

public class MainMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getChoice(scanner);

            try {
                switch (choice) {
                    case 1:
                        System.out.println("\nLaunching Dance...\n");
                        DanceProgram.main(null);
                        break;

                    case 2:
                        System.out.println("\nLaunching Search for Light...\n");
                        // TODO: Replace with real main class once confirmed
                        // YourMainClassHere.main(null);
                        System.out.println("Search for Light is not connected yet.");
                        break;

                    case 3:
                        System.out.println("\nLaunching Noughts and Crosses...\n");
                        Noughts_and_Crosses.main(null);                     
                        break;

                    case 4:
                        System.out.println("\nLaunching ZigZag...\n");
                        ZigZagProgram.main(null);
                        break;

                    case 5:
                        System.out.println("\nLaunching MasterMind...\n");
                        Mastermind.main(null);
                        break;

                    case 6:
                        System.out.println("\nLaunching Draw Shape...\n");
                        MainProgram.main(null);
                        break;

                    case 7:
                        System.out.println("\nLaunching Traffic Lights...\n");
                        // TODO: Replace with real main class once confirmed
                        // YourMainClassHere.main(null);
                        System.out.println("Traffic Lights is not connected yet.");
                        break;

                    case 0:
                        System.out.println("\nExiting program...");
                        running = false;
                        break;

                    default:
                        System.out.println("\nInvalid option. Please choose a valid number.");
                }
            } catch (Exception e) {
                System.out.println("\nError running selected task: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nReturning to Main Menu...\n");
            }
        }

        scanner.close();
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

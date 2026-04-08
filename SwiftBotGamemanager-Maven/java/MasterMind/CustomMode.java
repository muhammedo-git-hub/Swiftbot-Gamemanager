package MasterMind;


import java.util.Scanner;

/**
 * CustomMode class demonstrating INHERITANCE and POLYMORPHISM
 * Implements user-customizable parameters per SRS FR-5 to FR-8
 */
public class CustomMode extends GameMode {
    // ENCAPSULATION: Private field
    private Scanner scanner;

    public CustomMode(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * POLYMORPHISM: Override configure() with Custom mode behavior
     * Prompts user for length [3-6] and attempts with validation per SRS FR-6,
     * FR-7, FR-8
     */
    @Override
    public void configure() {
        System.out.println("[MODE] Custom Mode Selected.\n");

        // FR-6: Prompt for code length between 3 and 6
        System.out.print("-> Enter Sequence Length [3 - 6]: ");
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                codeLength = Integer.parseInt(input);
                if (codeLength >= 3 && codeLength <= 6) {
                    System.out.println("[CONFIG] Length set to " + codeLength + ".");
                    break;
                } else {
                    // FR-8: Informative error message
                    System.out.print(
                            "[ERROR] Invalid Range. Sequence length must be between 3 and 6.\n-> Enter Sequence Length [3 - 6]: ");
                }
            } catch (NumberFormatException e) {
                // FR-8: Informative error message for non-numeric input
                System.out.print(
                        "[ERROR] Invalid Input. Please enter a numeric value.\n-> Enter Sequence Length [3 - 6]: ");
            }
        }

        // FR-7: Prompt for maximum attempts (positive integer)
        System.out.print("-> Enter Maximum Attempts [Positive Integer]: ");
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                maxAttempts = Integer.parseInt(input);
                if (maxAttempts > 0) {
                    System.out.println("[CONFIG] Maximum attempts set to " + maxAttempts + ".");
                    break;
                } else {
                    // FR-8: Informative error message
                    System.out.print(
                            "[ERROR] Invalid Value. Attempt limit must be a positive integer.\n-> Enter Maximum Attempts [Positive Integer]: ");
                }
            } catch (NumberFormatException e) {
                // FR-8: Informative error message for non-numeric input
                System.out.print(
                        "[ERROR] Invalid Input. Please enter a numeric value.\n-> Enter Maximum Attempts [Positive Integer]: ");
            }
        }

        System.out.println("[CONFIG] Color Repetition: ALLOWED");
        System.out.println("[SYSTEM] Parameters locked. Proceeding to challenge generation...\n");
    }

    /**
     * POLYMORPHISM: Override getModeName() to return mode name
     */
    @Override
    public String getModeName() {
        return "Custom";
    }

    /**
     * Custom mode ALLOWS color repetition per SRS FR-13
     */
    @Override
    public boolean allowsRepetition() {
        return true;
    }
}
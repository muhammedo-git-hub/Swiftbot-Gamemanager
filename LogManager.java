import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * LogManager
 * CS1814 Software Implementation - Brunel University London
 *
 * Handles all log file operations and program termination output (FR41 - FR44).
 *
 * Responsibilities:
 *   - Collect all valid hex values entered during the session  (FR41)
 *   - Sort them in ascending order using manual insertion sort (FR42)
 *   - Write sorted values to a timestamped text file           (FR43)
 *   - Display the file path to the user before exit            (FR44)
 *
 * Sort method: manual insertion sort ordered by decimal value.
 * No Collections.sort, Arrays.sort, or any built-in sort used.
 *
 * @author Mohammed Yahya Uddin (2520724)
 */
public class LogManager {

    // Private constructor - utility class, not instantiated
    private LogManager() {}

    // =========================================================================
    // Save and exit   FR41 - FR44
    // =========================================================================

    /**
     * Sorts all collected hex values ascending (FR42), writes them to a
     * timestamped plain-text file (FR43), and displays the file path (FR44).
     *
     * Called once when the user presses X to terminate the program.
     *
     * @param allHexList          all valid hex values entered this session (FR41)
     * @param totalDancesExecuted total number of dance routines completed
     */
    public static void saveLogAndExit(List<String> allHexList, int totalDancesExecuted) {

        printDivider();
        System.out.println("  ****** SWIFTBOT DANCE PROGRAM - EXITING ******");
        printDivider();

        // Handle case where no dances were run
        if (allHexList.isEmpty()) {
            System.out.println("  No hex values were executed this session.");
            System.out.println("  Goodbye!");
            return;
        }

        // FR42 - sort using manual insertion sort (no built-in sort methods)
        List<String> sorted = insertionSort(new ArrayList<>(allHexList));

        System.out.println("  Saving data to log file...");
        System.out.println("    [1] Collecting hexadecimal values");
        System.out.println("    [2] Sorting in ascending order");

        // Build timestamped file path
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filePath = System.getProperty("user.home")
                + "/swiftbot_dance_log_" + timestamp + ".txt";

        // FR43 - write log file
        boolean success = writeLog(filePath, sorted, totalDancesExecuted);
        System.out.println("    [3] Writing to text file");
        System.out.println();

        if (success) {
            System.out.println("  LOG FILE SAVED SUCCESSFULLY");
            printDivider();
            System.out.println("  Location : " + filePath);           // FR44
            System.out.println("  Format   : Plain text (.txt)");
            System.out.println("  Contents : Hexadecimal values in ascending order");
            System.out.println("  Values   : " + String.join(", ", sorted));
            System.out.println();
            System.out.printf("  Total routines executed: %d%n", totalDancesExecuted);
        } else {
            System.out.println("  [ERROR] Failed to save log file.");
        }

        printDivider();
        System.out.println("  Thank you for using the SwiftBot Dance Program to dance with us!!!!!");
        printDivider();
    }

    // =========================================================================
    // File writing   FR43
    // =========================================================================

    /**
     * Writes the sorted hex list and session summary to a plain-text file.
     *
     * File format:
     *   SwiftBot Dance Program - Session Log
     *   Generated : yyyy-MM-dd HH:mm:ss
     *   Total dances executed : N
     *
     *   Hexadecimal values entered (ascending order):
     *     HH    (decimal: D)
     *     ...
     *
     * @param filePath             absolute path to write the log file
     * @param sorted               hex values already sorted in ascending order
     * @param totalDancesExecuted  count of completed dance routines
     * @return                     true if write succeeded, false on IOException
     */
    private static boolean writeLog(String filePath, List<String> sorted,
            int totalDancesExecuted) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filePath))) {
            w.write("SwiftBot Dance Program - Session Log");
            w.newLine();
            w.write("Generated : " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            w.newLine();
            w.write("Total dances executed : " + totalDancesExecuted);
            w.newLine();
            w.newLine();
            w.write("Hexadecimal values entered (ascending order):");
            w.newLine();
            for (String hex : sorted) {
                // Include decimal equivalent for readability
                w.write(String.format("  %-4s  (decimal: %d)",
                        hex, HexConverter.hexToDecimal(hex)));
                w.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("  [ERROR] File write failed: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Manual insertion sort   FR42
    // =========================================================================

    /**
     * Sorts a list of hex strings in ascending order by their decimal value.
     *
     * Uses manual insertion sort algorithm - no Collections.sort, Arrays.sort,
     * or any other built-in sorting method is used (FR42).
     *
     * Algorithm:
     *   For each element from index 1 onward, compare its decimal value against
     *   elements to its left. Shift larger elements one position right until the
     *   correct insertion point is found, then place the element there.
     *
     * Example: ["C8", "1F", "3F"] -> ["1F", "3F", "C8"]
     *          (decimal:  200,  31,  63) -> (31, 63, 200)
     *
     * @param list  mutable list of upper-cased hex strings (copied before passing in)
     * @return      the same list, sorted in ascending decimal order
     */
    public static List<String> insertionSort(List<String> list) {
        for (int i = 1; i < list.size(); i++) {
            String key  = list.get(i);
            int    keyD = HexConverter.hexToDecimal(key); // convert to decimal for comparison
            int    j    = i - 1;

            // Shift elements that are greater than key one position to the right
            while (j >= 0 && HexConverter.hexToDecimal(list.get(j)) > keyD) {
                list.set(j + 1, list.get(j));
                j--;
            }
            // Insert key at its correct position
            list.set(j + 1, key);
        }
        return list;
    }

    // =========================================================================
    // UI helper
    // =========================================================================

    /** Prints a standard divider line for consistent UI formatting. */
    private static void printDivider() {
        System.out.println("########################################################");
    }
}
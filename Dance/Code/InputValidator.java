import java.util.ArrayList;
import java.util.List;

/**
 * InputValidator
 * CS1814 Software Implementation - Brunel University London
 *
 * Handles all QR code input validation and parsing (FR2 - FR11).
 *
 * Responsibilities:
 *   - Split raw QR string on '&' separator (FR7)
 *   - Enforce maximum 5 values per QR code (FR5, FR6)
 *   - Validate each token is 1-2 hex digits, 0-9 / A-F (FR2, FR3, FR4, FR8)
 *   - Reject invalid tokens (FR9, FR10)
 *   - Notify user of ignored values with reasons (FR11)
 *   - Detect wrong separator format and report clearly (NFR46)
 *
 * @author Mohammed Yahya Uddin (2520724)
 */
public class InputValidator {

    /** Maximum number of hex values allowed in one QR code scan (FR5). */
    private static final int MAX_VALUES_PER_QR = 5;

    // Private constructor - utility class, not instantiated
    private InputValidator() {}

    // =========================================================================
    // Main validation entry point
    // =========================================================================

    /**
     * Validates and parses a raw QR code string into a list of valid hex values.
     *
     * Steps:
     *   1. Detect incorrect separator (space instead of '&') and report
     *   2. Split on '&' to get individual tokens (FR7)
     *   3. Cap at MAX_VALUES_PER_QR (FR5)
     *   4. Validate each token character-by-character (FR8)
     *   5. Collect valid tokens; report invalid ones with reasons (FR9-FR11)
     *
     * @param rawInput  decoded QR code text, trimmed
     * @return          list of valid upper-cased hex strings; empty if none found
     */
    public static List<String> validateAndParse(String rawInput) {

        System.out.println("\nValidating Hexadecimal values for you now!!!");

        // FR9 - detect common mistake: space-separated instead of '&'-separated
        if (rawInput.contains(" ") && !rawInput.contains("&")) {
            System.out.println("[ERROR] Invalid QR code format detected!");
            System.out.println("        Values must be separated by '&', not spaces.");
            System.out.println("        EXPECTED FORMAT: 1F&2D&3A");
            System.out.println("        YOUR INPUT:      " + rawInput);
            printDivider();
            return new ArrayList<>();
        }

        // Split on '&' separator (FR7)
        String[] tokens = rawInput.split("&");

        // FR5 - cap at 5 values, warn user if more were provided
        if (tokens.length > MAX_VALUES_PER_QR) {
            System.out.printf("[WARNING] %d values found; maximum is %d. "
                    + "Only the first %d will be used.%n",
                    tokens.length, MAX_VALUES_PER_QR, MAX_VALUES_PER_QR);
            String[] trimmed = new String[MAX_VALUES_PER_QR];
            System.arraycopy(tokens, 0, trimmed, 0, MAX_VALUES_PER_QR);
            tokens = trimmed;
        }

        List<String> validList   = new ArrayList<>();
        List<String> invalidList = new ArrayList<>();

        // Validate each token (FR8, FR9, FR10)
        for (String token : tokens) {
            String t = token.trim();
            if (isValidHex(t)) {
                System.out.println("  [OK] Value: " + t.toUpperCase() + " - VALID!!!");
                validList.add(t.toUpperCase());
            } else {
                String reason = getInvalidReason(t);
                System.out.printf("  [!!] Value: %-6s - INVALID :( (%s)%n", t, reason);
                invalidList.add(t);
            }
        }

        // FR11 - report summary and any ignored values
        System.out.println();
        System.out.printf("  SUMMARY: %d Valid | %d Invalid (ignored)%n",
                validList.size(), invalidList.size());

        if (!invalidList.isEmpty()) {
            System.out.println("  Ignored values: " + String.join(", ", invalidList));
        }
        if (!validList.isEmpty()) {
            System.out.println("  Proceeding with: " + String.join(", ", validList));
        }

        printDivider();
        return validList;
    }

    // =========================================================================
    // Validation helpers
    // =========================================================================

    /**
     * FR8 - Checks whether a string is a valid hex value.
     *
     * A valid hex value must:
     *   - Not be null or empty
     *   - Be exactly 1 or 2 characters long (FR2)
     *   - Contain only characters from: 0-9, A-F, a-f (FR3, FR4)
     *
     * @param s  token to check
     * @return   true if the token is a valid 1-2 digit hex value
     */
    public static boolean isValidHex(String s) {
        if (s == null || s.isEmpty()) return false;
        if (s.length() > 2)          return false; // FR2 - max 2 digits

        for (int i = 0; i < s.length(); i++) {
            char c      = Character.toUpperCase(s.charAt(i));
            boolean dig = (c >= '0' && c <= '9'); // FR3 - numeric digits
            boolean let = (c >= 'A' && c <= 'F'); // FR4 - hex letters A-F
            if (!dig && !let) return false;
        }
        return true;
    }

    /**
     * Returns a human-readable reason string explaining why a token is invalid.
     * Used in FR11 error reporting.
     *
     * @param s  invalid token string
     * @return   descriptive reason string
     */
    public static String getInvalidReason(String s) {
        if (s == null || s.isEmpty()) return "empty value";
        if (s.length() > 2)           return "exceeds 2-digit maximum";
        return "contains non-hex characters";
    }

    // =========================================================================
    // UI helper
    // =========================================================================

    /** Prints a standard divider line for consistent UI formatting. */
    private static void printDivider() {
        System.out.println("########################################################");
    }
}

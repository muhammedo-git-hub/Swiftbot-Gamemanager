package Dance;

/**
 * HexConverter
 * CS1814 Software Implementation - Brunel University London
 *
 * Handles all manual number system conversions.
 * No Java built-in conversion methods are used (FR12, FR13, FR14).
 *
 * Conversions provided:
 *   hexToDecimal  (FR12) - hexadecimal string  -> decimal integer
 *   decimalToOctal (FR14) - decimal integer    -> octal string
 *   decimalToBinary (FR13) - decimal integer   -> binary string
 *
 * Verified spec examples:
 *   F  -> oct=17,  dec=15,  bin=1111
 *   3F -> oct=77,  dec=63,  bin=111111
 *   C8 -> oct=310, dec=200, bin=11001000
 *
 * @author Mohammed Yahya Uddin (2520724)
 */
public class HexConverter {

    // Private constructor - utility class, not instantiated
    private HexConverter() {}

    // =========================================================================
    // FR12 - Hexadecimal to Decimal
    // =========================================================================

    /**
     * Manually converts a hexadecimal string to its decimal integer equivalent.
     *
     * Algorithm: iterate right-to-left over each hex character, compute its
     * digit value (0-15), multiply by 16^position, accumulate the total.
     *
     * Example: "1F" -> (1 * 16^1) + (15 * 16^0) = 16 + 15 = 31
     * Example: "C8" -> (12 * 16^1) + (8 * 16^0) = 192 + 8 = 200
     *
     * No Integer.parseInt, Integer.valueOf, or any built-in hex parsing used.
     *
     * @param hex  valid 1-2 char hex string (case insensitive)
     * @return     decimal integer value
     */
    public static int hexToDecimal(String hex) {
        String upper = hex.toUpperCase();
        int decimal  = 0;
        int position = 0;
        int length   = upper.length();

        for (int i = length - 1; i >= 0; i--) {
            char c         = upper.charAt(i);
            int digitValue = (c >= '0' && c <= '9')
                    ? (c - '0')        // ASCII subtraction: '3'-'0' = 3
                    : (c - 'A' + 10);  // 'A'->10, 'B'->11, ... 'F'->15

            decimal += digitValue * powerOf16(position); // FR12 positional algorithm
            position++;
        }
        return decimal;
    }

    // =========================================================================
    // FR14 - Decimal to Octal
    // =========================================================================

    /**
     * Manually converts a decimal integer to its octal string representation.
     *
     * Algorithm: repeatedly divide by 8, prepend each remainder to build the
     * octal string from least-significant digit upward.
     *
     * Example: 31  -> 31/8=3 r7, 3/8=0 r3  -> "37"
     * Example: 200 -> 200/8=25 r0, 25/8=3 r1, 3/8=0 r3 -> "310"
     *
     * No Integer.toOctalString or any built-in octal conversion used.
     *
     * @param decimal  non-negative integer
     * @return         octal string representation
     */
    public static String decimalToOctal(int decimal) {
        if (decimal == 0) return "0";
        StringBuilder octal = new StringBuilder();
        int temp = decimal;
        while (temp > 0) {
            octal.insert(0, temp % 8); // prepend remainder (0-7)
            temp /= 8;
        }
        return octal.toString();
    }

    // =========================================================================
    // FR13 - Decimal to Binary
    // =========================================================================

    /**
     * Manually converts a decimal integer to its binary string representation.
     *
     * Algorithm: repeatedly divide by 2, prepend each remainder (0 or 1).
     *
     * Example: 15  -> "1111"
     * Example: 200 -> "11001000"
     *
     * No Integer.toBinaryString or any built-in binary conversion used.
     *
     * @param decimal  non-negative integer
     * @return         binary string representation
     */
    public static String decimalToBinary(int decimal) {
        if (decimal == 0) return "0";
        StringBuilder binary = new StringBuilder();
        int temp = decimal;
        while (temp > 0) {
            binary.insert(0, temp % 2); // prepend bit (0 or 1)
            temp /= 2;
        }
        return binary.toString();
    }

    // =========================================================================
    // Helper
    // =========================================================================

    /**
     * Manual exponentiation: 16^n without using Math.pow.
     * Keeps all conversions hand-written as required.
     *
     * @param n  exponent (>= 0)
     * @return   16 raised to the power n
     */
    private static int powerOf16(int n) {
        int result = 1;
        for (int i = 0; i < n; i++) {
            result *= 16;
        }
        return result;
    }
}
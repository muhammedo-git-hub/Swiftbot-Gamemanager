
/**
 * Feedback class demonstrating ENCAPSULATION
 * Implements guess evaluation per SRS FR-24 to FR-30
 */
public class Feedback {
    // ENCAPSULATION: Private fields
    private int exactMatches;
    private int partialMatches;
    private String[] positionalFeedback;

    /**
     * ENCAPSULATION: Calculate feedback from secret and guess
     * Implements two-pass algorithm with positional tracking
     */
    public void calculate(Color[] secret, Color[] guess) {
        exactMatches = 0;
        partialMatches = 0;

        int length = secret.length;
        positionalFeedback = new String[length];
        boolean[] secretUsed = new boolean[length];
        boolean[] guessUsed = new boolean[length];

        // Pass 1: Exact matches
        for (int i = 0; i < length; i++) {
            if (secret[i].equals(guess[i])) {
                exactMatches++;
                positionalFeedback[i] = "+";
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Pass 2: Partial matches
        for (int i = 0; i < length; i++) {
            if (!guessUsed[i]) {
                boolean found = false;
                for (int j = 0; j < length; j++) {
                    if (!secretUsed[j] && guess[i].equals(secret[j])) {
                        partialMatches++;
                        positionalFeedback[i] = "-";
                        secretUsed[j] = true;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    positionalFeedback[i] = "."; // No match for this position
                }
            }
        }
    }

    /**
     * Return positional feedback as an array (e.g. ["+", "-", ".", "+"])
     */
    public String[] getPositionalFeedback() {
        return positionalFeedback;
    }

    /**
     * Return positional feedback as a single string (e.g. "+ - . +")
     */
    public String getPositionalFeedbackString() {
        if (positionalFeedback == null)
            return "N/A";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < positionalFeedback.length; i++) {
            sb.append(positionalFeedback[i]);
            if (i < positionalFeedback.length - 1)
                sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * ENCAPSULATION: Format feedback as string per Original SRS
     * (Sorted: all '+' first, then all '-' first)
     */
    public String getStandardFeedbackString() {
        StringBuilder fb = new StringBuilder();
        for (int i = 0; i < exactMatches; i++) {
            fb.append("+");
        }
        for (int i = 0; i < partialMatches; i++) {
            fb.append("-");
        }
        return fb.length() > 0 ? fb.toString() : "(no matches)";
    }

    // ENCAPSULATION: Public getters
    public int getExactMatches() {
        return exactMatches;
    }

    public int getPartialMatches() {
        return partialMatches;
    }

    /**
     * Check if player has won per FR-31
     * Win condition: all '+' symbols matching code length
     */
    public boolean isWin(int codeLength) {
        return exactMatches == codeLength;
    }
}

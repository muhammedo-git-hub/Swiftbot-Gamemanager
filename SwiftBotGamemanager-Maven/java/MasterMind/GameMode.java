package MasterMind;


/**
 * Abstract GameMode class demonstrating ABSTRACTION
 * Defines contract for Default and Custom game modes per SRS
 */
public abstract class GameMode {
    // ENCAPSULATION: Protected fields accessible to subclasses
    protected int codeLength;
    protected int maxAttempts;

    /**
     * ABSTRACTION: Abstract method - subclasses must implement
     * Configures game parameters per SRS FR-3 to FR-8
     */
    public abstract void configure();

    /**
     * ABSTRACTION: Abstract method - subclasses must provide their name
     */
    public abstract String getModeName();

    /**
     * ABSTRACTION: Abstract method - determines if color repetition is allowed
     * Default Mode: false (FR-11), Custom Mode: true (FR-13)
     */
    public abstract boolean allowsRepetition();

    // ENCAPSULATION: Public getters for protected fields
    public int getCodeLength() {
        return codeLength;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }
}
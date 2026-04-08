package MasterMind;


/**
 * DefaultMode class demonstrating INHERITANCE and POLYMORPHISM
 * Implements fixed parameters per SRS FR-3 and FR-4
 */
public class DefaultMode extends GameMode {

    /**
     * POLYMORPHISM: Override configure() with Default mode behavior
     * Sets fixed values: 4 colors, 6 attempts per SRS FR-3
     */
    @Override
    public void configure() {
        this.codeLength = 4;
        this.maxAttempts = 6;
        System.out.println("[MODE] Default Mode Selected.");
        System.out.println("[CONFIG] Sequence Length: 4 colors");
        System.out.println("[CONFIG] Maximum Attempts: 6");
        System.out.println("[CONFIG] Color Repetition: NOT ALLOWED\n");
    }

    /**
     * POLYMORPHISM: Override getModeName() to return mode name
     */
    @Override
    public String getModeName() {
        return "Default";
    }

    /**
     * Default mode does NOT allow color repetition per SRS FR-11
     */
    @Override
    public boolean allowsRepetition() {
        return false;
    }
}

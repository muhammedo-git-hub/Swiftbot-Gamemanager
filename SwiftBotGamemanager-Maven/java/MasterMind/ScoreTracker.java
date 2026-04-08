package MasterMind;

/**
 * ScoreTracker class demonstrating ENCAPSULATION
 * Maintains persistent Player vs Computer score per SRS FR-35
 */
public class ScoreTracker {
    // ENCAPSULATION: Private fields
    private int playerScore;
    private int computerScore;

    public ScoreTracker() {
        this.playerScore = 0;
        this.computerScore = 0;
    }

    /**
     * Increment player score on win per FR-32
     */
    public void playerWins() {
        playerScore++;
    }

    /**
     * Increment computer score on loss per FR-34
     */
    public void computerWins() {
        computerScore++;
    }

    /**
     * Get formatted score string per FR-35 (e.g., "1-0")
     */
    public String getScoreString() {
        return playerScore + "-" + computerScore;
    }

    /**
     * Get formatted display string for UI
     */
    public String getDisplayString() {
        return "Player " + playerScore + " - Computer " + computerScore;
    }

    // ENCAPSULATION: Public getters
    public int getPlayerScore() {
        return playerScore;
    }

    public int getComputerScore() {
        return computerScore;
    }

    /**
     * Reset scores (for new session)
     */
    public void reset() {
        playerScore = 0;
        computerScore = 0;
    }
}

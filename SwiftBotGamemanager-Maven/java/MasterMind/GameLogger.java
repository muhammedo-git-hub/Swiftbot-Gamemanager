package MasterMind;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * GameLogger class demonstrating ENCAPSULATION
 * Implements comprehensive logging per SRS FR-36 to FR-38 and FR-42
 */
public class GameLogger {
    // ENCAPSULATION: Private static configuration
    private static final String LOG_DIR = "saved_game";
    private static final String LOG_FILE = "mastermind_log.txt";

    private int roundNumber;

    public GameLogger() {
        this.roundNumber = 0;
    }

    /**
     * Increment round counter
     */
    public void nextRound() {
        roundNumber++;
    }

    /**
     * Get current round number
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Reset round counter for new game session
     */
    public void resetRound() {
        roundNumber = 0;
    }

    /**
     * Log game with detailed information per SRS FR-37
     * 
     * @param mode             Game mode name
     * @param secretCode       The secret code (revealed after game)
     * @param lastGuess        The player's last guess
     * @param score            Current score string (e.g., "1-0")
     * @param guessesUsed      Number of guesses used
     * @param guessesRemaining Remaining guesses at end
     * @param won              Whether player won
     */
    public void logGame(String mode, String secretCode, String lastGuess,
            String score, int guessesUsed, int guessesRemaining, boolean won) {
        try {
            ensureDirectoryExists();
            String logEntry = formatDetailedLogEntry(mode, secretCode, lastGuess,
                    score, guessesUsed, guessesRemaining, won);
            writeToFile(logEntry);
            System.out.println("[LOG] Game logged to " + getLogFilePath());
        } catch (IOException e) {
            // EC-6: Handle locked file gracefully
            System.out.println("[WARN] Unable to write log: " + e.getMessage());
            System.out.println("[WARN] Logging skipped. Continuing execution.");
        }
    }

    /**
     * Simple logging method for backward compatibility
     */
    public void logGame(String mode, int rounds, boolean won) {
        try {
            ensureDirectoryExists();
            String logEntry = formatSimpleLogEntry(mode, rounds, won);
            writeToFile(logEntry);
            System.out.println("[LOG] Game logged to " + getLogFilePath());
        } catch (IOException e) {
            System.out.println("[WARN] Unable to write log: " + e.getMessage());
        }
    }

    /**
     * ENCAPSULATION: Private method - directory creation
     */
    private void ensureDirectoryExists() {
        File directory = new File(LOG_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Format detailed log entry per FR-37
     */
    private String formatDetailedLogEntry(String mode, String secretCode, String lastGuess,
            String score, int guessesUsed, int guessesRemaining, boolean won) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String result = won ? "WIN" : "LOSS";

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("[").append(timestamp).append("] GAME RECORD\n");
        sb.append("-------------------------------------------------\n");
        sb.append("Round Number    : ").append(roundNumber).append("\n");
        sb.append("Game Mode       : ").append(mode).append("\n");
        sb.append("Secret Sequence : ").append(secretCode).append("\n");
        sb.append("Final Guess     : ").append(lastGuess).append("\n");
        sb.append("Guesses Used    : ").append(guessesUsed).append("\n");
        sb.append("Guesses Left    : ").append(guessesRemaining).append("\n");
        sb.append("Cumulative Score: ").append(score).append("\n");
        sb.append("Result          : ").append(result).append("\n");
        sb.append("=================================================\n\n");

        return sb.toString();
    }

    /**
     * Format simple log entry (legacy compatibility)
     */
    private String formatSimpleLogEntry(String mode, int rounds, boolean won) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String result = won ? "Win" : "Loss";
        return String.format("[%s] | Mode: %s | Rounds: %d | Result: %s\n",
                timestamp, mode, rounds, result);
    }

    /**
     * ENCAPSULATION: Private method - file writing
     */
    private void writeToFile(String logEntry) throws IOException {
        FileWriter writer = new FileWriter(LOG_DIR + "/" + LOG_FILE, true);
        writer.write(logEntry);
        writer.close();
    }

    /**
     * Get absolute path to log file per FR-42
     */
    public String getLogFilePath() {
        File logFile = new File(LOG_DIR + "/" + LOG_FILE);
        return logFile.getAbsolutePath();
    }
}
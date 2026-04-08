package MasterMind;


import swiftbot.SwiftBotAPI;
import swiftbot.Button;
import java.util.Scanner;

/**
 * Mastermind Main Class - Game Orchestration
 * Fully compliant with SRS Functional Requirements FR-1 through FR-44
 * 
 * Demonstrates all 4 OOP principles:
 * - ENCAPSULATION: Uses encapsulated classes (Color, SecretCode, etc.)
 * - INHERITANCE: Uses GameMode hierarchy (DefaultMode, CustomMode)
 * - ABSTRACTION: Works with abstract GameMode interface
 * - POLYMORPHISM: GameMode behaves differently based on actual type
 */
public class Mastermind {
    // ENCAPSULATION: Private fields
    private SwiftBotAPI swiftBot;
    private Scanner scanner;
    private GameMode currentMode;
    private SecretCode secretCode;
    private ColorDetector detector;
    private GameLogger logger;
    private ScoreTracker scoreTracker;
    private PraiseSequence praiseSequence;
    private volatile String buttonPressed;

    private int currentAttempt;
    private Color[] lastGuess;

    public Mastermind() {
        scanner = new Scanner(System.in);
        secretCode = new SecretCode();
        logger = new GameLogger();
        scoreTracker = new ScoreTracker();
    }

    public static void main(String[] args) {
        Mastermind game = new Mastermind();
        game.initialize();
        game.run();
    }

    /**
     * Initialize SwiftBot connection per SRS system initialization
     */
    private void initialize() {
        System.out.println("[SYSTEM] Initialising Mastermind v2.0...");
        try {
            swiftBot = SwiftBotAPI.INSTANCE;
            detector = new ColorDetector(swiftBot);
            praiseSequence = new PraiseSequence(swiftBot);
            System.out.println("[SYSTEM] Software Ready. Hardware Status: OK.\n");
        } catch (Exception e) {
            System.out.println("[ERROR] I2C disabled or SwiftBot not detected!");
            System.out.println("[ERROR] Please check hardware connection.");
            System.exit(5);
        }
    }

    /**
     * Main game loop per SRS FR-1, FR-39
     */
    private void run() {
        while (true) {
            // FR-1: Display menu and enter wait state
            displayMenu();

            // FR-2: Monitor for control signal
            currentMode = selectMode();

            // FR-9: Quit option
            if (currentMode == null) {
                gracefulExit();
                break;
            }

            // FR-3/FR-5: Configure mode
            currentMode.configure();

            // FR-11/FR-13: Generate secret code based on mode
            secretCode.generate(currentMode.getCodeLength(), currentMode.allowsRepetition());

            // Main game play
            playGame();

            // FR-39: Wait for restart or quit signal
            if (!promptPlayAgain()) {
                gracefulExit();
                break;
            }
        }
    }

    /**
     * Display CLI menu per FR-1
     */
    private void displayMenu() {
        System.out.println("===============================================");
        System.out.println("             M A S T E R M I N D");
        System.out.println("===============================================");
        System.out.println("[A] Start Default Game (4 Colors, 6 Attempts)");
        System.out.println("[B] Start Custom Game");
        System.out.println("[X] Exit");
        System.out.println("-----------------------------------------------");
        System.out.println("Current Score: " + scoreTracker.getDisplayString());
        System.out.println("-----------------------------------------------");
        System.out.println("Waiting for input...\n");
    }

    /**
     * Mode selection per FR-2, FR-3, FR-5
     */
    private GameMode selectMode() {
        buttonPressed = null;
        setupModeSelectionButtons();

        while (buttonPressed == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        swiftBot.disableAllButtons();

        // FR-3/FR-4: Button A = Default Mode
        if (buttonPressed.equals("A")) {
            return new DefaultMode();
        }
        // FR-5: Button B = Custom Mode
        else if (buttonPressed.equals("B")) {
            return new CustomMode(scanner);
        }
        // FR-9: Button X = Quit
        else {
            return null;
        }
    }

    private void setupModeSelectionButtons() {
        swiftBot.disableAllButtons();

        swiftBot.enableButton(Button.A, () -> {
            if (buttonPressed == null) {
                buttonPressed = "A";
                System.out.println("[PRESSED A]");
                swiftBot.disableAllButtons();
            }
        });

        swiftBot.enableButton(Button.B, () -> {
            if (buttonPressed == null) {
                buttonPressed = "B";
                System.out.println("[PRESSED B]");
                swiftBot.disableAllButtons();
            }
        });

        swiftBot.enableButton(Button.X, () -> {
            if (buttonPressed == null) {
                buttonPressed = "X";
                System.out.println("[PRESSED X]");
                swiftBot.disableAllButtons();
            }
        });
    }

    /**
     * Main game loop per FR-15 to FR-34
     */
    private void playGame() {
        currentAttempt = 0;
        boolean won = false;
        logger.nextRound();

        while (currentAttempt < currentMode.getMaxAttempts()) {
            currentAttempt++;
            int remaining = currentMode.getMaxAttempts() - currentAttempt;

            System.out.println("\n-----------------------------------------------");
            System.out.println("ATTEMPT " + currentAttempt + " of " + currentMode.getMaxAttempts());
            System.out.println("-----------------------------------------------");

            // FR-15 to FR-23: Color acquisition cycle
            lastGuess = scanColorSequence();

            // FR-24 to FR-30: Evaluation
            Feedback feedback = new Feedback();
            feedback.calculate(secretCode.getCode(), lastGuess);

            // FR-28: Display feedback
            System.out.println("\n[FEEDBACK] Result: " + feedback.getPositionalFeedbackString());
            System.out.println("[HELP] (+ = Exact, - = Color Match, . = No Match)");

            // FR-31: Check win condition
            if (feedback.isWin(currentMode.getCodeLength())) {
                won = true;
                break;
            }

            System.out.println("[STATUS] " + remaining + " attempts remaining.");
        }

        handleGameResult(won);
    }

    /**
     * Color acquisition cycle per FR-15 to FR-23
     */
    private Color[] scanColorSequence() {
        Color[] guess = new Color[currentMode.getCodeLength()];

        for (int i = 0; i < currentMode.getCodeLength(); i++) {
            // FR-16: Explicit instruction
            System.out.println(
                    "\n-> Hold card " + (i + 1) + " of " + currentMode.getCodeLength() + " in front of the camera.");
            System.out.println("[PRESS BUTTON A TO SCAN]");

            // FR-17 to FR-23: Detect and record color
            guess[i] = detector.detectColor();
        }

        // Display complete guess
        System.out.print("\n[GUESS] Your sequence: [");
        for (int i = 0; i < guess.length; i++) {
            System.out.print(guess[i].getAbbreviation());
            if (i < guess.length - 1)
                System.out.print(" ");
        }
        System.out.println("]");

        return guess;
    }

    /**
     * Handle game result per FR-31 to FR-38
     */
    private void handleGameResult(boolean won) {
        System.out.println("\n===============================================");

        if (won) {
            // FR-32: Success message and score update
            System.out.println("🎉 CONGRATULATIONS! SUCCESS! 🎉");
            System.out.println("You cracked the code in " + currentAttempt + " attempt(s)!");
            scoreTracker.playerWins();

            // AR-1 to AR-4: Kinetic Praise Sequence
            praiseSequence.execute();
        } else {
            // FR-33/FR-34: Defeat handling
            System.out.println("💀 GAME OVER! YOU LOST! 💀");
            System.out.println("You used all " + currentMode.getMaxAttempts() + " attempts.");

            // FR-34: Reveal secret code
            System.out.println("The secret code was: " + secretCode.toString());
            System.out.println("(" + secretCode.toAbbreviationString() + ")");

            scoreTracker.computerWins();
            praiseSequence.executeDefeat();
        }

        // FR-35: Display cumulative score
        System.out.println("\nFINAL SCORE: " + scoreTracker.getDisplayString());
        System.out.println("===============================================");

        // FR-36/FR-37: Log game details
        String guessStr = formatGuessString(lastGuess);
        int remaining = currentMode.getMaxAttempts() - currentAttempt;
        logger.logGame(
                currentMode.getModeName(),
                secretCode.toAbbreviationString(),
                guessStr,
                scoreTracker.getScoreString(),
                currentAttempt,
                remaining,
                won);
    }

    /**
     * Format guess array as string
     */
    private String formatGuessString(Color[] guess) {
        if (guess == null)
            return "N/A";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < guess.length; i++) {
            sb.append(guess[i].getAbbreviation());
            if (i < guess.length - 1)
                sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * FR-39: Post-game prompt
     */
    private boolean promptPlayAgain() {
        System.out.println("\n[RESTART Y / QUIT X]?");
        buttonPressed = null;
        setupPlayAgainButtons();

        while (buttonPressed == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // FR-40/FR-41: Restart retains score
        return buttonPressed.equals("Y");
    }

    private void setupPlayAgainButtons() {
        swiftBot.disableAllButtons();

        swiftBot.enableButton(Button.Y, () -> {
            if (buttonPressed == null) {
                buttonPressed = "Y";
                System.out.println("[PRESSED Y - RESTART]");
                swiftBot.disableAllButtons();
            }
        });

        swiftBot.enableButton(Button.X, () -> {
            if (buttonPressed == null) {
                buttonPressed = "X";
                System.out.println("[PRESSED X - QUIT]");
                swiftBot.disableAllButtons();
            }
        });
    }

    /**
     * Graceful exit per FR-42, FR-43, FR-44
     */
    private void gracefulExit() {
        System.out.println("\n[SYSTEM] Exiting Mastermind...");

        // FR-42: Display log file path
        System.out.println("[LOG] Game logs saved to: " + logger.getLogFilePath());

        // FR-43: Hardware cleanup
        System.out.println("[SYSTEM] Cleaning up hardware...");
        swiftBot.disableAllButtons();
        swiftBot.disableUnderlights();
        swiftBot.disableButtonLights();

        // FR-44: Successful termination
        System.out.println("[SYSTEM] Goodbye! Thank you for playing.");
        System.out.println("Final Score: " + scoreTracker.getDisplayString());
    }
}

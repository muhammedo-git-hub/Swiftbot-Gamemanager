
// --- Main Swing and IO Imports ---
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

/**
 * SwiftBotGameManager
 * 
 * A centralized application (GUI + CLI) to manage and launch the various games
 * developed for the SwiftBot robot. This provides a user-friendly
 * interface to select and run any of the 7 games.
 * 
 * DESIGN FEATURES:
 * - Premium Dark Mode Aesthetic: Uses a custom color palette (Neon Purple,
 * Blue, Green).
 * - Raspberry Pi Optimized: Includes a CLI fallback if no display is detected.
 * - SwiftBot API Integration: Uses hardware feedback (underlights) when
 * launching.
 * - Stability: Launches games in separate processes to prevent System.exit()
 * from closing the manager.
 * 
 */
public class SwiftBotGameManager extends JFrame {

    // --- SwiftBot API (using Object to avoid direct dependency for robustness) ---
    private Object swiftBot;
    private Process activeProcess;

    // --- Color Palette (Vibrant Dark Mode) ---
    private static final java.awt.Color BG_DARK = new java.awt.Color(18, 18, 18);
    private static final java.awt.Color CARD_DARK = new java.awt.Color(33, 33, 33);
    private static final java.awt.Color TEXT_PRIMARY = new java.awt.Color(245, 245, 245);
    private static final java.awt.Color NEON_PURPLE = new java.awt.Color(187, 134, 252);
    private static final java.awt.Color NEON_BLUE = new java.awt.Color(3, 218, 198);
    private static final java.awt.Color NEON_GREEN = new java.awt.Color(0, 255, 127);

    // --- Game Metadata List ---
    private final List<GameInfo> games = new ArrayList<>();

    public SwiftBotGameManager() {
        // --- Setup Window Properties ---
        setTitle("SwiftBot Game Hub");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(20, 20));

        // --- Initialize Games ---
        initializeGameList();

        // --- Build UI Components ---
        add(createHeader(), BorderLayout.NORTH);
        add(createGameGrid(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    /**
     * Initializes the list of games with their relative paths and class names.
     */
    private void initializeGameList() {
        games.add(new GameInfo("Mastermind", "Mastermind", "Mastermind/Code",
                "A color-guessing game using the robot camera."));
        games.add(
                new GameInfo("Dance & Hex", "DanceProgram", "Dance/Code", "Hex conversion and robot dance routines."));
        games.add(new GameInfo("Shape Drawer", "MainProgram", "Draw_Shape_Dean/Draw_Shape_Code",
                "Draw shapes based on QR code commands."));
        games.add(new GameInfo("Light Seeker", "SearchForLight", "Jassim's code",
                "Autonomous light source tracking and obstacle avoidance."));
        games.add(new GameInfo("Noughts & Crosses", "Noughts_and_Crosses", "Noughts_and_Crosses",
                "Classic 3x3 game against the SwiftBot AI."));
        games.add(new GameInfo("Traffic Light", "TrafficLightSystem", "Sharifs Trafficlight",
                "Autonomous navigation with traffic light detection."));
        games.add(
                new GameInfo("ZigZag Path", "ZigZagProgram", "zigzag/Code", "Follow a zigzag path with LED feedback."));
    }

    /**
     * Creates the header panel with a stylized title.
     */
    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setBackground(BG_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JLabel title = new JLabel("SWIFTBOT GAME HUB");
        title.setFont(new Font("Inter", Font.BOLD, 36));
        title.setForeground(NEON_PURPLE);
        header.add(title);

        return header;
    }

    /**
     * Creates the main grid of game cards/buttons.
     */
    private JScrollPane createGameGrid() {
        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setBackground(BG_DARK);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        for (GameInfo game : games) {
            grid.add(createGameCard(game));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(BG_DARK);
        return scroll;
    }

    /**
     * Creates a stylized button card for a single game.
     */
    private JPanel createGameCard(GameInfo game) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_DARK);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(60, 60, 60), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Title and Description
        JLabel name = new JLabel(game.name);
        name.setFont(new Font("Inter", Font.BOLD, 20));
        name.setForeground(NEON_BLUE);

        JTextArea desc = new JTextArea(game.description);
        desc.setFont(new Font("Inter", Font.PLAIN, 14));
        desc.setForeground(TEXT_PRIMARY);
        desc.setBackground(CARD_DARK);
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);

        // Launch Button
        JButton launchBtn = new JButton("Launch Game");
        launchBtn.setBackground(CARD_DARK);
        launchBtn.setForeground(NEON_GREEN);
        launchBtn.setFont(new Font("Inter", Font.BOLD, 14));
        launchBtn.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 1));
        launchBtn.setFocusPainted(false);
        launchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        launchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                launchBtn.setBackground(NEON_GREEN);
                launchBtn.setForeground(BG_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                launchBtn.setBackground(CARD_DARK);
                launchBtn.setForeground(NEON_GREEN);
            }
        });

        launchBtn.addActionListener(e -> launchGame(game));

        card.add(name, BorderLayout.NORTH);
        card.add(desc, BorderLayout.CENTER);
        card.add(launchBtn, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Creates a footer with status or helpful info.
     */
    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(BG_DARK);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        JLabel info = new JLabel("Each game opens in a separate process. Use the 'Stop' button to cancel.");
        info.setFont(new Font("Inter", Font.ITALIC, 12));
        info.setForeground(new java.awt.Color(150, 150, 150));
        footer.add(info);

        return footer;
    }

    /**
     * Launches the selected game as a separate process using ProcessBuilder.
     * This ensures the manager remains stable if the game exits or crashes.
     */
    private void launchGame(GameInfo game) {
        System.out.println("\n[ACTION] Preparing to launch " + game.name + "...");

        // Provide hardware feedback
        provideHardwareFeedback();

        // Create a modal dialog to show progress and allow cancellation
        JDialog progressDialog = new JDialog(this, "Game Running", true);
        progressDialog.getContentPane().setBackground(BG_DARK);
        progressDialog.setSize(400, 200);
        progressDialog.setLocationRelativeTo(this);
        progressDialog.setLayout(new GridBagLayout());

        JLabel statusLabel = new JLabel("Running: " + game.name);
        statusLabel.setForeground(TEXT_PRIMARY);
        statusLabel.setFont(new Font("Inter", Font.BOLD, 18));

        JButton stopBtn = new JButton("Stop Game & Return to Hub");
        stopBtn.setBackground(new java.awt.Color(200, 50, 50));
        stopBtn.setForeground(java.awt.Color.WHITE);
        stopBtn.setFocusPainted(false);
        stopBtn.addActionListener(e -> {
            if (activeProcess != null) {
                System.out.println("[ACTION] Forcefully stopping " + game.name + "...");
                activeProcess.destroyForcibly();
                progressDialog.dispose();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        progressDialog.add(statusLabel, gbc);
        gbc.gridy = 1;
        progressDialog.add(stopBtn, gbc);

        new Thread(() -> {
            try {
                // Determine the correct classpath including the SwiftBot API JAR
                String apiPath = findApiJar();
                String classpath = "." + File.pathSeparator + apiPath + File.pathSeparator + game.path;

                System.out.println("[CMD] java -cp " + classpath + " " + game.mainClass);

                // Build the command: java -cp <classpath> <mainClass>
                ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, game.mainClass);
                pb.inheritIO();

                activeProcess = pb.start();
                int exitCode = activeProcess.waitFor();

                System.out.println("\n[INFO] " + game.name + " finished with exit code: " + exitCode);
                activeProcess = null;

                SwingUtilities.invokeLater(progressDialog::dispose);

            } catch (IOException | InterruptedException ex) {
                String error = "Error launching game: " + ex.getMessage();
                System.err.println("[ERROR] " + error);
                SwingUtilities.invokeLater(progressDialog::dispose);
                if (!GraphicsEnvironment.isHeadless()) {
                    JOptionPane.showMessageDialog(this, error, "Launch Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();

        progressDialog.setVisible(true); // Blocks until disposed
    }

    /**
     * Blinks the robot LEDs to indicate a game is launching.
     * Uses reflection if the SwiftBot hardware/API is present to avoid crashes.
     */
    private void provideHardwareFeedback() {
        if (swiftBot == null)
            return;
        new Thread(() -> {
            try {
                // Blink Purple 3 times using reflection: swiftBot.underlights.setAll(187, 134,
                // 252);
                Object underlights = swiftBot.getClass().getField("underlights").get(swiftBot);
                java.lang.reflect.Method setAll = underlights.getClass().getMethod("setAll", int.class, int.class,
                        int.class);
                java.lang.reflect.Method off = underlights.getClass().getMethod("off");

                for (int i = 0; i < 3; i++) {
                    setAll.invoke(underlights, 187, 134, 252);
                    Thread.sleep(200);
                    off.invoke(underlights);
                    Thread.sleep(200);
                }
            } catch (Exception ignored) {
                // Fallback: SwiftBot API structure might differ across versions
            }
        }).start();
    }

    /**
     * Tries to locate the SwiftBot API JAR file in common locations.
     */
    private String findApiJar() {
        String[] potentialPaths = {
                "SwiftBot-API-6.0.0.jar",
                "/data/home/pi/SwiftBot-API-6.0.0.jar"
        };
        for (String p : potentialPaths) {
            if (new File(p).exists())
                return p;
        }
        return "SwiftBot-API-6.0.0.jar"; // Fallback to current dir
    }

    /**
     * CLI menu fallback for environments without a display (SSH/Headless Pi).
     */
    public void startCommandLineHub() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n==================================");
            System.out.println("     SWIFTBOT GAME HUB (CLI)       ");
            System.out.println("==================================");
            for (int i = 0; i < games.size(); i++) {
                System.out.println((i + 1) + ". " + games.get(i).name + " - " + games.get(i).description);
            }
            System.out.println("0. Exit");
            System.out.print("\nSelect a game to launch: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 0)
                    break;
                if (choice > 0 && choice <= games.size()) {
                    launchGame(games.get(choice - 1));
                    // Give the game some time to start before printing menu again
                    Thread.sleep(2000);
                } else {
                    System.out.println("[!] Invalid selection.");
                }
            } catch (Exception e) {
                System.out.println("[!] Please enter a valid number.");
            }
        }
    }

    /**
     * Simple record-like class to store game information.
     */
    private static class GameInfo {
        final String name;
        final String mainClass;
        final String path;
        final String description;

        GameInfo(String name, String mainClass, String path, String description) {
            this.name = name;
            this.mainClass = mainClass;
            this.path = path;
            this.description = description;
        }
    }

    public static void main(String[] args) {
        SwiftBotGameManager manager = new SwiftBotGameManager();

        // Initialize SwiftBot API dynamically via reflection to handle classpath issues
        // safely
        try {
            Class<?> apiClass = Class.forName("swiftbot.SwiftBotAPI");
            java.lang.reflect.Field instanceField = apiClass.getField("INSTANCE");
            manager.swiftBot = instanceField.get(null);
            System.out.println("[SUCCESS] SwiftBot hardware integrated.");
        } catch (Exception e) {
            System.out
                    .println("[WARN] SwiftBot hardware not detected or API missing. Continuing in software-only mode.");
        }

        // Check if we have a graphical environment
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("[INFO] No display detected. Starting CLI version...");
            manager.startCommandLineHub();
        } else {
            // Set Dark Look and Feel if possible
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            SwingUtilities.invokeLater(() -> {
                manager.setVisible(true);
            });
        }
    }
}

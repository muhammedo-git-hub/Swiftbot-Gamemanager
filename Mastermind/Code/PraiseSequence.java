
import swiftbot.SwiftBotAPI;

/**
 * PraiseSequence class demonstrating ENCAPSULATION
 * Executes kinetic celebration sequence per SRS AR-1 to AR-4
 */
public class PraiseSequence {
    // ENCAPSULATION: Private fields
    private SwiftBotAPI swiftBot;

    // AR-3: Motor power at 50%
    private static final int MOTOR_POWER = 50;

    // AR-3: Duration of 2.0 seconds
    private static final int DURATION_MS = 2000;

    // LED pulse interval
    private static final int PULSE_INTERVAL_MS = 200;

    public PraiseSequence(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
    }

    /**
     * Execute the victory celebration sequence per AR-1 to AR-4
     * 360-degree rotation at 50% power for 2.0 seconds with LED pulsing
     */
    public void execute() {
        System.out.println("[ROBOT] Commencing Kinetic Praise Sequence...");
        System.out.println("[ROBOT] 360-degree rotation @ 50% power...");

        try {
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;

            // Rainbow colors for victory pulse
            int[][] colors = {
                    { 255, 0, 0 }, // Red
                    { 255, 165, 0 }, // Orange
                    { 255, 255, 0 }, // Yellow
                    { 0, 255, 0 }, // Green
                    { 0, 0, 255 }, // Blue
                    { 255, 192, 203 } // Pink
            };
            int colorIndex = 0;

            // AR-4: Pulse LEDs in sync with rotation for 2.0 seconds
            while (elapsedTime < DURATION_MS) {
                // Pulse underlight with current color
                swiftBot.fillUnderlights(colors[colorIndex]);

                // AR-3: Pulse rotation to avoid 0ms duration exception
                // Spin in place for the duration of the pulse
                swiftBot.move(MOTOR_POWER, -MOTOR_POWER, PULSE_INTERVAL_MS);

                // Cycle to next color
                colorIndex = (colorIndex + 1) % colors.length;
                elapsedTime = System.currentTimeMillis() - startTime;
            }

            // Stop motors after duration
            swiftBot.disableUnderlights();

            System.out.println("[ROBOT] Praise sequence complete!");

        } catch (Exception e) {
            // Ensure motors stop on error - avoiding 0ms move
            swiftBot.disableUnderlights();
            System.out.println("[ROBOT] Praise sequence ended with status: " + e.getMessage());
        }
    }

    /**
     * Execute a simpler defeat sequence (optional enhancement)
     */
    public void executeDefeat() {
        System.out.println("[ROBOT] Executing defeat acknowledgment...");

        try {
            // Flash red briefly
            swiftBot.fillUnderlights(new int[] { 255, 0, 0 });
            Thread.sleep(500);
            swiftBot.disableUnderlights();

        } catch (InterruptedException e) {
            swiftBot.disableUnderlights();
        }
    }
}

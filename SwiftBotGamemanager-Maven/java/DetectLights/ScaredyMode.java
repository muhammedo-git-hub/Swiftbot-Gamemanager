package DetectLights;

import swiftbot.SwiftBotAPI;

/**
 * Scaredy SwiftBot Mode Implementation.
 * 
 * This mode flees when it detects an object within 50cm.
 * Demonstrates 'Inheritance' and 'Abstraction'.
 */
public class ScaredyMode extends AbstractRobotMode {

    private static final String NAME = "Scaredy SwiftBot";
    private static final double TRIGGER_CM = 50.0;
    private static final int BACKUP_MS = 1000;
    private static final int TURN_MS = 650;
    private static final int FLEE_MS = 3000;
    private static final int BLINK_INTERVAL_MS = 200;
    private static final int BLINK_COUNT = 4;

    private int backupSpeed;
    private int fleeSpeed;

    public ScaredyMode(int backupSpeed, int fleeSpeed) {
        this.backupSpeed = backupSpeed;
        this.fleeSpeed = fleeSpeed;
    }

    @Override
    public String getName() { return NAME; }

    /**
     * Implements the 'Fling' sequence: Photo -> Red Blinks -> Reversing -> Turning -> Fleeing.
     */
    @Override
    public void handleObjectDetected(double distanceCm, SwiftBotAPI swiftBot, SessionLogger logger) {
        if (distanceCm > TRIGGER_CM) return; // Only respond if within 50cm

        System.out.println("[Scaredy] Object detected! RUNNING AWAY!");

        String imgPath = captureAndSave(swiftBot, "scaredy");
        logger.recordEncounter(imgPath);

        blinkRed(swiftBot);
        swiftBot.fillUnderlights(new int[]{220, 0, 0});
        swiftBot.move(-backupSpeed, -backupSpeed, BACKUP_MS);
        swiftBot.move(fleeSpeed, -fleeSpeed, TURN_MS);
        swiftBot.move(fleeSpeed, fleeSpeed, FLEE_MS);

        swiftBot.stopMove();
        swiftBot.disableUnderlights();
    }

    private void blinkRed(SwiftBotAPI swiftBot) {
        for (int i = 0; i < BLINK_COUNT; i++) {
            swiftBot.fillUnderlights(new int[]{220, 0, 0});
            sleep(BLINK_INTERVAL_MS);
            swiftBot.disableUnderlights();
            sleep(BLINK_INTERVAL_MS);
        }
    }
}

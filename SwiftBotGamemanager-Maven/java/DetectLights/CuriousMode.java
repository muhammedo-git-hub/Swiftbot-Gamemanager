package DetectLights;

import swiftbot.SwiftBotAPI;

/**
 * Curious SwiftBot Mode Implementation.
 * 
 * This mode approaches objects until it reaches a 30cm 'buffer zone'.
 * It uses the 'Inheritance' principle by extending AbstractRobotMode.
 */
public class CuriousMode extends AbstractRobotMode {

    private static final String NAME = "Curious SwiftBot";
    private static final double BUFFER_CM = 30.0;
    private static final double TOLERANCE_CM = 2.0;

    private int approachSpeed;
    private int retreatSpeed;

    private static final int BLINK_INTERVAL_MS = 300;
    private static final int BLINK_COUNT = 5;

    public CuriousMode(int approachSpeed, int retreatSpeed) {
        this.approachSpeed = approachSpeed;
        this.retreatSpeed = retreatSpeed;
    }

    @Override
    public String getName() { return NAME; }

    /**
     * Handles the behavior when an object is detected.
     * Implements the logic to move closer, move further, or stay at the 30cm mark.
     */
    @Override
    public void handleObjectDetected(double distanceCm, SwiftBotAPI swiftBot, SessionLogger logger) {
        System.out.println("[Curious] Object detected at " + String.format("%.1f", distanceCm) + " cm.");

        // Visual feedback (Green underlights)
        swiftBot.fillUnderlights(new int[]{0, 200, 0});

        if (distanceCm > BUFFER_CM + TOLERANCE_CM) {
            approachUntilBuffer(swiftBot);
        } else if (distanceCm < BUFFER_CM - TOLERANCE_CM) {
            retreatUntilBuffer(swiftBot);
        } else {
            blinkGreen(swiftBot);
        }

        swiftBot.disableUnderlights();
        String imgPath = captureAndSave(swiftBot, "curious");
        logger.recordEncounter(imgPath);

        sleep(5000);
        double newDistance = readUltrasound(swiftBot);
        if (Math.abs(newDistance - distanceCm) > TOLERANCE_CM) {
            handleObjectDetected(newDistance, swiftBot, logger);
        }
    }

    private void approachUntilBuffer(SwiftBotAPI swiftBot) {
        swiftBot.startMove(approachSpeed, approachSpeed);
        waitForDistance(swiftBot, BUFFER_CM, true);
        swiftBot.stopMove();
    }

    private void retreatUntilBuffer(SwiftBotAPI swiftBot) {
        swiftBot.startMove(-retreatSpeed, -retreatSpeed);
        waitForDistance(swiftBot, BUFFER_CM, false);
        swiftBot.stopMove();
    }

    private void waitForDistance(SwiftBotAPI swiftBot, double targetCm, boolean approaching) {
        for (int i = 0; i < 150; i++) {
            double d = readUltrasound(swiftBot);
            if (approaching ? (d <= targetCm + TOLERANCE_CM) : (d >= targetCm - TOLERANCE_CM)) break;
            sleep(100);
        }
    }

    private void blinkGreen(SwiftBotAPI swiftBot) {
        for (int i = 0; i < BLINK_COUNT; i++) {
            swiftBot.fillUnderlights(new int[]{0, 200, 0});
            sleep(BLINK_INTERVAL_MS);
            swiftBot.disableUnderlights();
            sleep(BLINK_INTERVAL_MS);
        }
    }
}


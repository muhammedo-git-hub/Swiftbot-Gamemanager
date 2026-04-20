package DetectLights;

import swiftbot.SwiftBotAPI;
import java.util.Random;

/**
 * Dubious SwiftBot Mode Implementation.
 * 
 * Polymorphism Principle: This class implements RobotMode by dynamic selection.
 * It randomly wraps either a CuriousMode or a ScaredyMode instance.
 */
public class DubiousMode implements RobotMode {

    private final RobotMode chosenMode;

    public DubiousMode(int wanderSpeed, int actionSpeed) {
        if (new Random().nextBoolean()) {
            chosenMode = new CuriousMode(actionSpeed, actionSpeed);
        } else {
            chosenMode = new ScaredyMode(actionSpeed, actionSpeed);
        }
    }

    @Override
    public String getName() {
        return "Dubious SwiftBot (acting as: " + chosenMode.getName() + ")";
    }

    @Override
    public void handleObjectDetected(double distanceCm, SwiftBotAPI swiftBot, SessionLogger logger) {
        chosenMode.handleObjectDetected(distanceCm, swiftBot, logger);
    }
}

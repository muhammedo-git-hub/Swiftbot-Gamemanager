package DetectLights;

import swiftbot.SwiftBotAPI;

/**
 * Interface that defines the requirements for every operational robot mode.
 * 
 * Abstraction Principle: This interface decouples the controller logic 
 * from the specific details of how each mode responds to light/objects.
 */
public interface RobotMode {

    /**
     * Returns the human-readable name of this mode.
     */
    String getName();

    /**
     * Called by the controller whenever the ultrasound sensor detects an object.
     *
     * @param distanceCm current measured distance to the nearest object (cm)
     * @param swiftBot   the SwiftBot API instance
     * @param logger     the session logger
     */
    void handleObjectDetected(double distanceCm, SwiftBotAPI swiftBot, SessionLogger logger);
}

package DetectLights;

import swiftbot.SwiftBotAPI;
import swiftbot.ImageSize;
import java.awt.image.BufferedImage;

/**
 * Abstract base class for specific robot behavior modes (e.g., Curious, Scaredy).
 * 
 * Inheritance Principle: This class provides common, shared functionality 
 * (like utility methods for sleeping and sensor reading) so that child classes 
 * don't have to duplicate code.
 */
public abstract class AbstractRobotMode implements RobotMode {

    /**
     * Pauses the current thread execution.
     * @param ms The duration to sleep in milliseconds.
     */
    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Synchronously reads the distance from the ultrasound sensor.
     * @param swiftBot The API instance.
     * @return The measured distance in centimeters, or -1 if an error occurs.
     */
    protected double readUltrasound(SwiftBotAPI swiftBot) {
        try {
            return swiftBot.useUltrasound();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Captures a still image and saves it via the ImageSaver utility.
     * @param swiftBot The API instance.
     * @param prefix   A prefix for the filename (e.g. "curious").
     * @return The absolute path to the saved image file.
     */
    protected String captureAndSave(SwiftBotAPI swiftBot, String prefix) {
        try {
            BufferedImage img = swiftBot.takeStill(ImageSize.SQUARE_480x480);
            return ImageSaver.saveImage(img, prefix);
        } catch (Exception e) {
            System.err.println("[" + getName() + "] Failed to capture image: " + e.getMessage());
            return null;
        }
    }
}

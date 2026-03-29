import swiftbot.*;

/**
 * Controls SwiftBot movement and underlights
 * Demonstrates: Encapsulation, Single Responsibility Principle
 * Responsibility: Handle all robot physical actions
 */
public class SwiftBotController {
    private final SwiftBotAPI swiftbot;
    private static final int[] YELLOW = {255, 255, 0};
    
    public SwiftBotController() {
        this.swiftbot = SwiftBotAPI.INSTANCE;
    }
    
    // Movement methods - encapsulated robot control
    
    public void moveForward(int speed, int duration) {
        swiftbot.move(speed, speed, duration);
    }
    
    public void startMoving(int speed) {
        swiftbot.startMove(speed, speed);
    }
    
    public void stop() {
        swiftbot.stopMove();
    }
    
    public void turnLeft(int speed, int duration) {
        swiftbot.startMove(-speed, speed);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void turnAround(int speed, int duration) {
        swiftbot.startMove(speed, -speed);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void move(int leftSpeed, int rightSpeed, int duration) {
        swiftbot.move(leftSpeed, rightSpeed, duration);
    }
    
    // Underlight methods
    
    public void setUnderlights(int[] rgb) {
        try {
            swiftbot.fillUnderlights(rgb);
        } catch (Exception e) {
            System.err.println("Error setting underlights: " + e.getMessage());
        }
    }
    
    public void setYellowUnderlights() {
        setUnderlights(YELLOW);
    }
    
    public void disableUnderlights() {
        swiftbot.disableUnderlights();
    }
    
    public void turnOffUnderlights() {
        setUnderlights(new int[]{0, 0, 0});
    }
    
    // Sensor methods
    
    public double getDistance() {
        try {
            return swiftbot.useUltrasound();
        } catch (IllegalStateException e) {
            System.err.println("Ultrasound timeout");
            return 9999;
        }
    }
    
    // Button methods - delegation to SwiftBot API
    
    public void enableButton(Button button, Runnable action) {
        swiftbot.enableButton(button, action);
    }
    
    public void disableButton(Button button) {
        swiftbot.disableButton(button);
    }
    
    // Accessor for camera (used by detector)
    SwiftBotAPI getSwiftBot() {
        return swiftbot;
    }
}

package Trafficlights;

/**
 * Blue traffic light implementation with yield maneuver
 * Demonstrates: Inheritance, Polymorphism
 */
public class BlueLight extends TrafficLight {
    private static final int TURN_DURATION = 750;
    private static final int LOW_SPEED = 15;
    private static final int INITIAL_SPEED = 30;
    
    public BlueLight() {
        super("BLUE", new int[]{0, 0, 255});
    }
    
    @Override
    public void respond(SwiftBotController controller) throws InterruptedException {
        controller.stop();
        Thread.sleep(1000);
        
        blinkLights(controller);
        performYieldManeuver(controller);
    }
    
    private void blinkLights(SwiftBotController controller) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            controller.setUnderlights(rgbValue);
            Thread.sleep(300);
            controller.disableUnderlights();
            Thread.sleep(300);
        }
    }
    
    private void performYieldManeuver(SwiftBotController controller) throws InterruptedException {
        controller.turnLeft(INITIAL_SPEED, TURN_DURATION);
        controller.stop();
        
        controller.moveForward(LOW_SPEED, 1000);
        Thread.sleep(1000);
        
        controller.turnAround(INITIAL_SPEED, TURN_DURATION * 2);
        controller.stop();
        
        controller.moveForward(LOW_SPEED, 1000);
        
        controller.turnLeft(INITIAL_SPEED, TURN_DURATION);
        controller.stop();
    }
}
/**
 * Green traffic light implementation
 * Demonstrates: Inheritance, Polymorphism
 */
public class GreenLight extends TrafficLight {
    private static final int PASS_SPEED = 50;
    private static final int PASS_DURATION = 2000;
    
    public GreenLight() {
        super("GREEN", new int[]{0, 255, 0});
    }
    
    @Override
    public void respond(SwiftBotController controller) throws InterruptedException {
        controller.setUnderlights(rgbValue);
        controller.move(PASS_SPEED, PASS_SPEED, PASS_DURATION);
        Thread.sleep(1000);
    }
}

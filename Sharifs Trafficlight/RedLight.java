/**
 * Red traffic light implementation
 * Demonstrates: Inheritance, Polymorphism
 */
public class RedLight extends TrafficLight {
    
    public RedLight() {
        super("RED", new int[]{255, 0, 0});
    }
    
    @Override
    public void respond(SwiftBotController controller) throws InterruptedException {
        controller.setUnderlights(rgbValue);
        controller.stop();
        Thread.sleep(1000);
    }
}

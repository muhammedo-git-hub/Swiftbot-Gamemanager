package Trafficlights;

/**
 * Abstract base class for all traffic lights
 * Demonstrates: Inheritance, Encapsulation, Polymorphism
 */
public abstract class TrafficLight {
    protected String colour;
    protected int[] rgbValue;
    
    public TrafficLight(String colour, int[] rgbValue) {
        this.colour = colour;
        this.rgbValue = rgbValue;
    }
    
    // Encapsulation - getters
    public String getColour() {
        return colour;
    }
    
    public int[] getRgbValue() {
        return rgbValue;
    }
    
    // Abstract method - must be implemented by subclasses (POLYMORPHISM)
    public abstract void respond(SwiftBotController controller) throws InterruptedException;
    
    @Override
    public String toString() {
        return colour + " Light";
    }
}
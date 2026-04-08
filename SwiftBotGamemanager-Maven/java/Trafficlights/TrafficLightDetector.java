package Trafficlights;

import swiftbot.*;
import java.awt.image.BufferedImage;

/**
 * Detects and analyzes traffic lights using camera
 * Demonstrates: Single Responsibility Principle, Encapsulation
 * Responsibility: Detect traffic lights and determine their color
 */
public class TrafficLightDetector {
    private final SwiftBotController controller;
    private static final double DETECTION_DISTANCE = 30.0; // As per Task 4 requirement
    private static final int CHECK_INTERVAL = 200;
    
    public TrafficLightDetector(SwiftBotController controller) {
        this.controller = controller;
    }
    
    /**
     * Detects traffic light and returns appropriate TrafficLight object
     * Demonstrates: Polymorphism (returns different subclass instances)
     */
    public TrafficLight detectTrafficLight() {
        controller.startMoving(30);
        
        try {
            Thread.sleep(CHECK_INTERVAL);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
        
        double distance = controller.getDistance();
        
        if (distance > 1000 || distance > DETECTION_DISTANCE) {
            return null;
        }
        
        controller.stop();
        System.out.println("Object detected at " + String.format("%.1f", distance) + " cm");
        
        BufferedImage image = captureImage();
        if (image == null) {
            System.err.println("Error: Image capture failed - defaulting to RED");
            return new RedLight();
        }
        
        String colour = analyzeColour(image);
        return createTrafficLight(colour);
    }
    
    private BufferedImage captureImage() {
        try {
            return controller.getSwiftBot().takeStill(ImageSize.SQUARE_480x480);
        } catch (Exception e) {
            System.err.println("Camera error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Factory method - creates appropriate TrafficLight subclass
     * Demonstrates: Polymorphism, Factory Pattern
     */
    private TrafficLight createTrafficLight(String colour) {
        switch (colour) {
            case "RED":
                return new RedLight();
            case "GREEN":
                return new GreenLight();
            case "BLUE":
                return new BlueLight();
            default:
                return new RedLight();
        }
    }
    
    private String analyzeColour(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        int startX = width / 4;
        int endX = width * 3 / 4;
        int startY = height / 4;
        int endY = height * 3 / 4;
        
        long totalRed = 0;
        long totalGreen = 0;
        long totalBlue = 0;
        int pixelCount = 0;
        
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                
                totalRed += red;
                totalGreen += green;
                totalBlue += blue;
                pixelCount++;
            }
        }
        
        long avgRed = totalRed / pixelCount;
        long avgGreen = totalGreen / pixelCount;
        long avgBlue = totalBlue / pixelCount;
        
        if (avgRed > avgGreen && avgRed > avgBlue) {
            return "RED";
        } else if (avgGreen > avgRed && avgGreen > avgBlue) {
            return "GREEN";
        } else {
            return "BLUE";
        }
    }
}
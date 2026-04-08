package Trafficlights;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles logging of navigation statistics
 * Demonstrates: Single Responsibility Principle, Encapsulation
 * Responsibility: Track and log navigation data
 */
public class NavigationLogger {
    private int totalLights;
    private Map<String, Integer> lightCounts;
    private long startTime;
    private long endTime;
    
    public NavigationLogger() {
        this.totalLights = 0;
        this.lightCounts = new HashMap<>();
        this.lightCounts.put("RED", 0);
        this.lightCounts.put("GREEN", 0);
        this.lightCounts.put("BLUE", 0);
        this.startTime = 0;
        this.endTime = 0;
    }
    
    // Encapsulation - controlled access to data
    
    public void startTracking() {
        this.startTime = System.currentTimeMillis();
    }
    
    public void stopTracking() {
        this.endTime = System.currentTimeMillis();
    }
    
    public void logLight(String colour) {
        totalLights++;
        lightCounts.put(colour, lightCounts.get(colour) + 1);
    }
    
    public int getTotalLights() {
        return totalLights;
    }
    
    public boolean shouldPrompt() {
        return totalLights % 3 == 0 && totalLights > 0;
    }
    
    private String getMostFrequentColour() {
        String mostFrequent = "NONE";
        int maxCount = 0;
        
        for (Map.Entry<String, Integer> entry : lightCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        
        return mostFrequent;
    }
    
    private double getElapsedSeconds() {
        return (endTime - startTime) / 1000.0;
    }
    
    public void displayAndSaveLog() {
        String mostFrequent = getMostFrequentColour();
        int mostFrequentCount = lightCounts.get(mostFrequent);
        double elapsedSeconds = getElapsedSeconds();
        
        System.out.println("\nLogging traffic light navigation details:");
        System.out.println("Total lights encountered: " + totalLights);
        System.out.println("Red lights: " + lightCounts.get("RED"));
        System.out.println("Green lights: " + lightCounts.get("GREEN"));
        System.out.println("Blue lights: " + lightCounts.get("BLUE"));
        System.out.println("Most frequent colour: " + mostFrequent + " (" + mostFrequentCount + " times)");
        System.out.println("Total execution time: " + String.format("%.1f", elapsedSeconds) + " seconds");
        
        saveToFile(mostFrequent, mostFrequentCount, elapsedSeconds);
    }
    
    private void saveToFile(String mostFrequent, int mostFrequentCount, double elapsedSeconds) {
        String fileName = "traffic_light_log.txt";
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            writer.write("Traffic Light Navigation Log\n");
            writer.newLine();
            writer.write("Total lights encountered: " + totalLights + "\n");
            writer.write("Red lights: " + lightCounts.get("RED") + "\n");
            writer.write("Green lights: " + lightCounts.get("GREEN") + "\n");
            writer.write("Blue lights: " + lightCounts.get("BLUE") + "\n");
            writer.write("Most frequent colour: " + mostFrequent + " (" + mostFrequentCount + " times)\n");
            writer.write("Total execution time: " + String.format("%.1f", elapsedSeconds) + " seconds\n");
            writer.newLine();
            
            System.out.println("Log saved to " + fileName);
            
        } catch (IOException e) {
            System.err.println("Error writing log: " + e.getMessage());
        }
    }
}
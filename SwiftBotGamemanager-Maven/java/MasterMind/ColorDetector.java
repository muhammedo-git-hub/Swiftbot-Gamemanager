package MasterMind;


import swiftbot.SwiftBotAPI;
import swiftbot.Button;
import swiftbot.ImageSize;
import java.awt.image.BufferedImage;

/**
 * ColorDetector class demonstrating ENCAPSULATION
 * Implements color acquisition pipeline per SRS FR-15 to FR-23 and EC-4
 */
public class ColorDetector {
    // ENCAPSULATION: Private fields
    private SwiftBotAPI swiftBot;
    private Color[] availableColors;
    private volatile Color detectedColor;
    private volatile double lastConfidence;

    // Confidence threshold per SRS EC-4
    private static final double CONFIDENCE_THRESHOLD = 65.0;
    // Maximum possible distance in RGB space (sqrt(255^2 + 255^2 + 255^2))
    private static final double MAX_DISTANCE = 441.67;

    public ColorDetector(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
        this.availableColors = Color.getAllColors();
    }

    /**
     * ENCAPSULATION: Public interface for color detection
     * Implements FR-15 to FR-23 acquisition cycle
     */
    public Color detectColor() {
        while (true) {
            detectedColor = null;
            lastConfidence = 0;

            // Setup button A to trigger scan per FR-17
            swiftBot.disableAllButtons();
            System.out.println("[PRESS BUTTON A TO SCAN]");
            swiftBot.enableButton(Button.A, () -> {
                if (detectedColor == null) {
                    performScan();
                }
            });

            // Wait for scan to complete or fail
            while (detectedColor == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // EC-4: Check confidence threshold
            if (lastConfidence >= CONFIDENCE_THRESHOLD) {
                return detectedColor; // Successful scan
            }

            // If we are here, confidence was low or scan failed
            System.out.println("[WARN] Low Confidence (" + String.format("%.1f", lastConfidence) + "%).");
            System.out.println("[RETRY] Please press BUTTON B to retry scanning.");

            // Wait for Button B per user request
            final boolean[] retryTriggered = { false };
            swiftBot.disableAllButtons();
            swiftBot.enableButton(Button.B, () -> {
                retryTriggered[0] = true;
            });

            while (!retryTriggered[0]) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Loop continues to re-enable Button A
        }
    }

    /**
     * Perform the actual scan operation
     */
    private void performScan() {
        try {
            // FR-17: Capture digital image
            BufferedImage image = swiftBot.takeStill(ImageSize.SQUARE_240x240);

            // FR-18 to FR-22: Process image and identify color
            ColorResult result = analyzeImage(image);

            // Use local variable to avoid race condition with main thread setting it to
            // null
            Color identifiedColor = result.color;
            double identifiedConfidence = result.confidence;

            // FR-22: Visual confirmation on platform
            flashColor(identifiedColor);

            System.out.println("[RESULT] Color Identified: " + identifiedColor.getName() +
                    " (Confidence: " + String.format("%.1f", identifiedConfidence) + "%)");

            // Now update the shared fields
            this.lastConfidence = identifiedConfidence;
            this.detectedColor = identifiedColor;

        } catch (Exception e) {
            System.out.println("[ERROR] Scan failed: " + e.getMessage());
            System.out.println("[ERROR] System error during image processing.");
            this.detectedColor = Color.getAllColors()[0]; // Set to something to break the wait loop
            this.lastConfidence = 0; // Force retry via threshold
        }
        swiftBot.disableAllButtons();
    }

    /**
     * ENCAPSULATION: Private method - image analysis algorithm
     * Implements FR-18 to FR-21
     */
    private ColorResult analyzeImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // FR-18: Transform to pixel matrix, sample center 50%
        int startX = width / 4;
        int endX = 3 * width / 4;
        int startY = height / 4;
        int endY = 3 * height / 4;

        long sumR = 0, sumG = 0, sumB = 0;
        int pixelCount = 0;

        // FR-19: Extract RGB values for every pixel
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                sumR += r;
                sumG += g;
                sumB += b;
                pixelCount++;
            }
        }

        // FR-20: Calculate arithmetic mean
        int avgR = (int) (sumR / pixelCount);
        int avgG = (int) (sumG / pixelCount);
        int avgB = (int) (sumB / pixelCount);

        // FR-21: Compare to palette using geometric similarity
        Color closest = availableColors[0];
        double minDistance = closest.distanceTo(avgR, avgG, avgB);

        for (int i = 1; i < availableColors.length; i++) {
            double distance = availableColors[i].distanceTo(avgR, avgG, avgB);
            if (distance < minDistance) {
                minDistance = distance;
                closest = availableColors[i];
            }
        }

        // Calculate confidence as inverse of distance (0-100%)
        double confidence = Math.max(0, 100 * (1 - (minDistance / MAX_DISTANCE)));

        return new ColorResult(closest, confidence);
    }

    /**
     * ENCAPSULATION: Private method - visual feedback per FR-22
     */
    private void flashColor(Color color) {
        try {
            swiftBot.fillUnderlights(color.getRGB());
            Thread.sleep(500);
            swiftBot.disableUnderlights();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inner class to hold detection result with confidence
     */
    private static class ColorResult {
        Color color;
        double confidence;

        ColorResult(Color color, double confidence) {
            this.color = color;
            this.confidence = confidence;
        }
    }
}
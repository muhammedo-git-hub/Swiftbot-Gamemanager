package DetectLights;

import swiftbot.SwiftBotAPI;
import java.awt.image.BufferedImage;

/**
 * Handles the scanning and decoding of QR codes for mode selection.
 * Decodes strings to "Curious SwiftBot", "Scaredy SwiftBot", or "Dubious SwiftBot".
 */
public class ModeSelector {

    public static final String CURIOUS = "Curious SwiftBot";
    public static final String SCAREDY = "Scaredy SwiftBot";
    public static final String DUBIOUS = "Dubious SwiftBot";

    private final SwiftBotAPI swiftBot;

    public ModeSelector(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
    }

    public String selectMode() {
        System.out.println("Scanning for mode QR code...");
        for (int i = 0; i < 200; i++) {
            try {
                BufferedImage img = swiftBot.getQRImage();
                if (img == null) { sleep(500); continue; }
                String decoded = swiftBot.decodeQRImage(img);
                if (decoded != null && !decoded.isBlank()) {
                    String trimmed = decoded.trim();
                    if (isValidMode(trimmed)) return trimmed;
                }
                sleep(500);
            } catch (Exception e) { sleep(500); }
        }
        return CURIOUS;
    }

    private boolean isValidMode(String s) {
        return CURIOUS.equals(s) || SCAREDY.equals(s) || DUBIOUS.equals(s);
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}

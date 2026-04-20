package DetectLights;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles recording the operational session and writing log files to 'logs/'.
 * Stores timestamps, mode information, encounter counts, and image paths.
 */
public class SessionLogger {

    private final String modeName;
    private final long sessionStartTime;
    private final List<String> imagePaths;
    private int encounterCount;

    private static final String LOG_DIR = "logs";

    public SessionLogger(String modeName) {
        this.modeName = modeName;
        this.sessionStartTime = System.currentTimeMillis();
        this.imagePaths = new ArrayList<>();
        new File(LOG_DIR).mkdirs();
    }

    public void recordEncounter(String imagePath) {
        encounterCount++;
        if (imagePath != null) imagePaths.add(imagePath);
    }

    public String writeLog() {
        long durationMs = System.currentTimeMillis() - sessionStartTime;
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File logFile = new File(LOG_DIR, "swiftbot_log_" + timestamp + ".txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(logFile))) {
            pw.println("SwiftBot Session Log");
            pw.println("Mode: " + modeName);
            pw.println("Total Encounters: " + encounterCount);
            pw.println("Duration: " + (durationMs / 60000) + "m " + ((durationMs % 60000) / 1000) + "s");
            pw.println("--- Captured Images ---");
            for (String path : imagePaths) pw.println(path);
            return logFile.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("[Logger] Failed to write log file: " + e.getMessage());
            return null;
        }
    }
}

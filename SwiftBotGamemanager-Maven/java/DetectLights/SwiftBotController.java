package DetectLights;

import swiftbot.SwiftBotAPI;
import swiftbot.Button;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The core controller that manages the SwiftBot's operational cycle.
 * It coordinates the wandering state and delegates object encounters to 
 * the selected RobotMode.
 */
public class SwiftBotController {

    private final SwiftBotAPI swiftBot;
    private final RobotMode mode;
    private final WanderingBehaviour wander;
    private final SessionLogger logger;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean xPressed = new AtomicBoolean(false);

    private long lastEncounterTime = System.currentTimeMillis();
    private int encounterCount = 0;
    private long windowStartTime = System.currentTimeMillis();

    private final int wanderSpeed;
    private final int actionSpeed;

    public SwiftBotController(SwiftBotAPI swiftBot, String modeName, int wanderSpeed, int actionSpeed) {
        this.swiftBot = swiftBot;
        this.wanderSpeed = wanderSpeed;
        this.actionSpeed = actionSpeed;
        this.wander = new WanderingBehaviour(wanderSpeed);
        this.mode = buildMode(modeName, actionSpeed);
        this.logger = new SessionLogger(mode.getName());
    }

    private RobotMode buildMode(String modeName, int speed) {
        if (modeName.equals(ModeSelector.CURIOUS)) return new CuriousMode(speed, speed);
        if (modeName.equals(ModeSelector.SCAREDY)) return new ScaredyMode(speed, speed);
        if (modeName.equals(ModeSelector.DUBIOUS)) return new DubiousMode(wanderSpeed, speed);
        return new CuriousMode(speed, speed);
    }

    public void registerXButton() {
        swiftBot.enableButton(Button.X, () -> {
            xPressed.set(true);
            running.set(false);
        });
    }

    /**
     * The main execution loop. 
     * Handles switching between wandering and responding to objects.
     */
    public void run() {
        running.set(true);
        wander.startWander(swiftBot);
        lastEncounterTime = System.currentTimeMillis();

        while (running.get()) {
            double distance = readUltrasound();
            
            // If an object is detected within range, stop and respond
            if (distance > 0 && isObjectInRange(distance)) {
                wander.stopWander(swiftBot);
                trackEncounter();
                
                // Polymorphism: Call handleObjectDetected without knowing the exact mode implementation
                mode.handleObjectDetected(distance, swiftBot, logger);
                
                // If the user needs to change mode due to rapid encounters, break the loop
                if (shouldPromptModeChange()) { running.set(false); break; }
                
                // Resume wandering
                wander.startWander(swiftBot);
                lastEncounterTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - lastEncounterTime >= 5000) {
                // Idle Rule: Change direction if no objects encountered for 5 seconds
                wander.wanderNewDirection(swiftBot);
                lastEncounterTime = System.currentTimeMillis();
            }
            sleep(200);
        }
        wander.stopWander(swiftBot);
        logger.writeLog();
    }

    private boolean isObjectInRange(double d) {
        return d <= (mode.getName().contains("Scaredy") ? 50.0 : 80.0);
    }

    private void trackEncounter() {
        encounterCount++;
        if (System.currentTimeMillis() - windowStartTime > 300000) {
            encounterCount = 1;
            windowStartTime = System.currentTimeMillis();
        }
    }

    private boolean shouldPromptModeChange() {
        return encounterCount > 3;
    }

    private double readUltrasound() {
        try { return swiftBot.useUltrasound(); } catch (Exception e) { return -1; }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public boolean isXPressed() { return xPressed.get(); }
}


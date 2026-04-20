package DetectLights;

import swiftbot.SwiftBotAPI;
import java.util.Random;

/**
 * Implements the wandering state. 
 * The robot moves with blue underlights until an object is detected.
 */
public class WanderingBehaviour {
    private int wanderSpeed;
    private final Random random = new Random();

    public WanderingBehaviour(int wanderSpeed) {
        this.wanderSpeed = wanderSpeed;
    }

    public void startWander(SwiftBotAPI swiftBot) {
        swiftBot.fillUnderlights(new int[]{0, 0, 255});
        swiftBot.startMove(wanderSpeed, wanderSpeed);
    }

    public void stopWander(SwiftBotAPI swiftBot) {
        swiftBot.stopMove();
    }

    public void wanderNewDirection(SwiftBotAPI swiftBot) {
        swiftBot.stopMove();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        int bias = random.nextInt(31);
        boolean turnLeft = random.nextBoolean();
        int left = turnLeft ? Math.max(1, wanderSpeed - bias) : wanderSpeed;
        int right = turnLeft ? wanderSpeed : Math.max(1, wanderSpeed - bias);
        swiftBot.fillUnderlights(new int[]{0, 0, 255});
        swiftBot.startMove(left, right);
    }
}

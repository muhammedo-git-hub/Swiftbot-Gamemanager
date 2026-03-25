package full_method_and_class_implementations;

import java.io.ObjectInputFilter.Config;
import java.util.Scanner;

import full_method_and_class_implementations.Game_Board.Board;
import full_method_and_class_implementations.Swiftbot_Interface.SwiftBotInterface;

public class Game_Controller {
	
	static class GameController {
        private final Config cfg;
        private final Board board = new Board();
        private final LoggerService logger = new LoggerService();
        private SwiftBotInterface swiftBot;
        private AI_Engine aiEngine;
        private final Scanner in = new Scanner(System.in);

        private Player user, bot, currentPlayer;
        private int userUndoRemaining;
        private int roundNumber = 0;
        private int scoreUser = 0;
        private int scoreBot = 0;
        
        public GameController(Config cfg) {
            this.cfg = cfg;
            this.swiftBot = cfg.hardwareEnabled ? new MockSwiftBot() : new MockSwiftBot();
            this.aiEngine = new AI_Engine(cfg.strategicAI);
            applyDifficulty(cfg.difficulty);
        }
        
        private void applyDifficulty(Difficulty d) {
            cfg.difficulty = d;
            switch (d) {
                case EASY -> { cfg.turnTimerSeconds = 0; cfg.undoQuota = 2; }
                case MEDIUM -> { cfg.turnTimerSeconds = 10; cfg.undoQuota = 1; }
                case HARD -> { cfg.turnTimerSeconds = 5; cfg.undoQuota = 0; }
            }


}

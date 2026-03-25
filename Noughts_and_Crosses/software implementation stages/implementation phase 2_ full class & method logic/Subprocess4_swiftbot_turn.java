package full_method_and_class_implementations;

import java.io.IOException;
import java.time.Instant;

public class Subprocess4_swiftbot_turn {
	
	 public void swiftBotTurn() {
		 Move m = aiEngine.chooseMove(board, bot.piece);
         if (m == null) { 
        	 System.out.println("[INFO] No valid moves for SwiftBot."); return; 
        	 }
         System.out.printf("[%s – %s] will move to square %s\n", bot.name, bot.piece, m);
         
         try {
             executeMotionSequence(m.row, m.col, "GREEN", 3);
             boolean ok = applyMoveAndUpdateUI(bot, m);
             if (!ok) {
                 System.out.println("[ERROR] Failed to apply SwiftBot move to internal board.");
             }
         } catch (IOException ex) {
             System.out.println("[ERROR] Motion error: " + ex.getMessage());
             logger.append("MOTION_FAILURE,"+ex.getMessage()+","+Instant.now().toString());
         }
     }
         
	 }



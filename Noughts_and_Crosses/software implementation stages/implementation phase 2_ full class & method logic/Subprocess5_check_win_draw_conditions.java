package full_method_and_class_implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Subprocess5_check_win_draw_conditions {
	
	public Optional<Piece> checkWinDrawAndAct() {
		
		 List<int[]> winCoords = new ArrayList<>();
		 Optional<Piece> w = board.checkWinner(winCoords);
		 
		 if (w.isPresent()) {
             Piece winner = w.get();
             
             if (winner == user.piece) {
                 System.out.println("*** WINNER: " + user.name + " (" + winner + ") ***");
                 
                 try {
                     executeMotionSequenceForTrace(winCoords, "GREEN",3);
                     
                 } catch (IOException e) { System.out.println("[ERROR] Motion during win trace: " + e.getMessage()); }
                 scoreUser++;
                 
             } else {
                 System.out.println("*** WINNER: " + bot.name + " (" + winner + ") ***");
                 try { executeMotionSequenceForTrace(winCoords, "RED",3); 
                 } 
                 catch (IOException e) { System.out.println("[ERROR] Motion during win trace: " + e.getMessage()); 
                 }
                 }
                 scoreBot++;
             }
		     return Optional.of(winner);
		     
		} else if (board.isFull()) {
			System.out.println("*** ROUND RESULT: DRAW ***");
			try { swiftBot.spinOnce(); swiftBot.blink("BLUE", 3); } 
			catch (IOException e) { 
				
			}
        return Optional.of(Piece.EMPTY);
        
	} else {
		return Optional.empty();
	}
}
}

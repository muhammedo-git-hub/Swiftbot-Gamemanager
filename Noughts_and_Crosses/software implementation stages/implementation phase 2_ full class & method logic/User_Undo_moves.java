package full_method_and_class_implementations;

import java.time.Instant;

public class User_Undo_moves {
	
	private synchronized boolean handleUndo() {
        if (userUndoRemaining <= 0) {
            System.out.println("Undo moves remaining: 0. Cannot undo.");
            return false;
        }
        System.out.print("Confirm undo last move? (Y/N) > ");
        String conf = in.nextLine().trim();
        
        if (!conf.equalsIgnoreCase("Y"))
        	return false;
        
        Move undone = null;
        List<Move> temp = board.getHistory();
        if (temp.isEmpty()) { 
        	System.out.println("Nothing to undo.");
        	return false; 
        	
        	Move last = temp.get(temp.size()-1);
            if (last.piece != user.piece) {
                System.out.println("Last move was not yours. Cannot undo until after your move.");
                return false;
                
        	} else {
        		undone = board.undoLast();
        		
        	}
            if (undone != null) {
            	
                userUndoRemaining--;
                
                logger.append("UNDO," + undone.toString() + "," + Instant.now().toString());
                System.out.println("[OK] Last move undone. Undo left: " + userUndoRemaining);
                System.out.println(board.toAscii());
                return true;
            } else {
                System.out.println("Nothing to undo.");
                return false;
        }
        }
	}
}

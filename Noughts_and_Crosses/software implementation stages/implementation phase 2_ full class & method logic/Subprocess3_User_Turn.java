package full_method_and_class_implementations;

public class Subprocess3_User_Turn {
	
	public boolean userTurn() {
		 System.out.println(board.toAscii());
         System.out.println("Current turn: ", currentPlayer==user?user.name:bot.name, currentPlayer.piece);
         System.out.println("Undo moves left: ", userUndoRemaining);
         System.out.println("Time limit: ", cfg.turnTimerSeconds)
         System.out.print("Your move (format row,column) or 'undo' > ");
         
         ExecutorService ex = Executors.newSingleThreadExecutor();
         Future<String> future = ex.submit(() -> {
             try { return in.nextLine().trim(); } catch (Exception e) { return ""; }
         });
         String input = null;
         try {
             if (cfg.turnTimerSeconds > 0) input = future.get(cfg.turnTimerSeconds, TimeUnit.SECONDS);
             else input = future.get();
         } catch (TimeoutException te) {
             future.cancel(true);
             System.out.println("*** TIME EXPIRED — turn forfeited ***");
             logger.append("FORFEIT,"+user.name+","+Instant.now().toString());
             ex.shutdownNow();
             return false; 
             
         } catch (Exception e) {
             future.cancel(true);
             System.out.println("[ERROR] Input error. Try again.");
             ex.shutdownNow();
             return false;
	} finally {
        ex.shutdownNow();
    }
    if (input.equalsIgnoreCase("undo")) {
    	return handleUndo();
    }
    String[] parts = input.split(",");
    if (parts.length != 2) {
        System.out.println("[ERROR] Invalid format. Use row,column (e.g., 2,3).");
        return userTurn();
    }
    int r,c;
    try { r = Integer.parseInt(parts[0].trim()); c = Integer.parseInt(parts[1].trim()); }
    
    catch (NumberFormatException nfe) { 
    	System.out.println("[ERROR] Non-numeric input. Try again."); 
    	return userTurn(); 
    	}
    
    if (r<1 || r>3 || c<1 || c>3) { 
    	System.out.println("[ERROR] Row/Col must be 1..3."); 
    	return userTurn(); 
    	}
    if (!board.isEmpty(r,c)) { 
    	System.out.printf("[ERROR] Square [%d,%d] occupied. Try again.\n", r, c); return userTurn(); 
    	}

    Move m = new Move(r,c,user.piece);
    boolean ok = applyMoveAndUpdateUI(user, m);
    return ok;
}
    
    
    
    
    
    
    
    
    
         }
         
         


}

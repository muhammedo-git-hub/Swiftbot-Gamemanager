package full_method_and_class_implementations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subprocess7_play_again_or_quit {
	
	public boolean playAgainOrQuit() {
        System.out.print("Play again? (Y=play again / X=quit) > ");
        String s = in.nextLine().trim();
        
        if (s.equalsIgnoreCase("Y")) {
            board.reset();
            userUndoRemaining = cfg.undoQuota;
            return true;
            
        } else {
            String fname = "noughts_log_" + DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now()) + ".csv";
            
            try { logger.flushToDisk(fname); System.out.println("[OK] Log saved to: " + logger.samplePath(fname)); }
            catch (IOException e) { System.out.println("[ERROR] Failed to write log: " + e.getMessage()); }
            System.out.println("Program terminating. Goodbye.");
            return false;
        }

	}

}

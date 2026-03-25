package full_method_and_class_implementations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class Subprocess6_update_round_and_scoreboard {
	
	public void appendRoundLogAndUpdateScoreboard(Optional<Piece> outcome) {
        roundNumber++;
        
        String result = "DRAW";
        String winnerName = "";
        
        if (outcome.isPresent() && outcome.get() != Piece.EMPTY) {
        	
            result = outcome.get() == user.piece ? "USER_WIN" : "SWIFTBOT_WIN";
            winnerName = outcome.get() == user.piece ? user.name : bot.name;
        }
        
        List<Move> hist = board.getHistory();
        
        StringBuilder seq = new StringBuilder();
        
        for (int i = hist.size()-1; i>=0; i--) { 
        	Move m = hist.get(i); seq.append(m.toCSV()).append(";"); 
        	}
        
        String csvLine = String.join(",", Integer.toString(roundNumber), seq.toString(), result, winnerName, Instant.now().toString());      
        logger.append(csvLine);
        
        
        System.out.println("--- Round summary ---");
        System.out.printf("Round #: %d\nResult: %s\n", roundNumber, result);
        System.out.printf("Scoreboard:\n %s: %d\n %s: %d\n", user.name, scoreUser, bot.name, scoreBot);
    }



}

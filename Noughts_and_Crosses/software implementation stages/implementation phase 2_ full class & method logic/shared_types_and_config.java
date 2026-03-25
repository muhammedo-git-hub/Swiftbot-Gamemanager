package full_method_and_class_implementations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class shared_types_and_config {

	public static void main(String[] args) {
		
		enum Piece { X, O, EMPTY; }
	    enum Difficulty { EASY, MEDIUM, HARD; }
	    static class Config {
	        public Difficulty difficulty = Difficulty.MEDIUM;
	        public int undoQuota = 1;
	        public int turnTimerSeconds = 10;
	        public boolean hardwareEnabled = false;
	        public boolean strategicAI = false;
	        public double secondsPer25cm = 1.0;
	        
	    static class Player {
	        public String name;
	        public Piece piece;
	        public Player(String name, Piece piece) { 
	        	this.name = name; 
	        	this.piece = piece; 
	        	}
	            
	        }
	    static class Move {
	    	public final int row, col;
	    	public final Piece piece;
	    	public final Instant ts;
	    	
	    	public Move(int r, int c, Piece p) { 
	    		row = r; col = c; piece = p; ts = Instant.now(); 
	    		}
	    	
	    	public String toCSV() { 
	    		return String.format("%s|%s|%d|%d", ts.toString(), piece, row, col); 
	    		}
	    	
	    	public String toString() { 
	    		return "[" + row + "," + col + "]"; 
	    		}
	            
	    }
	    static class RoundOutcome {
	    	public Optional<Piece> winner;
	    	public List<Move> moves;
	    	public RoundOutcome(Optional<Piece> winner, List<Move> moves) { 
	    		this.winner = winner; 
	    		this.moves = moves; 
	    		}
	            
	        }

	        }
	        
	    }
	}


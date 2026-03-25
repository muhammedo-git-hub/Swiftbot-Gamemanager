package full_method_and_class_implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import full_method_and_class_implementations.Game_Board.Board;

public class AI_Engine {
	private final Random rnd = new Random();
    private final boolean strategic;

    public AI_Engine(boolean strat) { 
    	this.strategic = strat; 
    	}

    public Move chooseMove(Board board, Piece piece) {
        if (!strategic) 
        	return chooseRandom(board, piece);
        
        return chooseMinimax(board, piece);
    }

    private Move chooseRandom(Board board, Piece piece) {
        List<int[]> empties = new ArrayList<>();
        
        for (int r=1;r<=3;r++) 
        	for (int c=1;c<=3;c++) 
        		if (board.at(r,c)==Piece.EMPTY) 
        			empties.add(new int[]{r,c});
        
        if (empties.isEmpty()) 
        	return null;
        
        int[] rc = empties.get(rnd.nextInt(empties.size()));
        return new Move(rc[0], rc[1], piece);
    }

    private Move chooseMinimax(Board board, Piece piece){
        Piece opponent = (piece == Piece.X ? Piece.O : Piece.X);
        int bestScore = Integer.MIN_VALUE;
        Move best = null;
        for (int r=1;r<=3;r++) 
        	for (int c=1;c<=3;c++) {
        		if (board.at(r,c)!=Piece.EMPTY) 
        			continue;
        		
        		Move trial = new Move(r,c,piece);
        		board.applyMove(trial);
        		int score = minimax(board, 0, false, piece, opponent);
        		board.undoLast();
        		if (score > bestScore) { bestScore = score; best = trial; }
        }
        return best != null ? best : chooseRandom(board, piece);
    }

    private int minimax(Board board, int depth, boolean isMax, Piece aiPiece, Piece humanPiece) {
        List<int[]> dump = new ArrayList<>();
        
        Optional<Piece> winner = board.checkWinner(dump);
        if (winner.isPresent()) {
            if (winner.get() == aiPiece) 
            	return 10 - depth;
            
            if (winner.get() == humanPiece) 
            	return depth - 10;
        }
        if (board.isFull()) return 0;
        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Piece turn = isMax ? aiPiece : humanPiece;
        for (int r=1;r<=3;r++) for (int c=1;c<=3;c++) {
            if (board.at(r,c) != Piece.EMPTY) 
            	continue;
            
            Move mv = new Move(r,c,turn);
            board.applyMove(mv);
            int val = minimax(board, depth+1, !isMax, aiPiece, humanPiece);
            board.undoLast();
            
            if (isMax) best = Math.max(best, val); 
            else best = Math.min(best, val);
        }
        return best;
    }
	

}

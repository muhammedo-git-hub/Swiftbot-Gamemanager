package data;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import data.DataTypes.Move;
import data.DataTypes.Piece;

public class Board {
	
		public synchronized void reset()
	
	    public synchronized boolean isEmpty(int r, int c)
		 
		public synchronized boolean applyMove(Move m)
		    
		public synchronized Move undoLast()

	    public synchronized List<Move> MoveHistory()

	    public synchronized Piece at(int r, int c)
		    
		public synchronized Optional<Piece> checkWinner(List<int[]> winningCoordsOut);
		   
		public synchronized boolean isFull()

		public synchronized String toAscii() 
}



package data;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import data.DataTypes.Move;
import data.DataTypes.Piece;

abstract public class Board {
	
		 reset();
		 
		 applyMove();
		    
		 undoLastMove();
		    
		 isCellEmpty();
		    
		 Optional<Piece> checkWinner();
		   
		 isFull();
		 
		 MoveHistory();
		 
		 boardlocation(int, int);
		 
		 asciiBoard();
		   	

}


package NoughtsAndCrosses;


import data.shared_types_and_configuration.Move;
import data.shared_types_and_configuration.Piece;

abstract public class AI_engine {

		abstract static class AIEngine {
			
			abstract public Move chooseMove(Board board, Piece piece);
			
			abstract private Move chooseRandom(Board board, Piece piece);
			
			abstract private Move chooseMinimax(Board board, Piece piece);
			
			abstract private int minimax(Board board, int depth, boolean isMax, Piece aiPiece, Piece humanPiece);
		

	}

	}
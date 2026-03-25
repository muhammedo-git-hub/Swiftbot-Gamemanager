package data;

import data.shared_types_and_configuration.Player;

abstract public class Utility_methods {
		
		abstract private synchronized boolean applyMoveAndUpdateUI(Player p, Move m);
		
		abstract private synchronized boolean handleUndo();
		
		abstract private void executeMotionSequence(int targetRow, int targetCol, String blinkColor, int blinkTimes);
		
		abstract private void executeMotionSequenceForTrace(List<int[]> coords, String blinkColor, int blinkTimes);		

	}




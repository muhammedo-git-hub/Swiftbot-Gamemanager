package full_method_and_class_implementations;

public class update_moves_and_UI {
	
	private synchronized boolean applyMoveAndUpdateUI(Player p, Move m) {
		boolean applied = board.applyMove(m);
		
		if (!applied) return false;
		
		System.out.printf("[%s – %s] moved to square %s\n", p.name, p.piece, m);
        System.out.println(board.toAscii());
        return true;
	}
	

}

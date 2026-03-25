package full_method_and_class_implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game_Board {
	
	static class Board {
		
		private final Piece[][] grid = new Piece[3][3];
        private final Deque<Move> history = new ArrayDeque<>();

        public Board() { reset(); }

        public synchronized void reset() {
            for (int r=0;r<3;r++) for (int c=0;c<3;c++) grid[r][c] = Piece.EMPTY;
            history.clear();
        }
        
        public synchronized boolean isEmpty(int r, int c) {
            return grid[r-1][c-1] == Piece.EMPTY;
        }
        
        public synchronized boolean applyMove(Move m) {
            if (!isEmpty(m.row, m.col)) return false;
            grid[m.row-1][m.col-1] = m.piece;
            history.push(m);
            return true;
        }
        
        public synchronized Move undoLast() {
            if (history.isEmpty()) return null;
            Move m = history.pop();
            grid[m.row-1][m.col-1] = Piece.EMPTY;
            return m;
        }
        
        public synchronized List<Move> getHistory() {
            return new ArrayList<>(history);
            
        }
        public synchronized Piece at(int r, int c) { 
        	return grid[r-1][c-1]; 
        	}
        
        public synchronized Optional<Piece> checkWinner(List<int[]> winningCoordsOut) {
            
            for (int r=0;r<3;r++) {
                
            	if (grid[r][0] != Piece.EMPTY && grid[r][0]==grid[r][1] && grid[r][1]==grid[r][2]) {
                    winningCoordsOut.add(new int[]{r+1,1}); 
                    winningCoordsOut.add(new int[]{r+1,2}); 
                    winningCoordsOut.add(new int[]{r+1,3});
                    
                    return Optional.of(grid[r][0]);
                }
            }
           
            for (int c=0;c<3;c++) {
            
                if (grid[0][c] != Piece.EMPTY && grid[0][c]==grid[1][c] && grid[1][c]==grid[2][c]) {
                    winningCoordsOut.add(new int[]{1,c+1}); 
                    winningCoordsOut.add(new int[]{2,c+1}); 
                    winningCoordsOut.add(new int[]{3,c+1});
                    
                    return Optional.of(grid[0][c]);
                }
            }
            if (grid[0][0]!=Piece.EMPTY && grid[0][0]==grid[1][1] && grid[1][1]==grid[2][2]) {
            	
                winningCoordsOut.add(new int[]{1,1}); 
                winningCoordsOut.add(new int[]{2,2}); 
                winningCoordsOut.add(new int[]{3,3});
                return Optional.of(grid[0][0]);
            }
            if (grid[0][2]!=Piece.EMPTY && grid[0][2]==grid[1][1] && grid[1][1]==grid[2][0]) {
            	
                winningCoordsOut.add(new int[]{1,3}); 
                winningCoordsOut.add(new int[]{2,2}); 
                winningCoordsOut.add(new int[]{3,1});
                return Optional.of(grid[0][2]);
            }
            return Optional.empty();
        }
        
        public synchronized boolean isFull() {
            for (int r=1;r<=3;r++) for (int c=1;c<=3;c++) if (at(r,c) == Piece.EMPTY) return false;
            return true;
        }

        public synchronized String toAscii() {
            StringBuilder sb = new StringBuilder();
            sb.append("      Columns ->   1   2   3\nRows\n");
            for (int r=1;r<=3;r++) {
                sb.append("  ").append(r).append("             ");
                for (int c=1;c<=3;c++) {
                    Piece p = at(r,c);
                    sb.append("[").append(p==Piece.EMPTY?" ":p.toString()).append("] ");
                }
                sb.append("\n");
            }
            return sb.toString();
        }

	}

}

package full_software_implementation;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;


public class NoughtsAndCrosses {

 public static void main(String[] args) {
     Config cfg = new Config();
     for (String a : args) {
         if (a.equalsIgnoreCase("--no-hw")) cfg.hardwareEnabled = false;
         if (a.equalsIgnoreCase("--ai=strategic")) cfg.strategicAI = true;
         if (a.startsWith("--difficulty=")) {
             String v = a.substring("--difficulty=".length()).toLowerCase();
             switch (v) {
                 case "easy" -> cfg.difficulty = Difficulty.EASY;
                 case "hard" -> cfg.difficulty = Difficulty.HARD;
                 default -> cfg.difficulty = Difficulty.MEDIUM;
             }
         }
     }
     GameController controller = new GameController(cfg);
     controller.run();
 }


 enum Piece { X, O, EMPTY; }
 enum Difficulty { EASY, MEDIUM, HARD; }
 static class Config {
     public Difficulty difficulty = Difficulty.MEDIUM;
     public int undoQuota = 1;
     public int turnTimerSeconds = 10;
     public boolean hardwareEnabled = false;
     public boolean strategicAI = false;
     public double secondsPer25cm = 1.0; 
 }

 static class Player {
     public String name;
     public Piece piece;
     public Player(String name, Piece piece) { this.name = name; this.piece = piece; }
 }

 static class Move {
     public final int row, col;
     public final Piece piece;
     public final Instant ts;
     public Move(int r, int c, Piece p) { row = r; col = c; piece = p; ts = Instant.now(); }
     public String toCSV() { return String.format("%s|%s|%d|%d", ts.toString(), piece, row, col); }
     public String toString() { return "[" + row + "," + col + "]"; }
 }

 static class RoundOutcome {
     public Optional<Piece> winner; 
     public List<Move> moves;
     public RoundOutcome(Optional<Piece> winner, List<Move> moves) { this.winner = winner; this.moves = moves; }
 }


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

     public synchronized Piece at(int r, int c) { return grid[r-1][c-1]; }

     /** checks winner and fills winning coords */
     public synchronized Optional<Piece> checkWinner(List<int[]> winningCoordsOut) {
         // rows
         for (int r=0;r<3;r++) {
             if (grid[r][0] != Piece.EMPTY && grid[r][0]==grid[r][1] && grid[r][1]==grid[r][2]) {
                 winningCoordsOut.add(new int[]{r+1,1}); winningCoordsOut.add(new int[]{r+1,2}); winningCoordsOut.add(new int[]{r+1,3});
                 return Optional.of(grid[r][0]);
             }
         }
 
         for (int c=0;c<3;c++) {
             if (grid[0][c] != Piece.EMPTY && grid[0][c]==grid[1][c] && grid[1][c]==grid[2][c]) {
                 winningCoordsOut.add(new int[]{1,c+1}); winningCoordsOut.add(new int[]{2,c+1}); winningCoordsOut.add(new int[]{3,c+1});
                 return Optional.of(grid[0][c]);
             }
         }
      
         if (grid[0][0]!=Piece.EMPTY && grid[0][0]==grid[1][1] && grid[1][1]==grid[2][2]) {
             winningCoordsOut.add(new int[]{1,1}); winningCoordsOut.add(new int[]{2,2}); winningCoordsOut.add(new int[]{3,3});
             return Optional.of(grid[0][0]);
         }
         if (grid[0][2]!=Piece.EMPTY && grid[0][2]==grid[1][1] && grid[1][1]==grid[2][0]) {
             winningCoordsOut.add(new int[]{1,3}); winningCoordsOut.add(new int[]{2,2}); winningCoordsOut.add(new int[]{3,1});
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

 interface SwiftBotInterface {
     void moveTo(int row, int col) throws IOException;
     void blink(String color, int times) throws IOException;
     void traceLine(List<int[]> coords) throws IOException;
     void spinOnce() throws IOException;
     void returnToStart() throws IOException;
     boolean isCalibrated();

 }

 static class MockSwiftBot implements SwiftBotInterface {
     public void moveTo(int row, int col) { System.out.println("[Motion] Mock moving to ["+row+","+col+"]"); }
     public void blink(String color, int times) { System.out.println("[Motion] Mock blink "+color+" x"+times); }
     public void traceLine(List<int[]> coords) { System.out.println("[Motion] Mock trace coords: "+coordsToString(coords)); }
     public void spinOnce() { System.out.println("[Motion] Mock spin once"); }
     public void returnToStart() { System.out.println("[Motion] Mock return to start"); }
     public boolean isCalibrated() { return true; }

     private static String coordsToString(List<int[]> coords) {
         StringBuilder sb = new StringBuilder();
         sb.append("[");
         for (int[] p : coords) sb.append("("+p[0]+","+p[1]+")");
         sb.append("]");
         return sb.toString();
     }
 }


 static class LoggerService {
     private final List<String> entries = Collections.synchronizedList(new ArrayList<>());
     private final Path dir;
     public LoggerService() {
         String home = System.getProperty("user.home");
         dir = Paths.get(home, "Documents", "swiftbot_logs");
         try { Files.createDirectories(dir); } catch (IOException ignored) {}
     }
     public void append(String csv) { entries.add(csv); }
     public synchronized void flushToDisk(String filename) throws IOException {
         if (entries.isEmpty()) return;
         Path path = dir.resolve(filename);
         try (BufferedWriter w = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
             for (String s : entries) w.write(s + System.lineSeparator());
             w.flush();
             entries.clear();
         }
     }
     public String samplePath(String filename) { return dir.resolve(filename).toAbsolutePath().toString(); }
 }


 static class AIEngine {
     private final Random rnd = new Random();
     private final boolean strategic;

     public AIEngine(boolean strat) { this.strategic = strat; }

     public Move chooseMove(Board board, Piece piece) {
         if (!strategic) return chooseRandom(board, piece);
         return chooseMinimax(board, piece);
     }

     private Move chooseRandom(Board board, Piece piece) {
         List<int[]> empties = new ArrayList<>();
         for (int r=1;r<=3;r++) for (int c=1;c<=3;c++) if (board.at(r,c)==Piece.EMPTY) empties.add(new int[]{r,c});
         if (empties.isEmpty()) return null;
         int[] rc = empties.get(rnd.nextInt(empties.size()));
         return new Move(rc[0], rc[1], piece);
     }

     private Move chooseMinimax(Board board, Piece piece){
         Piece opponent = (piece == Piece.X ? Piece.O : Piece.X);
         int bestScore = Integer.MIN_VALUE;
         Move best = null;
         for (int r=1;r<=3;r++) for (int c=1;c<=3;c++) {
             if (board.at(r,c)!=Piece.EMPTY) continue;
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
             if (winner.get() == aiPiece) return 10 - depth;
             if (winner.get() == humanPiece) return depth - 10;
         }
         if (board.isFull()) return 0;
         int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
         Piece turn = isMax ? aiPiece : humanPiece;
         for (int r=1;r<=3;r++) for (int c=1;c<=3;c++) {
             if (board.at(r,c) != Piece.EMPTY) continue;
             Move mv = new Move(r,c,turn);
             board.applyMove(mv);
             int val = minimax(board, depth+1, !isMax, aiPiece, humanPiece);
             board.undoLast();
             if (isMax) best = Math.max(best, val); else best = Math.min(best, val);
         }
         return best;
     }
 }


 static class GameController {
     private final Config cfg;
     private final Board board = new Board();
     private final LoggerService logger = new LoggerService();
     private SwiftBotInterface swiftBot;
     private AIEngine aiEngine;
     private final Scanner in = new Scanner(System.in);

     private Player user, bot, currentPlayer;
     private int userUndoRemaining;
     private int roundNumber = 0;
     private int scoreUser = 0;
     private int scoreBot = 0;

     public GameController(Config cfg) {
         this.cfg = cfg;
         this.swiftBot = cfg.hardwareEnabled ? new MockSwiftBot() : new MockSwiftBot(); // placeholder for real driver
         this.aiEngine = new AIEngine(cfg.strategicAI);
         applyDifficulty(cfg.difficulty);
     }

     private void applyDifficulty(Difficulty d) {
         cfg.difficulty = d;
         switch (d) {
             case EASY -> { cfg.turnTimerSeconds = 0; cfg.undoQuota = 2; }
             case MEDIUM -> { cfg.turnTimerSeconds = 10; cfg.undoQuota = 1; }
             case HARD -> { cfg.turnTimerSeconds = 5; cfg.undoQuota = 0; }
         }
     }


     public void initAndRegister() {
         System.out.println();
         System.out.println("Microsoft Windows [Version 10.0]");
         System.out.println("(c) 2026 SwiftBot Labs. All rights reserved.");
         System.out.println("===========================================");
         System.out.println("   NOUGHTS & CROSSES  — SwiftBot (CLI)");
         System.out.println("===========================================\n");
         System.out.println("Welcome! Play Noughts & Crosses (3x3) vs SwiftBot.");
         System.out.println("Rules (brief):");
         System.out.println(" - Enter moves as row,column using numbers 1..3 (example: 2,3)");
         System.out.println(" - Difficulty sets Undo quota & timer");
         System.out.println(" - Use 'undo' to revert your last move (if available)");
         System.out.print("\nPress SwiftBot Button A (or type anything + Enter) to begin... ");
         in.nextLine(); 


         String name;
         while (true) {
             System.out.print("\nEnter your player name (alphanumeric, 1–30 chars):\n> ");
             name = in.nextLine().trim();
             if (name.matches("[A-Za-z0-9 ]{1,30}")) break;
             System.out.println("[ERROR] Invalid name. Use letters, digits and spaces (1–30).");
         }
         user = new Player(name, Piece.X);
         System.out.println("[OK] Player name set: " + name);

         String botName = "Robo-" + (100 + new Random().nextInt(900));
         System.out.println("SwiftBot default name: \"" + botName + "\"");
         System.out.print("Override SwiftBot name? (Y/N) > ");
         String yn = in.nextLine().trim();
         if (yn.equalsIgnoreCase("Y")) {
             System.out.print("Enter SwiftBot name > ");
             botName = in.nextLine().trim();
         }
         bot = new Player(botName, Piece.O);
         System.out.println("[OK] SwiftBot name: " + botName);
     }


     public void generateAndRollDice() {
         Random r = new Random();
         int ur, br;
         do {
             ur = r.nextInt(6) + 1;
             br = r.nextInt(6) + 1;
             System.out.printf("[%s] rolled: %d\n", user.name, ur);
             System.out.printf("[%s] rolled: %d\n", bot.name, br);
             if (ur == br) System.out.println("Tie — re-rolling...");
         } while (ur == br);

         if (ur > br) {
             user.piece = Piece.O; bot.piece = Piece.X; currentPlayer = user;
         } else {
             bot.piece = Piece.O; user.piece = Piece.X; currentPlayer = bot;
         }
         System.out.printf("Starter: %s (%s)\n", currentPlayer==user ? user.name : bot.name,
                 currentPlayer==user ? user.piece : bot.piece);
     }


     public boolean userTurn() {
  
         System.out.println(board.toAscii());
         System.out.printf("Current turn: %s (%s)\n", currentPlayer==user?user.name:bot.name, currentPlayer.piece);
         System.out.printf("Undo moves left: %d    Time limit: %d seconds\n", userUndoRemaining, cfg.turnTimerSeconds);
         System.out.print("Your move (format row,column) or 'undo' > ");

         ExecutorService ex = Executors.newSingleThreadExecutor();
         Future<String> future = ex.submit(() -> {
             try { return in.nextLine().trim(); } catch (Exception e) { return ""; }
         });
         String input = null;
         try {
             if (cfg.turnTimerSeconds > 0) input = future.get(cfg.turnTimerSeconds, TimeUnit.SECONDS);
             else input = future.get();
         } catch (TimeoutException te) {
             future.cancel(true);
             System.out.println("*** TIME EXPIRED — turn forfeited ***");
             logger.append("FORFEIT,"+user.name+","+Instant.now().toString());
             ex.shutdownNow();
             return false; 
         } catch (Exception e) {
             future.cancel(true);
             System.out.println("[ERROR] Input error. Try again.");
             ex.shutdownNow();
             return false;
         } finally {
             ex.shutdownNow();
         }

         if (input.equalsIgnoreCase("undo")) {
             return handleUndo(); 
         }

    
         String[] parts = input.split(",");
         if (parts.length != 2) {
             System.out.println("[ERROR] Invalid format. Use row,column (e.g., 2,3).");
             return userTurn();
         }
         int r,c;
         try { r = Integer.parseInt(parts[0].trim()); c = Integer.parseInt(parts[1].trim()); }
         catch (NumberFormatException nfe) { System.out.println("[ERROR] Non-numeric input. Try again."); return userTurn(); }
         if (r<1 || r>3 || c<1 || c>3) { System.out.println("[ERROR] Row/Col must be 1..3."); return userTurn(); }
         if (!board.isEmpty(r,c)) { System.out.printf("[ERROR] Square [%d,%d] occupied. Try again.\n", r, c); return userTurn(); }

         Move m = new Move(r,c,user.piece);
         boolean ok = applyMoveAndUpdateUI(user, m);
         return ok;
     }

   
     public void swiftBotTurn() {
         Move m = aiEngine.chooseMove(board, bot.piece);
         if (m == null) { System.out.println("[INFO] No valid moves for SwiftBot."); return; }
         System.out.printf("[%s – %s] will move to square %s\n", bot.name, bot.piece, m);

         try {
             executeMotionSequence(m.row, m.col, "GREEN", 3);
             boolean ok = applyMoveAndUpdateUI(bot, m);
             if (!ok) {
                 System.out.println("[ERROR] Failed to apply SwiftBot move to internal board.");
             }
         } catch (IOException ex) {
             System.out.println("[ERROR] Motion error: " + ex.getMessage());
             logger.append("MOTION_FAILURE,"+ex.getMessage()+","+Instant.now().toString());
         }
     }

    
     public Optional<Piece> checkWinDrawAndAct() {
         List<int[]> winCoords = new ArrayList<>();
         Optional<Piece> w = board.checkWinner(winCoords);
         if (w.isPresent()) {
             Piece winner = w.get();
             if (winner == user.piece) {
                 System.out.println("*** WINNER: " + user.name + " (" + winner + ") ***");
                 try {
                     executeMotionSequenceForTrace(winCoords, "GREEN",3);
                 } catch (IOException e) { System.out.println("[ERROR] Motion during win trace: " + e.getMessage()); }
                 scoreUser++;
             } else {
                 System.out.println("*** WINNER: " + bot.name + " (" + winner + ") ***");
                 try { executeMotionSequenceForTrace(winCoords, "RED",3); } catch (IOException e) { System.out.println("[ERROR] Motion during win trace: " + e.getMessage()); }
                 scoreBot++;
             }
             return Optional.of(winner);
         } else if (board.isFull()) {
             System.out.println("*** ROUND RESULT: DRAW ***");
             try { swiftBot.spinOnce(); swiftBot.blink("BLUE", 3); } catch (IOException e) { /* log */ }
             return Optional.of(Piece.EMPTY);
         } else {
             return Optional.empty();
         }
     }

 
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
         for (int i = hist.size()-1; i>=0; i--) { Move m = hist.get(i); seq.append(m.toCSV()).append(";"); }
         String csvLine = String.join(",", Integer.toString(roundNumber), seq.toString(), result, winnerName, Instant.now().toString());
         logger.append(csvLine);
         
         System.out.println("--- Round summary ---");
         System.out.printf("Round #: %d\nResult: %s\n", roundNumber, result);
         System.out.printf("Scoreboard:\n %s: %d\n %s: %d\n", user.name, scoreUser, bot.name, scoreBot);
     }

     
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

   
     private synchronized boolean applyMoveAndUpdateUI(Player p, Move m) {
         boolean applied = board.applyMove(m);
         if (!applied) return false;
       
         System.out.printf("[%s – %s] moved to square %s\n", p.name, p.piece, m);
         System.out.println(board.toAscii());
         return true;
     }

     
     private synchronized boolean handleUndo() {
         if (userUndoRemaining <= 0) {
             System.out.println("Undo moves remaining: 0. Cannot undo.");
             return false;
         }
         System.out.print("Confirm undo last move? (Y/N) > ");
         String conf = in.nextLine().trim();
         if (!conf.equalsIgnoreCase("Y")) return false;
        
         Move undone = null;
         List<Move> temp = board.getHistory();
         if (temp.isEmpty()) { System.out.println("Nothing to undo."); return false; }
 
         Move last = temp.get(temp.size()-1);
         if (last.piece != user.piece) {
             System.out.println("Last move was not yours. Cannot undo until after your move.");
             return false;
         } else {
             undone = board.undoLast();
         }
         if (undone != null) {
             userUndoRemaining--;
             logger.append("UNDO," + undone.toString() + "," + Instant.now().toString());
             System.out.println("[OK] Last move undone. Undo left: " + userUndoRemaining);
             System.out.println(board.toAscii());
             return true;
         } else {
             System.out.println("Nothing to undo.");
             return false;
         }
     }

   
     private void executeMotionSequence(int targetRow, int targetCol, String blinkColor, int blinkTimes) throws IOException {
        
         if (!swiftBot.isCalibrated()) throw new IOException("Calibration out of bounds");
        
         swiftBot.moveTo(targetRow, targetCol);
         swiftBot.blink(blinkColor, blinkTimes);
         swiftBot.returnToStart();
       
     }

     
     private void executeMotionSequenceForTrace(List<int[]> coords, String blinkColor, int blinkTimes) throws IOException {
         if (!swiftBot.isCalibrated()) throw new IOException("Calibration out of bounds");
         swiftBot.blink(blinkColor, blinkTimes);
         swiftBot.traceLine(coords);
         swiftBot.blink(blinkColor, blinkTimes);
     }

    
     public void run() {
         initAndRegister();
         System.out.print("Choose difficulty (easy/medium/hard) > ");
         String d = in.nextLine().trim().toLowerCase();
         if (d.equals("easy")) applyDifficulty(Difficulty.EASY);
         else if (d.equals("hard")) applyDifficulty(Difficulty.HARD);
         else applyDifficulty(Difficulty.MEDIUM);
         System.out.printf("Difficulty: %s (Undo=%d, Timer=%ds)\n", cfg.difficulty, cfg.undoQuota, cfg.turnTimerSeconds);

         generateAndRollDice();

         boolean keepPlaying = true;
         userUndoRemaining = cfg.undoQuota;

         while (keepPlaying) {
             board.reset();
             boolean roundOver = false;
             while (!roundOver) {
                 if (currentPlayer == user) {
                     boolean applied = userTurn();
                     Optional<Piece> outcome = checkWinDrawAndAct();
                     if (outcome.isPresent()) { appendRoundLogAndUpdateScoreboard(outcome); roundOver = true; break; }
                     currentPlayer = bot;
                 } else {
                     swiftBotTurn();
                     Optional<Piece> outcome = checkWinDrawAndAct();
                     if (outcome.isPresent()) { appendRoundLogAndUpdateScoreboard(outcome); roundOver = true; break; }
                     currentPlayer = user;
                 }
             }
             keepPlaying = playAgainOrQuit();
         }
     }
 }
}

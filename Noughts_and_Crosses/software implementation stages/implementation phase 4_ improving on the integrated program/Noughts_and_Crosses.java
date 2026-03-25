package full_software_implementation;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import swiftbot.Button;
import swiftbot.SwiftBotAPI;


public class Noughts_and_Crosses {
	
    static final String RESET  = "\u001B[0m";
    static final String RED    = "\u001B[31m";
    static final String BLUE = "\u001B[34m";
    static final String GREEN  = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String ORANGE = "\u001B[38;2;255;165;0m";
    static final String PURPLE = "\u001B[35m";
    static final String CYAN = "\u001B[36m";
    static final String MAGENTA = "\u001B[35m";

	public static void main(String[] args) {
		
        GameController game = new GameController();
        game.run();
		
	}
	
	static class GameController {
		
		SwiftBotAPI api;
		
		String[][] InitializeBoard = new String[3][3];
		
        String UserName   = "";
        String SwiftbotName    = "";
        String UserPiece  = "";   
        String SwiftbotPiece   = "";   
        String CurrentTurn = "";
        
        int UserScore   = 0;
        int SwiftbotScore   = 0;
        int RoundNumber = 0;
		
        int LastUserRow = -1; 
        int LastUserCol = -1;
        
        String difficulty = "";
        int UndoMove;
        int CountdownTimer;
        int UndoMovesRemaining;
                
        
        Scanner in = new Scanner(System.in);
        
        Random random = new Random();
        
        boolean SwiftbotOn = false;
        
        List<String> LogRecord = new ArrayList<>();
        
        
        
		public GameController() {
        	
        	try {
                api = SwiftBotAPI.INSTANCE;
                SwiftbotOn = true;
                System.out.println(YELLOW + "[SUCCESS] The swiftBot has been connected successfully." + RESET);
                
            } catch (Exception e) {
            	
                System.out.println(ORANGE + "[WARNING] SwiftBot isnt detected or connected. please try again or use keyboard inputs" + RESET);
                SwiftbotOn = false;
            }
        }
			
			
		public void initAndRegister() {
			
	         System.out.println("===========================================");
	         System.out.println("----------- NOUGHTS & CROSSES -------------");
	         System.out.println("===========================================\n");
	         System.out.println("Welcome! Play Noughts & Crosses 1v1 against SwiftBot.");
	         System.out.println();
	         System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
	         System.out.println("                SUMMARY OF GAME RULES:          ");
	         System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
	         System.out.println(" 1) Enter moves in the format [row,column]. these should be in integer form only.e.g, (1,3) with no spaces in-between. ");
	         System.out.println();       
	         System.out.println(" 2) there will be 3 modes of difficulty (easy/medium/hard)");
	         System.out.println();
	         System.out.println(" 3) there will be a time limit applied only to the medium and hard modes. the easy mode will have no such limit. ");
	         System.out.println();
	         System.out.println(" 4) Use 'undo' to revert your last move (if available)");
	         System.out.println();
	         System.out.println(" 5) user undos vary by difficulty. for instance, hard mode (no undo), medium mode (1 undo) and easy mode (2 undos) "
	         		            + "these undos are here in case the user wants to revise back their choices ");
	         System.out.println();
	         System.out.println(" 6) the user only wins the game when he/she attains 3 'X' or 'O' consecutively. whether horizontal, vertical or diagonal");
	         System.out.println();
	         System.out.println(" 7) if its a tie, the swiftbot will blink blue 3x before and after spinning once");
	         System.out.println("------------------------------------------------------------------------------------------------------------------------------");
	         
	         System.out.print("\n[INPUT] Please press button A on the swiftbot to begin... ");
	         
	         if (SwiftbotOn) {
	        	 
	        	 try {
	        		 api.setButtonLight(Button.A, true);
	        		 boolean [] pressed = { false };
	        		 api.enableButton(Button.A, () -> {
	        			 pressed[0] = true;
	        			 api.disableButton(Button.A);
	        			 api.setButtonLight(Button.A, false);});
	        		 
	        		 while (!pressed[0]) {
	        			 
	        			 Thread.sleep(100);
	        			 
	        		 }
	        		 System.out.println(MAGENTA + "[PROCESS] starting registration...." + RESET);
	        		 	        	 
	        	 } catch (Exception e) {
	        		 in.nextLine();
	        	 }
	         } else {
	        	 
	        	 in.nextLine();
	         }
	         
	         while (true) {
	        	 System.out.println();
	        	 System.out.println("-----------------------------------------------------------------------------");
	             System.out.print("\n[INPUT] Please enter your player name (alphanumeric, 1-15 chars maxinum):\n> ");
	             System.out.println("-----------------------------------------------------------------------------");
	             
	             String name = in.nextLine().trim();
	             if (name.matches("^[a-z0-9]{1,15}$")) 
	            	 break;
	             
	             System.out.println();
	             System.out.println(RED + "*** [ERROR] Invalid username. it cannot be empty, contain spaces, capitals and maximum is 15 characters long. ***" + RESET);
	             System.out.println();
	         
	         }
	         System.out.println(YELLOW + "[SUCCESS] Username set: " + RESET + UserName);
	         System.out.println();
	         System.out.println(MAGENTA + "[PROCESS] rolling the dice now....." + RESET);
	         
	         String random_5_letters = "";
	         int length = 5;
	         
	         for (int i = 0; i < length; i++) {
	        	 int random = (int) (Math.random() * 26);
	        	 
	        	 char randomChar = (char) ('a' + random);
	        	 random_5_letters += randomChar;
	        	 
	         }
	         String botName = random_5_letters + (1000 + new Random().nextInt(1000));
	         System.out.println();
	         System.out.println(MAGENTA + "[PROCESS] Generated name for the swiftbot: \"" + botName + "\"" + RESET);
	         System.out.println();
	         System.out.print("[INPUT] do you want to change SwiftBot name? (yes/no): \n> ");
	         String yn = in.nextLine().trim();
	         
	         if (yn.equalsIgnoreCase("yes")) {
	        	 System.out.println();
	             System.out.print("[INPUT] Enter SwiftBot new name: \n> ");
	             botName = in.nextLine().trim();
	             
	         }
	         System.out.println(YELLOW + "[SUCCESS] your swiftbot name has been set: " + RESET + botName);	         
		}
		
		
		public void rollDice() {
			System.out.println(MAGENTA + "\n[PROCESS] rolling dice now....." + RESET);
			
			int UserRoll, SwiftbotRoll;
			
			do {
				UserRoll = random.nextInt(7);
				SwiftbotRoll = random.nextInt(7);
				
				System.out.println("***************** -- OUTCOME -- ******************");
                System.out.println("[" + UserName + "] rolled a: " + UserRoll);
                System.out.println("[" + SwiftbotName  + "] rolled a: " + SwiftbotRoll);
			
			if (UserRoll == SwiftbotRoll) {
				System.out.println(MAGENTA + "[PROCESS] the dice rolled is a tie. re-rolling......" + RESET);
			}
			
			} while (UserRoll == SwiftbotRoll);
			
			if (UserRoll > SwiftbotRoll) {
				
				UserPiece = "O";
				SwiftbotPiece = "X";
				CurrentTurn = "user";
		
			} else {
                SwiftbotName = "O";
                UserName   = "X";
                CurrentTurn = "bot";
                
			}
			String StarterName = CurrentTurn.equals("user") ? UserName : SwiftbotName;
			String StarterPiece = CurrentTurn.equals("user") ? UserPiece : SwiftbotPiece;
			
			System.out.println(PURPLE + "\n[RESULT] " + StarterName + " goes first with piece: " + StarterPiece + RESET);
        }
		
			  
		public boolean userTurn() {

			boolean[] timedOut = { false };

    		if (CountdownTimer > 0) {
    		    Thread timerThread = new Thread(() -> {
    		        try {
    		            Thread.sleep(CountdownTimer * 1000L);
    		            timedOut[0] = true;
    		        } catch (InterruptedException e) {   		        	  	
    		        }
    		    });
    		    timerThread.setDaemon(true);  
    		    timerThread.start();
    		}
				  
            while (true) {
            	
            	System.out.println();
                System.out.println("-------------------------------------------------------------------------------------------------------");
                System.out.println();
                System.out.println(BLUE + "[INFO] Your turn: " + UserName + " (" + UserPiece + ")" + RESET);
                System.out.println(BLUE + "[INFO] Undos left: " + UndoMovesRemaining + "   Time limit: " + CountdownTimer + "s" + RESET);
                System.out.print("[INPUT] Enter move (row,column) or 'undo':\n> ");
                System.out.println();
                System.out.println("-------------------------------------------------------------------------------------------------------");
                
                if (timedOut[0]) {
                    System.out.println(ORANGE + "\n--- [WARNING] the timer ran out! Your turn is now passed on to the swiftbot. ---" + RESET);
                    LogRecord.add("FORFEIT," + UserName + "," + LocalDateTime.now());
                    return false;   
                    }
                    String input = in.nextLine().trim(); 
                    
                
                if (input.isEmpty()) {
                    continue;
                }
                
                if (input.equalsIgnoreCase("undo")) {
                    handleUndoMoves();
                    continue;
                }
                String[] parts = input.split(",");
                if (parts.length != 2) {
                    System.out.println(RED + "*** [ERROR] Invalid format. Use row,column e.g. 2,3 ***" + RESET);
                    continue;
                }
                
                int row, column;
                try {
                    row = Integer.parseInt(parts[0].trim());
                    column = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e) {
                    System.out.println(RED + "*** [ERROR] Numbers only please. ***" + RESET);
                    continue;
                }

                if (row < 1 || row > 3 || column < 1 || column > 3) {
                    System.out.println(RED + "*** [ERROR] Row and column must each be between 1 and 3. ***" + RESET);
                    continue;
                }

                if (!InitializeBoard[row - 1][column - 1].equals(" ")) {
                    System.out.println(RED + "*** [ERROR] Square [" + row + "," + column + "] is already taken. Try again. ***" + RESET);
                    continue;
                }

                
                InitializeBoard[row - 1][column - 1] = UserPiece;
                LastUserRow = row;    
                LastUserCol = column;
                System.out.println(PURPLE + "[RESULT] " + UserName + " (" + UserPiece + ") moved to [" + row + "," + column + "]" + RESET);
                
                LogRecord.add(UserName + "," + UserPiece + "," + row + "," + column + "," + LocalDateTime.now());
                return true;
            }      	
            }
		
		public void handleUndoMoves() {
			
			if (UndoMovesRemaining <= 0) {
                System.out.println(ORANGE + "--- [WARNING] No undos remaining. ---" + RESET);
                return;
            }
            if (LastUserRow == -1) {
                System.out.println(ORANGE + "--- [WARNING] No move to undo yet. ---" + RESET);
                return;
            }
            System.out.print("[INPUT] Confirm undo? (yes/no):\n> ");
            String conf = in.nextLine().trim();
            if (!conf.equalsIgnoreCase("yes")) {
                return;
            }


            InitializeBoard[LastUserRow - 1][LastUserCol - 1] = " ";
            UndoMovesRemaining--;
            LastUserRow = -1;
            LastUserCol = -1;
            System.out.println(YELLOW + "[SUCCESS] Move undone. Undos left: " + UndoMovesRemaining + RESET);
            DisplayBoard();
			
		}
		
		public void SwiftBotTurn() {
			
			List<int[]> emptySquares = new ArrayList<>();
            for (int r = 1; r <= 3; r++) {
                for (int c = 1; c <= 3; c++) {
                    if (InitializeBoard[r - 1][c - 1].equals(" ")) {
                        emptySquares.add(new int[]{ r, c });
                    }
                }
            }
            if (emptySquares.isEmpty()) {
                return;   
            }
            
            int[] chosen = emptySquares.get(random.nextInt(emptySquares.size()));
            int r = chosen[0];
            int c = chosen[1];
            
            System.out.println(BLUE + "\n[INFO] " + SwiftbotName + " (" + SwiftbotPiece + ") will move to [" + r + "," + c + "]" + RESET);
            
            if (SwiftbotOn) {
            	
            	try {
            		 int MilliSecPerCell = 1000;
            		 
            		 api.move(100, 100, r * MilliSecPerCell);
                     Thread.sleep(300);
                     api.move(100, -100, c * MilliSecPerCell);
                     Thread.sleep(300);
                     
                     BlinkLEDlights("GREEN", 3);
                     
                     api.move(-100, 100, c * MilliSecPerCell);
                     Thread.sleep(300);
                     api.move(-100, -100, r * MilliSecPerCell);
                     Thread.sleep(300);
                     
            	} catch (Exception e) {
            		System.out.println(RED + "*** [ERROR] SwiftBot motion error: " + e.getMessage() + " ***" + RESET);
            	}
            }
            InitializeBoard[r - 1][c - 1] = SwiftbotPiece;
            System.out.println(PURPLE + "[RESULT] " + SwiftbotName + " (" + SwiftbotPiece + ") moved to [" + r + "," + c + "]" + RESET);
            LogRecord.add(SwiftbotName + "," + SwiftbotPiece + "," + r + "," + c + "," + LocalDateTime.now());
            DisplayBoard();
			
		}
		
		public String CheckWinOrDraw() {
			
			for (int r = 0; r < 3; r++) {
                if (!InitializeBoard[r][0].equals(" ")
                        && InitializeBoard[r][0].equals(InitializeBoard[r][1])
                        && InitializeBoard[r][1].equals(InitializeBoard[r][2])) {
                    return InitializeBoard[r][0];  
                }
            }
			for (int c = 0; c < 3; c++) {
                if (!InitializeBoard[0][c].equals(" ")
                        && InitializeBoard[0][c].equals(InitializeBoard[1][c])
                        && InitializeBoard[1][c].equals(InitializeBoard[2][c])) {
                    return InitializeBoard[0][c];
                }
            }

           
            if (!InitializeBoard[0][0].equals(" ")
                    && InitializeBoard[0][0].equals(InitializeBoard[1][1])
                    && InitializeBoard[1][1].equals(InitializeBoard[2][2])) {
                return InitializeBoard[0][0];
            }
            if (!InitializeBoard[0][2].equals(" ")
                    && InitializeBoard[0][2].equals(InitializeBoard[1][1])
                    && InitializeBoard[1][1].equals(InitializeBoard[2][0])) {
                return InitializeBoard[0][2];
            }

            
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (InitializeBoard[r][c].equals(" ")) {
                        return null;
                    }
                }
            }
            return "DRAW";
			
		}
		
		
		void RoundOutcome(String result) {
			if (result.equals("DRAW")) {
				System.out.println("\n***** DRAW! *****");
			
			if (SwiftbotOn) {
                Spin();
                BlinkLEDlights("BLUE", 3);
			}
			
			} else if (result.equals(UserPiece)) {
				System.out.println("\n***** " + UserName + " WINS! *****");
			    UserScore++;
            
            if (SwiftbotOn) {
                BlinkLEDlights("GREEN", 3);
            }
            } else {
                System.out.println("\n***** " + SwiftbotName + " WINS! *****");
                SwiftbotScore++;
                
                if (SwiftbotOn) {
                	BlinkLEDlights("RED", 3);
                }
            }
			}

		public boolean PlayAgainOrQuit() {
			System.out.println("\n-----------------------------------------------------------------------------------------");
	        System.out.println("[INPUT] Please press button Y to play again or X to quit or type (X/Y) and press Enter:/n> ");
	        System.out.println("-------------------------------------------------------------------------------------------");
	        
	        char choice = '?';

            if (SwiftbotOn) {
                try {
                	 api.setButtonLight(Button.Y, true);  
                     api.setButtonLight(Button.X, true);
                     
                     boolean[] pressed = { false };
                     char[]    result  = { '?' };
                     
                     api.enableButton(Button.Y, () -> {
                         if (!pressed[0]) {
                             result[0]  = 'Y';
                             pressed[0] = true;
                         }
                     });
                     api.enableButton(Button.X, () -> {
                         if (!pressed[0]) {
                             result[0]  = 'X';
                             pressed[0] = true;
                         }
                     });
                     
                     while (!pressed[0]) {
                         Thread.sleep(100);
                     }

               
                     api.disableAllButtons();
                     api.setButtonLight(Button.Y, false);
                     api.setButtonLight(Button.X, false);
                     choice = result[0];
                     System.out.println(CYAN + "[ACTION] Button " + choice + " pressed." + RESET);

                } catch (Exception e) {
                    
                    System.out.print("[INPUT] Enter Y or X:\n> ");
                    String s = in.nextLine().trim().toUpperCase();
                    choice = s.isEmpty() ? '?' : s.charAt(0);
                }
                
                } else {
                    
                    System.out.print("[INPUT] Enter Y or X:\n> ");
                    String s = in.nextLine().trim().toUpperCase();
                    choice = s.isEmpty() ? '?' : s.charAt(0);
                }
            
            return choice == 'Y';
   
			 
		}
		public void BlinkLEDlights(String LEDcolor, int NumTimes) {
			if (!SwiftbotOn) {
                System.out.println(GREEN + "[MOTION] Blink " + LEDcolor + " x" + NumTimes + RESET);
                return;
            }
            int[] rgb;
            if      (LEDcolor.equals("GREEN"))  rgb = new int[]{ 0,   255, 0   };
            else if (LEDcolor.equals("RED"))    rgb = new int[]{ 255, 0,   0   };
            else if (LEDcolor.equals("BLUE"))   rgb = new int[]{ 0,   0,   255 };
            else                             rgb = new int[]{ 255, 255, 0   };

            try {
                for (int i = 0; i < NumTimes; i++) {
                    api.fillUnderlights(rgb);
                    Thread.sleep(300);
                    api.disableUnderlights();
                    Thread.sleep(300);
                }
            } catch (Exception e) {
                System.out.println(RED + "*** [ERROR] Blink failed: " + e.getMessage() + " ***" + RESET);
            }
			 
		 }
		 public void Spin() {
			 if (!SwiftbotOn) {
	                System.out.println(GREEN + "[MOTION] Spin once" + RESET);
	                return;
	            }
	            try {
	                api.move(100, -100, 600);  
	                Thread.sleep(100);
	                api.move(100, -100, 600);   
	            } catch (Exception e) {
	                System.out.println(RED + "*** [ERROR] Spin failed: " + e.getMessage() + " ***" + RESET);
	            }
			 
		 }
		 
		 String winner, result;
		 public void DisplayScore() {
	            
	            System.out.println("\n-------- ROUND SUMMARY --------");
	            System.out.println("Round #: " + RoundNumber);
	            if (result.equals("DRAW")) {
	                System.out.println("Result:  DRAW");
	            } else {
	                System.out.println("Result:  " + winner + " wins!");
	            }
	            System.out.println("\n ---------- SCOREBOARD ---------");
	            System.out.println("  " + UserName + ": " + UserScore);
	            System.out.println("  " + SwiftbotName  + ": " + SwiftbotScore);
	            System.out.println("--------------------------------\n");
	        }
		 
	     public void SaveLog() {
	    	 
	    	 RoundNumber++;    
	            if (result.equals("DRAW")) {
	                winner = "DRAW";
	            } else if (result.equals(UserPiece)) {
	                winner = UserName;
	            } else {
	                winner = SwiftbotName;
	            }
	            
	            String logLine = "Round " + RoundNumber + ","
	                    + winner + ","
	                    + result + ","
	                    + LocalDateTime.now().toString();
	            LogRecord.add(logLine);
	     }
	            
	      
		public void LogFile() {
			String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
            String filename  = "noughts_log_" + timestamp + ".txt";
            try {
                FileWriter fw = new FileWriter(filename, true);  
                for (String entry : LogRecord) {
                    fw.write(entry + "\n");
                }
                fw.close();
                System.out.println(YELLOW + "[SUCCESS] Log saved to: " + filename + RESET);
            } catch (IOException e) {
                System.out.println(RED + "*** [ERROR] Could not save log: " + e.getMessage() + " ***" + RESET);
            }
		}
        public void run() {
   			 
   			 initAndRegister();
   			 
   	         System.out.println();
   	         System.out.print("[INPUT] Choose difficulty (easy / medium / hard):\n> ");
   	         String d = in.nextLine().trim().toLowerCase();
   	         
   	         if (d.equals("easy")) {
   	                difficulty = "easy";
   	                UndoMove = 2;
   	                CountdownTimer = 0;
   	                
   	         } else if (d.equals("hard")) {
   	             difficulty = "hard";
   	             UndoMove = 0;
   	             CountdownTimer = 5;
   	                
   	         } else {
   	             difficulty = "medium";
   	             UndoMove = 1;
   	             CountdownTimer = 10;
   	         }
   	         
   	        System.out.println(BLUE + "[INFO] Difficulty selected: " + difficulty + " | Undo moves allowed attempts: " + UndoMove + " | Timer: " + CountdownTimer + "s" + RESET);
   	        
   	        rollDice();
   	        
   	        boolean keepPlaying = true;
   	        while (keepPlaying) {
   	        	
   	        	for (int r = 0; r < 3; r++) {
                       for (int c = 0; c < 3; c++) {
                    	   InitializeBoard[r][c] = " ";
                       }
   	        	}
   	        	UndoMovesRemaining = UndoMove;
                   LastUserRow   = -1;
                   LastUserCol   = -1;
                   
                   boolean roundOver = false;
                   while (!roundOver) {

                       if (CurrentTurn.equals("user")) {
                           userTurn();
                           
                           CurrentTurn = "bot";
                           
                       } else {
                           SwiftBotTurn();
                           
                           CurrentTurn = "user";
                       }
                       String result = CheckWinOrDraw();
                       
                       if (result != null) {
                           RoundOutcome(result);
                           DisplayScore();
                           SaveLog();
                           roundOver = true;
                           }         
   	            }
                   keepPlaying = PlayAgainOrQuit();	
   	        }
               LogFile();
               System.out.println();
               System.out.println(MAGENTA + "[PROCESS] Program terminating....." + RESET);
               System.out.println();
               System.out.println(YELLOW + "------- SEE YOU LATER! -------" + RESET);
             
                
   		}
    		public void DisplayBoard() {
    			System.out.println();
                System.out.println("      Columns ->   1   2   3");
                System.out.println("Rows");
                for (int r = 0; r < 3; r++) {
                    System.out.print("  " + (r + 1) + "             ");
                    for (int c = 0; c < 3; c++) {
                        System.out.print("[" + InitializeBoard[r][c] + "] ");
                    }
                    System.out.println();
                }
                System.out.println();
    		}			
		}			

}

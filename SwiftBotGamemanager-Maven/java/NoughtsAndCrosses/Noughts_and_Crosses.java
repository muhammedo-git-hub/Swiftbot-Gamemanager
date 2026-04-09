package NoughtsAndCrosses;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import swiftbot.Button;
import swiftbot.SwiftBotAPI;

/**
 * @author Albert Guo
 * Last updated: 15:25, 07/03/2026
 * PURPOSE OF PROGRAM: this program commands the Swiftbot to perform a 1v1 game with the user 
 * in a game of noughts and crosses.
 * FUNCTIONALITIES: it handles user input via swiftbot buttons, robot movement and handles general game logic  
 */

/** 
 * the class for the overall program. this class contains, including the GameController class, which has all the game logic, swiftbot movements, button event handling
 * and the ASCII colour escape codes to colour categories of outputs for organization and clarity 
 */
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
    static final String LIGHT_GREEN = "\u001B[38;2;144;238;144m";
    static final String LIGHT_BLUE = "\u001B[94m";
    static final String LIGHT_PURPLE = "\u001B[95m";
    /**
     * main method that calls and creates a new instance of the static class 'GameController' 
     * and assigns it to the variable 'game'.
     * then calls the 'run' method for that instance and acts as the main entry point to the program
     */

	public static void main(String[] args) {
		
		CoreGameLogic game = new CoreGameLogic();
		game.run();
		
	}
	/**
	 * this is the main class that handles the bulk of the game's central logic
	 * the @private access modifier are there to show that these declared variables belong
	 * only to CoreGameLogic and can only be accessed within that class itself. (Encapsulation)
	 */
static class CoreGameLogic {
		
		/*provides access to all swiftbot hardware functionalities in the swiftbotAPI*/
		private SwiftBotAPI api;
		
		/*a string 2D array called 'InitializeBoard' representing a 3x3 gameboard for storing X/O positions*/
		private String[][] InitializeBoard = new String[3][3];
		
		/*represents the username entered by the player*/
        private String UserName = "";
        
        /*the randomly generated username assigned to the swiftbot*/
        private String SwiftbotName = "";
        
        /*randomly assigns game pieces (X or O) based on dice roll and assigns to the user and swiftbot*/
        private String UserPiece  = "";   
        private String SwiftbotPiece   = "";  
            
        /*the first turn the player takes after winning the dice roll*/
        private String CurrentTurn = "";
        
        /*increments the score number every time the player/swiftbot wins for each round*/
        private int UserScore = 0;
        private int SwiftbotScore = 0;
        
        /*assigns the results of the dice roll to these variables*/
        private int UserRoll = 0;
        private int SwiftbotRoll = 0;
        
        /*increments the round number every time a round is completed*/
        private int RoundNumber = 0;
		
        /*reverses a occupied square back to its original state via undoing moves*/
        private int LastUserRow = -1; 
        private int LastUserCol = -1;
        
        /*three modes of difficulty: easy/medium/hard*/
        private String difficulty = "";
        
        /*reverse moves to go back to the original state of the board*/
        private int UndoMove;
        
        /*time limit for moves for only the medium and hard modes*/
        private int CountdownTimer;
        
        /*the number of nndos the user has left*/
        private int UndoMovesRemaining;
                
        /*new scanner instance for reading and responding to user inputs from keyboard*/
        private Scanner in = new Scanner(System.in);
        
        /*new random instance to roll a dice to determine turns*/
        private Random random = new Random();
        
        /*sets the conditions for swiftbot connection/availability to be false to ensure 
          that the program doesn't mistakenly thinks that the swiftbot is connected*/
        private boolean SwiftbotOn = false;
        
        /*an array list of strings containing the results from each round and assigns them to an 
          array list as string*/
        private List<String> LogRecord = new ArrayList<>();

        /**
         * detects if the swiftbot is connected and detects any exceptions that may cause it to do so otherwise
         */
		public CoreGameLogic() {
        	
        	try {
                api = SwiftBotAPI.INSTANCE; /*an instance of swiftbotAPI that checks if the swiftbot is available*/
                SwiftbotOn = true;
                System.out.println(YELLOW + "[SUCCESS] The swiftBot has been connected successfully." + RESET);
                
            } catch (Exception e) {
            	
                System.out.println(ORANGE + "[WARNING] SwiftBot isn't detected or connected. please try again or use keyboard inputs" + RESET);
                SwiftbotOn = false;
            }
        }	
		/**
		 * displays the game rules, handles button even handling, registers the user and inputting, assigning and generating
		 * a username for both user and swiftbot
		 * implements input validation rules for usernames by regex
		 */
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
	        		 	        	 
	        	 } catch (Exception e) {
	        		 in.nextLine();
	        	 }
	         } else {
	        	 
	        	 in.nextLine();
	         }
	         
	         while (true) {
	        	 System.out.println();
	        	 System.out.println(MAGENTA + "[PROCESS] starting registration...." + RESET);
	        	 System.out.println();
	        	 System.out.println("-----------------------------------------------------------------------------");
	             System.out.print("[INPUT] Please enter a username (alphanumeric, 1-15 chars maxinum):\n> ");
	             System.out.println(LIGHT_GREEN);
	             System.out.println("-----------------------------------------------------------------------------");
	             
	             UserName = in.nextLine().trim();
	             if (UserName.matches("^[a-z0-9]{1,15}$")) 
	            	 break;
	             
	             System.out.println();
	             System.out.println(RED + "*** [ERROR] Invalid username. it cannot be empty, contain spaces, capitals and maximum is 15 characters long. ***" + RESET);
	             System.out.println();
	         
	         }
	         System.out.println(YELLOW + "[SUCCESS] Username set: " + RESET + UserName);
	         System.out.println();
	         System.out.println(MAGENTA + "[PROCESS] rolling the dice now....." + RESET);
	         System.out.println();
	         
	         String random_5_letters = "";
	         int length = 5;
	         
	         for (int i = 0; i < length; i++) {
	        	 int random = (int) (Math.random() * 26);
	        	 
	        	 char randomChar = (char) ('a' + random);
	        	 random_5_letters += randomChar;
	        	 
	         }
	         SwiftbotName = random_5_letters + (1000 + new Random().nextInt(1000));
	         System.out.println();
	         System.out.println(MAGENTA + "[PROCESS] Generated name for the swiftbot: \"" + SwiftbotName + "\"" + RESET);
	         System.out.println();
	         System.out.print("[INPUT] do you want to change SwiftBot name? (yes/no): \n> ");
	         String yn = in.nextLine().trim();
	         
	         if (yn.equalsIgnoreCase("yes")) {
	        	 System.out.println();
	             System.out.print("[INPUT] Enter SwiftBot new name: \n> ");
	             SwiftbotName = in.nextLine().trim();
	         }
	         
	         else if (yn.equalsIgnoreCase("no")) {
	        	 System.out.println();
	        	 System.out.println(YELLOW + "[SUCCESS] your swiftbot name has been set: " + RESET + SwiftbotName);	
	         
	         } else {
	        	 System.out.println();
	        	 System.out.println("*** [ERROR] Please enter 'yes' for change or 'no' for staying the same ***");
	        	 SwiftbotName = in.nextLine().trim();
	             
	         }
	         	         
		}
		
		/**
		 * rolls a random integer from 1-6 and if either the user/swiftbot goes first if one of them wins
		 * if its a draw, the dice re-rolls until one gets a higher score
		 * displays which goes first based on results
		 */
		public void rollDice() {
			System.out.println(MAGENTA + "\n[PROCESS] rolling dice now....." + RESET);
			
			do {
				UserRoll = random.nextInt(6) + 1;
				SwiftbotRoll = random.nextInt(6) + 1;
				
				System.out.println("***************** -- OUTCOME -- ******************");
                System.out.println("[" + UserName + "]" + " rolled a: " + UserRoll);
                System.out.println(SwiftbotName  + " rolled a: " + SwiftbotRoll);
                System.out.println("**************************************************");
			
			if (UserRoll == SwiftbotRoll) {
				System.out.println(MAGENTA + "[PROCESS] the dice rolled is a tie. re-rolling......" + RESET);
			} 
			
			} while (UserRoll == SwiftbotRoll);	
			if (UserRoll > SwiftbotRoll) {
				
				UserPiece = "O";
				SwiftbotPiece = "X";
				CurrentTurn = "user";
		
			} else {
                SwiftbotPiece = "O";
                UserPiece   = "X";
                CurrentTurn = "bot";
                
			}
			
			String StarterName = CurrentTurn.equals("user") ? UserName : SwiftbotName;
			String StarterPiece = CurrentTurn.equals("user") ? UserPiece : SwiftbotPiece;
			
			System.out.println(PURPLE + "\n[RESULT] " + StarterName + " goes first with piece: " + StarterPiece + RESET);

		}
		/**
		 * user enters move in format [row,column] and implements input validation to check for format
		 * if format is invalid, prompts user to re-enter otherwise accepts the move. 
		 * an option to undo move only if the user had a previous move before, then calls the undo method 
		 * handles the countdown time for the medium and hard modes, 
		 * @return true if a move is successfully accepted and processed
		 */	
		public boolean userTurn() {        
            boolean[] timedOut = { false };

            if (CountdownTimer > 0) {
                System.out.println(YELLOW + "[TIMER] You have " + CountdownTimer + " seconds to make your move." + RESET);
                Thread timerThread = new Thread(() -> {
                    try {
                        Thread.sleep(CountdownTimer * 1000);
                        timedOut[0] = true;
                        System.out.println();
                        System.out.println(ORANGE + "--- [WARNING] the timer is up! Your turn is now passed to the swiftbot. Press ENTER to continue..... " + RESET);
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
                System.out.println(BLUE + "[INFO] Your turn is up now: " + UserName + " [" + UserPiece + "]" + RESET);
                System.out.println();
                System.out.println(BLUE + "[INFO] Undos left: " + UndoMovesRemaining + "   Time limit: " + CountdownTimer + "s" + RESET);
                System.out.println();
                System.out.print("[INPUT] Please enter move in the format (row,column) with a comma please or enter 'undo' to undo moves:\n> ");
                System.out.print(LIGHT_GREEN);
                
                String input = in.nextLine().trim(); 
             
                if (timedOut[0]) {
                    LogRecord.add("FORFEIT," + UserName + " , " + LocalDateTime.now());
                    return false;   
                    }
                                    
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
                System.out.println();
                System.out.println(PURPLE + "[RESULT] " + UserName + " [" + UserPiece + "] " + "moved to [" + row + "," + column + "]" + RESET);
                
                LogRecord.add(UserName + "," + UserPiece + "," + row + "," + column + "," + LocalDateTime.now());
                System.out.println();
                System.out.println(GREEN + "------- GAMEBOARD -------" + RESET);
                CalibrationHandling.DisplayBoard(InitializeBoard); /*calls the class method 'DisplayBoard' from 
                                                                     the class 'CalibrationHandling' without 
                                                                     showing true complexity (Abstraction) */
                return true;
            }      	
        }
		/**
		 * calls the handle undo moves method and only reverts the users move if
		 * they had at least 1 move before, otherwise the undo will fail. 
		 * the user will need to confirm their options 
		 */
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

            InitializeBoard[LastUserRow - 1][LastUserCol - 1] = " "; /*resets the user's last move to return square
                                                                       to  original state*/
            UndoMovesRemaining--; /*deducts the number of moves available*/
            LastUserRow = -1; 
            LastUserCol = -1;
            System.out.println(YELLOW + "[SUCCESS] Move undone. Undos left: " + UndoMovesRemaining + RESET);
            System.out.println();
            System.out.println(GREEN + "------- GAMEBOARD -------" + RESET);
            CalibrationHandling.DisplayBoard(InitializeBoard);
			
		}
		
		/**
		 * swiftbot will randomly select and go to the designated square and will tell 
		 * the user which square via [row,col] it will move to
		 */	
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
            
            int[] chosen = emptySquares.get(random.nextInt(emptySquares.size())); /*swiftbot randomly chooses square via generated moves (2 intergers) */
            int r = chosen[0];
            int c = chosen[1];
            
            System.out.println(BLUE + "\n[INFO] " + SwiftbotName + RESET + "[" + SwiftbotPiece + "]" + " will move to " + "[" + r + "," + c + "]" + RESET);
            
            if (SwiftbotOn) {
            	
            	try {
            		 int MilliSecPerCell = 1000; /*the time value that the swiftbot takes to move per cell at 100% its speed*/
            		 
            		 api.move(100, 100, r * MilliSecPerCell);
                     Thread.sleep(300);
                     api.move(100, -100, c * MilliSecPerCell);
                     Thread.sleep(300);
                     
                     CalibrationHandling.BlinkLEDlights(api, SwiftbotOn,"GREEN", 3);
                     
                     api.move(-100, 100, c * MilliSecPerCell);
                     Thread.sleep(300);
                     api.move(-100, -100, r * MilliSecPerCell);
                     Thread.sleep(300);
                     
            	} catch (Exception e) {
            		System.out.println(RED + "*** [ERROR] SwiftBot motion error: " + e.getMessage() + " ***" + RESET);
            	}
            }
            InitializeBoard[r - 1][c - 1] = SwiftbotPiece;
            System.out.println();
            System.out.println(PURPLE + "[RESULT] " + SwiftbotName + " [" + SwiftbotPiece + "] moved to [" + r + "," + c + "]" + RESET);
            LogRecord.add(SwiftbotName + "," + SwiftbotPiece + "," + r + "," + c + "," + LocalDateTime.now());
            System.out.println();
            System.out.println(GREEN + "------- GAMEBOARD -------" + RESET);
            CalibrationHandling.DisplayBoard(InitializeBoard);
            
		}
		
		/**
		 * evaluates the whole board based on if the user/swiftbot gets 3 in a row vertically, horizontally or diagonally
		 * @return null if one of the 2 players get three consecutive squares in a row
		 * @return "DRAW" if both players don't get 3 in a row 
		 */
		
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
		/**
		 * this method, based on the completed evaluation in the CheckWinOrDraw method, decides if the user/swiftbot ends in 
		 * a win or a draw, and blinks the appropriate LED lights upon printing out the message
		 * @param result - the string that is compared against the results (WIN/DRAW) conditions from this method
		 * and if the string matches one of the 3 conditions, it returns that specific outcome. 
		 */
		public void RoundOutcome(String result) {
			if (result.equals("DRAW")) {
				System.out.println(BLUE + "\n***** IT'S A DRAW! *****" + RESET);
				System.out.println();
				System.out.println();
				System.out.println("*****************************************");
				System.out.println();
				System.out.println(     UserName + " : " + UserScore);
				System.out.println();
				System.out.println(     SwiftbotName + " : " + SwiftbotScore);
				System.out.println();
			
			if (SwiftbotOn) {
				System.out.println();
                System.out.println(BLUE + "[ACTION] blinking in blue 3 times.... " + RESET);
				System.out.println();
				CalibrationHandling.BlinkLEDlights(api, SwiftbotOn, "BLUE", 3);
				System.out.println();
				System.out.println(BLUE + "[MOTION] spinning.... " + RESET);
				System.out.println();
				CalibrationHandling.Spin(api, SwiftbotOn);
                System.out.println();
                System.out.println(BLUE + "[ACTION] blinking in blue 3 times.... " + RESET);
				System.out.println();
				CalibrationHandling.BlinkLEDlights(api, SwiftbotOn, "BLUE", 3);
			}
			
			} else if (result.equals(UserPiece)) {
				System.out.println("\n***** " + GREEN + UserName + RESET + " WINS! *****");
			    UserScore++;
            
            if (SwiftbotOn) {
				System.out.println();
				System.out.println(GREEN + "[ACTION] blinking in green 3 times.... " + RESET);
				System.out.println();
				CalibrationHandling.BlinkLEDlights(api, SwiftbotOn, "GREEN", 3);
            	System.out.println();
				System.out.println(GREEN + "[MOTION] Tracing winning line for: " + RESET + UserName);
				System.out.println();
                api.move(100, 100, 3000); 
				System.out.println(GREEN + "[ACTION] blinking in green 3 times.... " + RESET);
				System.out.println();
				CalibrationHandling.BlinkLEDlights(api, SwiftbotOn, "GREEN", 3);
            }
            
            } else {
                System.out.println("\n***** " + RED + SwiftbotName + RESET + " WINS! *****");
                SwiftbotScore++;
                
                if (SwiftbotOn) {
    				System.out.println();
    				System.out.println(RED + "[ACTION] blinking in red 3 times.... " + RESET);
    				System.out.println();
    				CalibrationHandling.BlinkLEDlights(api, SwiftbotOn, "RED", 3);
                	System.out.println();
    				System.out.println(RED + "[MOTION] Tracing winning line for: " + RESET + SwiftbotName);
    				System.out.println();
                    api.move(100, 100, 3000); 
    				System.out.println(RED + "[ACTION] blinking in red 3 times.... " + RESET);
    				System.out.println();
    				CalibrationHandling.BlinkLEDlights(api, SwiftbotOn, "RED", 3);
                }
            }
			}
		
		/**
		 * asks the user to press or enter X to quit or Y to continue/play again.
		 * button light is one unless the swiftbot is connected and only turns off if 
		 * either button is pressed
		 * displays message on which button is pressed
		 * @return 'Y' for playing again 
		 */

		public boolean PlayAgainOrQuit() {
			System.out.println("\n-----------------------------------------------------------------------------------------");
	        System.out.println("[INPUT] Please press button Y to play again or X to quit (or type (X/Y)) and press Enter:/n> ");
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
                     System.out.println();
                     System.out.println(CYAN + "[ACTION] Button " + choice + " pressed." + RESET);
                     System.out.println();

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
		/**
		 * runs the general program and calls other methods
		 * handles the selection of the games difficulty and then assigns them number of undos and time limit for
		 * each move (easy mode has no time limit)
		 */
        public void run() {
   			      	 
   			 initAndRegister();
   			 
   			 while (true) {
   				 System.out.println();
   				 System.out.print("[INPUT] Choose difficulty (easy / medium / hard):\n> ");
   				 String d = in.nextLine().trim().toLowerCase(); 
   				 
   				 if (d.equals("easy")) {
   					 difficulty = "easy";
   					 UndoMove = 2;
   					 CountdownTimer = 0;
   					 break;
   					 
   				 } else if (d.equals("medium")) {
   				     difficulty = "medium";    
   				     UndoMove = 1;
   				     CountdownTimer = 20;
   				     break;
   						 
   				 } else if (d.equals("hard")) {
   				     difficulty = "hard";
  					 UndoMove = 0;
  					 CountdownTimer = 10; 
  					 break;
   				     
   	             } else {
   	            	 System.out.println(RED + "*** [ERROR] invalid input. please enter (easy/medium/hard) ***"  + RESET);
   	            	 }
   				 }
   			 
   			System.out.println(BLUE + "[INFO] Difficulty selected: " + difficulty + " | Undo moves allowed attempts: " + UndoMove + " | Timer: " + CountdownTimer + "s" + RESET);
   	        
   	        
   	        boolean keepPlaying = true;
   	        while (keepPlaying) {
   	        	
   	        	rollDice();
   	        	
   	        	for (int r = 0; r < 3; r++) {
                       for (int c = 0; c < 3; c++) {
                    	   InitializeBoard[r][c] = " "; /*maintains the board as long as the game is still playing*/
                       }
   	        	}
   	        	
   	        	System.out.println(GREEN + "-------- GAMEBOARD (EMPTY) --------" + RESET); /*displays empty board after initialisation*/
   	        	CalibrationHandling.DisplayBoard(InitializeBoard);
   	        	
   	        	UndoMovesRemaining = UndoMove; /*deducts the number of undos everytime the user uses it*/
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
                           RoundNumber++; 
                           log.SaveLogAndDisplayScore(result, RoundNumber, UserName, SwiftbotName, UserScore, SwiftbotScore, UserPiece, SwiftbotPiece, LogRecord);
                           roundOver = true;
                           }         
   	            }
                   keepPlaying = PlayAgainOrQuit();	
   	        }
               log.LogFileRecord(LogRecord);
               System.out.println();
               System.out.println(MAGENTA + "[PROCESS] Program terminating....." + RESET);
               System.out.println();
               System.out.println(YELLOW + "------- SEE YOU LATER! -------" + RESET);
                           
   		}
			

	}
	
	/**
	 * this class contains and handles the core calibration and movement logic for the Swiftbot to perform
	 * and that other methods from the main class calls upon
	 */
static class CalibrationHandling {
		
		/**
		 * blinks the appropriate LED lights of either GREEN, RED or BLUE upon round conditions
		 * @param LEDcolor is the colour that is activated when certain conditions are met 
		 * @param NumTimes is the number of times that colour is blinked, in this case 3x before and after
		 * catches any exceptions that causes the blinking to fail, and displays an error
		 */
		
		public static void BlinkLEDlights(SwiftBotAPI api, boolean SwiftbotOn, String LEDcolor, int NumTimes) {
			if (!SwiftbotOn) {
                System.out.println(GREEN + "[MOTION] swiftbot will blink " + LEDcolor + " x" + NumTimes + RESET);
                return;
            }
            int[] rgb;
            if (LEDcolor.equals("GREEN"))  
            	rgb = new int[]{ 0,   255, 0   };
            
            else if (LEDcolor.equals("RED"))    
            	rgb = new int[]{ 255, 0,   0   };
            
            else if (LEDcolor.equals("BLUE"))   
            	rgb = new int[]{ 0,   0,   255 };
            
            else                             
            	rgb = new int[]{ 255, 255, 0   };

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
		
		/**
		 * if the swiftbot is not disconnected or unavailable, then it will spin once upon meeting certain conditions
		 * Spins at 100% the robot speed both forwards and backwards and runs for 600 milliseconds (0.6 seconds)
		 * catches any exceptions and prints out that error 
		 */	
		 public static void Spin(SwiftBotAPI api, boolean SwiftbotOn) {
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
  
        /**
         * displays the dimensions of the board labelling 'columns' and 'rows' 
         * 
         */
        public static void DisplayBoard(String[][] InitializeBoard) {
    		System.out.println();
            System.out.println("     Columns ->   1   2   3");
            System.out.println("Rows");
            for (int r = 0; r < 3; r++) {
                System.out.print("  " + (r + 1) + "          ");
                for (int c = 0; c < 3; c++) {
                   System.out.print("[" + InitializeBoard[r][c] + "] ");
                  }
                  System.out.println();
            }
            System.out.println();
    	}			
				
		
	}
static class log {	
	/**
	  * the log saves the results of each round played
	  * displays the score of each player, and the results of the round, ending in WIN or DRAW
	  */		 
     public static void SaveLogAndDisplayScore(String result, int RoundNumber, String UserName, String SwiftbotName, int UserScore, int SwiftbotScore,
    		 String UserPiece, String SwiftbotPiece, List<String> LogRecord ) {
    	  
    	 
    	 String winner;
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
            
	  	
            System.out.println("\n-------- ROUND SUMMARY --------");
            System.out.println("Round number: " + LIGHT_BLUE + RoundNumber + RESET);
            if (result.equals("DRAW")) {
                System.out.println("Result:  [DRAW]");
            } else {
                System.out.println("Result:  " + LIGHT_PURPLE + "[" + winner + "]" + RESET + " WINS!");
            }
            System.out.println("\n----------- SCOREBOARD ----------");
            System.out.println(LIGHT_GREEN + UserName + RESET + ": " + LIGHT_BLUE + UserScore + RESET);
            System.out.println(LIGHT_BLUE + SwiftbotName  + RESET + ": " + LIGHT_BLUE + SwiftbotScore + RESET);
            System.out.println("-----------------------------------\n");

     }
            
    /**
     * the log saves the results and and writes to the file for every round, and then shows the
     * log file path.   
     */
	public static void LogFileRecord(List<String> LogRecord ) {
		String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
       String filename  = "noughts_log_" + timestamp + ".txt";
       try {
           FileWriter fw = new FileWriter(filename, true);  
           for (String entry : LogRecord) {
               fw.write(entry + "\n");
           }
           fw.close();
           System.out.println();
           System.out.println(YELLOW + "[SUCCESS] Log saved to: " + filename + RESET);
       } catch (IOException e) {
           System.out.println(RED + "*** [ERROR] Could not save log: " + e.getMessage() + " ***" + RESET);
       }
	}
	
}
  
}


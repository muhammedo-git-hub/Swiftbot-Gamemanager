package ZigZag;

//Handles all user interface interactions for the Zigzag SwiftBot program
//Displays instructions, prompts user input, and confirms system actions
import java.util.Scanner;

public class UIHandler {
	
/* =====================================================
*  PROGRAM INSTRUCTION DISPLAY
* =====================================================
* Displays the initial instructions explaining how the 
* QR code format should be provided by the user
*/
public static void displayInstructions() { 
	    System.out.println();
 	System.out.println("******************************************");
     System.out.println("******************************************");
     System.out.println();
     System.out.println("** ZIGZAG SWIFTBOT PROGRAM **");
     System.out.println();
     System.out.println("******************************************");
     System.out.println("******************************************");
     System.out.println();
     System.out.println("Please scan a QR code to begin.");
     System.out.println();
     System.out.println("Required format: <Length>-<Sections>");
     System.out.println();
     System.out.println("Example: 40-6");
     System.out.println();
 }

/*=====================================================
* INVALID QR CODE MESSAGE
* =====================================================
* Displays an error message when the scanned QR code 
* fails validation requirements
* 
*/
public static void displayInvalidQR() {	   
		 System.out.println();
	     System.out.println("******************************************");
	     System.out.println();
	     System.out.println("** ERROR: Invalid QR Code **");
	     System.out.println();
	     System.out.println("******************************************");
	     System.out.println();
	     System.out.println("Length must be between 15 and 85 cm.");
	     System.out.println();
	     System.out.println("Number of sections must be even and ≤ 12.");
	     System.out.println();
	     System.out.println("Action Required: Please scan a valid QR code.");
	     System.out.println();	    
	    }

/*=====================================================
* QR DATA CONFIRMATION DISPLAY
* =====================================================
* Displays the validated QR code values that will be 
* used to generate the zigzag movement 
*/
	    public static void displayParsedValues(QRData data) {	    	
	    	System.out.println();
	        System.out.println("----------------------------------------");
	        System.out.println();
	        System.out.println("QR Code Validated Successfully");
	        System.out.println();
	        System.out.println("----------------------------------------");
	        System.out.println();

	        System.out.println("Zigzag section length: " + data.length + " cm");
	        System.out.println();
	       
	        System.out.println("Number of zigzag sections: " + data.sections);
	        System.out.println();

	        System.out.println("----------------------------------------");
	        System.out.println();
	    }
	    
/* =====================================================
* SPEED PROFILE SELECTION
* =====================================================
* Prompts the user to select a speed profile for the zigzag movement 
* 
* S = Slow
* N = Normal
* F = Fast
* 
* if no selection is made, the system defaults to 
* the normal speed profile	    
*/
	   public  static char selectSpeedProfile(Scanner scanner) 
	   {	        
	    	while (true) {	    	
	    		System.out.println();
	    		 System.out.println("******************************************");
	    		 System.out.println();
	    	     System.out.println("** Select Speed Profile **");
	    	     System.out.println();
	    	     System.out.println("******************************************");
	    	     System.out.println();
	    	     System.out.println("[S] Slow");
	    	     System.out.println();
	    	     System.out.println("[N] Normal");
	    	     System.out.println();
	    	     System.out.println("[F] Fast");
	    	     System.out.println();
	    	     System.out.print("Enter your choice: ");
	    	     System.out.println();
	    	     
	    	     String input = scanner.nextLine().trim().toUpperCase();
	    	         	   
	    	        if (input.isEmpty()) {
	    	            System.out.println("No selection made — defaulting to Normal (N).");
	    	            return 'N';
	    	        }
	    	     
	    	     if (input.equals("S") || input.equals("N") || input.equals("F")) {
	    	            return input.charAt(0); // Valid profile
	    	        }

	    	     System.out.println();
              System.out.println("******************************************");
              System.out.println();
	    	     System.out.println("** ERROR: Invalid Selection **");
	    	     System.out.println();
	    	     System.out.println("******************************************");
	    	     System.out.println();
	    	     System.out.println("Please enter:");
	    	     System.out.println();
	    	     System.out.println("S for Slow");
	    	     System.out.println();
	    	     System.out.println("N for Normal");
	    	     System.out.println();
	    	     System.out.println("F for Fast");
	    	     System.out.println();
	    	     System.out.println("========================================"); 
	    	     System.out.println();
	    	}
	    }
	   
/* =====================================================
* SPEED CONFIRMATION DISPLAY
* =====================================================
* Displays the generated wheel speed after the user 
* selects a speed profile
* 	   
*/
	   public static void displaySpeedGenerated(char profile, int speed) {
		   System.out.println();
	        System.out.println("------------------------------------------");
	        System.out.println();
	        System.out.println("Speed profile selected: " + profile);
	        System.out.println();
	        System.out.println("Generated wheel speed: " + speed);
	        System.out.println();
	        System.out.println("------------------------------------------");
	        System.out.println();	    	
	   }
	   
/*=====================================================
* MOVEMENT CONFIRMATION DISPLAY
* =====================================================
* Displays the final zigzag parameters before the 
* robot begins executing the movement 
*/
	    public static void displayMovementConfirmation(QRData data, char profile) {
	    	System.out.println();
	        System.out.println("******************************************");
	        System.out.println();
	        System.out.println("** Zigzag Parameters Confirmed **");
	        System.out.println();
	        System.out.println("******************************************");
	        System.out.println();
	        System.out.println("Section Length: " + data.length + " cm");
	        System.out.println();
	        System.out.println("Number of Sections: " + data.sections);
	        System.out.println();
	        System.out.println("Speed Profile: " + profile);
	        System.out.println();
	        System.out.println("Executing zigzag movement...");
	        System.out.println();
	        System.out.println("******************************************");
	        System.out.println();
	    }  
	    
/*=====================================================
* ZIGZAG COMPLETION MESSAGE
* =====================================================
* Displays confirmation when the zigzag path has 
* finished and the robot begins the return journey	    
*/
	   public static void displayZigzagComplete() {
		   System.out.println();
	        System.out.println("******************************************");
	        System.out.println();
	        System.out.println("** Zigzag Complete **");
	        System.out.println();
	        System.out.println("******************************************");
	        System.out.println();
	        System.out.println("The zigzag path has been completed.");
	        System.out.println();
	        System.out.println("The robot is returning to the start.");
	        System.out.println();
	        System.out.println("******************************************");
	        System.out.println();
	    }
	   
/*=====================================================
* LOGGING CONFIRMATION DISPLAY
* =====================================================
* 	Displays confirmation that journey data has been
* successfully written to the log file  
*/
	public static void displayLoggingConfirmation(String logFilePath) {
			
    System.out.println();
	   System.out.println("******************************************");
	   System.out.println();
	   System.out.println("** Journey Logged **");
	   System.out.println();
	   System.out.println("******************************************");
	   System.out.println();
	   System.out.println("Journey data has been saved successfully.");
	   System.out.println();
	   System.out.println("Log file location: " + logFilePath);
	   System.out.println();
	   System.out.println("******************************************");
	   System.out.println();
	 }
	
/* =====================================================
* PROGRAM REPAET OR EXIT CONTROL
* =====================================================
* Allows the user to be either repeat the program by 
* scanning another QR code or exit the system
*/
 	public static boolean promptRepeatOrExit(Scanner scanner) {
 		
         System.out.println();
 	    System.out.println("========================================");
 	    System.out.println();
 	    System.out.println("** Next Action **");
 	    System.out.println();
 	    System.out.println("========================================");
 	    System.out.println();
 	    System.out.println("Press Y to scan another QR code.");
 	    System.out.println();
 	    System.out.println("Press X to exit the program.");
 	    System.out.println();
 	    System.out.print("Enter choice: ");
 	    System.out.println();

 	    String input = scanner.nextLine().trim().toUpperCase();

 	    if (input.equals("Y")) {
 	        System.out.println("Resetting system for next journey...");
 	        return true;   
 	    }
 	    else if (input.equals("X")) {
 	        System.out.println("Preparing final summary...");
 	        return false;  
 	    }
 	    else {
 	        System.out.println("Invalid input. Please enter Y or X.");
 	        return promptRepeatOrExit(scanner);
 	    }
 	}
}

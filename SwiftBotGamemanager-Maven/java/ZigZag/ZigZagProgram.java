package ZigZag;

import java.util.Scanner;   // Reads user input
import swiftbot.*;         // SwiftBot robot API
/* =====================================================
 *             ZigzagProgram
 * ---------------------------------------------------
 * Main Controller the SwiftBot Zigzag System
 * 
 *  Responsibilities
 *  - Scan QR codes
 *  - Validate inputs
 *  - Execute forward zigzag movement
 *  - Execute return journey
 *  - Log journey information
 *  - Handle User interaction
 *  =====================================================
 */
public class ZigZagProgram {
	
    // Shared Scanner for Command line input
    static Scanner scanner = new Scanner(System.in);
    
   // SwiftBot Hardware Controller
    static SwiftBotAPI swiftbot;
   
    // Logging System for journeys
    static JourneyLogger logger = new JourneyLogger();
    
    // System components 
    static QRScanner qrScanner;
    static Journey forwardJourney;    
    static Journey returnJourney;   
    static SpeedProfiles speedProfile;

    /* =====================================================
     *             Main Program flow
     * -----------------------------------------------------
     * Coordinates QR Scanning, Validation, movement execution and logging
     */ 
    public static void main(String[] args) { 
    	
    	// initialise SwiftBot Hardware 
    	try {
    		swiftbot = SwiftBotAPI.INSTANCE;
    	} catch (Exception e) {
    		System.out.println("SwiftBot not detected!");
    	}
    	
    	// initialise system components 
    	qrScanner = new QRScanner(swiftbot);  	 
    	forwardJourney = new ForwardJourney(swiftbot);   	
        returnJourney = new ReturnJourney(swiftbot);    	
    	speedProfile = new SpeedProfiles();
    
        boolean running = true; 
              
        // Main Program loop
        while (running) {
        	
        // Show instructions
        UIHandler.displayInstructions();  
        
        // Scan QR code
        String qrInput = qrScanner.scanQRCode();
         if (qrInput == null) {
             System.out.println("QR scanning failed. Restarting..."); 
                continue;
            }  
         
         // Validate QR Values   
        QRData data = QRValidator.validateQR(qrInput);             
          if (data == null) {
               UIHandler.displayInvalidQR();
                continue;
            }
         // Show parsed values
         UIHandler.displayParsedValues(data);          
         
         // select speed profile
         char speedProfileChoice = UIHandler.selectSpeedProfile(scanner);
         
         // Generate wheel speed
         int wheelSpeed = speedProfile.generateWheelSpeed(speedProfileChoice);            
         UIHandler.displaySpeedGenerated(speedProfileChoice, wheelSpeed);            
         UIHandler.displayMovementConfirmation(data,speedProfileChoice); 
         
         // Execute forward Journey
         forwardJourney.execute(data, wheelSpeed);   
         
         // Execute return Journey
         returnJourney.execute(data, wheelSpeed);                     
         UIHandler.displayZigzagComplete();
       
         // Log journey data
         boolean logged = logger.writeLog(data, wheelSpeed, speedProfileChoice); 
         if (!logged) {
        	 System.out.println("WARNING: Log File could not be written"); 
         }
         logger.updateStatistics(data);            
         UIHandler.displayLoggingConfirmation(logger.getLogFilePath());           
         
         // Ask user to repeat or exit
         running = UIHandler.promptRepeatOrExit(scanner);   
         
         // Display journey Summary
         logger.displaySummary();   
        }
    }    
}



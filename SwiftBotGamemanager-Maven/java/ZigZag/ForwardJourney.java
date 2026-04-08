package ZigZag;

//Handles forward zigzag traversal of the SwiftBot
import swiftbot.*;

public class ForwardJourney extends Journey{
		 
	// Constructor passes SwiftBot instance to parent Journey class
  public ForwardJourney(SwiftBotAPI swiftbot) {
		 super(swiftbot);
  }
 
 // Executes the forward zigzag movement based on QR data
 @Override
 public void execute(QRData data, int speed) {
  
 // Calculates duration for each section
	 long durationMs = SwiftbotConfig.sectionDurationMs(data.length, speed);
  
	 // Loop through each zigzag section
	 for (int section = 1; section <= data.sections; section++) {

	 // Alternate LED colours for visual feedback green then blue
	 if (section % 2 != 0) {
	 swiftbot.fillUnderlights(SwiftbotConfig.rgb(0, 255, 0)); 
	 } else {
	 swiftbot.fillUnderlights(SwiftbotConfig.rgb(0, 0, 255)); 
	  }

	 // Move forward one section
	  swiftbot.move(speed, speed,(int) durationMs);
	         
	 // Pause 1 second before turning
	  try { Thread.sleep(SwiftbotConfig.PAUSE_MS); } 
	  catch (InterruptedException ignored) {}

	  // Turns after each section EXCEPT the last one
	    if (section < data.sections) {

	    // Odd section turn left
	    if (section % 2 != 0) { 	            	 
	    swiftbot.move(
	    (int)(SwiftbotConfig.TURN_SPEED * SwiftbotConfig.LEFT_TURN_BALANCE),
	    (int)(-SwiftbotConfig.TURN_SPEED),
	     SwiftbotConfig.turn90Ms(speed)); 
	    
	    // Even section turn right
	   } else {
	    swiftbot.move(
	    (int)(-SwiftbotConfig.TURN_SPEED),
	    (int)(SwiftbotConfig.TURN_SPEED * SwiftbotConfig.RIGHT_TURN_BALANCE),
	     SwiftbotConfig.turn90Ms(speed)); 
	             }
	         }
	     }
	 }
}

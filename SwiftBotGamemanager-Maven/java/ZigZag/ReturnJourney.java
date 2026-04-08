package ZigZag;

//Handles return Journey by retracing the zigzag path
import swiftbot.*;

public class ReturnJourney extends Journey{
	
	// Constructor passes SwiftBot instance to parent Journey class
	public ReturnJourney(SwiftBotAPI swiftbot) {
		 super(swiftbot);
	 }
	
	  // Executes return journey based on QR data
	  @Override
	  public void execute(QRData data, int speed) {

	  // Calculates duration for each section	
	  long durationMs = SwiftbotConfig.sectionDurationMs(data.length, speed);

	   // Turn 180 degrees to face starting position
	   swiftbot.move(SwiftbotConfig.TURN_SPEED, - SwiftbotConfig.TURN_SPEED, SwiftbotConfig.TURN_180_MS);
	     
	   // Traverse sections in reverse order
	  for (int section = data.sections; section >= 1; section--) {

	   // Matches LED colour with corresponding forward section green then blue
	   if (section % 2 != 0) {
	       swiftbot.fillUnderlights(SwiftbotConfig.rgb(0, 255, 0));
	    } else {
	        swiftbot.fillUnderlights(SwiftbotConfig.rgb(0, 0, 255));
	    }

	   // Move forward one section back towards start
	    swiftbot.move(speed, speed, (int) durationMs);

	    // Pause 1 second before turning 
	     try { Thread.sleep(SwiftbotConfig.PAUSE_MS); } 
	     catch (InterruptedException ignored) {}

	    // Reverse previous turn except final section
	     if (section > 1) {
	            
	     int forwardTurnSection = section - 1;
	     
     // if forward turn was left, reverse must turn right
	      if (forwardTurnSection % 2 != 0) {	                
	      swiftbot.move(
	       (int)(-SwiftbotConfig.TURN_SPEED),
	       (int)(SwiftbotConfig.TURN_SPEED * SwiftbotConfig.RIGHT_TURN_BALANCE),
	        SwiftbotConfig.turn90Ms(speed));
	      
	      // if forward turn was right, reverse must turn left
	        } else {
	         swiftbot.move(
	         (int)(SwiftbotConfig.TURN_SPEED * SwiftbotConfig.LEFT_TURN_BALANCE),
	         (int)(-SwiftbotConfig.TURN_SPEED),
	          SwiftbotConfig.turn90Ms(speed));
	     }
	     }
	     }
	    // Turn off LEDS when Journey completes
	     swiftbot.disableUnderlights();
	  }  
	  }


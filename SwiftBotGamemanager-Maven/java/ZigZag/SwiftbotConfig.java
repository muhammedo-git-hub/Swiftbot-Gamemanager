package ZigZag;

//Stores SwiftBot movement calibration values and timing helpers
public class SwiftbotConfig {
	
/*  =====================================================
* 	 SWIFTBOT CALIBRATION CONSTANTS
*  =====================================================
*/
	
	// Calculates adjusted duration for a 90 degree turn based on speed
	public static int turn90Ms(int speed) {
	    	
	    	speed = Math.max(speed, 1);
	    	
	    	int ms = (int) Math.round(
	    			TURN_90_MS * (TURN_SPEED / (double) speed)
	    );
	    	return clamp(ms, 150, 1200);
	    }
	  
	// 1 second pause before  each turn
	public static final int PAUSE_MS = 1000;

	// Calibrated turn durations (tested on the SwiftBot)
	public static final int TURN_SPEED = 100;
	public static final int TURN_90_MS  = 125;
	public static final int TURN_45_MS = 170; 
	public static final int TURN_180_MS = 500;   
	
 // motor bias compensation to correct slight hardware turning gift
	public static final double LEFT_TURN_BALANCE =  1.00;
	public static final double RIGHT_TURN_BALANCE = 1.00;
	
	// Calculates adjusted duration for a 45 degree turn
	public static int turn45Ms(int speed) {
		speed = Math.max(speed, 1);
		
		int ms =(int) Math.round(TURN_45_MS*TURN_SPEED/(double) speed);
		
		return clamp(ms, 80,600);		
	}
	
	// Restricts a value within a safe range
	public static int clamp(int v, int min, int max) {
		return Math.max(min, Math.min(max, v));
	}
	
	// Converts section length and speed into movement duration (ms)
	public static long sectionDurationMs(int lengthCm, int speed) {
	    
		// Prevents invalid speed values
	    if (speed <= 0) speed = 50;
	    // simple proportional timing method
	    return (long) ((lengthCm * 20.0) * (60.0 / speed));
	}

	 // Helper for generating RGB colour arrays for SwiftBot LEDs
	public static int[] rgb(int r, int g, int b) {
	    return new int[]{r, g, b};
	}
}

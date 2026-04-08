package ZigZag;

//Handles logging of zigzag journeys and session statistics 
public class JourneyLogger {
	
/* =====================================================
* 	JOURNEY LOGGING SYSTEM
* =====================================================
* Records journey data to a log file and tracks 
* statistics for the current program session
*/
	
	private int journeyCount = 0;
	private double longestJourney = 0;
	private double shortestJourney = Double.MAX_VALUE;
	
	// Path of the log file where journey records are stored
	private String logFilePath = "zigzag_log.txt";
	
 // Writes journey data to the log file path
	public boolean writeLog(QRData data, int speed, char profile) {
		
      System.out.println();
	     System.out.println("==========================================");
	     System.out.println();
	     System.out.println("Starting log process...");
	     System.out.println();
	     System.out.println("==========================================");
	     System.out.println();

	     double straightLineDistance = data.length * data.sections;
	     double forwardDistance = straightLineDistance;
	     long duration = SwiftbotConfig.sectionDurationMs(data.length, speed);
	    		 
	     try (java.io.FileWriter writer = new java.io.FileWriter(logFilePath, true)) {

	         writer.write("=====================================\n");
	         writer.write("New Zigzag Journey\n");
	         writer.write("Section Length: " + data.length + " cm\n");
	         writer.write("Number of Sections: " + data.sections + "\n");
	         writer.write("Speed Profile: " + profile + "\n");
	         writer.write("Wheel Speed: " + speed + "\n");
	         writer.write("Forward Distance: " + forwardDistance + " cm\n");
	         writer.write("Forward Duration: " + duration + " ms\n");
	         writer.write("Straight Line Distance: " + straightLineDistance + " cm\n");
	         writer.write("=====================================\n\n");

	     
	         return true;

	     } catch (Exception e) {
          System.out.println();
	         System.out.println("******************************************");
	         System.out.println();
	         System.out.println("** ERROR: Logging Failed **");
	         System.out.println();
	         System.out.println("******************************************");
	         System.out.println();

	         return false;
	     }
	 }
	
	// Updates session statistics after a journey is completed 
	 public void updateStatistics(QRData data) {
		 
	     double straightLineDistance = data.length * data.sections; 	 
	      // Update global statistics (Flowchart 6 requirement)
	      journeyCount++;

	      if (straightLineDistance > longestJourney) {
	          longestJourney = straightLineDistance;
	      }

	      if (straightLineDistance < shortestJourney) {
	          shortestJourney = straightLineDistance;
	      }
	      }
	 // Returns the path to the log file
	 public String getLogFilePath() {
		 return logFilePath; // encapsulation for log file
	 }

/* =====================================================
* SESSION SUMMARY DISPLAY
* =====================================================
* Displays summary statistics when the program ends
* 	 
*/
	 public void displaySummary() {
		        System.out.println();
		        System.out.println("========================================");
		        System.out.println();
	    	    System.out.println("** Session Summary **");
	    	    System.out.println();
	    	    System.out.println("========================================");
	    	    System.out.println();

	    	    System.out.println("Journeys completed: " + journeyCount);

	    	    if (journeyCount > 0) {
	    	    	System.out.println();
	    	        System.out.println("Longest straight-line distance: " + longestJourney + " cm");
	    	        System.out.println();
	    	        System.out.println("Shortest straight-line distance: " + shortestJourney + " cm");
	    	        System.out.println();
	    	    }
	    	    else {
	    	        System.out.println("No journeys completed.");
	    	    }

	    	    System.out.println("Log file location: " + logFilePath);
             System.out.println();
	    	    System.out.println("========================================");
	    	    System.out.println();
	    	    System.out.println("Program terminated.");
	    	}	
	 }


package data;

import java.util.List;

abstract public interface swiftbot_interface {
	 abstract interface SwiftBotInterface {
		 
		 void moveTo(int row, int col);
		 
	     void blink(String color, int times);
	     
	     void traceLine(List<int[]> coords);
	     
	     void spinOnce();
	     
	     void returnToStart();
	     
	     boolean isCalibrated();
	 }
	 abstract static class SwiftBot implements SwiftBotInterface {
	 	 
		 abstract public void moveTo(int row, int col);
		 
		 abstract public void blink(String color, int times);
		 
		 abstract public void traceLine(List<int[]> coords);
		 
		 abstract public void spinOnce();
		 
		 abstract public void returnToStart();
		 
	     abstract public boolean isCalibrated();
	     
	     abstract private static String coordsToString(List<int[]> coords); 
	 } 

	 }

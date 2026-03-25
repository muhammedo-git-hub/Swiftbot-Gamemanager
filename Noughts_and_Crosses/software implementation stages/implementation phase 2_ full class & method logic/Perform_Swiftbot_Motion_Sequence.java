package full_method_and_class_implementations;

import java.io.IOException;
import java.util.List;

public class Perform_Swiftbot_Motion_Sequence {
		
		private void executeMotionSequence(List<int[]> coords, int targetRow, int targetCol, String blinkColor, int blinkTimes) throws IOException {
			
			if (!swiftBot.isCalibrated()) {
				throw new IOException("Calibration out of bounds");
				
				swiftBot.moveTo(targetRow, targetCol);
				swiftBot.blink(blinkColor, blinkTimes);
				swiftBot.returnToStart();
            
			}
			if (!swiftBot.isCalibrated()) {
				throw new IOException("Calibration out of bounds");
				swiftBot.blink(blinkColor, blinkTimes);
				swiftBot.traceLine(coords);
				swiftBot.blink(blinkColor, blinkTimes);
		}
		
	}
}

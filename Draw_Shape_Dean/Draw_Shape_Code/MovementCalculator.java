public class MovementCalculator {
	// This class is responsible for converting a distance in centimetres
	// into a movement time in milliseconds using calibration values

	private static final double CALIBRATION_COEFFICIENT = 0.6944444;
	// This value comes from the calibration workbook analysis sheet

	private static final int DEFAULT_SPEED_PERCENT = 40;
	// This is the motor speed percentage used for movement calculations

	public int calculateMoveTime(int distanceCm) {
		// This method calculates how long the robot should move
		// to travel the requested distance using calibration data

		double speedCmPerSecond = CALIBRATION_COEFFICIENT * DEFAULT_SPEED_PERCENT;
		// This calculates the robot speed in cm/s from the calibration coefficient

		double timeSeconds = distanceCm / speedCmPerSecond;
		// This calculates the travel time in seconds for the requested distance

		return (int) Math.round(timeSeconds * 1000);
		// This converts the travel time from seconds into milliseconds
	}
	
	public int getRepositionMoveTime() {
		// This method returns the movement time needed to move backwards by 15 cm

		return calculateMoveTime(15);
		// This reuses the main movement calculation for the repositioning requirement
	}
}

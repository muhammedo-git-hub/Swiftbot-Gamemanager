import swiftbot.SwiftBotAPI;
// This imports the SwiftBot API so the robot can move


public class ShapeDrawer {
	// This class is responsible for handling the drawing behaviour of valid shapes
	
	private MovementCalculator movementCalculator = new MovementCalculator();
	// This gives the drawing class access to movement timing calculations
	
	private static final int MOVE_SPEED = 40;
	// This is the motor speed percentage used for straight movement

	private static final int TURN_90_TIME_MS = 800;
	// This is a temporary turn calibration value for a 90-degree turn
	// May need to adjust this on the real SwiftBot during testing
	
	public void moveBackward15cm(int moveTime) {
		// This method moves the SwiftBot backwards by 15 cm using the calculated time

		if (MainProgram.isTerminated()) {
			// Stop immediately if the X button has been pressed
			return;
		}

		try {
			SwiftBotAPI.INSTANCE.move(-MOVE_SPEED, -MOVE_SPEED, moveTime);
			// This moves both wheels backwards for the repositioning time
		} catch (Exception e) {
			// This handles movement interruption safely
			System.out.println("Error: Reposition movement was interrupted.");
		}
	}
	
	private void blinkGreenCompletion() {
		int[] green = {0, 255, 0};

		try {
			SwiftBotAPI.INSTANCE.fillUnderlights(green);
			Thread.sleep(250);
			SwiftBotAPI.INSTANCE.disableUnderlights();
			Thread.sleep(250);
			SwiftBotAPI.INSTANCE.fillUnderlights(green);
			Thread.sleep(250);
			SwiftBotAPI.INSTANCE.disableUnderlights();
		} catch (InterruptedException e) {
			System.out.println("Green completion blink was interrupted.");
		}
	}
	
	public void drawSquare(int sideLength) {
		// This method will handle the drawing steps for a square
		
		System.out.println("Drawing square with side length: " + sideLength + " cm");
		// This will display the square drawing information to the user on the console
		
		int edgeCount = 0;
		// This keeps track of how many square edges have been drawn
		
		while(edgeCount < 4) {
			// This repeats the square drawing steps until all 4 edges are completed
			
			if (MainProgram.isTerminated()) {
				System.out.println("Square drawing stopped by user.");
				
				SwiftBotAPI.INSTANCE.stopMove();
				return;
			}
			
			int moveTime = movementCalculator.calculateMoveTime(sideLength);
			// This calculates the movement time needed for this square edge

			try {
				SwiftBotAPI.INSTANCE.move(MOVE_SPEED, MOVE_SPEED, moveTime);
				// This moves the SwiftBot forward for one square edge

				if (!MainProgram.isTerminated()) {
					// Only turn if the program has not been terminated
					SwiftBotAPI.INSTANCE.move(MOVE_SPEED, -MOVE_SPEED, TURN_90_TIME_MS);
					// This performs a simple 90-degree turn
				}
			} catch (Exception e) {
				// This handles movement interruption safely
				System.out.println("Error: Square drawing was interrupted.");
				SwiftBotAPI.INSTANCE.stopMove();
				return;
				
			}

		    edgeCount++;
		}
		System.out.println("Square drawing complete");
		// This displays a message when all square edges have been processed
		
		System.out.println("All 4 square edges processed successfully.");
		// This gives a clearer completion message for the square drawing sequence
		
		SwiftBotAPI.INSTANCE.stopMove();
		blinkGreenCompletion();
		
		
		
	}
	
	public void drawTriangle(int side1, int side2, int side3) {
		// This method will handle the drawing steps for a triangle
		
		int[] sides = {side1, side2, side3};
		// This stores the three triangle side lengths in an array

		double[] angles = calculateTriangleAngles(side1, side2, side3);
		// This calculates the three internal angles before drawing starts

		int sideIndex = 0;
		// This keeps track of which triangle side is currently being processed

		System.out.println("Drawing triangle with sides: " + side1 + ", " + side2 + ", " + side3);
		// This displays the triangle drawing information to the user

		System.out.println("Calculated triangle angles: " 
				+ Math.round(angles[0]) + ", "
				+ Math.round(angles[1]) + ", "
				+ Math.round(angles[2]));
		// This displays the calculated internal angles to the user
		
		while (sideIndex < 3) {
			// This repeats the triangle drawing steps until all 3 sides are completed
			
			if (MainProgram.isTerminated()) {
				System.out.println("Triangle drawing stopped by user.");
				
				SwiftBotAPI.INSTANCE.stopMove();
				return;
			}

			int currentSide = sides[sideIndex];
			// This gets the current triangle side length

			int moveTime = movementCalculator.calculateMoveTime(currentSide);
			// This calculates the movement time for the current triangle side

			try {
				SwiftBotAPI.INSTANCE.move(MOVE_SPEED, MOVE_SPEED, moveTime);
				// This moves the SwiftBot forward for the current triangle side

				if (!MainProgram.isTerminated()) {
				    double turnAngle = 180 - angles[(sideIndex + 2) % 3];

					int turnTime = calculateTurnTime(turnAngle);
					// This converts the turning angle into a turn duration

					SwiftBotAPI.INSTANCE.move(MOVE_SPEED, -MOVE_SPEED, turnTime);
					// This performs the required turn for the next side
				}
			} catch (Exception e) {
				// This handles movement interruption safely
				System.out.println("Error: Triangle drawing was interrupted.");
				
				SwiftBotAPI.INSTANCE.stopMove();
				return;
			}
			sideIndex++;
			// This moves to the next triangle side
		}

		System.out.println("Triangle drawing complete");
		// This displays a message when all triangle sides have been processed
		
		SwiftBotAPI.INSTANCE.stopMove();
		blinkGreenCompletion();


	}
	
	private double calculateAngle(double oppositeSide, double sideA, double sideB) {
		// This method uses the cosine rule to calculate one internal triangle angle

		double numerator = (sideA * sideA) + (sideB * sideB) - (oppositeSide * oppositeSide);
		// This is the top part of the cosine rule formula
		// A^2 + B^2 - C^2

		double denominator = 2 * sideA * sideB;
		// This is the bottom part of the cosine rule formula
		// 2 * A * B

		double cosineValue = numerator / denominator;
		// This calculates the cosine of the angle

		if (cosineValue > 1) {
			cosineValue = 1;
		} else if (cosineValue < -1) {
			cosineValue = -1;
		}
		// This protects against tiny rounding errors before using acos

		return Math.toDegrees(Math.acos(cosineValue));
		// This converts the angle from radians into degrees
	}
	
	private double[] calculateTriangleAngles(int side1, int side2, int side3) {
		// This method calculates the three internal angles of the triangle

		double angle1 = calculateAngle(side1, side2, side3);
		// This calculates the angle opposite side1

		double angle2 = calculateAngle(side2, side1, side3);
		// This calculates the angle opposite side2

		double angle3 = calculateAngle(side3, side1, side2);
		// This calculates the angle opposite side3

		return new double[] {angle1, angle2, angle3};
		// This returns all three internal angles together
	}
	
	private int calculateTurnTime(double turnAngle) {
		// This converts a turn angle in degrees into a motor turn time in milliseconds

		return (int) Math.round((turnAngle / 90.0) * TURN_90_TIME_MS);
		// This scales the calibrated 90-degree turn time to the required angle
	}
	

}

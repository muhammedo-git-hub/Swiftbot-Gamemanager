import java.awt.image.BufferedImage;
// This is needed because the SwiftBot camera returns a BufferedImage

import swiftbot.SwiftBotAPI;
//Imports the SwiftBot API so the program can communicate with the robot

import swiftbot.Button;
//This lets the program use the SwiftBot physical buttons

import java.util.ArrayList;
// Allows arrays to be used in the main program

import java.io.File;
// Allows the program to access the files

public class MainProgram {
	// My class "called MainProgram" which is the main entry point of the program
	
	private static volatile boolean terminated = false;
	// This stores whether the user has pressed the X button to stop the program
	
	private static ArrayList<String> shapeLog = new ArrayList<>();
	
	private static ArrayList<Long> drawTimes = new ArrayList<>();

	public static boolean isTerminated() {
		// This lets other classes check whether the program has been terminated
		return terminated;
	}

	private static String getQrInputFromSwiftBot() {
		// This method captures a QR image from the SwiftBot and decodes the QR text


		BufferedImage qrImage = SwiftBotAPI.INSTANCE.getQRImage();
		// This captures an image from the SwiftBot camera for QR scanning

		if (qrImage == null) {
			// If the image could not be captured, return an empty string
			
			deleteTemporaryBmpFiles();
			
			return "";
		}
		
		try {
			// Try to decode the QR text from the captured image
			String qrText = SwiftBotAPI.INSTANCE.decodeQRImage(qrImage);
			deleteTemporaryBmpFiles();

			if (qrText == null || qrText.trim().isEmpty()) {
				// If no QR code text was found, return an empty string
				
				return "";
			}

			
			// This confirms the decoded QR text to the user

			return qrText.trim();
			// This returns the cleaned QR text

		} catch (Exception e) {
			// This handles invalid image input safely
			System.out.println("Error: Invalid QR image.");
			return "";
		}
	}
	
	private static void printBanner() {
		System.out.println("==================================================");
		System.out.println("       SwiftBot Draw Shape");
		System.out.println("==================================================");
		System.out.println("Supported shapes : SQUARE, TRIANGLE");
		System.out.println("Input method     : QR Code");
		System.out.println("--------------------------------------------------");
		System.out.println("QR Format Examples:");
		System.out.println("  Single square   : S:30");
		System.out.println("  Single triangle : T:20:30:25");
		System.out.println("  Multiple shapes : S:30&T:20:30:25");
		System.out.println("--------------------------------------------------");
		System.out.println("Press X on the Swiftbot to stop and save the log");
		System.out.println("==================================================");
		
	}
	
	private static void deleteTemporaryBmpFiles() {
		File folder = new File(".");
		File[] files = folder.listFiles();
		if(files == null) {
			return;
		}
		for(File file : files) {
			if(file.isFile()&& file.getName().toLowerCase().endsWith(".bmp")) {
				file.delete();
			}
		}
	}
	
	private static double calculateAngle(double opp, double a, double b) {
	    double cos = ((a * a) + (b * b) - (opp * opp)) / (2 * a * b);
	    if (cos > 1) cos = 1;
	    if (cos < -1) cos = -1;
	    return Math.toDegrees(Math.acos(cos));
	}

	private static double[] calculateTriangleAngles(int s1, int s2, int s3) {
	    double a1 = calculateAngle(s1, s2, s3);
	    double a2 = calculateAngle(s2, s1, s3);
	    double a3 = calculateAngle(s3, s1, s2);
	    return new double[]{a1, a2, a3};
	}// This code is already in my Shape drawer class but in main program to use for the Log
	
	

	public static void main(String[] args) {
		// This is the method that runs first
		
		printBanner();
		
		SwiftBotAPI robot = SwiftBotAPI.INSTANCE;
		// This gets the single SwiftBot controller instance

		robot.enableButton(Button.X, () -> {
			// This enables the X button as an emergency stop

			terminated = true;
			// This marks the program as terminated

			System.out.println("X button pressed. Terminating program.");
			// This informs the user that the stop button was pressed

			robot.stopMove();
			// This immediately stops robot movement
		});

		//SwiftBotAPI robot = SwiftBotAPI.INSTANCE; 
		boolean waitingMessageShown = false;

		while(!terminated) {

		    if (!waitingMessageShown) {
		        System.out.println("--------------------------------------------------");
		        System.out.println("Ready. Please show a QR code to the SwiftBot camera.");
		        System.out.println("Press X on the SwiftBot to stop and save the log.");
		        System.out.println("--------------------------------------------------");
		        waitingMessageShown = true;
		    }

		    String input = getQrInputFromSwiftBot();
		    
		    if (input.isEmpty()) {
		        continue;
		    }

		System.out.println("You entered: " + input);
		// This prints out whatever the user typed , so you can confirm the input was read

		QRParser parser = new QRParser();
		// This creates a new QRParser object so that the main program file can use it in the ParseInput method

		String[] shapes = parser.parseInput(input);
		// This should send the input to the parser and stores the returned shape commands in an array

		if (shapes.length == 0) {
			// If the parser found no usable commands, the program should stop cleanly
			System.out.println("No valid shape commands were found.");
			System.out.println("Please scan another QR code.");
			
			try {
				Thread.sleep(1500);
			}catch(InterruptedException e) {
				System.out.println("Pause interupted");
			}
			
			continue;
		}

		ShapeValidator validator = new ShapeValidator();
		// creates a shape validator object so that the main program can check if each shape is valid

		System.out.println("Parsed shape commands:");
		
		for (String parsedShape : shapes) {
			// This loop prints each parsed shape command so the user can see what was read

			System.out.println(parsedShape);
			// This displays one parsed shape command
		}

		System.out.println("Validation and drawing results:");
		// This heading shows that the program will now validate each shape and then draw valid ones

		ShapeDrawer drawer = new ShapeDrawer();
		// This creates a ShapeDrawer object so MainProgram can send valid shapes to be drawn

		MovementCalculator movementCalculator = new MovementCalculator();
		// This creates a MovementCalculator object so the program can get reposition timing

		StringBuilder logText = new StringBuilder();
		// This stores the execution details before writing them to the log file
		
		int drawnShapeCount = 0;
		// This counts how many valid shapes have actually been drawn

		for (int i = 0; i < shapes.length; i++) {
			// This loop goes through each parsed shape command one by one using an index
			
			if (terminated) {
				// If the user pressed X, stop the loop
				try {
					Thread.sleep(1500);
				}catch(InterruptedException e) {
					System.out.println("Pause interupted");
				}
				break;
			}

			String shape = shapes[i];
			// This gets the current shape command from the array


			boolean valid = validator.isValidShape(shape);
			// This checks whether the current shape command is valid

			System.out.println(shape + " -> " + valid);
			// This prints whether the current command is valid or invalid

			logText.append("Shape command: ").append(shape)
			.append(" | Valid: ").append(valid)
			.append(System.lineSeparator());
			// This records the validation result of each shape command

			if (!valid) {
				// If the command is invalid, it is skipped safely
				System.out.println("Skipping invalid shape command. " + validator.getLastErrorMessage());
				continue;
			}

			if (drawnShapeCount > 0) {
				// This runs before every valid shape after the first one

				int repositionTime = movementCalculator.getRepositionMoveTime();
				// This calculates the time needed to move backwards by 15 cm
				
				
				
				drawer.moveBackward15cm(repositionTime);
				// makes the bot move 15cm backwards

				System.out.println("Move backwards 15 cm before next shape (time: " + repositionTime + " ms)");
				// This shows the repositioning step required before drawing the next shape

				logText.append("Reposition: move backwards 15 cm")
				.append(" | Time: ").append(repositionTime).append(" ms")
				.append(System.lineSeparator());
				// This records the repositioning step in the log
			}
			if (terminated) {
				// Check again after repositioning
				break;
			}

			if (shape.startsWith("S:")) {
				// This checks whether the valid command is a square

				String[] parts = shape.split(":");
				int sideLength = Integer.parseInt(parts[1]);
				// This gets the square side length from the command

				System.out.println("Now drawing: Square with side length " + sideLength + " cm");
				
				long startTime = System.currentTimeMillis();
				drawer.drawSquare(sideLength);
				// This sends the square to the ShapeDrawer class
				long drawTime = System.currentTimeMillis() - startTime;
				
				drawnShapeCount++;
				
				shapeLog.add("Square: " + sideLength + " (time: " + drawTime + " ms)");
				drawTimes.add(drawTime);
				System.out.println("Square complete. Time taken: " + drawTime + " ms");
				logText.append("Draw result: square drawn successfully").append(System.lineSeparator());

			} else if (shape.startsWith("T:")) {
				// This checks whether the valid command is a triangle

				String[] parts = shape.split(":");
				int side1 = Integer.parseInt(parts[1]);
				int side2 = Integer.parseInt(parts[2]);
				int side3 = Integer.parseInt(parts[3]);
				// These get the three triangle side lengths from the command
				
				System.out.println("Now drawing: Triangle with sides " + side1 + ", " + side2 + ", " + side3 + " cm");
				
				
				long startTime = System.currentTimeMillis();
				drawer.drawTriangle(side1, side2, side3);
				// This sends the triangle to the ShapeDrawer class
				long drawTime = System.currentTimeMillis() - startTime;
				
				drawnShapeCount++;
				
				// Calculate angles to include in log
				double[] angles = calculateTriangleAngles(side1, side2, side3);
				String angleStr = String.format("%.2f, %.2f, %.2f", angles[0], angles[1], angles[2]);
				shapeLog.add("Triangle: " + side1 + ", " + side2 + ", " + side3 + " (angles: " + angleStr + "; time: " + drawTime + " ms)");
				drawTimes.add(drawTime);
				System.out.println("Triangle complete. Time taken: " + drawTime + " ms");
				logText.append("Draw result: triangle drawn successfully").append(System.lineSeparator());
			}
		}
		logText.append("Program terminated: ").append(terminated).append(System.lineSeparator());
		// This records whether the program ended normally or by pressing X

		
		if (!terminated) {
			waitingMessageShown = false;  // Reset so the "ready" message shows once next cycle
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("Pause interrupted.");
			}
		}
		
		
		}
		LogWriter logWriter = new LogWriter();
		logWriter.writeLog(shapeLog, drawTimes);
		
		robot.disableAllButtons();
		// This disables the button handlers when the program ends

		System.out.println("Program finished.");
		// This confirms the end of the program

		
	}

}

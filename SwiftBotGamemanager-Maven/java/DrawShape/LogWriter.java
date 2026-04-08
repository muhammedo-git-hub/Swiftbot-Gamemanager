package DrawShape;

import java.io.FileWriter;
//This lets the program write to a text file

import java.io.IOException;
//This lets the program handle file errors safely

import java.io.PrintWriter;
//This makes it easier to write lines into text file

import java.util.ArrayList;
//Allows the use of Arrays in this class

import java.io.File;
//To make the Log into a file


public class LogWriter {
	// This class writes the execution log to a text file when X is pressed

	public void writeLog(ArrayList<String> shapeLog, ArrayList<Long> drawTimes) {
		// This method saves all the shape statistics to the log file

		try (PrintWriter writer = new PrintWriter(new FileWriter("drawshape_log.txt", false))) {
			// Opens the log file for writing, false means it overwrites the old file each time

			if (shapeLog.isEmpty()) {
				// If no shapes were drawn at all, just write a simple message
				writer.println("No shapes were drawn in this session.");

			} else {

				// Section 1 - write every shape drawn, in order, with its draw time
				writer.println("Shapes drawn (in order):");
				System.out.println("Shapes drawn (in order):");
				writer.println("--------------------------------------------------");
				for (int i = 0; i < shapeLog.size(); i++) {
					writer.println("  " + (i + 1) + ". " + shapeLog.get(i));
				}
				writer.println();

				// Section 2 - find the largest shape by area
				writer.println("Largest shape (by area):");
				System.out.println("Largest shape (by area)");
				writer.println("--------------------------------------------------");
				String largest = shapeLog.get(0);
				double largestArea = getArea(shapeLog.get(0));
				for (int i = 1; i < shapeLog.size(); i++) {
					double area = getArea(shapeLog.get(i));
					if (area > largestArea) {
						largestArea = area;
						largest = shapeLog.get(i);
					}
				}
				writer.println("  " + largest);
				writer.println();

				// Section 3 - count which shape type was drawn the most
				writer.println("Most frequently drawn shape:");
				System.out.println("Most frequently drawn shape:");
				writer.println("--------------------------------------------------");
				int squares = 0;
				int triangles = 0;
				for (String entry : shapeLog) {
					if (entry.startsWith("Square")) {
						squares++;
					} else {
						triangles++;
					}
				}
				if (squares > triangles) {
					writer.println("  Square: " + squares + " times");
				} else if (triangles > squares) {
					writer.println("  Triangle: " + triangles + " times");
				} else {
					writer.println("  Square and Triangle: " + squares + " times each");
				}
				writer.println();

				// Section 4 - work out the average draw time across all shapes
				writer.println("Average draw time:");
				System.out.println("Average draw time:");
				writer.println("--------------------------------------------------");
				long total = 0;
				for (long t : drawTimes) {
					total = total + t;
				}
				long average = total / drawTimes.size();
				writer.println("  " + String.format("%,d", average) + " ms");
			}

			writer.println();
			writer.println("==================================================");
			writer.println("End of log");
			writer.println("==================================================");

			// Print the file path to the console so the user knows where the log was saved
			String path = new File("drawshape_log.txt").getAbsolutePath();
			System.out.println("Log file saved to: " + path);

		} catch (IOException e) {
			// This stops the program crashing if the file cannot be written
			System.out.println("Error: Could not write log file.");
		}
	}

	private double getArea(String entry) {
		// This calculates the area of a shape using its log entry string and how it will pasted onto the console

		if (entry.startsWith("Square")) {
			// Square log format: "Square: 30 (time: 7100 ms)"
			// Split by space, take index 1 which is "30:", remove the colon
			String[] parts = entry.split(" ");
			int side = Integer.parseInt(parts[1].replace(":", ""));
			return side * side;
			// Area of a square is side x side

		} else {
			// Triangle log format: "Triangle: 16, 30, 24 (angles: ..."
			// Split by space, index 1 is "16,", index 2 is "30,", index 3 is "24"
			String[] parts = entry.split(" ");
			int s1 = Integer.parseInt(parts[1].replace(",", ""));
			int s2 = Integer.parseInt(parts[2].replace(",", ""));
			int s3 = Integer.parseInt(parts[3].replace(",", "").split("\\(")[0]);
			// s3 may have "(angles:..." attached so split on "(" to get just the number

			// This calculates triangle area from three side lengths
			// s is the semi-perimeter which is half the total peri-meter
			double s = (s1 + s2 + s3) / 2.0;
			return Math.sqrt(s * (s - s1) * (s - s2) * (s - s3));
		}
	}
}
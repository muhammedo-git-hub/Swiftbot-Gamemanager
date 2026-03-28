
public class ShapeValidator {
	
	private String lastErrorMessage = "";
	
	public String getLastErrorMessage() {
		return lastErrorMessage;
	}// For the error message

	public boolean isValidShape(String shape) {
		// This method will check if the shape command entered by the user is valid
		
		lastErrorMessage = ""; // error message value

		if(shape.startsWith("S:")) {
			// This checks if the command is for a square

			String[] parts = shape.split(":");
			//This splits the square command using ':' so each part can be checked

			if(parts.length != 2) {
				// A square command can only have 2 parts, which is 'S' and side length
				
				lastErrorMessage = "Square command must be in the format S:length";
				// error message for the square
				return false;
			}

			try {
				int sideLength = Integer.parseInt(parts[1]);
				// This converts the square side length from text into an integer


				if(sideLength < 15 || sideLength > 85) {
					lastErrorMessage = "Square side length must be between 15 and 85 cm.";
					// the square side length is between 15 and 85
					return false;
				}

				return true;
			} catch (NumberFormatException e) {
				// This returns false if the square side is not valid
				
				lastErrorMessage = "Square side length must be a whole number.";
				return false;
			}
		}


		if(shape.startsWith("T:")) {
			// This checks to see if the command is a triangle

			String[] parts = shape.split(":");
			// Splits the triangle command using ':' so each part can be checked

			if(parts.length != 4) {
				// A triangle must have 4 parts, which are 'T' and the three side lengths
				
				lastErrorMessage = "Triangle command must be in the format T:side1:side2:side3";
				return false;
			}

			try {
				int side1 = Integer.parseInt(parts[1]);
				int side2 = Integer.parseInt(parts[2]);
				int side3 = Integer.parseInt(parts[3]);
				// This converts all three triangle sides from text to integers

				if(side1 <15 || side1 >85 ||side2 <15 || side2 >85 || side3 <15 || side3 >85) {
					// If any of the sides is outside the range, it returns false
					
					lastErrorMessage = "Triangle side lengths must all be between 15 and 85 cm.";
					return false;
				}

				if(!(side1 + side2 > side3 && side1 + side3 > side2 && side2 + side3 > side1)) {
					// is the triangle inequality rule is satisfied
					lastErrorMessage = "These side lengths cannot form a triangle.";
					return false;
				}

				return true;

			} catch (NumberFormatException e) {
				// Returns false if one of the triangle side values is not a valid number
				
				lastErrorMessage = "Triangle side lengths must be whole numbers.";
				return false;
			}
			}



		lastErrorMessage = "Shape must start with S or T.";
		return false;// if the command is not a square or a triangle
	}

}

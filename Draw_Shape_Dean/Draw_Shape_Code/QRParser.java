
public class QRParser {

	// This method takes the full QR input and returns the shape commands
	public String[] parseInput(String qrString) {
		
		String trimmedInput = qrString.trim();
		// Removes the spaces from the beginning and end of the QR input
		
		if(trimmedInput.isEmpty()) {
			// This is to check if the QR input is empty
			System.out.println("Error: QR input is empty");
			// To print onto the console to tell user that the error
			return new String[0];
		}
		
		String[] tokens = trimmedInput.split("&");
		// This splits the QR input into shape commands using '&'
		
		if (tokens.length > 5) {
			System.out.println("Error: Maximum of 5 shapes allowed.");
			return new String[0];
		}// This checks if there is too many shapes (More that 5)
		
		for(int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].trim().toUpperCase();
			
			if(tokens[i].isEmpty()) {
			    // This checks if there is an empty command between '&' symbols
			    System.out.println("Error: Empty command between '&' symbols.");
			    return new String[0];
			}
		}// This trims off the spaces from individual token
		
		return tokens;
		// This returns the final list of the shape commands
		

	}

}

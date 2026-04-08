package ZigZag;

//Handles QR code validation according to flowchart 2
public class QRValidator {
	
/* =====================================================
* QR CODE VALIDATION
* =====================================================
* Validates raw QR String and returns QRData if valid
* Returns null if the QR code does not meet SRS rules
*/
public static  QRData validateQR(String input) {  
	     	
 	// Checks format contains separator '-'
 	if (!input.contains("-")) {
         return null;
     }
 	
 	// Split QR values (length-sections)
     String[] parts = input.split("-");
     if (parts.length != 2) {
         return null;
     }
  
    // Convert values to integers 
     int length;
     int sections;
     
     try {
         length = Integer.parseInt(parts[0]);
         sections = Integer.parseInt(parts[1]);
     } catch (NumberFormatException e) {
         return null; 
     } 
     
     // Validate length range (15-85 cm)
     if (length < 15 || length > 85) {
         return null;
     }
     
    // Validate sections (even number and less than or equal to 12)
     if (sections % 2 != 0 || sections > 12) {
         return null;
     }
     
     // Valid QR -> encapsulate values into QRData object
     return new QRData(length, sections);
 }
}

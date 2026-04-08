package ZigZag;

//Handles QR code scanning using the SwiftBot camera
import java.awt.image.BufferedImage; 
import swiftbot.*;

public class QRScanner {
	
	// SwiftBot controller used for accessing camera functions
	 private SwiftBotAPI swiftbot;
	 
	// Constructor receives SwiftBot instance from main program  
	 public QRScanner(SwiftBotAPI swiftbot) {
		 this.swiftbot = swiftbot;
	 }
	 
     /*  =====================================================
      *      QR CODE SCANNING
      * 	 =====================================================
      */
	 public String scanQRCode() {
         System.out.println();
	        System.out.println("******************************************");
	        System.out.println();
	        System.out.println("Scanning for QR code...");
	        System.out.println();
	        System.out.println("Hold QR steady in front of camera.");
	        System.out.println();
	        System.out.println("******************************************");
	        System.out.println();

	        long startTime = System.currentTimeMillis();
	        long timeout = 15000;

	        while (System.currentTimeMillis() - startTime < timeout) {

	            try {
	            	// Captures image from SwiftBot camera
	                BufferedImage img = swiftbot.getQRImage();
	               
	                // Attempts to decode QR data from image
	                String decoded = swiftbot.decodeQRImage(img);
	                
                 // if valid QR detected return decoded value 
	                if (decoded != null && !decoded.isEmpty()) {
	                    System.out.println("QR Code Detected: " + decoded);
	                    return decoded;
	                }
	                
                 System.out.println();
	                System.out.println("No QR detected. Retrying...");
	                Thread.sleep(1000);
                 
	                // Handles camera or decoding errors
	            } catch (Exception e) {
	                System.out.println("Camera error during QR scan.");
	                return null;
	            }
	        }
           // Timeout reached without detecting a QR code
	        System.out.println("QR scan timed out.");
	        return null;
	    }	 
}

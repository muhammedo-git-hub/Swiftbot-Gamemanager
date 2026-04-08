package ZigZag;

//Data model representing values extracted from the QR code
public class QRData {
	
   // Length of each zigzag section (cm)	
     int length; 
   
   // Number of zigzag sections  
     int sections;  
   
   // Constructor assigns QR values
    public QRData(int length, int sections) {
         this.length = length;
         this.sections = sections;
     }
 }


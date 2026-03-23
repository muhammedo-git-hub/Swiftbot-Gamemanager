// Base class for robot movement behaviours
import swiftbot.*;

 public abstract class Journey {
		
	 // shared SwiftBit controller used by all movement classes
		protected SwiftBotAPI swiftbot;
	 
	 // Constructor assigns SwiftBot instance to subclasses 	
		public Journey(SwiftBotAPI swiftbot) {
			this.swiftbot = swiftbot;
		}
		
     // Abstract method movement implemented by subclasses 
		public abstract void execute(QRData data, int speed);	
	}
package full_method_and_class_implementations;

import java.io.IOException;
import java.util.List;

public interface Swiftbot_Interface {
	
	interface SwiftBotInterface {
        void moveTo(int row, int col) throws IOException;
        void blink(String color, int times) throws IOException;
        void traceLine(List<int[]> coords) throws IOException;
        void spinOnce() throws IOException;
        void returnToStart() throws IOException;
        boolean isCalibrated();
        
    }
static class SwiftBot implements SwiftBotInterface {
		
		public void moveTo(int row, int col) { 
			System.out.println("[Motion] Mock moving to ["+row+","+col+"]"); 
			}
		
        public void blink(String color, int times) { 
        	System.out.println("[Motion] Mock blink "+color+" x"+times); 
        	}
        
        public void traceLine(List<int[]> coords) { 
        	System.out.println("[Motion] Mock trace coords: "+coordsToString(coords)); 
        }
        
        public void spinOnce() { 
        	System.out.println("[Motion] Mock spin once"); 
        	}
    
        public void returnToStart() { 
        	System.out.println("[Motion] Mock return to start"); 
        	}
        
        public boolean isCalibrated() { 
        	return true; 
        	}
        
        private static String coordsToString(List<int[]> coords) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            
            for (int[] p : coords) 
            	sb.append("("+p[0]+","+p[1]+")");
            sb.append("]");
            return sb.toString();
        }
	}

}

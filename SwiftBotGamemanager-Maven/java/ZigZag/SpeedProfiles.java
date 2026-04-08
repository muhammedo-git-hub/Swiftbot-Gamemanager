package ZigZag;

import java.util.Random;

//Handles generation of wheel speed based on predefined speed profiles
public class SpeedProfiles {

//random generator used to vary motor speed within profile ranges	
	private Random random = new Random();
	
/* =====================================================
* 	SPEED PROFILE GENERATION
* =====================================================
* returns a wheel speed based on the selected speed profile 
* 
* S = Slow
* N = Normal
* F = Fast
*/
	public int generateWheelSpeed(char profile) {
     
 	int speed;
 	
 	switch (profile) {
 	
 	// slow speed profile (20-40)
     case 'S':
         speed = 20 + random.nextInt(21);
         break;
     
     // Normal speed profile (50-70)    
     case 'N':
         speed = 50 + random.nextInt(21);
         break;

     // Fast speed profile (80-100)    
     case 'F':
         speed = 80 + random.nextInt(21);
         break;
      
     // Safety fallback if an unknown profile is used    
     default:
         speed = 60;
 }

     return speed;
 }
}

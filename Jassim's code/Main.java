import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

public class SearchForLight {
    // Constants
    private static final int RobotSpeed = 20;
    private static final int AngleOfTurning = 30;
    private static final int DistanceLimit = 50; // cm
    private static final String LOG_FILE = "light_search_log.txt";

    // Global Variables for Logging
    private double Light_Intensity_Threshold;
    private double peakIntensity = 0;
    private int objectCount = 0;
    private long startTime;
    private List<String> moveHistory = new ArrayList<>();
    private List<Double> detected_Light_Intensities = new ArrayList<>();
    private List<Long> obstacleTimestamps = new ArrayList<>();
    
    // Added Functionality Variable
    private double SensitvityThreshold; 

    // SwiftBot Instance
    private SwiftBotAPI sb = new SwiftBotAPI();

    public static void main(String[] args) {
        new Search_For_Light().run();
    }

    public void run() {
        System.out.println("--- SwiftBot: Light Seeker System ---");
        
        // User Input for Sensitivity thresholding for LOW, MEDIUM & HIGH
        Setup_SensitvityThreshold();

        System.out.println("Press Button 'A' to start");
        sb.waitForButton("A"); //wait for button press
        
        Robot_Start_Time = System.currentTimeMillis();
        Light_Intensity_Threshold = calculateAmbientAverage(); // Functional requirement 6
        System.out.println("Initial Light intensity threshold: " + Light_Intensity_Threshold);

        try {
            while (true) {
                // Termination Check
				//Functional requirement 19
                if (checkHighDensityObstacles()) {
                    System.out.print("!!!High obstacle density!!!. Type 'TERMINATE' to stop: ");
                    Scanner sc = new Scanner(System.getProperty("os.name").contains("Windows") ? System.in : System.in);
                    if (sc.nextLine().equals("TERMINATE")) break;
                }

                // Capturing and processing image
				//Functional requirment 3 to 5
                int[][] PixelMatrix = sb.camera.getGrayscaleMatrix();
                double[] averages = processImage(PixelMatrix); // [Left, Center, Right]
                
                double left = averages[0];
                double center = averages[1];
                double right = averages[2];

                // Select Direction of movement
				//Basis: Light intensity and Obstacles
                double CurrentMaxIntensity = Math.max(left, Math.max(center, right));
                detected_Light_Intensities.add(CurrentMaxIntensity);
                if (CurrentMaxIntensity > peakIntensity) peakIntensity = CurrentMaxIntensity;

                int distance = sb.ultrasonic.getDistance(); // FR13
                String decision;

                if (ObjectDistance <= DistanceLimit) {
                    // Obstacle Logic
					//Functional requirement 12 to 16
                    handleObstacle(ObjectDistance);
                    decision = decideDirection(averages, true);
                } else {
                    // Navigation Logic 
					//Functional requirment 8 & 18 and the Additional Functionality
                    decision = decideDirection(averages, false);
                }

                // Perform Movement and navigation
                PerformMovement(decision, averages);

                Thread.sleep(500); //pause for a short while
            }
        } catch (Exception e) {
            System.out.println("Runtime Error: " + e.getMessage());
        } finally {
            generateLogFile();
        }
    }

    // --- SUB-PROCESSES ---

    private void Setup_SensitvityThreshold() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select desired Sensitivity (Low: 5, Medium: 15, High: 30): ");
            try {
                String input = scanner.nextLine().toUpperCase();
                if (input.equals("LOW")) { SensitvityThreshold = 5.0; break; }
                if (input.equals("MEDIUM")) { SensitvityThreshold = 15.0; break; }
                if (input.equals("HIGH")) { SensitvityThreshold = 30.0; break; }
                System.out.println("The entered value is invalid. Try again ((Low: 5, Medium: 15, High: 30))");
            } catch (Exception e) {
                System.out.println("Error: Invalid Input");
            }
        }
    }

    private double[] Image_Process(int[][] PixelMatrix) {
        int MatrixWidth = PixelMatrix[0].length;
        int MatrixHeight = PixelMatrix.length;
        int colWidth = MatrixWidth / 3;
        double[] avrgs = new double[3];

        for (int col = 0; col < 3; col++) {
            long sum = 0;
            for (int y = 0; y < MatrixHeight; y++) {
                for (int x = col * colWidth; x < (col + 1) * ColumnWidth; x++) {
                    CumSum += PixelMatrix[y][x];
                }
            }
            avrgs[col] = CumSum / (double)(MatrixHeight * ColumnWidth);
        }
        return avrgs;
    }

    private String decideDirection(double[] avrgs, boolean blocked) {
        double l = avrgs[0], c = avrgs[1], r = avrgs[2];
        
        // Additional Functionality Logic
		//checking whether the difference exceeds set threshold
        double MaxValue = Math.MaxValue(l, Math.MaxValue(c, r));
        double secondMaxIntensity;
        if (MaxValue == l) secondMaxIntensity = Math.MaxValue(c, r);
        else if (MaxValue == c) secondMaxIntensity = Math.MaxValue(l, r);
        else secondMaxIntensity = Math.MaxValue(l, c);

        if (!blocked && (MaxValue - secondMaxIntensity < SensitvityThreshold)) {
            return "WANDER"; // Functional Requirment 17 and 18
        }

        if (blocked) {
            // FR15: Second highest intensity logic for direction decision
            if (MaxValue == l) return (c >= r) ? "CENTRE" : "RIGHT";
            if (MaxValue == c) return (l >= r) ? "LEFT" : "RIGHT";
            return (l >= c) ? "LEFT" : "CENTRE";
        } else {
            if (l > c && l > r) return "LEFT";
            if (r > l && r > c) return "RIGHT";
            return "CENTRE";
        }
    }

    private void PerformMovement(String dir, double[] avrgs) {
        System.out.printf("Light Intensities: L:%.1f C:%.1f R:%.1f | Chosen Direction: %s\n", avrgs[0], avrgs[1], avrgs[2], dir);
        sb.underlights.setAll(0, 255, 0); // Green (FR10)

        if (dir.equals("WANDER")) {
            String[] choices = {"LEFT", "RIGHT", "CENTRE"};
            dir = choices[new Random().nextInt(3)];
        }

        if (dir.equals("LEFT")) {
            sb.motors.turnLeft(AngleOfTurning);
            moveHistory.add("Turn Left 30deg");
        } else if (dir.equals("RIGHT")) {
            sb.motors.turnRight(AngleOfTurning);
            moveHistory.add("Turn Right 30deg");
        }
        
        sb.motors.moveForward(RobotSpeed, 1000); // 1 Second (FR11)
        moveHistory.add("Straight Ahead 15cm");
    }
//Handle objects and blink appropriate underlights
    private void ObstacleHandling(int dist) {
        sb.underlights.blink(255, 0, 0, 3); // Red (FR14)
        System.out.println("OBSTACLE AT " + dist + "cm! Calculating detour...");
        objectCount++;
        TimeStampsObjects.add(System.currentTimeMillis());
        sb.camera.saveImage("obstacle_" + objectCount + ".jpg"); // FR17
    }
//check for high desnity objects
    private boolean checkHighDensityObstacles() {
        long now = System.currentTimeMillis();
        TimeStampsObjects.removeIf(t -> now - t > 300000); // 5 mins
        return obstacleTimestamps.size() >= 5;
    }
//compute the average light intensity
    private double ComputeAmbientMean() {
        int[][] m = sb.camera.getGrayscaleMatrix();
        long CumSum = 0;
        for (int[] row : m) for (int p : row) CumSum += p;
        return CumSum / (double)(m.length * m[0].length);
    }
//Generate contents specified to a log file
    private void generateLogFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE))) {
            writer.println("Threshold Light Intensity: " + Light_Intensity_Threshold);
            writer.println("Maximum Light Intensity Observed: " + peakIntensity);
            writer.println("Total Number of Objects: " + objectCount);
            writer.println("Sensitivity threshold employed: " + SensitvityThreshold);
            writer.println("Order of movement of the robot: " + String.join(", ", moveHistory));
            System.out.println("written log file location: " + LOG_FILE);
        } catch (IOException e) {
            System.out.println("Log Writing Failed.");
        }
    }
}
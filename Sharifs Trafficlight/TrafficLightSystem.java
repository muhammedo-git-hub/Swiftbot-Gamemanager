import swiftbot.Button;

/**
 * Main system class - coordinates all components
 * Demonstrates: Composition, Association, Dependency Injection
 * Orchestrates: SwiftBotController, TrafficLightDetector, NavigationLogger, ButtonHandler
 */
public class TrafficLightSystem {
    private final SwiftBotController controller;
    private final TrafficLightDetector detector;
    private final NavigationLogger logger;
    private final ButtonHandler buttonHandler;
    
    private volatile boolean running;
    private boolean programStarted;
    private boolean terminationInitiated;
    
    /**
     * Constructor demonstrates Dependency Injection and Composition
     */
    public TrafficLightSystem() {
        this.controller = new SwiftBotController();
        this.detector = new TrafficLightDetector(controller);
        this.logger = new NavigationLogger();
        this.buttonHandler = new ButtonHandler(controller);
        
        this.running = false;
        this.programStarted = false;
        this.terminationInitiated = false;
    }
    
    public void initialize() {
        System.out.println("Traffic Light Navigation System");
        System.out.println("Press A to start");
        setupButtonListeners();
        
        // Keep main thread alive
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void setupButtonListeners() {
        // Button A - Start
        controller.enableButton(Button.A, () -> {
            if (!programStarted) {
                startProgram();
            } else {
                buttonHandler.displayInvalidButtonMessage();
            }
        });
        
        // Button X - Terminate
        controller.enableButton(Button.X, () -> {
            if (programStarted && !terminationInitiated) {
                initiateTermination();
            } else if (terminationInitiated) {
                exit();
            } else {
                buttonHandler.displayInvalidButtonMessage();
            }
        });
        
        // Button Y - Show log and exit
        controller.enableButton(Button.Y, () -> {
            if (terminationInitiated) {
                showLogAndExit();
            } else {
                buttonHandler.displayInvalidButtonMessage();
            }
        });
        
        // Button B - Invalid
        controller.enableButton(Button.B, () -> {
            buttonHandler.displayInvalidButtonMessage();
        });
    }
    
    private void startProgram() {
        System.out.println("Program started...");
        programStarted = true;
        logger.startTracking();
        
        // Run in separate thread
        new Thread(this::navigationLoop).start();
    }
    
    /**
     * Main navigation loop - demonstrates Polymorphism
     */
    private void navigationLoop() {
        running = true;
        controller.setYellowUnderlights();
        System.out.println("SwiftBot moving at initial speed: 30");
        
        while (running && !buttonHandler.isTerminationRequested()) {
            // Detect traffic light (returns TrafficLight object - polymorphism!)
            TrafficLight light = detector.detectTrafficLight();
            
            if (light != null) {
                handleTrafficLight(light);
                
                // Check if checkpoint prompt needed
                if (logger.shouldPrompt()) {
                    boolean continueNav = buttonHandler.promptContinueOrQuit(logger.getTotalLights());
                    if (!continueNav) {
                        running = false;
                        break;
                    }
                }
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    /**
     * Handles traffic light response
     * Demonstrates: Polymorphism - calls respond() on different TrafficLight subclasses
     */
    private void handleTrafficLight(TrafficLight light) {
        System.out.println("Light #" + (logger.getTotalLights() + 1) + " detected: " + light.getColour());
        logger.logLight(light.getColour());
        
        try {
            // POLYMORPHISM IN ACTION!
            // This calls RedLight.respond(), GreenLight.respond(), or BlueLight.respond()
            // depending on the runtime type of 'light'
            light.respond(controller);
            
            // Resume yellow lights
            controller.setYellowUnderlights();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void initiateTermination() {
        System.out.println("Termination initiated, press X again to exit or Y to view log.");
        terminationInitiated = true;
        running = false;
        buttonHandler.requestTermination();
        logger.stopTracking();
    }
    
    private void showLogAndExit() {
        logger.displayAndSaveLog();
        controller.turnOffUnderlights();
        System.exit(0);
    }
    
    private void exit() {
        System.out.println("Program exiting...");
        controller.turnOffUnderlights();
        System.exit(0);
    }
    
    public static void main(String[] args) {
        TrafficLightSystem system = new TrafficLightSystem();
        system.initialize();
    }
}

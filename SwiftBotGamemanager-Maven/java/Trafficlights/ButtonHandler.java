package Trafficlights;

import swiftbot.Button;
/**
 * Handles button inputs and user interaction
 * Demonstrates: Single Responsibility Principle, Association
 * Responsibility: Manage button presses and user prompts
 */
public class ButtonHandler {
    private final SwiftBotController controller;
    private volatile boolean terminationRequested;
    
    public ButtonHandler(SwiftBotController controller) {
        this.controller = controller;
        this.terminationRequested = false;
    }
    
    public boolean isTerminationRequested() {
        return terminationRequested;
    }
    
    public void requestTermination() {
        this.terminationRequested = true;
    }
    
    /**
     * Prompts user to continue or quit after checkpoint
     * Returns true if user wants to continue, false if they want to quit
     */
    public boolean promptContinueOrQuit(int lightsProcessed) {
        System.out.println(lightsProcessed + " lights processed.");
        System.out.println("Press Y to continue or X to quit");
        
        final boolean[] userResponded = {false};
        final boolean[] continueNavigation = {true};
        
        controller.disableButton(Button.Y);
        
        controller.enableButton(Button.Y, () -> {
            System.out.println("Continuing...");
            continueNavigation[0] = true;
            userResponded[0] = true;
        });
        
        while (!userResponded[0] && !terminationRequested) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        controller.disableButton(Button.Y);
        
        if (terminationRequested) {
            return false;
        }
        
        return continueNavigation[0];
    }
    
    public void displayInvalidButtonMessage() {
        System.out.println("That button is not valid. Please try again.");
    }
}
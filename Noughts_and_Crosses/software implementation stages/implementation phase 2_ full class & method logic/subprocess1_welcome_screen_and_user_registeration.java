package full_method_and_class_implementations;

public class subprocess1_welcome_screen_and_user_registeration {
	
	// subprocess 1: intro and register
	public void intro_and_register() {
        System.out.println();
        System.out.println("Microsoft Windows [Version 10.0]");
        System.out.println("(c) 2026 SwiftBot Labs. All rights reserved.");
        System.out.println("===========================================");
        System.out.println("   NOUGHTS & CROSSES  — SwiftBot (CLI)");
        System.out.println("===========================================\n");
        System.out.println("Welcome! Play Noughts & Crosses (3x3) vs SwiftBot.");
        System.out.println("Rules (brief):");
        System.out.println(" - Enter moves as row,column using numbers 1..3 (example: 2,3)");
        System.out.println(" - Difficulty sets Undo quota & timer");
        System.out.println(" - Use 'undo' to revert your last move (if available)");
        System.out.print("\nPress SwiftBot Button A (or type anything + Enter) to begin... ");
        in.nextLine();
        
        String name;
        while (true) {
            System.out.print("\nEnter your player name (alphanumeric, 1–30 chars):\n> ");
            name = in.nextLine().trim();
            if (name.matches("[A-Za-z0-9 ]{1,30}")) break;
            System.out.println("[ERROR] Invalid name. Use letters, digits and spaces (1–30).");
        }
        user = new Player(name, Piece.X);
        System.out.println("[OK] Player name set: " + name);

        String botName = "Robo-" + (100 + new Random().nextInt(900));
        System.out.println("SwiftBot default name: \"" + botName + "\"");
        System.out.print("Override SwiftBot name? (Y/N) > ");
        String yn = in.nextLine().trim();
        
        if (yn.equalsIgnoreCase("Y")) {
            System.out.print("Enter SwiftBot name > ");
            botName = in.nextLine().trim();
        }
        bot = new Player(botName, Piece.O);
        System.out.println("[OK] SwiftBot name: " + botName);
    }

}
}
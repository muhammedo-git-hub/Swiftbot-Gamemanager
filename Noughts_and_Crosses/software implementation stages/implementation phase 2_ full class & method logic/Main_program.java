package full_method_and_class_implementations;

public class Main_program {

	public static void main(String[] args) {
		Config cfg = new Config();
        for (String a : args) {
            if (a.equalsIgnoreCase("--no-hw")) cfg.hardwareEnabled = false;
            if (a.equalsIgnoreCase("--ai=strategic")) cfg.strategicAI = true;
            if (a.startsWith("--difficulty=")) {
                String v = a.substring("--difficulty=".length()).toLowerCase();
                switch (v) {
                    case "easy" -> cfg.difficulty = Difficulty.EASY;
                    case "hard" -> cfg.difficulty = Difficulty.HARD;
                    default -> cfg.difficulty = Difficulty.MEDIUM;
                }
            }
	   }
       GameController controller = new GameController(cfg);
       controller.run();
	}
}


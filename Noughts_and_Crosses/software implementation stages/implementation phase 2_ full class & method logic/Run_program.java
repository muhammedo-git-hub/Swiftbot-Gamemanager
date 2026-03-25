package full_method_and_class_implementations;

public class Run_program {
	
	public void run_program() {
		
		initAndRegister();
        System.out.print("Choose difficulty (easy/medium/hard) > ");
        String d = in.nextLine().trim().toLowerCase();
        if (d.equals("easy")) applyDifficulty(Difficulty.EASY);
        else if (d.equals("hard")) applyDifficulty(Difficulty.HARD);
        else applyDifficulty(Difficulty.MEDIUM);
        System.out.printf("Difficulty: %s (Undo=%d, Timer=%ds)\n", cfg.difficulty, cfg.undoQuota, cfg.turnTimerSeconds);

        generateAndRollDice();
        
        boolean keepPlaying = true;
        userUndoRemaining = cfg.undoQuota;
        
        while (keepPlaying) {
            board.reset();
            boolean roundOver = false;
            while (!roundOver) {
                if (currentPlayer == user) {
                    boolean applied = userTurn();
                    Optional<Piece> outcome = checkWinDrawAndAct();
                    if (outcome.isPresent()) { appendRoundLogAndUpdateScoreboard(outcome); roundOver = true; break; }
                    currentPlayer = bot;
                } else {
                    swiftBotTurn();
                    Optional<Piece> outcome = checkWinDrawAndAct();
                    if (outcome.isPresent()) { appendRoundLogAndUpdateScoreboard(outcome); roundOver = true; break; }
                    currentPlayer = user;
                }
            }
            keepPlaying = playAgainOrQuit();
        }

	}
	

}

package full_method_and_class_implementations;

import java.util.Random;

public class Subprocess2_generate_and_roll_dice {
	
	public void generateAndRollDice() {
        Random r = new Random();
        int ur, br;
        do {
            ur = r.nextInt(6) + 1;
            br = r.nextInt(6) + 1;
            System.out.printf("the user rolled a: ", user.name, ur);
            System.out.printf("the swiftbot rolled a: ", bot.name, br);
            if (ur == br) System.out.println("Tie — re-rolling...");
        } while (ur == br);

        if (ur > br) {
            user.piece = Piece.O; bot.piece = Piece.X; currentPlayer = user;
        } else {
            bot.piece = Piece.O; user.piece = Piece.X; currentPlayer = bot;
        }
        System.out.printf("Starter: %s (%s)\n", currentPlayer==user ? user.name : bot.name,
                currentPlayer==user ? user.piece : bot.piece);
    }

}

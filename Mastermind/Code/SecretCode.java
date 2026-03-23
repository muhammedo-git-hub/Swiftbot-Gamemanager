
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SecretCode class demonstrating ENCAPSULATION
 * Generates and stores the secret color sequence per SRS FR-11 to FR-14
 */
public class SecretCode {
    // ENCAPSULATION: Private fields
    private Color[] code;
    private Random random;

    public SecretCode() {
        this.random = new Random();
    }

    /**
     * Generate random code based on mode settings
     * 
     * @param length          Number of colors in the sequence
     * @param allowRepetition If true, colors can repeat (Custom Mode FR-13)
     *                        If false, no duplicates (Default Mode FR-11)
     */
    public void generate(int length, boolean allowRepetition) {
        code = new Color[length];
        Color[] availableColors = Color.getAllColors();

        if (allowRepetition) {
            // FR-13: Custom Mode - colors can repeat
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(availableColors.length);
                code[i] = availableColors[randomIndex];
            }
        } else {
            // FR-11: Default Mode - no color repetition
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < availableColors.length; i++) {
                indices.add(i);
            }

            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(indices.size());
                int colorIndex = indices.remove(randomIndex);
                code[i] = availableColors[colorIndex];
            }
        }

        // FR-14: Secret is hidden from user
        System.out.println("[GEN] Secret code generated successfully.");
        System.out.println("[GEN] Code stored in protected memory. (Hidden from player)");
    }

    /**
     * Legacy method for backward compatibility
     */
    public void generate(int length) {
        generate(length, false);
    }

    // ENCAPSULATION: Public getter (returns copy to prevent modification)
    public Color[] getCode() {
        return code.clone();
    }

    public Color getColorAt(int index) {
        return code[index];
    }

    public int getLength() {
        return code.length;
    }

    /**
     * Format code as abbreviation string (e.g., "R G B Y")
     */
    public String toAbbreviationString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length; i++) {
            sb.append(code[i].getAbbreviation());
            if (i < code.length - 1)
                sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < code.length; i++) {
            sb.append(code[i].getName());
            if (i < code.length - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

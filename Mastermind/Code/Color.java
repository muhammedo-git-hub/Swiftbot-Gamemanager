
/**
 * Color class demonstrating ENCAPSULATION
 * Represents the 6 distinct colors used in Mastermind per SRS FR-10
 */
public class Color {
    // ENCAPSULATION: Private fields
    private final String name;
    private final String abbreviation;
    private final int[] rgb;

    // Private constructor - use factory method instead
    private Color(String name, String abbreviation, int r, int g, int b) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.rgb = new int[] { r, g, b };
    }

    // ENCAPSULATION: Public getter for name
    public String getName() {
        return name;
    }

    // ENCAPSULATION: Public getter for abbreviation (R, G, B, Y, O, P)
    public String getAbbreviation() {
        return abbreviation;
    }

    // ENCAPSULATION: Public getter for RGB (returns copy to prevent modification)
    public int[] getRGB() {
        return rgb.clone();
    }

    /**
     * Calculate Euclidean distance to given RGB values
     * Used for color matching per SRS FR-21
     */
    public double distanceTo(int r, int g, int b) {
        return Math.sqrt(
                Math.pow(rgb[0] - r, 2) +
                        Math.pow(rgb[1] - g, 2) +
                        Math.pow(rgb[2] - b, 2));
    }

    /**
     * Factory method to get all 6 game colors per SRS FR-10
     * Red (R), Green (G), Blue (B), Yellow (Y), Orange (O), Pink (P)
     */
    public static Color[] getAllColors() {
        return new Color[] {
                new Color("Red", "R", 255, 0, 0),
                new Color("Green", "G", 0, 255, 0),
                new Color("Blue", "B", 0, 0, 255),
                new Color("Yellow", "Y", 255, 255, 0),
                new Color("Orange", "O", 255, 165, 0),
                new Color("Pink", "P", 255, 192, 203)
        };
    }

    @Override
    public String toString() {
        return abbreviation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Color))
            return false;
        Color other = (Color) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

package DetectLights;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Static utility for saving captured BufferedImage objects as PNG files in 'images/'.
 */
public class ImageSaver {

    private static final String IMAGE_DIR = "images";

    static {
        new File(IMAGE_DIR).mkdirs();
    }

    public static String saveImage(BufferedImage image, String modePrefix) {
        if (image == null) return null;

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String filename = modePrefix + "_" + timestamp + ".png";
        File outputFile = new File(IMAGE_DIR, filename);

        try {
            ImageIO.write(image, "PNG", outputFile);
            return outputFile.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("[ImageSaver] Failed to save image: " + e.getMessage());
            return null;
        }
    }
}

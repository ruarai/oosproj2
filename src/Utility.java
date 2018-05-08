import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class Utility {

    public static final Random random = new Random();

    //Simple static method to indicate whether a given vector is outside the screen space
    public static boolean offScreen(Vector vector) {
        return vector.x < 0 || vector.y < 0 || vector.x > App.SCREEN_WIDTH || vector.y > App.SCREEN_HEIGHT;
    }

    //Similar, but can tell if an entire image is off the screen
    public static boolean offScreen(Vector vector, Image image) {
        if(vector.x + image.getWidth() < 0 || vector.y + image.getHeight() < 0)
            return true;
        if(vector.x > App.SCREEN_WIDTH || vector.y > App.SCREEN_HEIGHT)
            return true;
        return false;
    }

    //Samples a random subImage from a given image
    public static Image getRandomSubImage(Image image)
    {
        int x = random.nextInt(image.getWidth());
        int y = random.nextInt(image.getHeight());

        int width = random.nextInt(image.getWidth() - x);
        int height = random.nextInt(image.getHeight() - y);


        return image.getSubImage(x,y,width,height);
    }
}

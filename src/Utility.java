import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class Utility {

    public static final Random random = new Random();

    //Simple static method to indicate whether a given vector is outside the screen space
    public static boolean offScreen(Vector2f vector) {
        if(vector.x < 0 || vector.y < 0)
            return true;
        if(vector.x > App.SCREEN_WIDTH || vector.y > App.SCREEN_HEIGHT)
            return true;
        return false;
    }

    //Similar, but can tell if an entire image is off the screen
    public static boolean offScreen(Vector2f vector, Image image) {
        if(vector.x + image.getWidth() < 0 || vector.y + image.getHeight() < 0)
            return true;
        if(vector.x > App.SCREEN_WIDTH || vector.y > App.SCREEN_HEIGHT)
            return true;
        return false;
    }

    public static Image getRandomSubImage(Image img)
    {
        int x = random.nextInt(img.getWidth());
        int y = random.nextInt(img.getHeight());

        int width = random.nextInt(img.getWidth() - x);
        int height = random.nextInt(img.getHeight() - y);


        return img.getSubImage(x,y,width,height);
    }
}

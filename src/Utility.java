import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

/**
 * Provides a number of useful tools for the game.
 */
public class Utility {

    /**
     * The common Random used by the game
     */
    public static final Random random = new Random();

    /**
     * Determines if a given vector is outside of the screen view
     * @param vector The vector to be tested
     * @return If the vector is off the screen
     */
    public static boolean offScreen(Vector vector) {
        return vector.x < 0 || vector.y < 0 || vector.x > App.SCREEN_WIDTH || vector.y > App.SCREEN_HEIGHT;
    }

    /**
     * Determines if a given vector and image is entirely off the screen
     * @param vector The vector to some point on the screen
     * @param image The image that will extend off the vector as a rectangle
     * @return If the image at vector is off the screen
     */
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

    public static Vector vectorToMouse(Input input, Vector start){
        return new Vector(input.getMouseX(),input.getMouseY()).sub(start);
    }
}

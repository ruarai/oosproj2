import org.newdawn.slick.geom.Vector2f;

public class Utility {
    //Simple static method to indicate whether a given vector is outside the screen space
    public static boolean offScreen(Vector2f vector) {
        if(vector.x < 0 || vector.y < 0)
            return true;
        if(vector.x > App.SCREEN_WIDTH || vector.y > App.SCREEN_HEIGHT)
            return true;
        return false;
    }
}

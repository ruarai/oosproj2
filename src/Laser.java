import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

//A laser Sprite that moves upwards and destroys enemies
public class Laser extends Sprite {

    //The speed that the laser moves upwards on the screen
    private final float VELOCITY = 3f;

    public Laser(Image img, float x, float y, World parent) {super(img, x, y, parent);}
    public Laser(Image img, Vector2f v, World parent) { super(img, v, parent); }

    @Override
    public void update(Input input, int delta) {

        //Move the laser upwards at VELOCITY
        location.y -= VELOCITY * delta;


        //If the laser goes off the screen, add it do the dead entities list
        //This will remove it from memory once the update is complete
        if(location.y < 0)
            parentWorld.killEntity(this);
    }
}

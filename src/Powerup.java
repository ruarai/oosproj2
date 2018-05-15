import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

/**
 * A base Powerup class that moves downwards on the screen
 */
public abstract class Powerup extends Sprite implements Collidable {

    private static final float SPEED = 0.1f;
    private static final float DIRECTION = 90f;

    public Powerup(Image img, Vector v, World parent) {
        super(img, v, parent);
        setVelocity(new Vector(DIRECTION).scale(SPEED));
    }

    public abstract void onCollision(Sprite sprite);
}

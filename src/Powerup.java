import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public abstract class Powerup extends Sprite implements Collidable {

    private static final float SPEED = 0.1f;
    private static final float DIRECTION = 90f;

    public Powerup(Image img, Vector v, World parent) {
        super(img, v, parent);
    }

    public void fixedUpdate(Input input) {
        //Very simple movement logic
        setVelocity(new Vector(DIRECTION).scale(SPEED));
    }

    public abstract void onCollision(Sprite sprite);
}

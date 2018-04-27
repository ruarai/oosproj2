import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public abstract class Powerup extends Sprite implements Collidable {

    private static final float VELOCITY = 0.1f;
    private static final float DIRECTION = 90f;

    public Powerup(Image img, Vector2f v, World parent) {
        super(img, v, parent);
    }

    public void update(Input input, int delta) {
        velocity = new Vector2f(DIRECTION);
        velocity.scale(VELOCITY * delta);

        location.add(velocity);
    }

    public abstract void onCollision(Sprite sprite);
}

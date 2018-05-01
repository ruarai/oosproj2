import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public abstract class Powerup extends Sprite implements Collidable {

    private static final float SPEED = 0.1f;
    private static final float DIRECTION = 90f;

    public Powerup(Image img, Vector2f v, World parent) {
        super(img, v, parent);
    }

    public void update(Input input, int delta) {
        setVelocity(new Vector2f(DIRECTION));
        getVelocity().scale(SPEED * delta);

        getLocation().add(getVelocity());
    }

    public abstract void onCollision(Sprite sprite);
}

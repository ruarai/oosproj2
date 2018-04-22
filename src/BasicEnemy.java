import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class BasicEnemy extends Enemy {

    private final float VELOCITY = 0.2f;
    private final float DIRECTION = 90f;

    public BasicEnemy(Vector2f location, World parent) {
        super(Resources.enemy, location, parent);
    }
    public BasicEnemy(float x, float y, World parent) {
        super(Resources.enemy, x, y, parent);
    }


    public void update(Input input, int delta) {
        super.update(input, delta);

        //Vector moving downwards
        Vector2f velocity = new Vector2f(DIRECTION);
        velocity.scale(VELOCITY);

        location.add(velocity);
    }
}

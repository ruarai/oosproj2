import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyShot extends Enemy {

    private final float VELOCITY = 0.7f;
    private final float DIRECTION = 90f;

    public EnemyShot(Vector2f location, World parent) { super(Resources.enemyShot, location, parent);}



    public void update(Input input, int delta) {
        super.update(input, delta);

        //Vector moving downwards
        Vector2f velocity = new Vector2f(DIRECTION);
        velocity.scale(VELOCITY * delta);

        location.add(velocity);
    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        return false;
    }
}

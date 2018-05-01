import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyShot extends Enemy {

    private static final float SPEED = 0.7f;
    private static final float DIRECTION = 90f;

    public EnemyShot(Vector2f location, World parent) { super(Resources.enemyShot, location, parent);}



    public void update(Input input, int delta) {
        super.update(input, delta);

        //Vector moving downwards
        velocity = new Vector2f(DIRECTION);
        velocity.scale(SPEED * delta);

        location.add(velocity);

        //Once the laser leaves the screen, kill it
        if(location.y > App.SCREEN_HEIGHT)
            parentWorld.killEntity(this);
    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        return false;
    }
}

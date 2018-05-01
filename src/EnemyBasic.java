import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyBasic extends Enemy {

    private static final float SPEED = 0.2f;
    private static final float DIRECTION = 90f;

    public EnemyBasic(Vector2f location, World parent) {
        super(Resources.basicEnemy, location, parent);
    }


    public void update(Input input, int delta) {
        super.update(input, delta);

        //Vector moving downwards
        Vector2f velocity = new Vector2f(DIRECTION);
        velocity.scale(SPEED * delta);

        getLocation().add(velocity);

        //Whilst we expect enemies to be off screen upwards, if they're off the screen downwards they should be killed
        if(getLocation().y > App.SCREEN_HEIGHT)
            parentWorld.killEntity(this);
    }

    public int getScoreValue() {
        return 50;
    }
    public boolean getDestroyable() {
        return true;
    }
}

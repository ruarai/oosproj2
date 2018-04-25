import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyJava extends Enemy {

    private final int SHOT_DELAY = 1500;


    public EnemyJava(Vector2f v, World parent) {
        super(Resources.java, v, parent);
    }

    //time in ms until enemy can shoot again
    private int shotDelay = 0;

    public void update(Input input, int delta) {
        super.update(input, delta);

        //Is our delay until shot all used up?
        if(shotDelay <= 0)
        {
            //Yep, let's shoot a laser and reset our delay
            parentWorld.addEntity(new EnemyJavaError(getCentre(),parentWorld));
            shotDelay = SHOT_DELAY;
        }
        else {
            //Nope, let's keep running down the delay.
            shotDelay -= delta;
        }
    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        //The java is not destroyable, and never will be destroyable.
        //Such is life
        return false;
    }

    public void onCollision(Sprite collidingSprite) {
        super.onCollision(collidingSprite);
    }
}

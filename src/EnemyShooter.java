import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyShooter extends Enemy {

    private static final float SPEED = 0.2f;
    private static final float DIRECTION = 90f;

    private static final float MIN_TARGET_Y = 48;
    private static final float MAX_TARGET_Y = 464;

    private static final int SHOT_DELAY = 3500;


    public EnemyShooter(Vector location, World parent) {

        super(Resources.basicShooter, location, parent);
        targetY = Utility.random.nextFloat() * (MAX_TARGET_Y-MIN_TARGET_Y) + MIN_TARGET_Y;
    }

    private float targetY;

    //time in ms until enemy can shoot again
    private int shotDelay = 0;


    public void update(Input input, int delta) {
        super.update(input, delta);

        //Are we at our target location yet?
        if(getLocation().y < targetY)
        {
            //No, keep moving down:

            //Vector moving downwards
            Vector velocity = new Vector(DIRECTION).scale(SPEED * delta);

            addLocation(velocity);
        }
        else {
            //Yes, let's try and shoot:

            //Is our delay until shot all used up?
            if(shotDelay <= 0)
            {
                //Yep, let's shoot a laser and reset our delay
                parentWorld.addEntity(new EnemyShot(getCentre(),parentWorld));
                shotDelay = SHOT_DELAY;
            }
            else {
                //Nope, let's keep running down the delay.
                shotDelay -= delta;
            }
        }

    }

    public int getScoreValue() {
        return 200;
    }
    public boolean getDestroyable() {
        return true;
    }
}

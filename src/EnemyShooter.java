import org.newdawn.slick.Input;

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


    public void looseUpdate(Input input, int delta) {
        super.looseUpdate(input, delta);

        if(getLocation().y >= targetY) {
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
    public void fixedUpdate(Input input) {
        //Are we at our target location yet?
        if(getLocation().y < targetY)
        {
            //No, keep moving down:

            //Vector moving downwards
            setVelocity(new Vector(DIRECTION).scale(SPEED));
        }
        else
            setVelocity(new Vector(0,0));
    }

    public int getScoreValue() {
        return 200;
    }
    public boolean getDestroyable() {
        return true;
    }
}

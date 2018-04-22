import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyBoss extends Enemy {

    private static final float INIT_WALK_VELOCITY = 0.05f;
    private static final float INIT_WALK_DIRECTION = 90;

    private static final float X_WALK_VELOCITY = 0.05f;
    private static final float LEFT_DIRECTION = 180f;

    private static final int X_RAND_MAX = 896;
    private static final int X_RAND_MIN = 128;

    private final int SHOT_DELAY = 200;

    public EnemyBoss(Vector2f location, World parent) {
        super(Resources.boss, location, parent);
    }

    private State currentState = State.InitialWalk;
    private float xGoal;
    private boolean xGoalHigher;

    public void update(Input input, int delta) {
        super.update(input, delta);

        switch (currentState) {
            case InitialWalk:
                initialWalk(delta);
                break;
            case FirstWait:
                if(waited(5000, delta))
                {
                    currentState = State.RandomXWalk;
                    xGoal = Utility.random.nextFloat() * (X_RAND_MAX-X_RAND_MIN) + X_RAND_MIN;
                    xGoalHigher =  location.x < xGoal;
                }
                break;
            case RandomXWalk:
                randomWalk(delta, State.SecondWait);
                break;
            case SecondWait:
                if(waited(2000, delta))
                {
                    currentState = State.RandomXWalkShooting;
                    xGoal = Utility.random.nextFloat() * (X_RAND_MAX-X_RAND_MIN) + X_RAND_MIN;
                    xGoalHigher =  location.x < xGoal;
                }
                break;
            case RandomXWalkShooting:
                randomWalk(delta, State.ShootingWait);
                shoot(delta);
                break;
            case ShootingWait:
                shoot(delta);

                if(waited(3000, delta))
                    currentState = State.FirstWait;
                break;
        }

    }

    private void initialWalk(int delta) {
        Vector2f velocity = new Vector2f(INIT_WALK_DIRECTION);
        velocity.scale(X_WALK_VELOCITY * delta);
        location.add(velocity);

        //We reached our goal, ensure we're exactly at goal then move to next state
        if(location.y >= 72)
        {
            location.y = 72;
            currentState = State.FirstWait;
        }
    }

    private int timeWaited = 0;

    private boolean waited(int goal, int delta){
        if(timeWaited >= goal)
        {
            timeWaited = 0;
            return true;
        }

        timeWaited += delta;
        return false;
    }

    private void randomWalk(int delta, State nextState){
        Vector2f velocity = new Vector2f(LEFT_DIRECTION);
        velocity.scale(X_WALK_VELOCITY * delta);

        if(xGoalHigher)
            velocity.scale(-1);

        location.add(velocity);

        if((xGoalHigher && location.x > xGoal)||(!xGoalHigher && location.x < xGoal))
        {
            //location.x = xGoal;
            currentState = nextState;
        }
    }


    //time in ms until enemy can shoot again
    private int shotDelay = 0;

    private void shoot(int delta)
    {
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

    public int getScoreValue() {
        return 5000;
    }
    public boolean getDestroyable() {
        return true;
    }


    private enum State {
        InitialWalk,
        FirstWait,
        RandomXWalk,
        SecondWait,
        RandomXWalkShooting,
        ShootingWait
    }
}

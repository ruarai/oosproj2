import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyBoss extends Enemy {

    private static final int FIRST_WAIT_TIME = 5000;
    private static final int SECOND_WAIT_TIME = 5000;
    private static final int SHOOTING_WAIT_TIME = 5000;

    private static final float INIT_WALK_VELOCITY = 0.05f;
    private static final float INIT_WALK_DIRECTION = 90;
    private static final float INIT_WALK_GOAL = 72;

    private static final float X_WALK_VELOCITY = 0.05f;
    private static final float LEFT_DIRECTION = 180f;

    private static final int X_RAND_MAX = 896;
    private static final int X_RAND_MIN = 128;

    private static final int DEFAULT_SHOTS_TO_KILL = 60;

    private static final int SHOT_DELAY = 200;

    private static final int DEATH_EXPLOSION_SIZE = 500;
    private static final float DEATH_EXPLOSION_SCALE = 0.30f;

    private static final int EXTRA_EXPLOSION_SIZE = 500;
    private static final float EXTRA_EXPLOSION_SCALE = 0.30f;

    public EnemyBoss(Vector2f location, World parent) {
        super(Resources.boss, location, parent);
    }

    private State currentState = State.InitialWalk;
    //The target x coordinate for the ship to move to
    private float xGoal;

    //Whether the target x goal is higher than the initial x goal
    //Used to tell when the goal has been reached
    private boolean xGoalHigher;

    private int hitLives = DEFAULT_SHOTS_TO_KILL;

    public void update(Input input, int delta) {
        super.update(input, delta);

        //Check which state we are in
        switch (currentState) {
            case InitialWalk:
                //In this state, we perform a simple walk towards a y-value
                initialWalk(delta);
                break;
            case FirstWait:
                //In this state, we wait 5000 milliseconds
                if(waited(FIRST_WAIT_TIME, delta))
                {
                    //Move to the next state, calculating a new random xGoal and thus xGoalHigher
                    currentState = State.RandomXWalk;
                    xGoal = Utility.random.nextFloat() * (X_RAND_MAX-X_RAND_MIN) + X_RAND_MIN;
                    xGoalHigher =  location.x < xGoal;
                }
                break;
            case RandomXWalk:
                //Perform a walk horizontally towards xGoal
                randomWalk(delta, State.SecondWait);
                break;
            case SecondWait:
                if(waited(SECOND_WAIT_TIME, delta))
                {
                    //Move to the next state, calculating again the random xGoal and xGoalHigher
                    currentState = State.RandomXWalkShooting;
                    xGoal = Utility.random.nextFloat() * (X_RAND_MAX-X_RAND_MIN) + X_RAND_MIN;
                    xGoalHigher =  location.x < xGoal;
                }
                break;
            case RandomXWalkShooting:
                //Perform a walk horizontally towards xGoal, shooting whilst doing so
                randomWalk(delta, State.ShootingWait);
                shoot(delta);
                break;
            case ShootingWait:
                //Wait for 3000 milliseconds, shooting whilst doing so
                shoot(delta);

                if(waited(SHOOTING_WAIT_TIME, delta))
                    currentState = State.FirstWait;
                break;
        }

    }

    private void initialWalk(int delta) {
        Vector2f velocity = new Vector2f(INIT_WALK_DIRECTION);
        velocity.scale(INIT_WALK_VELOCITY * delta);
        location.add(velocity);

        //We reached our goal, ensure we're exactly at goal then move to next state
        if(location.y >= INIT_WALK_GOAL)
        {
            location.y = INIT_WALK_GOAL;
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
        //Not destroyable in the same way as is expected by default Enemy, we'll overwrite that part of collision handling
        return false;
    }

    public void onCollision(Sprite collidingSprite) {
        //Want to still call super onCollision so that we can create explosions!
        //The boss won't die immediately since it's defined as non-destroyable above
        super.onCollision(collidingSprite);

        //Check if we're hit by a laser
        if (collidingSprite instanceof Laser) {
            if(hitLives < 0) {
                //Game over, boss has ded
                parentWorld.killEntity(this);
                parentWorld.getEntity(GameplayController.class).enemyDeath(this);


                parentWorld.createExplosion(image,location,DEATH_EXPLOSION_SIZE,DEATH_EXPLOSION_SCALE, new Vector2f(0,0));

                //This death is so dramatic, it requires like four explosions for sure
                parentWorld.createExplosion(Resources.shield,location,EXTRA_EXPLOSION_SIZE,EXTRA_EXPLOSION_SCALE, new Vector2f(0,0));
                parentWorld.createExplosion(Resources.shot,location,EXTRA_EXPLOSION_SIZE,EXTRA_EXPLOSION_SCALE, new Vector2f(0,0));
                parentWorld.createExplosion(Resources.enemyShot,location,EXTRA_EXPLOSION_SIZE,EXTRA_EXPLOSION_SCALE, new Vector2f(0,0));


            } else {
                //Otherwise, take away some life
                hitLives--;

                //Typically we don't care if the laser keeps existing, since it creates for a cool effect
                //But we need to destroy it so that it can't collide with us again
                parentWorld.killEntity(collidingSprite);
            }
        }


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

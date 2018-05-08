import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class Solitaire extends Sprite {

    private static final float INITIAL_SPEED = 0.5f;
    private static final float ANGLE_START_RANGE = 180f;

    private static final float FRICTION_FACTOR = 0.005f;

    private static final float GRAVITY_FACTOR = 0.001f;
    private static final float GRAVITY_DIRECTION = 90f;

    public Solitaire(World parent) {
        //Randomize our location at the start
        super(Resources.solitaire, generateRandomStart(), parent);

        //Set our velocity to point some random direction within ANGLE_START_RANGE
        setVelocity(new Vector2f(Utility.random.nextFloat()*ANGLE_START_RANGE));

        getVelocity().scale(INITIAL_SPEED);
    }

    public void update(Input input, int delta) {
        //Calculate the friction vector and subtract it from our velocity
        Vector2f friction = new Vector2f(getVelocity());
        friction.scale(FRICTION_FACTOR);

        getVelocity().sub(friction);

        //Calculate a gravity vector
        Vector2f gravity = new Vector2f(GRAVITY_DIRECTION);
        gravity.scale(delta * GRAVITY_FACTOR);

        getVelocity().add(gravity);

        //Update our location by scaling our velocity by delta
        Vector2f update = new Vector2f(getVelocity());
        update.scale(delta);
        getLocation().add(update);

        //Make sure we don't fly off the screen, add if we hit an edge, flip our velocity
        if(getLocation().y > App.SCREEN_HEIGHT - getImage().getHeight()){
            getLocation().y = App.SCREEN_HEIGHT - getImage().getHeight();
            getVelocity().y = -getVelocity().y;
        }
        if(getLocation().x < 0){
            getLocation().x = 0;
            getVelocity().x = -getVelocity().x;
        }
        if(getLocation().x > App.SCREEN_WIDTH - getImage().getWidth()){
            getLocation().x = App.SCREEN_WIDTH - getImage().getWidth();
            getVelocity().x = -getVelocity().x;
        }
    }

    private static Vector2f generateRandomStart(){
        return new Vector2f(Utility.random.nextFloat()*App.SCREEN_WIDTH,Utility.random.nextFloat()* App.SCREEN_HEIGHT);
    }
}

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
        setVelocity(new Vector(Utility.random.nextFloat() * ANGLE_START_RANGE).scale(INITIAL_SPEED));
    }

    public void update(Input input, int delta) {
        //Calculate the friction vector and subtract it from our velocity
        Vector friction = getVelocity().scale(FRICTION_FACTOR);

        subVelocity(friction);

        //Calculate a gravity vector
        Vector gravity = new Vector(GRAVITY_DIRECTION).scale(delta * GRAVITY_FACTOR);
        addVelocity(gravity);

        //Update our location by scaling our velocity by delta
        Vector update = getVelocity().scale(delta);
        addLocation(update);

        //Make sure we don't fly off the screen, add if we hit an edge, flip our velocity
        if(getLocation().y > App.SCREEN_HEIGHT - getImage().getHeight()){
            setLocation(new Vector(getLocation().x, App.SCREEN_HEIGHT - getImage().getHeight()));
            setVelocity(new Vector(getVelocity().x, -getVelocity().y));
        }
        if(getLocation().x < 0){
            setLocation(new Vector(0,getLocation().y));
            setVelocity(new Vector(-getVelocity().x, getVelocity().y));
        }
        if(getLocation().x > App.SCREEN_WIDTH - getImage().getWidth()){
            setLocation(new Vector(App.SCREEN_WIDTH - getImage().getWidth(), getLocation().y));
            setVelocity(new Vector(-getVelocity().x, getVelocity().y));
        }
    }

    private static Vector generateRandomStart(){
        return new Vector(Utility.random.nextFloat() * App.SCREEN_WIDTH, Utility.random.nextFloat() * App.SCREEN_HEIGHT);
    }
}

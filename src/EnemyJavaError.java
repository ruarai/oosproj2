import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyJavaError extends Enemy {

    private static final float FRICTION_FACTOR = 0.006f;
    private static final float RANDOM_SCALE = 20f;

    private static final float THRUST_SCALE = 0.01f;

    private static final float DIRECTION_FORWARDS = 90f;

    private static final float ROTATION_SPEED = 0.01f;


    public EnemyJavaError(Vector2f v, World parent) {
        super(Resources.getRandomJavaError(), v, parent);
    }

    public void update(Input input, int delta) {
        super.update(input, delta);

        //Determine the player so that we can find their location
        Player player = parentWorld.getEntity(Player.class);

        //If player is dead, don't bother
        if(player == null)
            return;

        //Like in Player, we calculate some amount of friction
        Vector2f friction = new Vector2f(getVelocity());

        //Calculate a friction vector to remove from the velocity
        //This sadly isn't frame-independent, but implementing this correctly seems difficult
        friction.scale(FRICTION_FACTOR);
        getVelocity().sub(friction);

        //Determine the vector from us to the player
        Vector2f towardsPlayer = new Vector2f(player.getLocation()).sub(getLocation());

        //Use our new vector to calculate some amount of thrust
        Vector2f thrust = new Vector2f(towardsPlayer.getTheta() + (Utility.random.nextFloat()-0.5f) * RANDOM_SCALE);

        //Determine the difference in rotation between our front and the player and adjust so that we face them
        float rotationDiff = ((float)towardsPlayer.getTheta() - DIRECTION_FORWARDS) - getRotation();
        setRotation(getRotation() + rotationDiff * ROTATION_SPEED);

        thrust.scale(THRUST_SCALE * delta);

        getVelocity().add(thrust);

        getLocation().add(getVelocity());

    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        return true;
    }
}

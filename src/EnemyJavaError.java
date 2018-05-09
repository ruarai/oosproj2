import org.newdawn.slick.Input;

public class EnemyJavaError extends Enemy {

    private static final float FRICTION_FACTOR = 0.006f;

    private static final float THRUST_SCALE = 0.01f;

    private static final float DIRECTION_FORWARDS = 90f;

    private static final float ROTATION_SPEED = 0.01f;


    public EnemyJavaError(Vector v, World parent) {
        super(Resources.getRandomJavaError(), v, parent);
    }

    public void fixedUpdate(Input input) {
        //Determine the player so that we can find their location
        Player player = parentWorld.getEntity(Player.class);

        //If player is dead, don't bother
        if(player == null)
            return;

        //Calculate a friction vector to remove from the velocity
        Vector friction = getVelocity().scale(FRICTION_FACTOR);
        subVelocity(friction);

        //Determine the vector from us to the player
        Vector towardsPlayer = player.getLocation().sub(getLocation());

        //Use our new vector to calculate some amount of thrust
        Vector thrust = new Vector(towardsPlayer.getAngle() );

        //Determine the difference in rotation between our front and the player and adjust so that we face them
        float rotationDiff = (towardsPlayer.getAngle() - DIRECTION_FORWARDS) - getRotation();
        setRotation(getRotation() + rotationDiff * ROTATION_SPEED);

        thrust = thrust.scale(THRUST_SCALE);

        addVelocity(thrust);

    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        return true;
    }
}

import org.newdawn.slick.*;

/**
 * A laser that moves in some given direction, accelerating and destroying any destroyable Enemies
 */
public class Laser extends Sprite implements Collidable {

    private static final float ACCEL = 0.3f;
    private static final float INITIAL_SPEED = 3f;

    private static final int LASER_EXPLOSION_SIZE = 200;
    private static final float LASER_EXPLOSION_SCALE = 0.4f;
    private static final float LASER_EXPLOSION_FORCE_SCALE = 2f;

    private static final float DIRECTION_FORWARDS = -90;

    private static final int BLUR_STEPS = 10;

    /**
     * @param location The location of the Laser on the screen
     * @param rotation The direction the laser moves and accelerates in
     * @param parent The parent game world
     */
    public Laser(Vector location, float rotation, World parent)
    {
        super(Resources.shot, location, parent);

        this.setRotation(rotation);
        setVelocity(new Vector(rotation + DIRECTION_FORWARDS));
        getVelocity().scale(INITIAL_SPEED);
    }

    @Override
    public void fixedUpdate(Input input) {

        Vector accel = new Vector(getRotation() + DIRECTION_FORWARDS);
        accel = accel.scale(ACCEL);

        addVelocity(accel);

        //If the laser goes off the screen, add it do the dead entities list
        //This will remove it from memory once the fixedUpdate is complete
        if(Utility.offScreen(getLocation()))
            parentWorld.killEntity(this);
    }

    //override rendering so that we can create a cool blur effect
    public void render(Graphics graphics) {
        //Create a vector pointing in opposite direction of velocity
        Vector behind = getVelocity().scale(-16f / (float)BLUR_STEPS);

        for (int i = 0; i < BLUR_STEPS; i++)
        {
            Color filter = new Color(1,1,1,(BLUR_STEPS-i)/ (float)BLUR_STEPS);

            getImage().draw(getLocation().x + behind.x * i, getLocation().y + behind.y * i, filter);
        }
    }

    public void onCollision(Sprite collidingSprite) {
        //Handle collision with an enemy, so that we can make some effects
        if(collidingSprite instanceof Enemy) {
            parentWorld.createExplosion(Resources.shot, getLocation(),LASER_EXPLOSION_SIZE,LASER_EXPLOSION_SCALE,
                    getVelocity().scale(LASER_EXPLOSION_FORCE_SCALE));
        }
    }
}

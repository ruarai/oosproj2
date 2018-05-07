import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;


public class Particle extends Sprite {

    private static final float ROTATION_SCALE = 0.5f;

    private static final float FORCE_ADDED_SCALE = 0.01f;

    private static final float FULL_CIRCLE = 360f;
    private static final float DIRECTION_FORWARDS = -90f;

    private static final float DEFAULT_LIFE_DECAY = 0.0001f;
    private static final float LIFE_POWER = 4f;
    private static final float LIFE_START_RANDOM_SCALE = 0.3f;

    //The constant speed we will rotate the particle at
    private float rotationSpeed;

    //The life of our particle, above 0 but not necessarily lesser than 1
    private float life;

    float lifeDecayRate = DEFAULT_LIFE_DECAY;

    public Particle(Image img, Vector2f location, World parent, float randomScale, Vector2f force) {
        super(img, location, parent);

        //Set our default velocity and shift it by some random value
        setVelocity(new Vector2f(Utility.random.nextFloat() * FULL_CIRCLE));
        getVelocity().scale(randomScale * Utility.random.nextFloat());

        //Add some scaled amount of force to our velocity, used to provide consistent movement to our particle
        getVelocity().add(new Vector2f(force).scale(FORCE_ADDED_SCALE));
        //Create a new rotation speed proportional to ROTATION_SCALE, randomly scaled in the positive or negative direction
        rotationSpeed = (Utility.random.nextFloat() - 0.5f) * ROTATION_SCALE;

        //Set a rotation, fairly arbitrary
        setRotation((float) getVelocity().getTheta() + DIRECTION_FORWARDS);

        //Shift our life value from 1 by some random number in the positive or negative direction
        life = 1f + (Utility.random.nextFloat() - 0.5f) * LIFE_START_RANDOM_SCALE;
    }


    public void update(Input input, int delta) {
        //Scale our velocity down by some constant factor (unrealistic physics again lol)
        Vector2f scaledVel = new Vector2f(getVelocity()).scale(delta);
        getLocation().add(scaledVel);

        //Adjust our rotation according to rotationSpeed
        setRotation(getRotation() + rotationSpeed * delta);

        //Decay our life
        life -= delta * lifeDecayRate;

        //Have we moved off the screen/ran out of life? We should remove ourselves so as to reduce cpu/memory usage
        if(Utility.offScreen(getLocation(), getImage()) || life < 0)
            parentWorld.killEntity(this);
    }


    public void render(Graphics graphics) {
        //Create an alpha proportional to life
        float alpha = (float)Math.pow(life, LIFE_POWER);

        //Create a filter based on our alpha
        Color filter = new Color(1,1,1,alpha);

        //Set our rotation and draw with our filter
        getImage().setRotation(getRotation());
        getImage().draw(getLocation().x, getLocation().y,filter);
    }
}

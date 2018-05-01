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

    private float rotationSpeed;
    private float life;

    float lifeDecayRate = DEFAULT_LIFE_DECAY;

    public Particle(Image img, Vector2f location, World parent, float randomScale, Vector2f force) {
        super(img, location, parent);

        setVelocity(new Vector2f(Utility.random.nextFloat() * FULL_CIRCLE));
        getVelocity().scale(randomScale * Utility.random.nextFloat());

        getVelocity().add(new Vector2f(force).scale(FORCE_ADDED_SCALE));
        rotationSpeed = (Utility.random.nextFloat()-0.5f) * ROTATION_SCALE;

        setRotation((float) getVelocity().getTheta() + DIRECTION_FORWARDS);

        life = 1f + (Utility.random.nextFloat()-0.5f) * LIFE_START_RANDOM_SCALE;
    }


    public void update(Input input, int delta) {
        Vector2f scaledVel = new Vector2f(getVelocity()).scale(delta);
        getLocation().add(scaledVel);

        setRotation(getRotation() + rotationSpeed * delta);

        life -= delta * lifeDecayRate;

        //Have we moved off the screen/ran out of life? We should remove ourselves
        if(Utility.offScreen(getLocation(), getImage()) || life < 0)
            parentWorld.killEntity(this);
    }


    //Render with a slight fade out based on life value
    public void render(Graphics graphics) {
        Color filter = new Color(1,1,1,1f-(float)Math.pow(1 - life,LIFE_POWER));

        getImage().setRotation(getRotation());
        getImage().draw(getLocation().x, getLocation().y,filter);
    }
}

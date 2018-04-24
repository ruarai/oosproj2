import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;


public class Particle extends Sprite {

    private static final float ROTATION_SCALE = 0.5f;

    private static final float FULL_CIRCLE = 360f;

    private static final float DEFAULT_LIFE_DECAY = 0.0001f;
    private static final float LIFE_POWER = 4f;
    private static final float LIFE_START_RANDOM_SCALE = 0.3f;

    private float rotationSpeed;
    private float life;

    protected float lifeDecayRate = DEFAULT_LIFE_DECAY;

    public Particle(Image img, Vector2f location, World parent, float randomScale, Vector2f force) {
        super(img, location, parent);

        velocity = new Vector2f(Utility.random.nextFloat() * FULL_CIRCLE);
        velocity.scale(randomScale * Utility.random.nextFloat());

        velocity.add(new Vector2f(force).scale(0.01f));
        rotationSpeed = (Utility.random.nextFloat()-0.5f) * ROTATION_SCALE;

        rotation = (float)velocity.getTheta() - 90f;

        life = 1f + (Utility.random.nextFloat()-0.5f) * LIFE_START_RANDOM_SCALE;
    }


    public void update(Input input, int delta) {
        Vector2f scaledVel = new Vector2f(velocity).scale(delta);
        location.add(scaledVel);

        rotation += rotationSpeed * delta;

        life -= delta * lifeDecayRate;

        //Have we moved off the screen/ran out of life? We should remove ourselves
        if(Utility.offScreen(location) || life < 0)
        {
            parentWorld.killEntity(this);
        }
    }


    //Render with a slight fade out based on life value
    public void render(Graphics graphics) {
        Color filter = new Color(1,1,1,1f-(float)Math.pow(1 - life,LIFE_POWER));

        image.setRotation(rotation);

        image.draw(location.x,location.y,filter);
    }
}

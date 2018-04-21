import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;


public class ExplosionParticle extends Sprite {

    private final float VELOCITY_SCALE = 0.1f;
    private final float ROTATION_SCALE = 0.5f;

    private final float FULL_CIRCLE = 360f;

    private final float LIFE_DECAY = 0.0001f;
    private final float LIFE_POWER = 4f;
    private final float LIFE_START_RANDOM_SCALE = 0.3f;

    private float rotationSpeed;
    private float life = 1;

    public ExplosionParticle(Image img, Vector2f location, World parent) {
        super(img, location, parent);

        velocity = new Vector2f(Utility.random.nextFloat() * FULL_CIRCLE);
        velocity.scale(VELOCITY_SCALE * Utility.random.nextFloat());

        rotationSpeed = (Utility.random.nextFloat()-0.5f) * ROTATION_SCALE;

        life = life + (Utility.random.nextFloat()-0.5f) * LIFE_START_RANDOM_SCALE;
    }


    public void update(Input input, int delta) {
        Vector2f scaledVel = new Vector2f(velocity).scale(delta);
        location.add(scaledVel);

        rotation += rotationSpeed * delta;

        life -= delta * LIFE_DECAY;

        if(Utility.offScreen(location))
        {
            parentWorld.killEntity(this);
        }

        if(life < 0)
        {
            parentWorld.killEntity(this);
            parentWorld.createExplosion(Resources.shot,location,5);
        } 
    }


    //Render with a slight fade out based on life value
    public void render() {
        Color filter = new Color(1,1,1,1f-(float)Math.pow(1 - life,LIFE_POWER));

        image.setRotation(rotation);

        image.draw(location.x,location.y,filter);
    }
}

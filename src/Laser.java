import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

//A laser Sprite that moves upwards and destroys enemies
public class Laser extends Sprite implements Collidable {

    //The speed that the laser moves upwards on the screen
    private static final float SPEED = 3f;

    private static final int LASER_EXPLOSION_SIZE = 200;
    private static final float LASER_EXPLOSION_SCALE = 0.4f;
    private static final float LASER_EXPLOSION_FORCE_SCALE = 2f;

    private static final float DIRECTION_FORWARDS = -90;

    private static final int BLUR_STEPS = 10;

    public Laser(Image img, Vector2f location, World parent, float rotation)
    {
        super(img, location, parent);

        this.setRotation(rotation);
    }

    @Override
    public void update(Input input, int delta) {

        setVelocity(new Vector2f(getRotation() + DIRECTION_FORWARDS));
        getVelocity().scale(SPEED * delta);

        getLocation().add(getVelocity());

        //If the laser goes off the screen, add it do the dead entities list
        //This will remove it from memory once the update is complete
        if(Utility.offScreen(getLocation()))
            parentWorld.killEntity(this);
    }

    //override rendering so that we can create a cool blur effect
    public void render(Graphics graphics) {
        //Create a vector pointing in opposite direction of velocity
        Vector2f behind = new Vector2f(getVelocity());
        behind.scale(-1f / (float)BLUR_STEPS);

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
                    new Vector2f(getVelocity()).scale(LASER_EXPLOSION_FORCE_SCALE));
        }
    }
}

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

//A laser Sprite that moves upwards and destroys enemies
public class Laser extends Sprite implements Collidable {

    //The speed that the laser moves upwards on the screen
    private static final float VELOCITY_MAGNITUDE = 3f;

    private static final int ENEMY_EXPLOSION_SIZE = 100;
    private static final float ENEMY_EXPLOSION_SCALE = 0.15f;
    private static final float ENEMY_DEATH_SCREEN_SHAKE = 0.8f;
    private static final int LASER_EXPLOSION_SIZE = 200;
    private static final float LASER_EXPLOSION_SCALE = 0.4f;

    private static final int BLUR_STEPS = 10;

    public Laser(Image img, Vector2f location, World parent, float rotation)
    {
        super(img, location, parent);

        this.rotation = rotation;
    }

    @Override
    public void update(Input input, int delta) {

        velocity = new Vector2f(rotation - 90);
        velocity.scale(VELOCITY_MAGNITUDE * delta);

        location.add(velocity);

        //If the laser goes off the screen, add it do the dead entities list
        //This will remove it from memory once the update is complete
        if(Utility.offScreen(location))
            parentWorld.killEntity(this);
    }

    //override rendering so that we can create a cool blur effect
    public void render(Graphics graphics) {
        //Create a vector pointing in opposite direction of velocity
        Vector2f behind = new Vector2f(velocity);
        behind.scale(-1f / (float)BLUR_STEPS);



        for (int i = 0; i < BLUR_STEPS; i++)
        {
            Color filter = new Color(1,1,1,(BLUR_STEPS-i)/ (float)BLUR_STEPS);

            image.draw(location.x + behind.x * i,location.y + behind.y * i, filter);
        }
    }

    public void onCollision(Sprite collidingSprite) {
        if(collidingSprite instanceof Enemy) {
            Enemy enemy = (Enemy)collidingSprite;

            parentWorld.createExplosion(enemy.image,location,ENEMY_EXPLOSION_SIZE,ENEMY_EXPLOSION_SCALE, velocity);
            parentWorld.createExplosion(Resources.shot,location,LASER_EXPLOSION_SIZE,LASER_EXPLOSION_SCALE, new Vector2f(velocity).scale(2));

            parentWorld.getEntity(GameplayController.class).shakeScreen(ENEMY_DEATH_SCREEN_SHAKE);
        }
    }
}

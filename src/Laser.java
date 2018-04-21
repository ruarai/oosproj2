import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

//A laser Sprite that moves upwards and destroys enemies
public class Laser extends Sprite {

    //The speed that the laser moves upwards on the screen
    private final float VELOCITY_MAGNITUDE = 3f;

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

        //Look for enemies to kill:
        for (Entity e : parentWorld.entities) {
            if(e instanceof Enemy)
            {
                Enemy enemy = (Enemy)e;

                if(enemy.getBoundingBox().intersects(this.getBoundingBox()))
                {
                    //Kill the enemy, it intersects with us, the laser
                    parentWorld.killEntity(enemy);
                }
            }
        }


        //If the laser goes off the screen, add it do the dead entities list
        //This will remove it from memory once the update is complete
        if(Utility.offScreen(location))
            parentWorld.killEntity(this);
    }

    public void render() {
        Vector2f behind = new Vector2f(velocity);
        behind.scale(-0.1f);

        for (int i = 0; i < 10; i++)
        {
            Color filter = new Color(1,1,1,(10-i)/10f);

            image.draw(location.x + behind.x * i,location.y + behind.y * i, filter);
        }


    }
}

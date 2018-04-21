import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

//Sprite that represents the player and associated logic
class Player extends Sprite {
    private final float MOVE_ACCEL = 0.001f;

    private final float ROTATION_SPEED = 0.1f;

    public Player(Image img, float x, float y, World parent) {
        super(img, x, y, parent);
    }
    public Player(Image img, Vector2f v, World parent) { super(img, v, parent); }

    @Override
    public void update(Input input, int delta) {
        move(input, delta);

        //See if we need to shoot a laser
        //Use isKeyPressed to ensure 1 laser per key press
        if(input.isKeyPressed(Input.KEY_SPACE))
        {
            //So shoot a laser!
            parentWorld.addEntity(new Laser(Resources.shot,this.getCentre(),parentWorld));
        }
    }


    //Method to calculate movement
    //Separate this so that we can perform more complex logic later
    private void move(Input input, int delta)
    {
        Vector2f thrust = new Vector2f();

        //respond to key inputs
        if(input.isKeyDown(Input.KEY_UP))
            thrust = new Vector2f(rotation - 90).scale(MOVE_ACCEL * delta);
        if(input.isKeyDown(Input.KEY_DOWN))
            thrust = new Vector2f(rotation - 90).scale(-MOVE_ACCEL * delta);
        if(input.isKeyDown(Input.KEY_LEFT))
            rotation += -ROTATION_SPEED * delta;
        if(input.isKeyDown(Input.KEY_RIGHT))
            rotation += ROTATION_SPEED * delta;

        velocity = velocity.add(thrust);

        location = location.add(velocity);




        //handle the literal edgecases:
        if(location.x > App.SCREEN_WIDTH - image.getWidth())
            location.x = App.SCREEN_WIDTH - image.getWidth();

        if(location.y > App.SCREEN_HEIGHT - image.getHeight())
            location.y = App.SCREEN_HEIGHT - image.getHeight();

        if(location.x < 0)
            location.x = 0;

        if(location.y < 0)
            location.y = 0;

    }

}

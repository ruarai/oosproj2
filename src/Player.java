import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

//Sprite that represents the player and associated logic
class Player extends Sprite {
    private final float MOVE_ACCEL = 0.01f;

    private final float ROTATION_SPEED = 0.1f;

    public Player(float x, float y, World parent) {
        super(Resources.spaceship, x, y, parent);
    }
    public Player(Vector2f v, World parent) { super(Resources.spaceship, v, parent); }

    //indicates number of ms left until player can shoot a laser again
    private int shotDelay = 0;

    @Override
    public void update(Input input, int delta) {
        move(input, delta);

        keepOnScreen();

        //See if we need to shoot a laser
        //Use isKeyPressed to ensure 1 laser per key press
        if(input.isKeyDown(Input.KEY_SPACE) && shotDelay <= 0)
        {
            //So shoot a laser!

            Laser laser = new Laser(Resources.shot,this.getCentre(),parentWorld, rotation);
            parentWorld.addEntity(laser);

            shotDelay = 250;
        }

        if(shotDelay > 0)
            shotDelay -= delta;
    }


    //Method to calculate movement
    private void move(Input input, int delta)
    {
        Vector2f friction = new Vector2f(velocity);
        friction.scale(0.01f);
        velocity.sub(friction);

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


        Vector2f drift = new Vector2f();
        velocity.projectOntoUnit(new Vector2f(rotation - 90),drift);
        drift.sub(velocity);
        drift.scale(1.5f);

        velocity.add(thrust);
        velocity.add(drift);

        location.add(velocity);
    }

    //Method to keep the object on the screen
    private void keepOnScreen()
    {
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

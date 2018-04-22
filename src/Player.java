import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

//Sprite that represents the player and associated logic
class Player extends Sprite {
    private static final float MOVE_ACCEL = 0.01f;
    private static final float RECOIL_ACCEL = 0.1f;
    private static final float ROTATION_SPEED = 0.1f;
    private static final float FRICTION_SCALE = 0.01f;
    private static final float DRIFT_SCALE = 1.7f;

    private static final int SHIELD_OFFSET = 16;

    private static final int SHOT_DELAY = 250;

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

            Vector2f recoil = new Vector2f(rotation - 90).scale(-RECOIL_ACCEL * delta);
            velocity.add(recoil);

            shotDelay = SHOT_DELAY;
        }

        //Run down the delay
        if(shotDelay > 0)
            shotDelay -= delta;
    }

    public void render(Graphics graphics) {
        super.render(graphics);

        //Check if we have a shield active, and if so, draw it
        if(parentWorld.getEntity(GameplayController.class).getIsShieldActive())
        {
            Resources.shield.setRotation(rotation);
            Resources.shield.draw(location.x - SHIELD_OFFSET,location.y - SHIELD_OFFSET);
        }
    }


    //Method to calculate movement
    private void move(Input input, int delta)
    {
        Vector2f friction = new Vector2f(velocity);
        friction.scale(FRICTION_SCALE);//fix???
        velocity.sub(friction);

        Vector2f thrust = new Vector2f();

        float rotationScale = Math.max(3-velocity.lengthSquared()/5f,1f);

        //respond to key inputs
        if(input.isKeyDown(Input.KEY_UP))
            thrust = new Vector2f(rotation - 90).scale(MOVE_ACCEL * delta);
        if(input.isKeyDown(Input.KEY_DOWN))
            thrust = new Vector2f(rotation - 90).scale(-MOVE_ACCEL * delta);
        if(input.isKeyDown(Input.KEY_LEFT))
            rotation += -ROTATION_SPEED * delta * rotationScale;
        if(input.isKeyDown(Input.KEY_RIGHT))
            rotation += ROTATION_SPEED * delta * rotationScale;


        Vector2f drift = new Vector2f();
        velocity.projectOntoUnit(new Vector2f(rotation - 90),drift);
        drift.sub(velocity);
        drift.scale(DRIFT_SCALE);

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

    public void onDeath()
    {

    }

}

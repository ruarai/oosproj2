import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

//Sprite that represents the player and associated logic
class Player extends Sprite implements Collidable
{
    private static final float MOVE_ACCEL = 0.01f;
    private static final float RECOIL_ACCEL = 0.01f;
    private static final float ROTATION_SPEED = 0.1f;
    private static final float FRICTION_SCALE = 0.01f;
    private static final float DRIFT_SCALE = 1.7f;

    private static final float DIR_FORWARDS = -90f;

    private static final int SHIELD_OFFSET = 16;

    private static final int SHOT_DELAY = 250;

    private static final float PLAYER_HIT_BOUNCE_SCALE = 0.1f;

    public Player(Vector2f v, World parent) { super(Resources.spaceship, v, parent); }

    //indicates number of ms left until player can shoot a laser again
    private int shotDelay = 0;

    public void update(Input input, int delta) {
        move(input, delta);

        keepOnScreen();

        tryShoot(input, delta);
    }

    private void tryShoot(Input input, int delta){
        //See if we need to shoot a laser
        //Use isKeyPressed to ensure 1 laser per key press
        if(input.isKeyDown(Input.KEY_SPACE) && shotDelay <= 0)
        {
            //So shoot a laser!
            Laser laser = new Laser(Resources.shot,this.getCentre(),parentWorld, rotation);
            parentWorld.addEntity(laser);

            //Calculate some amount of recoil
            Vector2f recoil = new Vector2f(rotation + DIR_FORWARDS).scale(-RECOIL_ACCEL * delta);
            velocity.add(recoil);

            //Reset the delay to SHOT_DELAY
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
            thrust = new Vector2f(rotation  + DIR_FORWARDS).scale(MOVE_ACCEL * delta);
        if(input.isKeyDown(Input.KEY_DOWN))
            thrust = new Vector2f(rotation  + DIR_FORWARDS).scale(-MOVE_ACCEL * delta);
        if(input.isKeyDown(Input.KEY_LEFT))
            rotation += -ROTATION_SPEED * delta * rotationScale;
        if(input.isKeyDown(Input.KEY_RIGHT))
            rotation += ROTATION_SPEED * delta * rotationScale;


        Vector2f drift = new Vector2f();
        velocity.projectOntoUnit(new Vector2f(rotation + DIR_FORWARDS),drift);
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

    public void onCollision(Sprite collidingSprite) {
        //We handle any player related logic related to being hit by enemy on this end
        if(collidingSprite instanceof Enemy) {
            Enemy enemy = (Enemy)collidingSprite;

            velocity.add(new Vector2f(enemy.velocity).scale(PLAYER_HIT_BOUNCE_SCALE));

            parentWorld.getEntity(GameplayController.class).playerDeath();
        }
    }
}

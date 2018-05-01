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

    private static final float PLAYER_HIT_BOUNCE_SCALE = 0.1f;

    private static final float EXHAUST_SPEED_REQUIRED = 2f;
    private static final float EXHAUST_OFFSET_Y = 24f;

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
        boolean tryShooting = input.isKeyDown(Input.KEY_SPACE) || input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);

        if(tryShooting && shotDelay <= 0)
        {
            //So shoot a laser!
            Laser laser = new Laser(Resources.shot,this.getCentre(),parentWorld, getRotation());
            parentWorld.addEntity(laser);

            //Calculate some amount of recoil
            Vector2f recoil = new Vector2f(getRotation() + DIR_FORWARDS).scale(-RECOIL_ACCEL * delta);
            getVelocity().add(recoil);

            //Reset the delay to SHOT_DELAY
            shotDelay = parentWorld.getEntity(GameplayController.class).getCurrentShotDelay();
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
            Resources.shield.setRotation(getRotation());
            Resources.shield.draw(getLocation().x - SHIELD_OFFSET, getLocation().y - SHIELD_OFFSET);
        }
    }

    private float lastRotation;


    //Method to calculate movement
    private void move(Input input, int delta)
    {
        lastRotation = getRotation();


        Vector2f friction = new Vector2f(getVelocity());

        //Calculate a friction vector to remove from the velocity
        //This sadly isn't frame-independent, but implementing this correctly seems difficult
        friction.scale(FRICTION_SCALE);
        getVelocity().sub(friction);

        Vector2f thrust = keyboardInput(input, delta);
        thrust.add(mouseInput(input, delta));

        /*We now calculate a vector we'll call 'drift':
          This is kind of complicated, but here's a basic explanation,
          Moving in a straight line, the ship's direction (rotation) and the ship's velocity are parallel
          Say the ship moves to a new rotation. The ships velocity wouldn't normally adjust for this at all,
          and just keep moving in the direction of the velocity.
          We can change this for more fun/intuitive movement by creating a 'drift' vector,
          which is the projection of the velocity onto the unit vector of the rotation. If we then subtract the
          original velocity, we get just the component of the velocity that is perpendicular to the ship's rotation.
          Scaling this down a little such that it's not excessive, we can then add it to the velocity, making the ship
          turn 'naturally' around corners. You'll just need to ignore that this doesn't make any sense in space.*/
        Vector2f drift = new Vector2f();
        getVelocity().projectOntoUnit(new Vector2f(getRotation() + DIR_FORWARDS),drift);
        drift.sub(getVelocity());
        drift.scale(DRIFT_SCALE);

        //Add our new vectors
        getVelocity().add(thrust);
        getVelocity().add(drift);

        //Move our ship according to our now-updated velocity
        getLocation().add(getVelocity());

        //Are we moving fast enough? Create some exhaust particles for fun.
        if(getVelocity().length() > EXHAUST_SPEED_REQUIRED)
            parentWorld.addEntity(new ExhaustParticle(getCentre().add(new Vector2f(getRotation() - DIR_FORWARDS).scale(EXHAUST_OFFSET_Y)),parentWorld, getVelocity()));
    }

    private Vector2f keyboardInput(Input input, int delta){
        Vector2f thrust = new Vector2f();

        float rotationScale = Math.max(3- getVelocity().lengthSquared()/5f,1f);

        //respond to key inputs, changing the thrust vector
        if(input.isKeyDown(Input.KEY_UP))
            thrust = new Vector2f(getRotation() + DIR_FORWARDS).scale(MOVE_ACCEL * delta);
        if(input.isKeyDown(Input.KEY_DOWN))
            thrust = new Vector2f(getRotation() + DIR_FORWARDS).scale(-MOVE_ACCEL * delta);

        if(input.isKeyDown(Input.KEY_LEFT))
            setRotation(getRotation() + -ROTATION_SPEED * delta * rotationScale);
        if(input.isKeyDown(Input.KEY_RIGHT))
            setRotation(getRotation() + ROTATION_SPEED * delta * rotationScale);

        return thrust;
    }

    private Vector2f mouseInput(Input input, int delta) {
        Vector2f toMouse = new Vector2f(input.getMouseX(),input.getMouseY()).sub(getCentre());

        if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) || input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)){

            setRotation((float)toMouse.getTheta() - DIR_FORWARDS);
        }

        if(toMouse.length() > 10f && input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
            return new Vector2f(getRotation() + DIR_FORWARDS).scale(MOVE_ACCEL * delta);

        return new Vector2f(0,0);
    }

    //Method to keep the object on the screen
    private void keepOnScreen()
    {
        //handle the literal edgecases:
        if(getLocation().x > App.SCREEN_WIDTH - getImage().getWidth())
            getLocation().x = App.SCREEN_WIDTH - getImage().getWidth();

        if(getLocation().y > App.SCREEN_HEIGHT - getImage().getHeight())
            getLocation().y = App.SCREEN_HEIGHT - getImage().getHeight();

        if(getLocation().x < 0)
            getLocation().x = 0;

        if(getLocation().y < 0)
            getLocation().y = 0;
    }

    public void onCollision(Sprite collidingSprite) {
        //We handle any player related logic related to being hit by enemy on this end
        if(collidingSprite instanceof Enemy) {
            Enemy enemy = (Enemy)collidingSprite;

            getVelocity().add(new Vector2f(enemy.getVelocity()).scale(PLAYER_HIT_BOUNCE_SCALE));

            parentWorld.getEntity(GameplayController.class).playerDeath();
        }
    }

    public float getRotationChange(){
        return getRotation() - lastRotation;
    }
}

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

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

    public Player(Vector v, World parent) { super(Resources.spaceship, v, parent); }

    //indicates number of ms left until player can shoot a laser again
    private int shotDelay = 0;

    public void looseUpdate(Input input, int delta) {
        super.looseUpdate(input, delta);

        //Run down the delay
        if(shotDelay > 0)
            shotDelay -= delta;


        float speed = getVelocity().getLength();
        float rotationScale = Math.max(3 - (speed*speed) / 5f,1f);

        if(input.isKeyDown(Input.KEY_LEFT))
            setRotation(getRotation() + -ROTATION_SPEED * rotationScale * delta);
        if(input.isKeyDown(Input.KEY_RIGHT))
            setRotation(getRotation() + ROTATION_SPEED * rotationScale * delta);

        if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) || input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)){
            setRotation(Utility.vectorToMouse(input, getCentre()).getAngle() - DIR_FORWARDS);
        }
    }

    public void fixedUpdate(Input input) {

        move(input);

        keepOnScreen();

        tryShoot(input);
    }
    private void tryShoot(Input input){
        //See if we need to shoot a laser
        //Use isKeyPressed to ensure 1 laser per key press
        boolean tryShooting = input.isKeyDown(Input.KEY_SPACE) || input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);

        if(tryShooting && shotDelay <= 0)
        {
            //So shoot a laser!
            Laser laser = new Laser(Resources.shot,this.getCentre(),parentWorld, getRotation());
            parentWorld.addEntity(laser);

            //Calculate some amount of recoil
            Vector recoil = new Vector(getRotation() + DIR_FORWARDS).scale(-RECOIL_ACCEL);
            addVelocity(recoil);

            //Reset the delay to whatever our default is
            shotDelay = parentWorld.getEntity(GameplayController.class).getCurrentShotDelay();
        }

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


    //Method to calculate movement
    private void move(Input input)
    {
        //Calculate a friction vector to remove from the velocity
        //This sadly isn't frame-independent, but implementing this correctly seems difficult
        Vector friction = getVelocity().scale(FRICTION_SCALE);
        subVelocity(friction);

        Vector thrust = keyboardInput(input);
        thrust = thrust.add(mouseInput(input));

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
        Vector drift = getVelocity().unitProjection(new Vector(getRotation() + DIR_FORWARDS));
        drift = drift.sub(getVelocity());
        drift = drift.scale(DRIFT_SCALE);

        //Add our new vectors
        addVelocity(thrust);
        addVelocity(drift);


        //Are we moving fast enough? Create some exhaust particles for fun.
        if(getVelocity().getLength() > EXHAUST_SPEED_REQUIRED)
            parentWorld.addEntity(new ExhaustParticle(getCentre().add(new Vector(getRotation() - DIR_FORWARDS).scale(EXHAUST_OFFSET_Y)),parentWorld, getVelocity()));
    }

    private Vector keyboardInput(Input input){
        Vector thrust = new Vector();

        //respond to key inputs, changing the thrust vector
        if(input.isKeyDown(Input.KEY_UP))
            thrust = new Vector(getRotation() + DIR_FORWARDS).scale(MOVE_ACCEL);
        if(input.isKeyDown(Input.KEY_DOWN))
            thrust = new Vector(getRotation() + DIR_FORWARDS).scale(-MOVE_ACCEL);


        return thrust;
    }

    private Vector mouseInput(Input input) {
        Vector toMouse = Utility.vectorToMouse(input, getCentre());

        if(toMouse.getLength() > 10f && input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
            return new Vector(getRotation() + DIR_FORWARDS).scale(MOVE_ACCEL);

        return new Vector(0,0);
    }

    //Method to keep the object on the screen
    private void keepOnScreen()
    {
        //handle the literal edgecases:
        if(getLocation().x > App.SCREEN_WIDTH - getImage().getWidth())
            setLocation(new Vector(App.SCREEN_WIDTH - getImage().getWidth(),getLocation().y));

        if(getLocation().y > App.SCREEN_HEIGHT - getImage().getHeight())
            setLocation(new Vector(getLocation().x, App.SCREEN_HEIGHT - getImage().getHeight()));

        if(getLocation().x < 0)
            setLocation(new Vector(0,getLocation().y));

        if(getLocation().y < 0)
            setLocation(new Vector(getLocation().x,0));
    }

    public void onCollision(Sprite collidingSprite) {
        //We handle any player related logic related to being hit by enemy on this end
        if(collidingSprite instanceof Enemy) {
            Enemy enemy = (Enemy)collidingSprite;

            //We bounce the player slightly when they hit an enemy
            addVelocity(enemy.getVelocity().scale(PLAYER_HIT_BOUNCE_SCALE));

            //Trigger a player death in the gameplay controller
            parentWorld.getEntity(GameplayController.class).playerDeath();
        }
    }
}

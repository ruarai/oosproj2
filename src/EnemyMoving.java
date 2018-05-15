import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

/**
 * A basic implentation of Enemy that moves downwards along the screen
 */
public abstract class EnemyMoving extends Enemy {

    private static final float DIRECTION = 90f;

    /**
     * @param image The image of the EnemyMoving
     * @param location The starting location of the EnemyMoving
     * @param parent The parent game world
     */
    public EnemyMoving(Image image, Vector location, World parent) {
        super(image, location, parent);
    }


    /**
     * Moves the EnemyMoving down on the screen according to DIRECTION and getSpeed()
     * @param input The current game input
     */
    public final void fixedUpdate(Input input) {
        super.fixedUpdate(input);

        //Vector moving downwards
        setVelocity(new Vector(DIRECTION).scale(getSpeed()));

        //Whilst we expect enemies to be off screen upwards, if they're off the screen downwards they should be killed
        if(getLocation().y > App.SCREEN_HEIGHT)
            parentWorld.killEntity(this);
    }

    public int getScoreValue() {
        return 50;
    }
    public boolean getDestroyable() {
        return true;
    }

    abstract float getSpeed();
}

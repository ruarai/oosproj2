import org.newdawn.slick.Input;

/**
 * A fun implementation of Enemy. Shoots EnemyJavaErrors towards the player.
 */
public class EnemyJava extends Enemy {

    private static final int SHOT_DELAY = 1500;

    /**
     * @param v The location of the Enemy on the game screen
     * @param parent The parent game world
     */
    public EnemyJava(Vector v, World parent) {
        super(Resources.java, v, parent);
    }

    //time in ms until enemy can shoot again
    private int shotDelay = 0;

    /**
     * Updates the EnemyJava logic, used to tell when it may shoot again
     * @param input Current game input
     * @param delta Time since last frame
     */
    public void looseUpdate(Input input, int delta) {
        super.looseUpdate(input, delta);

        //Is our delay until shot all used up?
        if(shotDelay <= 0)
        {
            //Yep, let's shoot a laser and reset our delay
            parentWorld.addEntity(new EnemyJavaError(getCentre(),parentWorld));
            shotDelay = SHOT_DELAY;
        }
        else {
            //Nope, let's keep running down the delay.
            shotDelay -= delta;
        }
    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        //The java is not destroyable, naturally
        return false;
    }
}

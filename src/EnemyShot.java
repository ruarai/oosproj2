import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

/**
 * Extremely basic enemy that just moves downwards and is not destroyable
 */
public class EnemyShot extends EnemyMoving {

    private static final float SPEED = 0.7f;

    /**
     * @param location The location of the shot on screen
     * @param parent The parent game world
     */
    public EnemyShot(Vector location, World parent) { super(Resources.enemyShot, location, parent);}



    public float getSpeed(){
        return SPEED;
    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        return false;
    }
}

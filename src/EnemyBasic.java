import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

/**
 * An implementation of EnemyMoving that represents the Basic enemy
 */
public class EnemyBasic extends EnemyMoving {

    private static final float SPEED = 0.2f;

    /**
     * @param location The location of this Enemy on screen
     * @param parent The parent game world
     */
    public EnemyBasic(Vector location, World parent) {
        super(Resources.basicEnemy, location, parent);
    }

    public float getSpeed(){
        return SPEED;
    }

    public int getScoreValue() {
        return 50;
    }
    public boolean getDestroyable() {
        return true;
    }
}

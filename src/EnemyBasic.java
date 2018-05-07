import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyBasic extends EnemyMoving {

    private static final float SPEED = 0.2f;
    private static final float DIRECTION = 90f;

    public EnemyBasic(Vector2f location, World parent) {
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

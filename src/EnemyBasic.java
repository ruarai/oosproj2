import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyBasic extends EnemyMoving {

    private static final float SPEED = 0.2f;

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

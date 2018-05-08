import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyShot extends EnemyMoving {

    private static final float SPEED = 0.7f;

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

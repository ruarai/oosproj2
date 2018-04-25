import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyJavaError extends Enemy {
    public EnemyJavaError(Vector2f v, World parent) {
        super(Resources.javaError1, v, parent);
    }

    public void update(Input input, int delta) {
        super.update(input, delta);
    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        return false;
    }

    public void onCollision(Sprite collidingSprite) {
        super.onCollision(collidingSprite);
    }
}

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

class Enemy extends Sprite {
    public Enemy(Image img, float x, float y, World parent) {
        super(img, x, y, parent);
    }
    public Enemy(Image img, Vector2f v, World parent) { super(img, v, parent); }

    @Override
    public void update(Input input, int delta) {

    }
}

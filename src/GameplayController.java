import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.TextField;

public class GameplayController implements Entity {

    public void update(Input input, int delta) {

    }

    public void render(Graphics graphics) {
        graphics.drawString("Hello",20,738);
    }
}

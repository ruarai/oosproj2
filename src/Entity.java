import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

//Base class for any 'object' that appears on screen and has logic/rendering
public abstract class Entity {
    public Entity(World parentWorld) {
        this.parentWorld = parentWorld;
    }

    //Pointer to allow entities to interact with world
    protected World parentWorld;

    //called every logic update
    public abstract void update(Input input, int delta);

    //called every frame render
    public abstract void render(Graphics g);
}

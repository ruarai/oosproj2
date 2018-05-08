import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

//Base class for any 'object' that appears on screen and has logic/rendering
public abstract class Entity {
    public Entity(World parentWorld) {
        this.parentWorld = parentWorld;
    }

    //Pointer to allow entities to interact with world
    World parentWorld;

    //called every fixed number of times a second, no delta, used to make sure physics consistent
    public void fixedUpdate(Input input) { }

    //called every update call, delta is dependent upon frame rate
    public void looseUpdate(Input input, int delta) { }

    //called every frame render
    public abstract void render(Graphics g);
}

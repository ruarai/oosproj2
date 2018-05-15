import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

//Base class for any 'object' that appears on screen and has logic/rendering
public abstract class Entity {
    public Entity(World parentWorld) {
        this.parentWorld = parentWorld;
    }

    //Pointer to allow entities to interact with world
    World parentWorld;

    /**
     * Called a fixed number of times per second. Allows for simple realistic physics implementation.
     * @param input The current game input
     */
    public void fixedUpdate(Input input) { }

    /**
     * Called a variable number of times per second. Should be used for general game logic
     * @param input The current game input
     * @param delta The time since the last looseUpdate
     */
    public void looseUpdate(Input input, int delta) { }

    /**
     * Called to render the Entity
     * @param graphics The graphics to use to render on screen
     */
    public abstract void render(Graphics graphics);
}

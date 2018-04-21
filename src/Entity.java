import org.newdawn.slick.Input;

//Basic interface for any 'object' that appears on screen and has logic/rendering
public interface Entity {
    //called every logic update
    public void update(Input input, int delta);

    //called every frame render
    public void render();
}

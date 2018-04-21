import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

//Sprites are basic entities with some kind of image and location
abstract class Sprite implements Entity {

    //Pointer to allow sprites to interact with world
    protected World parentWorld;

    //Holds the image resource to be rendered
    protected Image image;

    //location of the sprite
    protected Vector2f location = new Vector2f();

    //velocity of the sprite
    protected Vector2f velocity = new Vector2f();

    //rotation of the sprite, affect rendering of the sprite
    protected float rotation = 0f;

    //Returns the centre point of the image according to the size of the sprite
    protected Vector2f getCentre()
    {
        float x = location.x + image.getWidth() / 2;
        float y = location.y + image.getHeight() / 2;

        return new Vector2f(x,y);
    }


    //Create a new sprite from specified file with a default location x, y
    //Leave the work to the other constructor
    public Sprite(Image img, float x, float y, World parent) {
        this(img,new Vector2f(x,y),parent);
	}

    //Create a new sprite from specified file with a default location vector v
    public Sprite(Image img, Vector2f v, World parent) {
        this.image = img;

        //We will move the sprite such that its centre lay upon the vector coordinate
        location.x = v.x - img.getWidth() / 2;
        location.y = v.y - img.getHeight() / 2;

        parentWorld = parent;
    }

    //We don't want to try and implement any base update code for sprites, make it abstract
	abstract public void update(Input input, int delta);

	//but rendering is the same across all sprites
	public void render()
    {
        image.setRotation(rotation);
        image.draw(location.x,location.y);
	}
}

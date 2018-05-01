import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

//Sprites are basic entities with some kind of image and location
abstract class Sprite extends Entity {

    private static float DIR_BEHIND = 90f;


    //Holds the image resource to be rendered
    private Image image;

    //location of the sprite
    private Vector2f location = new Vector2f();

    //velocity of the sprite
    //not too much physical accuracy is present here, so it shouldn't be trusted beyond /cool/ effects
    private Vector2f velocity = new Vector2f();

    //rotation of the sprite, affect rendering of the sprite
    private float rotation = 0f;


    //Create a new sprite from specified file with a default location vector v
    public Sprite(Image img, Vector2f v, World parent) {
        super(parent);
        this.image = img;

        //We will move the sprite such that its centre lay upon the vector coordinate
        getLocation().x = v.x - img.getWidth() / 2;
        getLocation().y = v.y - img.getHeight() / 2;

        parentWorld = parent;
    }

    //Returns the centre point of the image according to the size of the sprite
    Vector2f getCentre()
    {
        float x = getLocation().x + getImage().getWidth() / 2;
        float y = getLocation().y + getImage().getHeight() / 2;

        return new Vector2f(x,y);
    }


    //We don't want to try and implement any base update code for sprites, make it abstract
	abstract public void update(Input input, int delta);

	//but rendering is the same across all sprites
	public void render(Graphics graphics)
    {
        getImage().setRotation(getRotation());
        getImage().draw(getLocation().x, getLocation().y);
	}

	//Method to get bounding box that accounts for rotation
	public Polygon getBoundingBox()
    {
        Polygon polygon = new Polygon();

        //Find the two half-lengths of the rectangle of our image
        float halfWidth = getImage().getWidth() / 2;
        float halfHeight = getImage().getHeight() / 2;

        //Find the centre location, this is the same as our centre of rotation
        Vector2f centre = getCentre();

        //Generate two vectors that are parallel/perpendicular to our /rotated/ image, of half lengths each
        Vector2f left = new Vector2f(getRotation()).scale(halfWidth);
        Vector2f down = new Vector2f(getRotation() + DIR_BEHIND).scale(halfHeight);

        //Calculate a simple vector that will adjust for the movement made between frames
        //Points in opposite direction of motion and is of length proportional to velocity
        //This really isn't a perfect solution, but helps a lot!
        float currSpeed = getVelocity().length();
        Vector2f extraBehind = new Vector2f(getRotation() + DIR_BEHIND).scale(currSpeed);


        //Calculate the four vectors by adding our new perpendicular vectors to our centre
        Vector2f a = new Vector2f(centre).sub(left).sub(down);
        Vector2f b = new Vector2f(centre).add(left).sub(down);
        Vector2f c = new Vector2f(centre).add(left).add(down).add(extraBehind);
        Vector2f d = new Vector2f(centre).sub(left).add(down).add(extraBehind);

        //Create a polygon from this
        polygon.addPoint(a.x,a.y);
        polygon.addPoint(b.x,b.y);
        polygon.addPoint(c.x,c.y);
        polygon.addPoint(d.x,d.y);

        return polygon;
    }

    public Image getImage() {
        return image;
    }

    public Vector2f getLocation() {
        return location;
    }

    public void setLocation(Vector2f location) {
        this.location = location;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}

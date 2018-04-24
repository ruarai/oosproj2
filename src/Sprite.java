import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

//Sprites are basic entities with some kind of image and location
abstract class Sprite extends Entity {
    //Holds the image resource to be rendered
    protected Image image;

    //location of the sprite
    protected Vector2f location = new Vector2f();

    //velocity of the sprite
    protected Vector2f velocity = new Vector2f();

    //rotation of the sprite, affect rendering of the sprite
    protected float rotation = 0f;

    //We can record, if we'd like, the last movement of the sprite (purely optional)
    //This is then used in the bounding box code
    protected Vector2f lastMovement = new Vector2f(0,0);

    //Returns the centre point of the image according to the size of the sprite
    protected Vector2f getCentre()
    {
        float x = location.x + image.getWidth() / 2;
        float y = location.y + image.getHeight() / 2;

        return new Vector2f(x,y);
    }


    //Create a new sprite from specified file with a default location vector v
    public Sprite(Image img, Vector2f v, World parent) {
        super(parent);
        this.image = img;

        //We will move the sprite such that its centre lay upon the vector coordinate
        location.x = v.x - img.getWidth() / 2;
        location.y = v.y - img.getHeight() / 2;

        parentWorld = parent;
    }

    //We don't want to try and implement any base update code for sprites, make it abstract
	abstract public void update(Input input, int delta);

	//but rendering is the same across all sprites
	public void render(Graphics graphics)
    {
        image.setRotation(rotation);
        image.draw(location.x,location.y);
	}

	//Method to get bounding box that accounts for rotation
	public Polygon getBoundingBox()
    {
        Polygon polygon = new Polygon();

        //Find the two half-lengths of the rectangle of our image
        float halfWidth = image.getWidth() / 2;
        float halfHeight = image.getHeight() / 2;

        //Find the centre location, this is the same as our centre of rotation
        Vector2f centre = getCentre();

        //Generate two vectors that are parallel/perpendicular to our /rotated/ image, of half lengths each
        Vector2f left = new Vector2f(rotation).scale(halfWidth);
        Vector2f down = new Vector2f(rotation + 90).scale(halfHeight);

        down.add(lastMovement);

        //Calculate the four vectors by adding our new perpendicular vectors to our centre
        Vector2f a = new Vector2f(centre).sub(left).sub(down);
        Vector2f b = new Vector2f(centre).add(left).sub(down);
        Vector2f c = new Vector2f(centre).add(left).add(down);
        Vector2f d = new Vector2f(centre).sub(left).add(down);

        //Create a polygon from this
        polygon.addPoint(a.x,a.y);
        polygon.addPoint(b.x,b.y);
        polygon.addPoint(c.x,c.y);
        polygon.addPoint(d.x,d.y);

        return polygon;
    }
}

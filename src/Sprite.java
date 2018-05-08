import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;

//Sprites are basic entities with some kind of image and location
abstract class Sprite extends Entity {

    private static final float DIR_BEHIND = 90f;


    //Holds the image resource to be rendered
    private Image image;

    //location of the sprite
    private Vector location;

    //velocity of the sprite
    private Vector velocity = new Vector();

    //velocity of the sprite once adjusted for delta
    private Vector scaledVelocity = new Vector();

    //rotation of the sprite, affect rendering of the sprite
    private float rotation = 0f;


    //Create a new sprite from specified file with a default location vector v
    public Sprite(Image img, Vector v, World parent) {
        super(parent);
        this.image = img;

        //We will move the sprite such that its centre lay upon the vector coordinate
        float x = v.x - img.getWidth() / 2;
        float y = v.y - img.getHeight() / 2;

        location = new Vector(x, y);

        parentWorld = parent;
    }

    //Returns the centre point of the image according to the size of the sprite
    Vector getCentre()
    {
        float x = location.x + getImage().getWidth() / 2;
        float y = location.y + getImage().getHeight() / 2;

        return new Vector(x,y);
    }

    public void looseUpdate(Input input, int delta) {
        scaledVelocity = velocity.scale(delta);
        location = location.add(scaledVelocity);
    }

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
        Vector centre = getCentre();

        //Generate two vectors that are parallel/perpendicular to our /rotated/ image, of half lengths each
        Vector left = new Vector(getRotation()).scale(halfWidth);
        Vector down = new Vector(getRotation() + DIR_BEHIND).scale(halfHeight);

        //Calculate a simple vector that will adjust for the movement made between frames
        //Points in opposite direction of motion and is of length proportional to velocity
        //This really isn't a perfect solution, but helps a lot!
        float currSpeed = scaledVelocity.getLength();
        Vector extraBehind = new Vector(getRotation() + DIR_BEHIND).scale(currSpeed);


        //Calculate the four vectors by adding our new perpendicular vectors to our centre
        Vector a = centre.sub(left).sub(down);
        Vector b = centre.add(left).sub(down);
        Vector c = centre.add(left).add(down).add(extraBehind);
        Vector d = centre.sub(left).add(down).add(extraBehind);

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

    public Vector getLocation() {
        return location;
    }

    public void setLocation(Vector location){
	    this.location = location;
    }

    public Vector getVelocity() {
        return velocity;
    }
    public Vector getScaledVelocity() {return scaledVelocity;}

    void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    void addVelocity(Vector change) {
	    velocity = velocity.add(change);
    }

    void subVelocity(Vector change) {
	    velocity = velocity.sub(change);
    }

    float getRotation() {
        return rotation;
    }

    void setRotation(float rotation) {
        this.rotation = rotation;
    }
}

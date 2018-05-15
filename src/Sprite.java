import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;

/**
 * Basic implementation of Entity, with an explicit Image and on-screen location, rotation and velocity
 */
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


    /**
     * @param img The image to be used to render the Sprite
     * @param v The starting location of the Sprite
     * @param parent The parent game world
     */
    public Sprite(Image img, Vector v, World parent) {
        super(parent);
        this.image = img;

        //We will move the sprite such that its centre lay upon the vector coordinate
        float x = v.x - img.getWidth() / 2;
        float y = v.y - img.getHeight() / 2;

        location = new Vector(x, y);

        parentWorld = parent;
    }

    /**
     * @return Returns the centre of the Sprite on-screen according to the Sprite's Image
     */
    Vector getCentre()
    {
        float x = location.x + getImage().getWidth() / 2;
        float y = location.y + getImage().getHeight() / 2;

        return new Vector(x,y);
    }

    /**
     * Updates the Sprite game logic and moves it along the screen according to its velocity
     * @param input The current game input
     * @param delta The time since the last looseUpdate
     */
    public void looseUpdate(Input input, int delta) {
        scaledVelocity = velocity.scale(delta);
        location = location.add(scaledVelocity);
    }

    /**
     * Renders the sprite from its Image and location
     * @param graphics The graphics to use to render on screen
     */
	public void render(Graphics graphics)
    {
        getImage().setRotation(getRotation());
        getImage().draw(getLocation().x, getLocation().y);
	}

    /**
     * @return A rectangular bounding box that accounts for rotation and how fast the sprite is moving on screen
     */
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

    /**
     * @return The image of the Sprite
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return The location of the Sprite on screen
     */
    public Vector getLocation() {
        return location;
    }

    /**
     * @param location The new location of the Sprite on screen
     */
    public void setLocation(Vector location){
	    this.location = location;
    }

    /**
     * @return The current velocity of the Sprite
     */
    public Vector getVelocity() {
        return velocity;
    }

    /**
     * @param velocity The new velocity of the Sprite
     */
    void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    /**
     * @param change The difference in Velocity to be added to the current velocity
     */
    void addVelocity(Vector change) {
	    velocity = velocity.add(change);
    }

    /**
     * @param change The difference in Velocity to be subtracted from the current velocity
     */
    void subVelocity(Vector change) {
	    velocity = velocity.sub(change);
    }

    /**
     * @return The rotation of the Sprite
     */
    float getRotation() {
        return rotation;
    }

    /**
     * @param rotation The new rotation of the Sprite
     */
    void setRotation(float rotation) {
        this.rotation = rotation;
    }
}

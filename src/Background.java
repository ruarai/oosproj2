import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

/**
 * A separate entity that provides a renderable scrolling background to cover the screen
 */
class Background extends Entity {

    private Image backgroundImage;

    //how far we push the background down the screen
    private float scroll = 0;

    //Number of times we must tile the background image to fill the screen entirely across each dimension
    private int xRepeats;
    private int yRepeats;

    private float scrollSpeed;

    /**
     * @param img The image used for the background
     * @param scrollSpeed The speed at which the image will move /down/ the screen
     * @param parentWorld The parent game world of this entity
     */
    public Background(Image img, float scrollSpeed,World parentWorld)
    {
        super(parentWorld);

        backgroundImage = img;
        this.scrollSpeed = scrollSpeed;

        //Calculate the number of times we can tile the background
        xRepeats = (int)Math.ceil((float)App.SCREEN_WIDTH / backgroundImage.getWidth());
        yRepeats = (int)Math.ceil((float)App.SCREEN_HEIGHT / backgroundImage.getHeight());
    }

    /**
     * Updates the position of the background on the screen
     * @param input Current game input
     * @param delta Time since last frame
     */
    public void looseUpdate(Input input, int delta) {
        //Increment the scroll value according to delta
        //We loop around the vertical dimension according to our calculated yRepeats
        scroll = (scroll + delta * scrollSpeed) % (yRepeats * backgroundImage.getHeight());
    }

    /**
     * Renders the background onto the screen
     */
    public void render(Graphics graphics) {

        //Tile horizontally from left to right
        for (int x = 0; x < xRepeats;x++)
        {
            //Tile vertically, from above the centre to below it
            //This ensures the entire vertical width is filled
            for (int y = -yRepeats; y < yRepeats; y++)
            {
                //Calculate the final x,y location of the individual tile
                float xLoc = x * backgroundImage.getWidth();
                float yLoc = scroll + y * backgroundImage.getHeight();

                backgroundImage.draw(xLoc,yLoc);
            }
        }
    }
}

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

//The background is a separate entity that renders the scrolling background without sprite logic (e.g. fixed location)
class Background implements Entity {

    private Image backgroundImage;

    private float scroll = 0;

    //Number of times we must tile the background image to fill the screen entirely across each dimension
    private int xRepeats;
    private int yRepeats;

    private final float SCROLL_SPEED = 0.2f;

    public Background()
    {
        backgroundImage = Resources.space;

        //Calculate the number of times we can tile the background
        xRepeats = (int)Math.ceil((float)App.SCREEN_WIDTH / backgroundImage.getWidth());
        yRepeats = (int)Math.ceil((float)App.SCREEN_HEIGHT / backgroundImage.getHeight());
    }


    public void update(Input input, int delta) {
        //Increment the scroll value according to delta
        //We loop around the vertical dimension according to our calculated yRepeats
        scroll = (scroll + delta * SCROLL_SPEED) % (yRepeats * backgroundImage.getHeight());
    }

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

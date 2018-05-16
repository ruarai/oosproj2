import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;

public class Renderer {

    //Allow for debug rendering of the bounding boxes of any sprites
    private static final boolean RENDER_BOUNDING_BOX = false;

    private static final boolean RENDER_ONLY_BLUR_FRAME_WHEN_PAUSED = false;


    //Constants for the chromatic aberration effect
    private static final float CHROMATIC_ABERRATION_SCALE = 15f;
    private static final Color FILTER_RED = new Color(1,0,0,0.333f);
    private static final Color FILTER_GREEN = new Color(0,0.3f,0.7f,0.333f);
    private static final Color FILTER_BLUE = new Color(0,0.7f,0.3f,0.333f);
    private static final float CONSTANT_ABERRATION_SHIFT = 1;

    private static final Color RENDER_BLUR_FILTER = new Color(1,1,1,0.99f);
    //most important feature do not delete (press A to activate)
    private static final Color SOLITAIRE_BLUR_FILTER = new Color(1,1,1,1f);

    World parentWorld;

    public Renderer(World parentWorld){
        this.parentWorld = parentWorld;
    }

    //Generate some buffer images that we can store the game's frame in for producing effects
    //Each image is a blank image with the same dimensions as the frame
    private Image blurImage = generateBlankImage();
    private Image blurImageBuffer = generateBlankImage();
    private Image frameImage = generateBlankImage();

    private static Image generateBlankImage(){
        try {
            return new Image(App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
        } catch (SlickException e) { return null;}
    }

    /**
     * Renders a single frame of the Renderer's parentWorld
     * @param graphics The graphics the frame is rendered onto
     */
    public void render(Graphics graphics) {
        //Make sure we're not drawing in some kind of weird way at the moment
        graphics.setDrawMode(Graphics.MODE_NORMAL);

        //If we're paused, just draw the blur image and return
        if(parentWorld.getIsPaused() && RENDER_ONLY_BLUR_FRAME_WHEN_PAUSED){
            blurImage.draw(0,0);
            return;
        }

        preBlur(graphics);

        //render each entity
        for(Entity e : parentWorld.getEntities()) {
            e.render(graphics);

            if(RENDER_BOUNDING_BOX && e instanceof Sprite)
                graphics.draw(((Sprite)e).getBoundingBox());
        }

        postBlur();

        chromaticAberration(graphics);
    }


    //Creates the blur filter to be drawn later
    private void preBlur(Graphics graphics) {
        ArrayList<Entity> drawnEntities = new ArrayList<>();


        /* Here, we (normally) draw in just the particles (we don't want to blur anything else, it hurts
           the eyes). These are drawn over the last blur frame which has had some amount of
           opacity reduced. This means that earlier frames are still partially visible, producing
           the blur effect. */
        //But we can also choose to render all Sprites, to improve SOLITAIRE MODE
        if(solitaireRendering)
            drawnEntities.addAll(parentWorld.getEntities(Sprite.class));
        else
            drawnEntities.addAll(parentWorld.getEntities(Particle.class));

        blurImage.draw(0,0, getBlurFilter());
        for(Entity e : drawnEntities) {
            e.render(graphics);
        }

        //We copy what we've just drawn into a buffer, which will become the blurImage for the next frame
        graphics.copyArea(blurImageBuffer,0,0);

        //Then we clear our frame, allowing normal rendering to commence
        graphics.clear();
    }

    //Actually renders the blur effect and preps for the next frame
    private void postBlur() {
        //We then draw on top of our current frame, the blur
        blurImage.draw(0,0,getBlurFilter());
        //And then swap out or blur with the blur image buffer generated earlier, allowing it to be used next frame
        blurImage = blurImageBuffer.copy();
    }

    private void chromaticAberration(Graphics graphics){
        //Find out our chromatic aberration intensity by taking the screen shake value from the gameplay controller
        float intensity = parentWorld.getEntity(GameplayController.class).getCurrentScreenShake() * CHROMATIC_ABERRATION_SCALE;

        intensity += CONSTANT_ABERRATION_SHIFT;

        //Check if we should bother with producing chromatic aberration
        if(intensity <= 0)
            return;

        //We now copy and clear the frame once more so that we can create the chromatic aberration effect
        graphics.copyArea(frameImage,0,0);
        graphics.clear();

        //Set the draw mode to add so that our frames add nicely
        graphics.setDrawMode(Graphics.MODE_ADD);

        frameImage.draw(intensity,0,FILTER_RED);
        frameImage.draw(0,0,FILTER_GREEN);
        frameImage.draw(-intensity,0,FILTER_BLUE);
    }

    private boolean solitaireRendering = false;

    private Color getBlurFilter() {
        return solitaireRendering ? SOLITAIRE_BLUR_FILTER : RENDER_BLUR_FILTER;
    }

    public void activateSolitaireRendering(){
        solitaireRendering = true;
    }

}

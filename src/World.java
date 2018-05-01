import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;


public class World {

    private final ArrayList<Entity> entities = new ArrayList<>();

    private static final float TIME_DILATION_FACTOR = 5f;

    private static final Vector2f PLAYER_LOCATION = new Vector2f(480,488);

    //Allow for debug rendering of the bounding boxes of any sprites
    private static final boolean RENDER_BOUNDING_BOX = false;
    private static final Color RENDER_BLUR_FILTER = new Color(1,1,1,0.8f);
    //most important feature do not delete (press A to activate)
    private static final Color SOLITAIRE_BLUR_FILTER = new Color(1,1,1,1f);

    private static final float CHROMATIC_ABERRATION_SCALE = 15f;
    private static final Color FILTER_RED = new Color(1,0,0,0.333f);
    private static final Color FILTER_GREEN = new Color(0,1,0,0.333f);
    private static final Color FILTER_BLUE = new Color(0,0,1,0.333f);

    private static final float BACKGROUND_SCROLL_NEAR = 0.17f;
    private static final float BACKGROUND_SCROLL_FAR = 0.13f;

	public World() {
	    //Load in all the imagery
	    Resources.loadResources();

	    //We've made the background transparent so we can render multiple layers for some parallax effect
        entities.add(new Background(Resources.spaceFar,BACKGROUND_SCROLL_FAR,this));
	    entities.add(new Background(Resources.space,BACKGROUND_SCROLL_NEAR,this));

	    entities.add(new GameplayController(this));

	    //Create a Player sprite centred on its default location
        entities.add(new Player(PLAYER_LOCATION,this));

        //Load in the world from the waves.txt file
        entities.addAll(Resources.loadWaveData(this));

        //entities.add(new EnemyBoss(new Vector2f(480,0),this));
        //entities.add(new EnemyJava(new Vector2f(240,240),this));
        //entities.add(new EnemyJava(new Vector2f(480+240,240),this));
	}

	private static Image generateBlankImage(){
	    try {
	        return new Image(App.SCREEN_WIDTH,App.SCREEN_HEIGHT);
        } catch (SlickException e) { return null;}
    }


	//temporary list that we can add entities to during enumeration of actual entities list
    //prevents a concurrentModificationException
	private final ArrayList<Entity> newEntities = new ArrayList<>();

	//similarly for removing entities, even allowing entities to remove themselves
    private final ArrayList<Entity> deadEntities = new ArrayList<>();

	//Allows for the addition of entities from outside classes
	public void addEntity(Entity e)
    {
        newEntities.add(e);
    }

    public void killEntity(Entity e)
    {
        deadEntities.add(e);
    }

    //Simple pause that lets you look at the current blur frame
    private boolean pause = false;

	public void update(Input input, int delta) {
	    delta *= getEntity(GameplayController.class).getCurrentTimeScale();

	    if(input.isKeyDown(Input.KEY_S))
	        delta *= TIME_DILATION_FACTOR;
        if(input.isKeyDown(Input.KEY_D))
            delta /= TIME_DILATION_FACTOR;
        if(input.isKeyPressed(Input.KEY_A))
            activateSolitaireMode();
        if(input.isKeyPressed(Input.KEY_P))
            pause = !pause;

        //If we're paused, don't update anything
        if(pause)
            return;

	    //clear the list of entities to add/remove
        newEntities.clear();
        deadEntities.clear();

	    //update each entity
	    for(Entity e : entities)
            e.update(input, delta);

	    //After all the entities have updated, check for collisions between Collidable Spritess
	    handleCollisions();

        //Add/remove any new entities that have been added/removed by other entities
        entities.addAll(newEntities);
        entities.removeAll(deadEntities);
	}

	//Generate some buffer images that we can store the game's frame in for producing effects
    //Each image is a blank image with the same dimensions as the frame
    private Image blurImage = generateBlankImage();
    private Image blurImageBuffer = generateBlankImage();
    private Image frameImage = generateBlankImage();

    //Whether or not we should allow particles to be drawn in solitaire mode
    private boolean solitaireRendering = false;

    private Color getBlurFilter(){
        return solitaireRendering ? SOLITAIRE_BLUR_FILTER : RENDER_BLUR_FILTER;
    }

    public void render(Graphics graphics) {
        //Make sure we're not drawing in some kind of weird way at the moment
        graphics.setDrawMode(Graphics.MODE_NORMAL);

        //If we're paused, just draw the blur image and return
        if(pause){
            blurImage.draw(0,0);
            return;
        }

        preBlur(graphics);

	    //render each entity
        for(Entity e : entities) {
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
            drawnEntities.addAll(getEntities(Sprite.class));
        else
            drawnEntities.addAll(getEntities(Particle.class));

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
        float intensity = getEntity(GameplayController.class).getCurrentScreenShake() * CHROMATIC_ABERRATION_SCALE;

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

	public void activateSolitaireMode() {
        //Most important thing, enables solitaire rendering
        solitaireRendering = true;

        getEntity(GameplayController.class).enableSolitaireSpawning();
    }

	private void handleCollisions() {
	    //Create a list of all collidable objects, then take the list as an array
	    ArrayList<Collidable> collidables = getEntities(Collidable.class);

	    //PS: Java sucks at this
        Collidable[] collidableArray = new Collidable[collidables.size()];
        collidableArray = collidables.toArray(collidableArray);

        //We perform this on an array such that the collision checks are not performed twice per Sprite
        for (int x = 0; x < collidableArray.length; x++){
            for(int y = x + 1; y < collidableArray.length; y++){
                //Not pretty, but we need a copy of both the collidable and sprite reference
                Collidable collidableA = collidableArray[x];
                Collidable collidableB = collidableArray[y];

                Sprite spriteA = (Sprite)collidableA;
                Sprite spriteB = (Sprite)collidableB;

                //Check if the sprites intersect
                if(spriteA.getBoundingBox().intersects(spriteB.getBoundingBox())){
                    //If they do, call the collision method on the collidables
                    collidableA.onCollision(spriteB);
                    collidableB.onCollision(spriteA);
                }
            }
        }
    }

	//Allows for the creation of an explosion
    //Obviously very important
    public void createExplosion(Image img, Vector2f location, int num, float scale, Vector2f force)
    {
        /*This is done by generating a some num of particles, where each particle's image is a
          random sample of some img. This allows for cool looking explosion effects.*/

        for (int i = 0; i < num; i ++)
        {
            Image subImage = Utility.getRandomSubImage(img);

            Particle particle = new Particle(subImage,location,this, scale, force);

            newEntities.add(particle);
        }
    }

    //Generic method that allows us to query the entity list for entities of a certain type 'type'
    //Really simplifies code elsewhere
    public <T> ArrayList<T> getEntities(Class<T> type)
    {
        ArrayList<T> addedEntities = new ArrayList<>();

        for (Entity e : entities)
        {
            if(type.isInstance(e))
                addedEntities.add((T)e);
        }
        return addedEntities;
    }

    //Similar to above, but only returns a single entity
    //Useful for singular entities like player/gamecontroller
    public <T> T getEntity(Class<T> type)
    {
        for (Entity e : entities)
        {
            if(type.isInstance(e))
                return (T)e;
        }

        return null;
    }
}

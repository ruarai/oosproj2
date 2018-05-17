import org.newdawn.slick.*;

import java.util.ArrayList;

/**
 * The game World, controlling all game updating and rendering
 */
public class World {
    private final ArrayList<Entity> entities = new ArrayList<>();

    //How much holding S/D affects the time scaling
    private static final float TIME_DILATION_FACTOR = 5f;

    private static final Vector PLAYER_LOCATION = new Vector(480,488);

    //Speeds of the layers of our background
    private static final float BACKGROUND_SCROLL_DUST = 0.125f;
    private static final float BACKGROUND_SCROLL_FAR = 0.13f;
    private static final float BACKGROUND_SCROLL_NEAR = 0.17f;

    //Number of times a second we update the physics, higher -> things go faster
    private static final int FIXED_UPDATE_RATE = 40;
    private static final int FIXED_TIME = 1000 / FIXED_UPDATE_RATE;
    private static final int MAX_FIXED_UPDATES_PER_UPDATE = 4;

    private Renderer renderer;

    /**
     * Initialises a new game World
     */
	public World() {
	    //Load in all the imagery
	    Resources.loadResources();

	    renderer = new Renderer(this);

	    resetWorld();
	}

	private void resetWorld(){
	    entities.clear();

        //We've made the background transparent so we can render multiple layers for some parallax effect
        entities.add(new Background(Resources.spaceDust,BACKGROUND_SCROLL_DUST,this));
        entities.add(new Background(Resources.spaceFar,BACKGROUND_SCROLL_FAR,this));
        entities.add(new Background(Resources.space,BACKGROUND_SCROLL_NEAR,this));

        entities.add(new GameplayController(this));
        entities.add(new CollisionManager(this));
        entities.add(new Wizard(this));

        //Create a Player sprite centred on its default location
        entities.add(new Player(PLAYER_LOCATION,this));

        //Load in the world from the waves.txt file
        entities.addAll(Resources.loadWaveData(this));
    }



	//temporary list that we can add entities to during enumeration of actual entities list
    //prevents a concurrentModificationException
	private final ArrayList<Entity> newEntities = new ArrayList<>();

	//similarly for removing entities, even allowing entities to remove themselves
    private final ArrayList<Entity> deadEntities = new ArrayList<>();

    /**
     * Queues an entity to be added to the game's simulation upon the end of the current update
     * @param e The entity that is added
     */
	public void addEntity(Entity e)
    {
        newEntities.add(e);
    }

    /**
     * Queues an entity to be removed from the game's simulation upon the end of the current update
     * @param e The entity removed
     */
    public void killEntity(Entity e)
    {
        deadEntities.add(e);
    }

    private int timeSinceFixedTimeUpdate;

    //Simple paused that lets you look at the current blur frame
    private boolean paused = false;

    /**
     * Performs a single update of the game world
     * @param input The current user input
     * @param delta The elapsed time since this method was last called, to ensure consistent simulation
     */
	public void update(Input input, int delta) {
	    delta *= getEntity(GameplayController.class).getCurrentTimeScale();

	    if(input.isKeyDown(Input.KEY_S))
	        delta *= TIME_DILATION_FACTOR;
        if(input.isKeyDown(Input.KEY_D))
            delta /= TIME_DILATION_FACTOR;
        if(input.isKeyPressed(Input.KEY_A))
            renderer.activateSolitaireRendering();
        if(input.isKeyPressed(Input.KEY_P))
            paused = !paused;
        if(input.isKeyPressed(Input.KEY_R)){
            resetWorld();
            renderer.disableSolitaireRendering();
        }
        if(input.isKeyPressed(Input.KEY_Q))
            System.exit(0);

        //If we're paused, don't update anything
        if(paused)
            return;

	    //clear the list of entities to add/remove
        newEntities.clear();
        deadEntities.clear();

	    //loose update each entity
	    for(Entity e : entities)
            e.looseUpdate(input, delta);

	    fixedTimeUpdate(input, delta);

        //Add/remove any new entities that have been added/removed by other entities
        entities.addAll(newEntities);
        entities.removeAll(deadEntities);
	}

    /**
     * Render a single game frame onto the provided Graphics object
     * @param graphics The Graphics that will be drawn onto
     */
    public void render(Graphics graphics) {
        renderer.render(graphics);
	}

    /**
     * Activates Solitaire mode. Self explanatory.
     */
	public void activateSolitaireMode() {
        //Most important thing, enables solitaire rendering
        renderer.activateSolitaireRendering();

        getEntity(GameplayController.class).enableSolitaireSpawning();
    }

    private void fixedTimeUpdate(Input input, int delta) {
        /* Here is a slightly overcomplicated solution to something that may not have deserved fixing.
           Calculating physics normally proves to be difficult in a variable framerate case, as calculations
           dependent upon elements that themselves depend on time (i.e. acceleration in the case of friction)
           would depend on the framerate rather than just delta. So we can make the framerate for physics fixed,
           eliminating this problem.
        */
        timeSinceFixedTimeUpdate += delta;

        int numPhysicsUpdates = timeSinceFixedTimeUpdate / FIXED_TIME;

        if(numPhysicsUpdates > 0)
            timeSinceFixedTimeUpdate = 0;

        //We don't want to perform too many updates, because then we can kind of skip ahead in time too far
        if(numPhysicsUpdates > MAX_FIXED_UPDATES_PER_UPDATE)
            numPhysicsUpdates = MAX_FIXED_UPDATES_PER_UPDATE;

        for (int i = 0; i < numPhysicsUpdates; i++){
            for(Entity e : entities){
                e.fixedUpdate(input);
            }
        }
    }

    /**
     * Creates a particle effect explosion on the screen
     * @param img The image that will be randomly sampled to create the particles
     * @param location The point that the particles will be spawned
     * @param num The number of particles to be created
     * @param scale The factor that the random movement of the explosion will be scaled by
     * @param force An added force to create a consistent movement to the particles
     */
    public void createExplosion(Image img, Vector location, int num, float scale, Vector force)
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

    /**
     * Returns a list of any entities within the game world of provided type
     * @param type The type that will be queried against
     * @return An ArrayList<T> of entities of type 'type'
     */
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

    /**
     * Returns the first entity within the game world of provided type
     * @param type The type that will be queried against
     * @return The first entity found of type 'type'
     */
    public <T> T getEntity(Class<T> type)
    {
        for (Entity e : entities)
        {
            if(type.isInstance(e))
                return (T)e;
        }

        return null;
    }

    /**
     * Safely returns the list of entities within this World
     * @return A copy of the list of entities
     */
    public ArrayList<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    /**
     * @return If the game is currently paused.
     */
    public boolean getIsPaused(){
        return paused;
    }
}

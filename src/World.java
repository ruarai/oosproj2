import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;


public class World {

    private ArrayList<Entity> entities = new ArrayList<>();

    //Allow for debug rendering of the bounding boxes of any sprites
    private static final boolean RENDER_BOUNDING_BOX = false;

	public World() {
	    //Load in all the imagery
	    Resources.loadResources();

	    entities.add(new Background(this));
	    entities.add(new GameplayController(this));

	    //Create a Player sprite centred on (480,688)
        Vector2f playerDefault = new Vector2f(480,688);
        entities.add(new Player(playerDefault,this));

        //Load in the world from the waves.txt file
        createWorld();
	}

	private void createWorld(){
	    //Load the waves file and split it into a stream of lines
	    try(Stream<String> lines = Files.lines(Paths.get("res","waves.txt"))){
	        //Iterate through the Stream by accessing its iterator
	        for (String line : (Iterable<String>)lines::iterator){
	            //Ignore comments:
	            if(line.startsWith("#"))
	                continue;

	            //Parse the line for name, pos and delay
                String[] parts = line.split(",");
                String name = parts[0];
                int xPosition = Integer.parseInt(parts[1]);
                int delay = Integer.parseInt(parts[2]);

                //Add a new spawner that will spawn the enemy
                entities.add(new EnemySpawner(name,xPosition,delay,this));
            }
        } catch (IOException e){
	        System.out.println("Failed to read the waves file.");
	        System.out.println(e);
        }
    }

	//temporary list that we can add entities to during enumeration of actual entities list
    //prevents a concurrentModificationException
	private ArrayList<Entity> newEntities = new ArrayList<>();

	//similarly for removing entities, even allowing entities to remove themselves
    private ArrayList<Entity> deadEntities = new ArrayList<>();

	//Allows for the addition of entities from outside classes
	public void addEntity(Entity e)
    {
        newEntities.add(e);
    }

    public void killEntity(Entity e)
    {
        deadEntities.add(e);
    }
	
	public void update(Input input, int delta) {
	    if(input.isKeyDown(Input.KEY_S))
	        delta *= 5f;
        if(input.isKeyDown(Input.KEY_D))
            delta /= 5f;


	    //clear the list of entities to add/remove
        newEntities.clear();
        deadEntities.clear();

	    //update each entity
	    for(Entity e : entities)
            e.update(input, delta);

        //Add/remove any new entities that have been added/removed by other entities
        entities.addAll(newEntities);
        entities.removeAll(deadEntities);
	}
	
	public void render(Graphics graphics, int scale) {
	    graphics.scale(scale,scale);

	    //render each entity
        for(Entity e : entities) {
            e.render(graphics);

            if(RENDER_BOUNDING_BOX && e instanceof Sprite)
                graphics.draw(((Sprite)e).getBoundingBox());

        }

	}

	//Allows for the creation of an explosion
    //Obviously very important
    public void createExplosion(Image img, Vector2f location, int num, float scale, Vector2f force)
    {
        for (int i = 0; i < num; i ++)
        {
            Image subImage = Utility.getRandomSubImage(img);

            ExplosionParticle particle = new ExplosionParticle(subImage,location,this, scale, force);

            newEntities.add(particle);
        }
    }

    //Generic method that allows us to query the entity list for entities of a certain type 'type'
    //Really simplifies code elsewhere
    public <T extends Entity> ArrayList<T> getEntitiesOfType(Class<T> type)
    {
        ArrayList<T> addedEntities = new ArrayList<>();

        for (Entity e : entities)
        {
            if(type.isInstance(e))
            {
                addedEntities.add((T)e);
            }
        }
        return addedEntities;
    }

    //Similar to above, but only returns a single entity
    //Useful for singular entities like player/gamecontroller
    public <T extends Entity> T getEntity(Class<T> type)
    {
        for (Entity e : entities)
        {
            if(type.isInstance(e))
            {
                return (T)e;
            }
        }

        return null;
    }
}

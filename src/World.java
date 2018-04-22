import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Random;


public class World {

    public ArrayList<Entity> entities = new ArrayList<>();

    private final boolean RENDER_BOUNDING_BOX = false;

	public World() {
	    //Load in all the imagery
	    Resources.loadResources();

	    entities.add(new Background());

	    //Create a Player sprite centred on (480,688)
        entities.add(new Player(480,688,this));

        //Create 8 Enemy sprites
        for (int i = 0; i < 8; i++)
        {
            entities.add(new BasicEnemy(64 + i * 128,28,this));
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
	        delta *= 5;


	    //clear the list of entities to add/remove
        newEntities.clear();
        deadEntities.clear();

	    //update each entity
	    for(Entity e : entities)
        {
            e.update(input, delta);
        }

        //Add/remove any new entities that have been added/removed by other entities
        entities.addAll(newEntities);
        entities.removeAll(deadEntities);
	}
	
	public void render(Graphics g) {
	    //render each entity
        for(Entity e : entities) {
            e.render();

            if(RENDER_BOUNDING_BOX && e instanceof Sprite)
            {
                Sprite s = (Sprite)e;

                g.draw(s.getBoundingBox());
            }
        }
	}

    public void createExplosion(Image img, Vector2f location, int num)
    {
        for (int i = 0; i < num; i ++)
        {
            Image subImage = Utility.getRandomSubImage(img);

            ExplosionParticle particle = new ExplosionParticle(subImage,location,this);

            newEntities.add(particle);
        }
    }
}

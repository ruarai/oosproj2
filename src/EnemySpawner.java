import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * An entity that will spawn a named Enemy after a given time period.
 */
public class EnemySpawner extends Entity {

    private final static float INITIAL_Y = -64;

    /**
     * @param enemyName The name of the enemy, either "EnemyBasic", "EnemySine", "BasicShooter" or "Boss"
     * @param xPosition The starting x-coordinate of the enemy
     * @param timeDelay The delay until the enemy is spawned in ms
     * @param parentWorld The parent game world
     */
    public EnemySpawner(String enemyName,int xPosition, int timeDelay, World parentWorld) {
        super(parentWorld);

        this.timeDelay = timeDelay;

        //Create a new vector based on the default INITIAL_Y and our given x position
        Vector location = new Vector(xPosition, INITIAL_Y);

        //Read in our string value and create a new enemy of coresponding type
        switch (enemyName){
            case "EnemyBasic":
                enemyEntity = new EnemyBasic(location,parentWorld);
                break;
            case "EnemySine":
                enemyEntity = new EnemySine(location,parentWorld);
                break;
            case "BasicShooter":
                enemyEntity = new EnemyShooter(location,parentWorld);
                break;
            case "Boss":
                enemyEntity = new EnemyBoss(location,parentWorld);
                break;
        }
    }

    private int timeDelay;
    private Enemy enemyEntity;

    /**
     * Run down the delay of the spawner
     * @param input The current game input
     * @param delta The time since the last looseUpdate
     */
    public void looseUpdate(Input input, int delta) {
        //Run down the time delay until we need to spawn the enemy
        timeDelay -= delta;

        //Once we run down the delay, we kill the spawner and spawn the enemy.
        if(timeDelay < 0)
        {
            parentWorld.addEntity(enemyEntity);

            //Destroy ourselves, we are no longer needed
            parentWorld.killEntity(this);
        }
    }


    //No graphics associated with this entity
    public void render(Graphics g) { }
}

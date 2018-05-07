import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemySpawner extends Entity {

    private final static float INITIAL_Y = -64;

    public EnemySpawner(String enemyName,int xPosition, int timeDelay, World parentWorld) {
        super(parentWorld);

        this.timeDelay = timeDelay;

        //Create a new vector based on the default INITIAL_Y and our given x position
        Vector2f location = new Vector2f(xPosition, INITIAL_Y);

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

    public void update(Input input, int delta) {
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

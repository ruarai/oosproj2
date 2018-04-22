import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

//A class to simply hold all image files for the game
//Meaning only one of each file is held in memory
class Resources {
    public static Image basicEnemy;
    public static Image basicShooter;
    public static Image boss;
    public static Image enemyShot;
    public static Image lives;
    public static Image shield;
    public static Image shieldPowerup;
    public static Image shot;
    public static Image shotSpeedPowerup;
    public static Image sineEnemy;
    public static Image space;
    public static Image spaceship;

    //Tries to load in all the resources from disk
    public static void loadResources(){
        try {
            basicEnemy = new Image("res/basic-enemy.png");
            basicShooter = new Image("res/basic-shooter.png");
            boss = new Image("res/boss.png");
            enemyShot = new Image("res/enemy-shot.png");
            lives = new Image("res/lives.png");
            shield = new Image("res/shield.png");
            shieldPowerup = new Image("res/shield-powerup.png");
            shot = new Image("res/shot.png");
            shotSpeedPowerup = new Image("res/shotspeed-powerup.png");
            sineEnemy = new Image("res/sine-enemy.png");
            space = new Image("res/space.png");
            spaceship = new Image("res/spaceship.png");
        } catch(SlickException e){
            //This shouldn't happen, but ok, print an exception message
            System.out.println("An error occurred whilst loading resources:");
            System.out.println(e);
        }

    }
}

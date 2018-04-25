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

    public static Image java;
    public static Image javaError1;
    public static Image javaError2;
    public static Image javaError3;
    public static Image javaError4;
    public static Image javaError5;
    public static Image javaError6;

    private static final int NUM_JAVA_ERRORS = 6;

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

            java = new Image("res/java.png");
            javaError1 = new Image("res/java-err-1.png");
            javaError2 = new Image("res/java-err-2.png");
            javaError3 = new Image("res/java-err-3.png");
            javaError4 = new Image("res/java-err-4.png");
            javaError5 = new Image("res/java-err-5.png");
            javaError6 = new Image("res/java-err-6.png");
        } catch(SlickException e){
            //This shouldn't happen, but ok, print an exception message
            System.out.println("An error occurred whilst loading resources:");
            System.out.println(e);
        }
    }

    public static Image getRandomJavaError(){
        int randInt = Utility.random.nextInt(NUM_JAVA_ERRORS) + 1;

        switch (randInt){
            case 1: return javaError1;
            case 2: return javaError2;
            case 3: return javaError3;
            case 4: return javaError4;
            case 5: return javaError5;
            case 6: return javaError6;
        }

        //Of course Java expects a random number between 1 and 6 to be possibly not be between 1 and 6,
        //So we add this for good measure
        return javaError1;
    }
}

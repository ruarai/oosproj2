import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

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


    public static Image spaceFar;

    public static Image java;
    public static Image javaError1;
    public static Image javaError2;
    public static Image javaError3;
    public static Image javaError4;
    public static Image javaError5;
    public static Image javaError6;

    public static Image solitaire;

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

            spaceFar = new Image("res/space-far.png");

            java = new Image("res/java.png");
            javaError1 = new Image("res/java-err-1.png");
            javaError2 = new Image("res/java-err-2.png");
            javaError3 = new Image("res/java-err-3.png");
            javaError4 = new Image("res/java-err-4.png");
            javaError5 = new Image("res/java-err-5.png");
            javaError6 = new Image("res/java-err-6.png");

            solitaire = new Image("res/solitaire.png");
        } catch(Exception e){
            //This shouldn't happen, but ok, print an exception message
            System.out.println("An exception occurred whilst loading resources:");
            e.printStackTrace();
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


    public static ArrayList<Entity> loadWaveData(World world){
        ArrayList<Entity> newEntities = new ArrayList<>();

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
                newEntities.add(new EnemySpawner(name,xPosition,delay,world));
            }
        } catch (IOException e){
            System.out.println("Failed to read the waves file.");
            e.printStackTrace();
        }
        return newEntities;
    }
}

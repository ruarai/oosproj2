import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

//A class to simply hold all image files for the game
//Meaning only one of each file is held in memory
class Resources {
    public static Image spaceship;
    public static Image enemy;
    public static Image shot;
    public static Image space;

    //Tries to load in all the resources from disk
    public static void loadResources(){
        try {
            spaceship = new Image("res/spaceship.png");
            enemy = new Image("res/basic-enemy.png");
            shot = new Image("res/shot.png");
            space = new Image("res/space.png");
        } catch(SlickException e){
            //This shouldn't happen, but ok, print an exception message
            System.out.println("An error occurred whilst loading resources:");
            System.out.println(e);
        }

    }
}

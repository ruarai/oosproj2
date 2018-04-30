import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class Solitaire extends Sprite {

    private static final float INITIAL_SPEED = 0.5f;

    public Solitaire(World parent) {
        super(Resources.solitaire, generateRandomStart(), parent);

        velocity = new Vector2f(Utility.random.nextFloat()*180);

        velocity.scale(INITIAL_SPEED);
    }

    public void update(Input input, int delta) {
        Vector2f friction = new Vector2f(velocity);

        friction.scale(0.005f);
        velocity.sub(friction);

        Vector2f gravity = new Vector2f(90);
        gravity.scale(delta * 0.001f);

        velocity.add(gravity);


        Vector2f update = new Vector2f(velocity);
        update.scale(delta);
        location.add(update);

        if(location.y > App.SCREEN_HEIGHT - image.getHeight()){
            location.y = App.SCREEN_HEIGHT - image.getHeight();
            velocity.y = -velocity.y;
        }
        if(location.x < 0){
            location.x = 0;
            velocity.x = -velocity.x;
        }
        if(location.x > App.SCREEN_WIDTH - image.getWidth()){
            location.x = App.SCREEN_WIDTH - image.getWidth();
            velocity.x = -velocity.x;
        }
    }

    private static Vector2f generateRandomStart(){
        return new Vector2f(Utility.random.nextFloat()*App.SCREEN_WIDTH,Utility.random.nextFloat()* App.SCREEN_HEIGHT);
    }
}

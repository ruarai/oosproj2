import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class Solitaire extends Sprite {

    private static final float INITIAL_SPEED = 0.5f;

    public Solitaire(World parent) {
        super(Resources.solitaire, generateRandomStart(), parent);

        setVelocity(new Vector2f(Utility.random.nextFloat()*180));

        getVelocity().scale(INITIAL_SPEED);
    }

    public void update(Input input, int delta) {
        Vector2f friction = new Vector2f(getVelocity());

        friction.scale(0.005f);
        getVelocity().sub(friction);

        Vector2f gravity = new Vector2f(90);
        gravity.scale(delta * 0.001f);

        getVelocity().add(gravity);


        Vector2f update = new Vector2f(getVelocity());
        update.scale(delta);
        getLocation().add(update);

        if(getLocation().y > App.SCREEN_HEIGHT - getImage().getHeight()){
            getLocation().y = App.SCREEN_HEIGHT - getImage().getHeight();
            getVelocity().y = -getVelocity().y;
        }
        if(getLocation().x < 0){
            getLocation().x = 0;
            getVelocity().x = -getVelocity().x;
        }
        if(getLocation().x > App.SCREEN_WIDTH - getImage().getWidth()){
            getLocation().x = App.SCREEN_WIDTH - getImage().getWidth();
            getVelocity().x = -getVelocity().x;
        }
    }

    private static Vector2f generateRandomStart(){
        return new Vector2f(Utility.random.nextFloat()*App.SCREEN_WIDTH,Utility.random.nextFloat()* App.SCREEN_HEIGHT);
    }
}

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public abstract class EnemyMoving extends Enemy {

    private static final float DIRECTION = 90f;

    public EnemyMoving(Image image, Vector2f location, World parent) {
        super(image, location, parent);
    }


    public final void update(Input input, int delta) {
        super.update(input, delta);

        //Vector moving downwards
        setVelocity(new Vector2f(DIRECTION));
        getVelocity().scale(getSpeed() * delta);

        getLocation().add(getVelocity());

        //Whilst we expect enemies to be off screen upwards, if they're off the screen downwards they should be killed
        if(getLocation().y > App.SCREEN_HEIGHT)
            parentWorld.killEntity(this);
    }

    public int getScoreValue() {
        return 50;
    }
    public boolean getDestroyable() {
        return true;
    }

    abstract float getSpeed();
}

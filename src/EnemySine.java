import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemySine extends Enemy {

    private static final float VELOCITY_STRAIGHT = 0.15f;
    private static final float DIRECTION_STRAIGHT = 90f;

    private static final float AMPLITUDE = 96f;
    private static final float PERIOD = 1500f;

    private float initialX;
    private float timeElapsed = 0;

    public EnemySine(Vector2f location, World parent) {

        super(Resources.sineEnemy, location, parent);
        initialX = location.x;
    }


    public void update(Input input, int delta) {
        super.update(input, delta);

        timeElapsed += delta;

        Vector2f velocity = new Vector2f(DIRECTION_STRAIGHT);
        velocity.scale(VELOCITY_STRAIGHT * delta);

        location.add(velocity);

        location.x = initialX + AMPLITUDE * (float)Math.sin(((2*Math.PI)/(PERIOD))*timeElapsed);


        //Kill the enemy once it leaves the screen
        if(location.y > App.SCREEN_HEIGHT)
            parentWorld.killEntity(this);
    }

    public int getScoreValue() {
        return 100;
    }
    public boolean getDestroyable() {
        return true;
    }
}

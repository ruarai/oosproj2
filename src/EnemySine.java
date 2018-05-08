import org.newdawn.slick.Input;

public class EnemySine extends Enemy {

    private static final float SPEED = 0.15f;
    private static final float DIRECTION_STRAIGHT = 90f;

    private static final float AMPLITUDE = 96f;
    private static final float PERIOD = 1500f;

    //Line of y = initialX that our sine function will oscillate over
    private float initialX;
    //Time elapsed since spawn to change sine value
    private float timeElapsed = 0;

    public EnemySine(Vector location, World parent) {

        super(Resources.sineEnemy, location, parent);

        //Take note of our initial x location for use in our sine function
        initialX = location.x;
    }

    public void looseUpdate(Input input, int delta) {
        //increment the amount of time that has passed for use in our sine function
        timeElapsed += delta;

        float newX = initialX + AMPLITUDE * (float)Math.sin(((2*Math.PI)/(PERIOD))*timeElapsed);

        setLocation(new Vector(newX, getLocation().y + SPEED * delta));

        //Kill the enemy once it leaves the screen
        if(getLocation().y > App.SCREEN_HEIGHT)
            parentWorld.killEntity(this);
    }

    public int getScoreValue() {
        return 100;
    }
    public boolean getDestroyable() {
        return true;
    }
}

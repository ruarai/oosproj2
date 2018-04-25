import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class EnemyJavaError extends Enemy {


    public EnemyJavaError(Vector2f v, World parent) {
        super(Resources.getRandomJavaError(), v, parent);
    }

    public void update(Input input, int delta) {
        super.update(input, delta);

        Player player = parentWorld.getEntity(Player.class);

        //If player is dead, don't bother
        if(player == null)
            return;


        Vector2f friction = new Vector2f(velocity);

        //Calculate a friction vector to remove from the velocity
        //This sadly isn't frame-independent, but implementing this correctly seems difficult
        friction.scale(0.006f);
        velocity.sub(friction);

        Vector2f towardsPlayer = new Vector2f(player.location).sub(location);

        Vector2f thrust = new Vector2f(towardsPlayer.getTheta() + (Utility.random.nextFloat()-0.5f) * 20);

        float rotationDiff = ((float)towardsPlayer.getTheta() - 90) - rotation;

        rotation += rotationDiff * 0.01;

        thrust.scale(0.01f * delta);

        velocity.add(thrust);

        location.add(velocity);

    }

    public int getScoreValue() {
        return 0;
    }

    public boolean getDestroyable() {
        return true;
    }

    public void onCollision(Sprite collidingSprite) {
        super.onCollision(collidingSprite);
    }
}

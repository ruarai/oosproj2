import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

abstract class Enemy extends Sprite {

    private static final int PLAYER_HIT_EXPLOSION_SIZE = 40;
    private static final int EXPLOSION_DELAY = 150;

    private static final float PLAYER_HIT_SCREEN_SHAKE = 1.5f;

    public Enemy(Image img, Vector2f v, World parent) {
        super(img, v, parent);
    }

    private int explosionDelay = 0;

    public void update(Input input, int delta) {
        Player player = parentWorld.getEntity(Player.class);

        //If the player doesn't exist, we certainly don't intersect with it
        if(player == null)
            return;

        //Do we intersect with the player?
        if (player.getBoundingBox().intersects(this.getBoundingBox())) {
            //If so, 'kill' the player
            parentWorld.getEntity(GameplayController.class).playerDeath();

            player.velocity.add(new Vector2f(velocity).scale(0.1f));


            //Create a cool explosion also, if we haven't recently
            if(explosionDelay <= 0)
            {
                parentWorld.createExplosion(Resources.enemyShot,getCentre(), PLAYER_HIT_EXPLOSION_SIZE,0.1f, velocity);
                explosionDelay = EXPLOSION_DELAY;
                parentWorld.getEntity(GameplayController.class).shakeScreen(PLAYER_HIT_SCREEN_SHAKE);
            }
        }

        if(explosionDelay > 0)
            explosionDelay -= delta;
    }



    //Returns the score value of the enemy
    public abstract int getScoreValue();

    //Returns whether the enemy can be destroyed by the players laser shots
    public abstract boolean getDestroyable();
}

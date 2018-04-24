import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

abstract class Enemy extends Sprite implements Collidable {

    private static final int HIT_EXPLOSION_SIZE = 40;
    private static final int HIT_EXPLOSION_DELAY = 150;
    private static final float HIT_EXPLOSION_SCALE = 0.1f;

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

        //Run down the explosion delay for collisions
        if(explosionDelay > 0)
            explosionDelay -= delta;

        //Whilst we expect enemies to be off screen upwards, if they're off the screen downwards they should be killed
        if(location.y > App.SCREEN_HEIGHT)
            parentWorld.killEntity(this);
    }



    //Returns the score value of the enemy
    public abstract int getScoreValue();

    //Returns whether the enemy can be destroyed by the players laser shots
    public abstract boolean getDestroyable();

    public void onCollision(Sprite collidingSprite) {
        //The player death is handled within Player, so we just handle the cool effects
        if(collidingSprite instanceof Player) {
            //Create a cool explosion and screen shake, if we haven't recently
            if(explosionDelay <= 0)
            {
                parentWorld.createExplosion(Resources.enemyShot,getCentre(), HIT_EXPLOSION_SIZE,HIT_EXPLOSION_SCALE, velocity);
                parentWorld.getEntity(GameplayController.class).shakeScreen(PLAYER_HIT_SCREEN_SHAKE);

                explosionDelay = HIT_EXPLOSION_DELAY;
            }
        } else if (collidingSprite instanceof Laser) {
            if(getDestroyable()){
                parentWorld.killEntity(this);
                parentWorld.getEntity(GameplayController.class).enemyDeath(this);
            }
        }
    }
}

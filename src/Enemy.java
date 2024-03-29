import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

/**
 * Abstract class that provides basic Enemy logic
 */
abstract class Enemy extends Sprite implements Collidable {
    private static final int HIT_EXPLOSION_SIZE = 40;
    private static final int HIT_EXPLOSION_DELAY = 150;
    private static final float HIT_EXPLOSION_SCALE = 0.1f;

    private static final int DEATH_EXPLOSION_SIZE = 100;
    private static final float DEATH_EXPLOSION_SCALE = 0.15f;

    private static final float PLAYER_HIT_SCREEN_SHAKE = 1.5f;

    private static final float POWERUP_CHANCE = 0.05f;

    /**
     * @param img The image of the Enemy
     * @param v The location of the Enemy on the game screen
     * @param parent The parent game world
     */
    public Enemy(Image img, Vector v, World parent) {
        super(img, v, parent);
    }

    //How long until we should make an explosion again, so as to not make one 60 times a second
    private int explosionDelay = 0;

    /**
     * Updates the non-physical elements of the Enemy
     * @param input Current game input
     * @param delta Time since last frame
     */
    public void looseUpdate(Input input, int delta) {
        super.looseUpdate(input, delta);

        //Run down the explosion delay for collisions
        if(explosionDelay > 0)
            explosionDelay -= delta;
    }


    /**
     * @return The point value of this Enemy when the Player kills it
     */
    public abstract int getScoreValue();

    /**
     * @return If the Enemy is destroyable by the Player's laser shots
     */
    public abstract boolean getDestroyable();

    /**
     * Handles collision with the Player or the Laser
     */
    public void onCollision(Sprite collidingSprite) {
        //The player death is handled within Player, so we just handle the cool effects
        if(collidingSprite instanceof Player) {
            if(explosionDelay <= 0)
            {
                //Create a cool explosion and screen shake, if we haven't recently
                parentWorld.createExplosion(Resources.enemyShot,collidingSprite.getCentre(), HIT_EXPLOSION_SIZE,HIT_EXPLOSION_SCALE, getVelocity());
                parentWorld.getEntity(GameplayController.class).shakeScreen(PLAYER_HIT_SCREEN_SHAKE);

                //Reset the explosion delay
                explosionDelay = HIT_EXPLOSION_DELAY;
            }
        } else if (collidingSprite instanceof Laser) {
            if(getDestroyable()){
                //If our implementing class has designated ourself as destroyable, we destroy ourselves
                parentWorld.killEntity(this);
                parentWorld.getEntity(GameplayController.class).enemyDeath(this);

                //create an explosion based on our image
                parentWorld.createExplosion(getImage(),collidingSprite.getCentre(),DEATH_EXPLOSION_SIZE,DEATH_EXPLOSION_SCALE, collidingSprite.getVelocity());

                //Conditional with a POWERUP_CHANCE chance of occurring
                if(Utility.random.nextFloat() < POWERUP_CHANCE) {
                    //Then split the conditional 50/50
                    if(Utility.random.nextFloat() > 0.5f)
                        parentWorld.addEntity(new ShieldPowerup(getCentre(),parentWorld));
                    else
                        parentWorld.addEntity(new ShotSpeedPowerup(getCentre(),parentWorld));
                }
            }
        }
    }
}

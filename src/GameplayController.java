import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

/**
 * Primarily non-graphical Entity that controls game logic.
 */
public class GameplayController extends Entity {

    private static final Vector2f SCORE_OFFSET = new Vector2f(20,738);
    private static final Vector2f LIVES_OFFSET = new Vector2f(20,696);
    private static final float LIVES_GAP = 40f;

    private static final int PLAYER_INIT_LIVES = 3;
    private static final int PLAYER_DEATH_SHIELD_TIME = 3000;
    private static final int PLAYER_INITIAL_SHIELD_TIME = 3000;

    private static final int PLAYER_DEATH_EXPLOSION_SIZE = 500;
    private static final float PLAYER_DEATH_EXPLOSION_SCALE = 2f;

    private static final float SCREEN_SHAKE_DECAY = 0.001f;
    private static final float SCREEN_SHAKE_MAGNITUDE = 3f;

    private static final float TIME_EFFECT_DECAY = 0.0001f;
    private static final float TIME_EFFECT_START = 0.8f;

    private static final int SOLITAIRE_INITIAL_DELAY = 3500;
    private static final int SOLITAIRE_DELAY = 500;

    private static final float PLAYER_SCORE_ICON_SCALE = 0.5f;

    private int playerScore = 0;
    private int playerLives = PLAYER_INIT_LIVES;

    private int shieldTime = PLAYER_INITIAL_SHIELD_TIME;

    private int shotSpeedPowerupTime = 0;
    private static final int DEFAULT_SHOT_DELAY = 250;
    private static final int POWERUP_SHOT_DELAY = 150;

    /**
     * @param parentWorld The parent game world
     */
    public GameplayController(World parentWorld) { super(parentWorld); }

    /**
     * Updates game features like powerups, screen shake, slow motion and solitaire
     * @param input The current game input
     * @param delta The time since the last looseUpdate
     */
    public void looseUpdate(Input input, int delta) {
        if(shotSpeedPowerupTime > 0)
            shotSpeedPowerupTime -= delta;

        if(shieldTime > 0)
            shieldTime -= delta;

        timeElapsed += delta;

        //Try to run down the shakeLife, and if it's less than zero make sure it's exactly zero
        if(shakeLife > 0)
            shakeLife -= delta * SCREEN_SHAKE_DECAY;
        else
            shakeLife = 0;

        //Same for slowLife
        if(slowLife > 0)
            slowLife -= delta * TIME_EFFECT_DECAY;
        else
            slowLife = 0;

        if(spawnSolitaires){
            if(solitaireDelay > 0){
                solitaireDelay -= delta;
            } else {
                parentWorld.addEntity(new Solitaire(parentWorld));
                solitaireDelay = SOLITAIRE_DELAY;
            }
        }
    }

    private float timeElapsed = 0;
    private float shakeLife = 0;

    /**
     * Shakes the screen by some given amount
     * @param shakeMagnitude The amount for the screen to shake
     */
    public void shakeScreen(float shakeMagnitude)
    {
        shakeLife = shakeMagnitude;
    }

    /**
     * @return The magnitude/life of the current screen shake
     */
    public float getCurrentScreenShake(){
        return shakeLife;
    }

    private float slowLife = 0;

    /**
     * Triggers the slow motion effect to begin
     */
    public void slowTime(){
        //Starts the time dilation effect
        //We can't set this to be too close to 1, since that will stop the game from updating and hence allowing the effect to decay
        slowLife = TIME_EFFECT_START;
    }

    /**
     * @return The current time scaling factor
     */
    public float getCurrentTimeScale(){
        return 1 - slowLife;
    }

    /**
     * Renders the screen shake, player score and player lives onto graphics
     * @param graphics The graphics to use to render on screen
     */
    public void render(Graphics graphics) {
        //Calculate how much the screen will shake
        float xOffset = shakeLife * SCREEN_SHAKE_MAGNITUDE * (float)Math.sin(timeElapsed);
        float yOffset = shakeLife * SCREEN_SHAKE_MAGNITUDE * (float)Math.cos(timeElapsed);

        //Perform a transform that will 'shake' the screen
        graphics.translate(xOffset,yOffset);

        //Draw in the players score
        graphics.drawString("Score:" + playerScore,SCORE_OFFSET.x,SCORE_OFFSET.y);

        //Make sure the spaceship is upright, so that we can use it to draw the player score
        Resources.spaceship.setRotation(0);

        //Draw in each of the player's lives
        for (int i = 0; i < playerLives; i ++)
        {
            Resources.spaceship.draw(LIVES_OFFSET.x + LIVES_GAP * i,LIVES_OFFSET.y,PLAYER_SCORE_ICON_SCALE);
        }
    }


    /**
     * To be called upon the death of an Enemy, will add to the Player's score
     * @param e The enemy that has died
     */
    public void enemyDeath(Enemy e) {
        playerScore += e.getScoreValue();
    }

    /**
     * To be called upon the hit of the Player, will determine if they die
     */
    public void playerHit(){

        //Is the shield active?
        if(!getIsShieldActive()) {
            //If not, we kill the player and create a cool explosion.
            Player player = parentWorld.getEntity(Player.class);

            if(playerLives > 1){
                //If the player has lives left, take one and active shield
                playerLives--;
                shieldTime = PLAYER_DEATH_SHIELD_TIME;
            }
            else {
                //The player is out of lives, remove the entity and create a very big explosion
                parentWorld.killEntity(player);
                parentWorld.createExplosion(player.getImage(),player.getCentre(), PLAYER_DEATH_EXPLOSION_SIZE, PLAYER_DEATH_EXPLOSION_SCALE, player.getVelocity());

                //Slow motion on death
                slowTime();

                parentWorld.activateSolitaireMode();
            }
        }

    }

    /**
     * @return If the Player's shield should be active
     */
    public boolean getIsShieldActive(){
        return shieldTime > 0;
    }

    /**
     * @param shieldTime How long the Player's shield should be active for
     */
    public void setShieldTime(int shieldTime)
    {
        this.shieldTime = shieldTime;
    }

    /**
     * @return The current shot delay for the Player
     */
    public int getCurrentShotDelay() {
        return shotSpeedPowerupTime > 0 ? POWERUP_SHOT_DELAY : DEFAULT_SHOT_DELAY;
    }

    /**
     * @param shotSpeedPowerupTime How long the ShotSpeed powerup should be active for
     */
    public void setShotSpeedPowerupTime(int shotSpeedPowerupTime) {
        this.shotSpeedPowerupTime = shotSpeedPowerupTime;
    }

    private boolean spawnSolitaires = false;
    private int solitaireDelay = SOLITAIRE_INITIAL_DELAY;

    /**
     * Enables spawning of the Solitaire entities according to some given delay
     */
    public void enableSolitaireSpawning() {
        spawnSolitaires = true;
    }
}

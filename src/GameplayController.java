import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

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

    private int playerScore = 0;
    private int playerLives = PLAYER_INIT_LIVES;

    private int shieldTime = PLAYER_INITIAL_SHIELD_TIME;

    private int shotSpeedTime = 0;
    private static final int DEFAULT_SHOT_DELAY = 250;
    private static final int POWERUP_SHOT_DELAY = 150;

    public GameplayController(World parentWorld) { super(parentWorld); }

    public void update(Input input, int delta) {
        if(shotSpeedTime > 0)
            shotSpeedTime -= delta;

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
    }

    private float timeElapsed = 0;
    private float shakeLife = 0;

    //Method to allow shaking of the screen on some important event
    public void shakeScreen(float shakeMagnitude)
    {
        shakeLife = shakeMagnitude;
    }

    public float getCurrentScreenShake(){
        return shakeLife;
    }

    private float slowLife = 0;

    public void slowTime(){
        //Starts the time dilation effect
        //We can't set this to be too close to 1, since that will stop the game from updating and hence allowing it decay
        slowLife = TIME_EFFECT_START;
    }

    public float getCurrentTimeScale(){
        return 1 - slowLife;
    }

    public void render(Graphics graphics) {
        //Calculate how much the screen will shake
        float xOffset = shakeLife * SCREEN_SHAKE_MAGNITUDE * (float)Math.sin(timeElapsed);
        float yOffset = shakeLife * SCREEN_SHAKE_MAGNITUDE * (float)Math.cos(timeElapsed);

        //Perform a transform that will 'shake' the screen
        graphics.translate(xOffset,yOffset);

        //Draw in the players score
        graphics.drawString("Score:" + playerScore,SCORE_OFFSET.x,SCORE_OFFSET.y);

        //Make sure the spaceship is upright:
        Resources.spaceship.setRotation(0);

        //Draw in each of the player's lives
        for (int i = 0; i < playerLives; i ++)
        {
            Resources.spaceship.draw(LIVES_OFFSET.x + LIVES_GAP * i,LIVES_OFFSET.y,0.5f);
        }
    }


    //Method called when an enemy is killed, adds score to the player
    public void enemyDeath(Enemy e) {
        playerScore += e.getScoreValue();
    }

    //Method called when the player is killed
    public void playerDeath(){

        //Is the shield active?
        if(getIsShieldActive()) {
            //If so, player doesn't die, try again later
            return;
        }
        else {
            //Otherwise, we kill the player and create a cool explosion.
            Player player = parentWorld.getEntity(Player.class);

            if(playerLives > 0){
                //If the player has lives left, take one and active shield
                playerLives--;
                shieldTime = PLAYER_DEATH_SHIELD_TIME;
            }
            else {
                //The player is out of lives, remove the entity and create a very big explosion
                parentWorld.killEntity(player);
                parentWorld.createExplosion(player.image,player.getCentre(), PLAYER_DEATH_EXPLOSION_SIZE, PLAYER_DEATH_EXPLOSION_SCALE, player.velocity);

                //Slow motion on death
                slowTime();

                parentWorld.activateSolitaireMode();
            }
        }

    }

    //Returns if the player's shield is active, allows for it to be rendered elsewhere
    public boolean getIsShieldActive(){
        return shieldTime > 0;
    }

    public void setShieldTime(int shieldTime)
    {
        this.shieldTime = shieldTime;
    }

    public int getCurrentShotDelay() {
        return shotSpeedTime > 0 ? POWERUP_SHOT_DELAY : DEFAULT_SHOT_DELAY;
    }

    public void setShotSpeedTime(int shotSpeedTime) {
        this.shotSpeedTime = shotSpeedTime;
    }
}

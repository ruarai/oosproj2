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

    private int playerScore = 0;
    private int playerLives = PLAYER_INIT_LIVES;

    private int shieldTime = PLAYER_INITIAL_SHIELD_TIME;

    public GameplayController(World parentWorld) { super(parentWorld); }

    public void update(Input input, int delta) {
        if(shieldTime > 0){
            shieldTime -= delta;
        }

        timeElapsed += delta;

        if(shakeLife > 0)
            shakeLife -= delta * 0.001f;
        else
            shakeLife = 0;

    }

    public void shakeScreen(){
        shakeLife = 1;
    }

    private float timeElapsed = 0;
    private float shakeLife = 0;

    public void render(Graphics graphics) {

        graphics.translate(shakeLife * 7f * (float)Math.sin(timeElapsed),shakeLife * 7f * (float)Math.cos(timeElapsed));


        graphics.drawString("Score:" + playerScore,SCORE_OFFSET.x,SCORE_OFFSET.y);

        //Make sure the spaceship is upright:
        Resources.spaceship.setRotation(0);

        for (int i = 0; i < playerLives; i ++)
        {
            Resources.spaceship.draw(LIVES_OFFSET.x + LIVES_GAP * i,LIVES_OFFSET.y,0.5f);

        }
    }

    public void enemyDeath(Enemy e) {
        playerScore += e.getScoreValue();
    }

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
                parentWorld.killEntity(player);

                parentWorld.createExplosion(player.image,player.getCentre(), PLAYER_DEATH_EXPLOSION_SIZE, 2f, player.velocity);
            }
        }

    }

    public boolean getIsShieldActive(){
        return shieldTime > 0;
    }
}

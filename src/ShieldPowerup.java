/**
 * A shield powerup that will create a shield effect of 5 seconds upon collision with player
 */
public class ShieldPowerup extends Powerup {

    private static final int SHIELD_TIME = 5000;

    public ShieldPowerup(Vector v, World parent) {
        super(Resources.shieldPowerup, v, parent);
    }

    public void onCollision(Sprite sprite) {
        if(sprite instanceof Player)
        {
            //If we collide with the Player, make their shield activate through the GameplayerController
            parentWorld.getEntity(GameplayController.class).setShieldTime(SHIELD_TIME);
            parentWorld.killEntity(this);
        }
    }
}

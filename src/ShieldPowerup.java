import org.newdawn.slick.geom.Vector2f;

public class ShieldPowerup extends Powerup {

    private static final int SHIELD_TIME = 5000;

    public ShieldPowerup(Vector2f v, World parent) {
        super(Resources.shieldPowerup, v, parent);
    }

    public void onCollision(Sprite sprite) {
        if(sprite instanceof Player)
        {
            parentWorld.getEntity(GameplayController.class).setShieldTime(SHIELD_TIME);
            parentWorld.killEntity(this);
        }
    }
}

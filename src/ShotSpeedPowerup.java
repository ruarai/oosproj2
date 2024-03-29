public class ShotSpeedPowerup extends Powerup {

    private static final int SHOT_SPEED_TIME = 5000;

    public ShotSpeedPowerup(Vector v, World parent) {
        super(Resources.shotSpeedPowerup, v, parent);
    }

    public void onCollision(Sprite sprite) {
        if(sprite instanceof Player){
            //If we collide with the player, increase the players rate of fire for SHOT_SPEED_TIME number of ms
            parentWorld.getEntity(GameplayController.class).setShotSpeedPowerupTime(SHOT_SPEED_TIME);
            parentWorld.killEntity(this);
        }
    }
}

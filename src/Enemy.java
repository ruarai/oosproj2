import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

abstract class Enemy extends Sprite {
    public Enemy(Image img, Vector2f v, World parent) { super(img, v, parent); }

    public void update(Input input, int delta) {

        //Look for players to explode:
        for (Entity e : parentWorld.entities) {
            if(e instanceof Player)
            {
                Player player = (Player) e;

                if(player.getBoundingBox().intersects(this.getBoundingBox()))
                {
                    //Kill the enemy, it intersects with us, the laser
                    parentWorld.killEntity(player);

                    parentWorld.createExplosion(player.image,player.getCentre(),15);
                }
            }
        }
    }

    //Returns the score value of the enemy
    public abstract int getScoreValue();

    //Returns whether the enemy can be destroyed by the players laser shots
    public abstract boolean getDestroyable();
}

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

class Enemy extends Sprite {
    public Enemy(Image img, float x, float y, World parent) {
        super(img, x, y, parent);
    }
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
}

/**
 * Interface for Sprites to implement that allows them to handle collision with other Collidables
 */
public interface Collidable {
    /**
     * Handles the implementing Sprite colliding with some other Collidable 'sprite'
     * @param sprite The Sprite colliding with the implementing Sprite
     */
    void onCollision(Sprite sprite);
}

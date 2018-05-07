//Interface for Sprites to implement that allows them to handle collision logic
public interface Collidable {
    //Method that handles this implementing Sprite colliding with some other Collidable Sprite
    void onCollision(Sprite sprite);
}

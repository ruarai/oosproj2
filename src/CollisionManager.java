import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;

public class CollisionManager extends Entity {
    public CollisionManager(World parentWorld) {
        super(parentWorld);
    }

    public void looseUpdate(Input input, int delta) {
        //Create a list of all collidable objects
        ArrayList<Collidable> collidables = parentWorld.getEntities(Collidable.class);


        //We perform this one one diagonal half such that the collision checks are not performed twice per Sprite
        for (int x = 0; x < collidables.size(); x++){
            for(int y = x + 1; y < collidables.size(); y++){
                //Not pretty, but we need a copy of both the collidable and sprite reference
                Collidable collidableA = collidables.get(x);
                Collidable collidableB = collidables.get(y);

                Sprite spriteA = (Sprite)collidableA;
                Sprite spriteB = (Sprite)collidableB;

                //Check if the sprites intersect
                if(spriteA.getBoundingBox().intersects(spriteB.getBoundingBox())){
                    //If they do, call the collision method on the collidables
                    collidableA.onCollision(spriteB);
                    collidableB.onCollision(spriteA);
                }
            }
        }
    }


    public void render(Graphics graphics) {

    }
}

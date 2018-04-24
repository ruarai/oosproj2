import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class ExhaustParticle extends Particle {

    private static final float LIFE_DECAY_RATE = 0.001f;
    private static final float RANDOM_SCALE = 0.03f;
    private static final float VELOCITY_SCALE = -3f;

    public ExhaustParticle(Vector2f location, World parent, Vector2f force) {
        super(generateImage(), location, parent, RANDOM_SCALE, new Vector2f(force).scale(VELOCITY_SCALE));

        //Set the life decay rate according to the constant default
        lifeDecayRate = LIFE_DECAY_RATE;
    }

    //Create a quick exhaust image by sampling Resources.shot
    private static Image generateImage(){
        return Resources.shot.getSubImage(8,8,2,8);
    }
}

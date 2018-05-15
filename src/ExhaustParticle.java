import org.newdawn.slick.Image;

/**
 * A specific Particle with some exhaust-like defaults
 */
public class ExhaustParticle extends Particle {

    private static final float LIFE_DECAY_RATE = 0.001f;
    private static final float RANDOM_SCALE = 0.03f;
    private static final float VELOCITY_SCALE = -3f;

    private static final Vector IMAGE_OFFSET = new Vector(8,8);
    private static final Vector IMAGE_SIZE = new Vector(8,8);


    /**
     * @param location The starting location of the ExhaustParticle
     * @param force The force that the exhaust particles will have added to them
     * @param parent The parent game world
     */
    public ExhaustParticle(Vector location, Vector force, World parent) {
        super(generateImage(), location, parent, RANDOM_SCALE, force.scale(VELOCITY_SCALE));

        //Set the life decay rate according to the constant default
        lifeDecayRate = LIFE_DECAY_RATE;
    }

    //Create a quick exhaust image by sampling Resources.shot
    private static Image generateImage(){
        return Resources.shot.getSubImage((int)IMAGE_OFFSET.x, (int)IMAGE_OFFSET.y,
                                          (int)IMAGE_SIZE.x,   (int)IMAGE_SIZE.y);
    }
}

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Wizard {

    private static final String[] MAGIC_NUMBERS = {
        "MOVE_ACCEL",
        "RECOIL_ACCEL",
        "ROTATION_SPEED",
        "FRICTION_SCALE",
        "DIR_FORWARDS",
        "PLAYER_HIT_BOUNCE_SCALE"
    };

    static void performMagic(Player player){
        try{
            //Find a random magic number to edit
            String magicNumberName = MAGIC_NUMBERS[Utility.random.nextInt(MAGIC_NUMBERS.length)];

            //Use reflection to get the field
            Field field = player.getClass().getDeclaredField(magicNumberName);
            field.setAccessible(true);

            //Find the modifiers
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);

            //Remove final modifier
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            //Calculate a new value at a random offset from its prior value
            float priorValue = (float)field.get(player);
            float newValue = priorValue + priorValue * 2 * (Utility.random.nextFloat());

            //Set it
            field.set(player, newValue);

            System.out.println("Changed " + magicNumberName + " from " + priorValue + " to " + newValue);
        } catch(Exception e){
            System.out.println("The wizard has failed his quest:");
            e.printStackTrace();
        }


    }
}

/*I was going to use the inbuilt Vector2f class for this purpose,
  but it wasn't immutable at all, leaving me with some rather gross code.
  So this is an immutable 2d vector class
*/

/**
 * An immutable 2D vector with useful methods
 */
public class Vector {
    public final float x;
    public final float y;

    /**
     * Creates an empty vector, (0,0)
     */
    public Vector() {
        x = 0;
        y = 0;
    }

    /**
     * Creates a vector at some given cartesian coordinates
     * @param x The x-coordinate of the vector
     * @param y The y-coordinate of the vector
     */
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a unit vector with some rotation counter-clockwise of angle
     * @param angle How much to rotate the unit vector counter-clockwise
     */
    public Vector(float angle) {
        this.x = (float)Math.cos(Math.toRadians(angle));
        this.y = (float)Math.sin(Math.toRadians(angle));
    }

    /**
     * @return The length of the vector
     */
    public float getLength(){
        return (float)Math.sqrt(x*x+y*y);
    }

    /**
     * @param vector The vector that is to be added to this one
     * @return The resultant vector
     */
    public Vector add(Vector vector) {
        return new Vector(x + vector.x, y + vector.y);
    }

    /**
     * @param vector The vector that is to be subtracted from this one
     * @return The resultant vector
     */
    public Vector sub(Vector vector) {
        return new Vector(x - vector.x, y - vector.y);
    }

    /**
     * @param scale The scalar value to multiply this vector by
     * @return The scaled vector
     */
    public Vector scale(float scale){
        return new Vector(x * scale, y * scale);
    }

    /**
     * @return The angle this vector makes with the positive x-axis, counter-clockwise
     */
    public float getAngle() {
        float angle = (float)(Math.toDegrees(Math.atan2(y,x)));

        return angle;
    }

    /**
     * @param vector The vector to be dot producted with this one
     * @return The dot product of this vector with the given vector
     */
    public float dotProduct(Vector vector) {
        return x * vector.x + y * vector.y;
    }

    /**
     * Returns this vector projected onto some given unit vector
     * @param unit The unit vector to be projected onto
     * @return The projected vector
     */
    public Vector unitProjection(Vector unit){
        float dotProduct = this.dotProduct(unit);

        return new Vector(dotProduct * unit.x,dotProduct * unit.y);
    }


}

/*I was going to use the inbuilt Vector2f class for this purpose,
  but it wasn't immutable at all, leaving me with some rather gross code.
  So this is an immutable 2d vector class
*/
public class Vector {
    public final float x;
    public final float y;

    public Vector() {
        x = 0;
        y = 0;
    }

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector(float angle) {
        this.x = (float)Math.cos(Math.toRadians(angle));
        this.y = (float)Math.sin(Math.toRadians(angle));
    }

    public float getLength(){
        return (float)Math.sqrt(x*x+y*y);
    }

    public Vector add(Vector vector) {
        return new Vector(x + vector.x, y + vector.y);
    }

    public Vector sub(Vector vector) {
        return new Vector(x - vector.x, y - vector.y);
    }

    public Vector scale(float scale){
        return new Vector(x * scale, y * scale);
    }

    public float getAngle() {
        float angle = (float)(Math.toDegrees(Math.atan2(y,x)));

        return angle;
    }

    public float dotProduct(Vector vector) {
        return x * vector.x + y * vector.y;
    }

    public Vector unitProjection(Vector unit){
        float dotProduct = this.dotProduct(unit);

        return new Vector(dotProduct * unit.x,dotProduct * unit.y);
    }


}

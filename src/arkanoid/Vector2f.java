/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid;

public class Vector2f {

    private double x;
    private double y;
    private double tilt;

    public Vector2f(float inX, float inY, double inTilt) {
        x = inX;
        y = inY;
        tilt = inTilt;
        double LENGTH = Math.sqrt(x * x + y * y);
        x /= LENGTH;
        y /= LENGTH;
    }

    public void invertX() {
        x *= -1;
        tilt *= -1;
    }

    public void invertY() {
        y *= -1;
    }

    public void setTitlt(double newTilt) {
        tilt = newTilt;
    }

    public double getX() {
        return x + tilt;
    }

    public double getY() {
        return y;
    }
}

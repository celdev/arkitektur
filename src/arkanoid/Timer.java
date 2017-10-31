/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid;

public class Timer {

    private float mark;
    private long startTime;
    private boolean isRunning;

    public Timer() {
        startTime = 0;
        isRunning = false;
    }

    public void startTimer(float inMark) {
        isRunning = true;

        mark = inMark;
        startTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        if (isRunning && ((System.currentTimeMillis() - startTime) > (1000 * mark))) {
            startTime = 0;
            isRunning = false;
            return true;
        } else {
            return false;
        }
    }

}

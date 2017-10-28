package ajil.com.bounce.newBounce;

import java.util.ArrayList;

/**
 * Created by ajil on 27/10/17.
 */

public interface OnBallMovedListener {
    ArrayList<Obstructor> onMoved(float centerX, float centerY);

    void disqualify(Ball ball, Paddle paddle);
}

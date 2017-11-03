package ajil.com.bounce.helpers;

import java.util.ArrayList;

import ajil.com.bounce.objects.Ball;
import ajil.com.bounce.objects.Paddle;

/**
 * Created by ajil on 27/10/17.
 */

public interface OnBallMovedListener {
    ArrayList<Obstructor> onMoved(Ball ball, float centerX, float centerY);

    void disqualify(Ball ball, Paddle paddle);
}

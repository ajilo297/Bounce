package ajil.com.bounce.first;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by ajilo on 24-10-2017. Class for ball params
 */

public class Ball extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "BALL";
    private Drawable drawable;
    private int ballId;
    private float vx = 5, vy = 3;
    public boolean isGravityOn;
    public static final int DELECTION_LEFT = 1;
    public static final int DELECTION_TOP = 2;
    public static final int DELECTION_RIGHT = 3;
    public static final int DELECTION_BOTTOM = 4;

    public Ball(Context context) {
        super(context);
    }

    public Ball(Context context, Drawable drawable, int ballId, CoordinatePair<Float> center, CoordinatePair<Float> velocity) {
        super(context);
        this.drawable = drawable;
        this.setImageDrawable(drawable);
        this.ballId = ballId;
        this.setCenter(center);
        this.setVelocity(velocity);
        Log.e(TAG, "Ball dropped");
    }

    public float getRadius () {
        return this.getDrawHeight() / 2;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public int getDrawWidth() {
        return drawable.getIntrinsicWidth();
    }

    public int getDrawHeight() {
        return drawable.getIntrinsicHeight();
    }

    public int getBallId() {
        return ballId;
    }

    public CoordinatePair<Float> getCenter() {
        float centerX = this.getX() + (getDrawWidth() / 2);
        float centerY = this.getY() + (getDrawHeight() / 2);
        return new CoordinatePair<>(centerX, centerY);
    }

    public CoordinatePair<Float> getVelocity() {
        return new CoordinatePair<>(getVx(), getVy());
    }

    public void setVelocity(CoordinatePair<Float> velocity) {
        this.vx = velocity.getX();
        this.vy = velocity.getY();
    }

    public void setCenter(CoordinatePair<Float> center) {
        float x = center.getX();
        float y = center.getY();

        this.setX(x - this.getRadius());
        this.setY(y - this.getRadius());
    }

    public void addGravity() {
        isGravityOn = true;
    }

    public int getDeflectionSide(CoordinatePair<Float> coordinatePair) {
        CoordinatePair<Float> c1 = new CoordinatePair<>(this.getX(), this.getY());
        CoordinatePair<Float> c2 = new CoordinatePair<>(this.getX() + this.getWidth(), this.getY());
        CoordinatePair<Float> c3 = new CoordinatePair<>(this.getX(), this.getY() + this.getHeight());
        CoordinatePair<Float> c4 = new CoordinatePair<>(this.getX() + this.getWidth(), this.getY() + this.getHeight());

        if (coordinatePair.getX() < c1.getX() && coordinatePair.getY() < c1.getY()) {
            return DELECTION_TOP;
        } else if (coordinatePair.getX() < c1.getX() && coordinatePair.getY() > c1.getY() && coordinatePair.getY() < c3.getY()) {
            return DELECTION_LEFT;
        } else if (coordinatePair.getX() > c2.getX() && coordinatePair.getY() > c2.getY() && coordinatePair.getY() < c3.getY()) {
            return DELECTION_RIGHT;
        } else {
            return DELECTION_BOTTOM;
        }
    }
}

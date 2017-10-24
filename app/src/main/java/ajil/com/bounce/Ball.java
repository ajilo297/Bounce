package ajil.com.bounce;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by ajilo on 24-10-2017.
 */

public class Ball extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "BALL";
    private Drawable drawable;
    private int ballId;
    private float vx = 5, vy = 3;

    public Ball(Context context) {
        super(context);
    }

    public Ball(Context context, Drawable drawable, int ballID) {
        super(context);
        this.drawable = drawable;
        this.setImageDrawable(drawable);
        this.ballId = ballID;
        float factor = ((float) ballID) / 3;
        vy *= factor;
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
}

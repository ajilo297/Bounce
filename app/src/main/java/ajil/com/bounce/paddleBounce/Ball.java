package ajil.com.bounce.paddleBounce;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

/**
 * Created by ajilo on 25-10-2017.
 */

public class Ball extends android.support.v7.widget.AppCompatImageView {

    private Context context;
    private ValuePair<Integer> velocity;
    private Drawable drawable;
    private int drawHeight;
    private int drawWidth;

    public Ball(Context context) {
        super(context);
        this.context = context;
    }

    public Ball(Context context, Drawable drawable) {
        super(context);
        this.context = context;
        this.drawable = drawable;
        drawHeight = drawable.getIntrinsicHeight();
        drawWidth = drawable.getIntrinsicWidth();
        this.setImageDrawable(drawable);
        this.setEnabled(false);
    }

    public float getRadius() {
        return (float) drawable.getIntrinsicWidth() / 2;
    }

    public ValuePair<Float> getCenter() {
        float x = this.getX() + this.getRadius();
        float y = this.getY() + this.getRadius();
        return new ValuePair<>(x, y);
    }

    public ValuePair<Float> getCoordinateFor(ValuePair<Float> center) {
        float x = center.getX() - this.getRadius();
        float y = center.getY() - this.getRadius();
        return new ValuePair<>(x,y);
    }

    public void setBallAtCoordinate(ValuePair<Float> coordinate) {
        this.setX(coordinate.getX());
        this.setY(coordinate.getY());
    }

    public ValuePair<Integer> getVelocity() {
        return velocity;
    }

    public void setVelocity(ValuePair<Integer> velocity) {
        this.velocity = velocity;
    }

    public int getDrawHeight() {
        return drawHeight;
    }

    public void setDrawHeight(int drawHeight) {
        this.drawHeight = drawHeight;
    }

    public int getDrawWidth() {
        return drawWidth;
    }

    public void setDrawWidth(int drawWidth) {
        this.drawWidth = drawWidth;
    }

    public void hitWall() {
        this.setEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Ball.this.setEnabled(false);
            }
        }, 50);
    }
}
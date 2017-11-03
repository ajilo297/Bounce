package ajil.com.bounce.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;

import ajil.com.bounce.R;

/**
 * Created by ajilo on 28-10-2017.
 */

public class Paddle extends AppCompatImageView{

    private Drawable drawable;
    private float mX;

    private Handler paddlieHandler;
    private float fieldWidth;
    private float fieldOrigin;
    private float fieldHeight;

    private boolean isPaddleStopped = false;

    public Paddle(Context context) {
        super(context);
        if (this.drawable == null) {
            drawable = ContextCompat.getDrawable(context, R.drawable.paddle);
            this.setImageDrawable(drawable);
            this.setScaleType(ScaleType.FIT_XY);
            this.setEnabled(false);
        }
        paddlieHandler = new Handler();
//        paddlieHandler.post(paddleControl);
    }

    private Runnable paddleControl = new Runnable() {
        @Override
        public void run() {
            movePaddle();
            paddlieHandler.post(paddleControl);
        }
    };

    private void movePaddle() {
        if (Math.abs(getCenterX() - mX) <= 25) {
            paddlieHandler.removeCallbacks(paddleControl);
            return;
        }
        if (mX < this.getCenterX()) {
            setCenterX(getCenterX() - 20);
        } else {
            setCenterX(getCenterX() + 20);
        }
    }

    public float getLeftCornerX() {
        return fieldOrigin + getCenterX() - (getDrawWidth() / 2);
    }

    public float getRightCornerX() {
        return fieldOrigin + getCenterX() + (getDrawWidth() / 2);
    }

    public float getCornerY() {
        return fieldHeight - getDrawHeight();
    }

    public float getCenterX() {
        return this.getX() + (this.getLayoutParams().width / 2);
    }

    public float getCenterY() {
        return this.getY() + (getDrawHeight() / 2);
    }

    public void setCenterX(float x) {
        this.setX(x - (getDrawWidth() / 2));
    }

    public void setCenterY(float y) {
        this.setY(y - (getDrawHeight() / 2));
    }

    public int getDrawWidth() {
        return this.getLayoutParams().width;
    }

    public int getDrawHeight() {
        return this.getLayoutParams().height;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        if (mX + (this.getLayoutParams().width / 2) >= fieldWidth) {
            this.mX = fieldWidth - (this.getLayoutParams().width / 2);
        } else if (mX - (this.getLayoutParams().width / 2) <= fieldOrigin) {
            this.mX = fieldOrigin + (this.getLayoutParams().width / 2);
        } else
            this.mX = mX;
        if (!isPaddleStopped)
            paddlieHandler.post(paddleControl);
    }

    public void stopPaddle() {
        paddlieHandler.removeCallbacks(paddleControl);
        isPaddleStopped = true;
    }

    public void setFieldWidth(float fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public void setFieldOrigin(float fieldOrigin) {
        this.fieldOrigin = fieldOrigin;
    }

    public void setFieldHeight(float fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public void lightUp() {
        this.setEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Paddle.this.setEnabled(false);
            }
        }, 100);
    }
}

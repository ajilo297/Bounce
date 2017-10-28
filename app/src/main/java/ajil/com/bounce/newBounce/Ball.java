package ajil.com.bounce.newBounce;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import ajil.com.bounce.R;
import ajil.com.bounce.StartActivity;

/**
 * Created by ajil on 27/10/17.
 */

public class Ball extends AppCompatImageView {

    private Drawable drawable;
    private int mass = 10;
    private float uX, uY;
    public Handler movementHandler;
    private Boolean gravityMode = false;
    private static final float t = 1f;
    private static final String TAG = "BALL";
    private OnBallMovedListener listener;
    private float time = 0.1f;
    private static final float g = 1f;
    private static final int MAXIMUM_SPEED = 15;
    private static final int MINIMUM_SPEED = 10;
    private boolean isStopped = false;

    public Ball(Context context) {
        super(context);
        if (this.drawable == null) {
            drawable = ContextCompat.getDrawable(context, R.drawable.ball);
        }
        setImageDrawable(drawable);
        this.setEnabled(false);
        movementHandler = new Handler();
        listener = (OnBallMovedListener) context;
    }

    public Ball(Context context, AttributeSet attrs, int defStyleAttr, Drawable drawable) {
        super(context, attrs, defStyleAttr);
        this.drawable = drawable;
        setImageDrawable(drawable);
        movementHandler = new Handler();
        listener = (OnBallMovedListener) context;
    }

    public Ball(Context context, AttributeSet attrs, int defStyleAttr, Drawable drawable, int mass) {
        super(context, attrs, defStyleAttr);
        this.mass = mass;
        this.drawable = drawable;
        setImageDrawable(drawable);
        movementHandler = new Handler();
        listener = (OnBallMovedListener) context;
    }

    public boolean isGravityOn() {
        return gravityMode;
    }

    public void startMove(int uX, int uY) {
        this.uX = uX;
        this.uY = uY;
        movementHandler.post(moveControl);
    }

    public void startMove() {
        this.uX = getRandomSpeed();
        this.uY = getRandomSpeed();
        movementHandler.post(moveControl);
        isStopped = false;
    }

    public void setGravityMode(boolean gravityMode) {
        this.gravityMode = gravityMode;
    }

    private Runnable moveControl = new Runnable() {
        @Override
        public void run() {
            move();
            if (!isStopped)
                movementHandler.post(moveControl);
        }
    };

    public int getDrawWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public int getDrawHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    private void move() {
        HashMap<String, Float> data = new HashMap<>();
        ArrayList<Obstructor> obstructorList = listener.onMoved(getNextX(), getNextY());
        for (Obstructor obstructor : obstructorList) {
            if (obstructor.getObject() != this) {
                if (obstructor.getType() == StartActivity.TYPE_FIELD) {
                    data =  handleFieldObstruction((FrameLayout) obstructor.getObject());
                } else if (obstructor.getType() == StartActivity.TYPE_BALL) {
                    data = handleBallObstruction((Ball) obstructor.getObject());
                } else if (obstructor.getType() == StartActivity.TYPE_PADDLE) {
                    data = handlePaddleObstruction((Paddle) obstructor.getObject());
                }
            }
        }

        setCenter(data.get("x"),data.get("y"));
    }

    private HashMap<String, Float> handlePaddleObstruction(Paddle paddle) {
        HashMap<String, Float> data = new HashMap<>();
        if (getCenterY() + getRadius() >= paddle.getCornerY()) {
            listener.disqualify(this,paddle);
        }
        if (getNextY() + getRadius() >= paddle.getY() && uY > 0) {
            uY *= -1;
            if (getNextX() <= paddle.getLeftCornerX() || getNextX() >= paddle.getRightCornerX()) {
                uY *= -1;
            } else {
                this.lightUp();
                paddle.lightUp();
//            double pow1;
//            if (uX < 0) {
//                pow1 = getNextX() - paddle.getLeftCornerX();
//            } else {
//                pow1 = getNextX() - paddle.getRightCornerX();
//            }
//            double pow2 = getNextY() - paddle.getCornerY();
//
//            double distance = Math.sqrt(Math.pow(pow1, 2) + Math.pow(pow2, 2));
//            if (distance <= getRadius()) {
//                uY *= -1;
//            }
            }
        }

        data.put("x", getNextX());
        data.put("y", getNextY());

        return data;
    }

    private HashMap<String, Float> handleBallObstruction(Ball ball) {
        HashMap<String, Float> data = new HashMap<>();

        double pow1 = getNextX() - ball.getCenterX();
        double pow2 = getNextY() - ball.getCenterY();

        double distance = Math.sqrt(Math.pow(pow1, 2) + Math.pow(pow2,2));
        if (distance <= (this.getRadius() + ball.getRadius())) {
            float vx, vy, bvx, bvy;
            vx = this.uX;
            vy = this.uY;
            bvx = ball.uX;
            bvy = ball.uY;
            uX = bvx;
            uY = bvy;
            ball.uX = vx;
            ball.uY = vy;
        }
        data.put("x", getNextX());
        data.put("y", getNextY());

        return data;
    }

    public float getNextX() {
        if (isGravityOn())
            if (Math.abs(uX) <=2 )
                uX = 0;
            if (uY == 0)
                uX = uX/1.111f;
        float deltaX = this.uX * t;
        return getCenterX() + deltaX;
    }

    public float getNextY() {
        if (isGravityOn()) {
            this.uY = uY + (g * t * 3);
            if (Math.abs(uY) < 2)
                uY = 0;
        }
        float deltaY = this.uY * t;
        return getCenterY() + deltaY;
    }

    public void lightUp() {
        this.setEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Ball.this.setEnabled(false);
            }
        }, 100);
    }

    private HashMap<String, Float> handleFieldObstruction(FrameLayout field) {
        HashMap<String, Float> data = new HashMap<>();
        if ((getNextX() - getRadius() <= field.getX() && uX < 0) || (getNextX() + getRadius() >= field.getWidth() && uX > 0)) {
            uX *= -1;
            this.lightUp();
            if (isGravityOn())
                uX = uX * 1/3;
        }
        if (getNextY() - getRadius() <= field.getY() && uY < 0) {
            uY *= -1;
            this.lightUp();
        } else if (getNextY() + getRadius() >= field.getHeight() && uY > 0) {
//            uY *= -1;
            if (isGravityOn())
                uY = uY * -1/5;
        }
        data.put("x", getNextX());
        data.put("y", getNextY());

        return data;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public void setSpeed(int vX, int vY) {
        this.uX = vX;
        this.uY = vY;
    }

    public float getRadius() {
        return this.drawable.getIntrinsicHeight() / 2;
    }

    public void setCenter(float x, float y) {
        setX(x - getRadius());
        setY(y - getRadius());
    }

    public float getCenterX () {
        return getX() + (getDrawWidth() / 2);
    }

    public float getCenterY() {
        return getY() + (getDrawHeight() / 2);
    }

    public int getRandomSpeed() {
        Random random = new Random();
        int speed = random.nextInt(MAXIMUM_SPEED - MINIMUM_SPEED) + MINIMUM_SPEED;
        int direction = random.nextInt(2);
        if (direction == 0) {
            return speed;
        }
        return speed * -1;
    }

    public void stopMove() {
        movementHandler.removeCallbacks(moveControl);
        invalidate();
        isStopped = true;
    }
}

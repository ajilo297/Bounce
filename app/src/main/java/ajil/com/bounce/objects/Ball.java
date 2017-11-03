package ajil.com.bounce.objects;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ajil.com.bounce.R;
import ajil.com.bounce.StartActivity;
import ajil.com.bounce.helpers.Obstructor;
import ajil.com.bounce.helpers.OnBallMovedListener;
import ajil.com.bounce.helpers.OnBlockHitListener;

/**
 * Created by ajil on 27/10/17.
 */

public class Ball extends AppCompatImageView {

    private static final int BLOCK_COLLISION_NONE = 0;
    private static final int BLOCK_COLLISION_LEFT = 1;
    private static final int BLOCK_COLLISION_TOP = 2;
    private static final int BLOCK_COLLISION_RIGHT = 3;
    private static final int BLOCK_COLLISION_BOTTOM = 4;
    private static final int BLOCK_COLLISION_CORNER = 5;
    private static final int MINIMUM_SPEED = 5;
    private static final int MAXIMUM_SPEED = 8;
    private static final String TAG = "BALL";
    private static final float t = 1f;
    private static final float g = 1f;
    private Drawable drawable;
    private int mass = 10;
    private float uX, uY;
    public Handler movementHandler;
    private Boolean gravityMode = false;
    private OnBallMovedListener listener;
    private OnBlockHitListener hitListener;
    private float time = 0.1f;
    private boolean isStopped = false;

    public Ball(Context context) {
        super(context);
        if (this.drawable == null) {
            drawable = ContextCompat.getDrawable(context, R.drawable.ball);
        }
        setImageDrawable(drawable);
        this.setEnabled(false);
        movementHandler = new Handler();
        hitListener = (OnBlockHitListener) context;
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
        ArrayList<Obstructor> obstructorList = listener.onMoved(this, getNextX(), getNextY());
        for (Obstructor obstructor : obstructorList) {
            if (obstructor.getObject() != this) {
                if (obstructor.getType() == StartActivity.TYPE_FIELD) {
                    data =  handleFieldObstruction((FrameLayout) obstructor.getObject());
                } else if (obstructor.getType() == StartActivity.TYPE_BALL) {
                    data = handleBallObstruction((Ball) obstructor.getObject());
                } else if (obstructor.getType() == StartActivity.TYPE_PADDLE) {

                    boolean willCollide = false;
                    Paddle paddle = (Paddle) obstructor.getObject();

                    Rect rect = new Rect((int) paddle.getX(), (int) paddle.getY(),
                            ((int) (paddle.getX() + paddle.getWidth())),
                            ((int) (paddle.getY() + paddle.getHeight())));
                    Rect bRect = new Rect((int) this.getX(), (int) this.getY(),
                            (int) this.getX() + this.getDrawWidth(),
                            (int) this.getY() + this.getDrawHeight());

                    if (rect.intersect(bRect)) {
                        if (rect.width() > rect.height()) {
                            uY *= -1;
                        } else if (rect.width() < rect.height()) {
                            uX *= -1;
                        } else {
                            uY *= -1;
                            uX *= -1;
                        }
                        willCollide = true;
                        paddle.lightUp();
                    }

                    data.put("x", getNextX());
                    data.put("y", getNextY());
                    if (willCollide)
                        break;
                    else if (getCenterY() + getRadius() >= paddle.getCornerY()) {
                        listener.disqualify(this,paddle);
                    }

                } else if (obstructor.getType() == StartActivity.TYPE_BLOCK) {

                    boolean willCollide = false;
                    Block block = (Block) obstructor.getObject();
                    Rect rect = new Rect((int) block.getX(), (int) block.getY(),
                            (int) block.getX() + block.getDrawWidth(),
                            (int) block.getY() + block.getDrawHeight());
                    Rect bRect = new Rect((int) this.getX(), (int) this.getY(),
                            (int) this.getX() + this.getDrawWidth(),
                            (int) this.getY() + this.getDrawHeight());

                    if (rect.intersect(bRect)) {
                        if (rect.width() > rect.height()) {
                            uY *= -1;
                        } else if (rect.width() < rect.height()) {
                            uX *= -1;
                        } else {
                            uY *= -1;
                            uX *= -1;
                        }
                        hitListener.onHit(block);
                        willCollide = true;
                    }

                    data.put("x", getNextX());
                    data.put("y", getNextY());
                    if (willCollide)
                        break;
                }
            }
        }

        setCenter(data.get("x"),data.get("y"));
    }

//    private HashMap<String, Float> handlePaddleObstruction(Paddle paddle_normal) {
//        HashMap<String, Float> data = new HashMap<>();
//        if (getCenterY() + getRadius() >= paddle_normal.getCornerY()) {
//            listener.disqualify(this,paddle_normal);
//        }
//        if (getNextY() + getRadius() >= paddle_normal.getY() && uY > 0) {
//            uY *= -1;
//            if (getNextX() <= paddle_normal.getLeftCornerX() || getNextX() >= paddle_normal.getRightCornerX()) {
//                uY *= -1;
//            } else {
//                this.lightUp();
//                paddle_normal.lightUp();
//            }
//        }
//
//        data.put("x", getNextX());
//        data.put("y", getNextY());
//
//        return data;
//    }

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

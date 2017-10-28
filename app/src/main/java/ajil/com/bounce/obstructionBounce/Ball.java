package ajil.com.bounce.obstructionBounce;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import ajil.com.bounce.StartActivity;
import ajil.com.bounce.first.MainActivity;

/**
 * Created by ajil on 26/10/17.
 */

public class Ball extends AppCompatImageView {

    private Context context;
    private float ux;
    private float uy;
    private Drawable drawable;
    private Handler movementHandler;
    public static final int t = 20;
    private OnBallReadyForMoveListener listener;
    private static final String TAG = "BALL";
    private boolean gravityMode;
    private float a = 1f;
    private float time = 0;
    private int counter = 0;
    public int object_type = StartActivity.TYPE_BALL;

    public Ball(Context context) {
        super(context);
        this.context = context;
        listener = (OnBallReadyForMoveListener) context;
    }

    private Runnable move = new Runnable() {
        @Override
        public void run() {
            moveBall();
            movementHandler.postDelayed(move, t);
        }
    };

    public Ball(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        listener = (OnBallReadyForMoveListener) context;
    }

    public Ball(Context context, Drawable drawable) {
        super(context);
        this.setImageDrawable(drawable);
        this.drawable = drawable;
        movementHandler = new Handler();
        listener = (OnBallReadyForMoveListener) context;
    }

    public int getDrawWidth() {
        return drawable.getIntrinsicWidth();
    }

    public int getDrawHeight() {
        return drawable.getIntrinsicHeight();
    }

    public float getRadius() {
        return (this.getDrawHeight() / 2);
    }

    public void setCenter(float x, float y) {
        float centerX = x - this.getRadius();
        float centerY = y - this.getRadius();
        this.setX(centerX);
        this.setY(centerY);
    }

    public float getUx() {
        return ux;
    }

    public void setUx(float ux) {
        this.ux = ux;
    }

    public float getUy() {
        return uy;
    }

    public void setUy(float uy) {
        this.uy = uy;
    }

    public float getCenterX () {
        return this.getX() + this.getRadius();
    }

    public float getCenterY () {
        return this.getY() + this.getRadius();
    }


    public void startMove(int vx, int vy) {
        this.ux = vx;
        this.uy = vy;
        movementHandler.postDelayed(move,t);
    }

    public void stopMove() {
        movementHandler.removeCallbacks(move);
        this.ux = 0;
        this.uy = 0;
//        this.time = 0;
    }

    private void moveBall() {


        Obstructor obstructor = listener.checkForObstruction(this);
        if (obstructor != null && !handleObstruction(obstructor)) {
            return;
        }
        this.setCenter(getNextX(), getNextY());
        if (isGravityOn()) {
            gravityAcceleration();
//            accelerateWithGravity();
        }
    }

    private Boolean handleObstruction(Obstructor obstructor) {
        if (obstructor.getType() == StartActivity.TYPE_FIELD) {
            if (obstructor.getObstruction() == StartActivity.TYPE_VERTICAL) {
                setUx(getUx() * -1);
            }
            if (obstructor.getObstruction() == StartActivity.TYPE_HORIZONTAL) {
                setUy(getUy() * -1);
            }
            return true;
        }
        if (obstructor.getType() == StartActivity.TYPE_BALL) {

            Ball object = (Ball) obstructor.getObject();

            float ballNextX = getNextX();
            float ballNextY = getNextY();

            float objectNextX = object.getNextX();
            float objectNextY = object.getNextY();

            float distance = (float) Math.sqrt(Math.pow(ballNextX - objectNextX, 2) + Math.pow(ballNextY - objectNextY, 2));
            if (distance <= this.getRadius() + object.getRadius()) {
                handleBallContact(object);
            }
            return true;
        }
        return false;
    }

    private void handleBallContact(Ball object) {
        float buX = this.getUx();
        float buY = this.getUy();

        float ouX = object.getUx();
        float ouY = object.getUy();

        object.setUx(buX);
        object.setUy(buY);

        this.setUy(ouX);
        this.setUy(ouY);
    }

    public void lightUp (long time) {
        this.setEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Ball.this.setEnabled(false);
            }
        }, time);
    }

    public void gravityAcceleration() {
        time += 0.5;
        float vy = uy + (a * time);

        if (uy < 0 && vy > 0) {
            time = 0.3f;
        } else if (vy < 0 && uy > 0) {
            time = 0.3f;
        } else if (uy == 0) {
            time = 0.3f;
        }
        this.uy =  vy;
    }

    public void accelerateWithGravity () {
        this.setUy(this.getUy() + (0.001f * t));
//        this.uy = vy;
    }

    public float getNextX () {
        float deltaX = ux;
        return this.getCenterX() + deltaX;
    }

    public float getNextY () {
        float deltaY = uy;
        return this.getCenterY() + deltaY;
    }

    public void setGravityMode (boolean gravityMode) {
        this.gravityMode = gravityMode;
    }

    public boolean isGravityOn () {
        return gravityMode;
    }
}

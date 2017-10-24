package ajil.com.bounce;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Ball> ballArrayList = new ArrayList<>();
    private static final String TAG = "MAIN_ACTIVITY";
    private Handler handler = new Handler();
    private int ballCount = 0;
    private FrameLayout field;
    private static final int t = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = findViewById(R.id.field);
        final Context context = MainActivity.this;

        field.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                field.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ballArrayList.add(addBall(context, field));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ballArrayList.add(addBall(context, field));
                    }
                }, 1000);
                startMove.run();
            }
        });
    }

    Runnable startMove = new Runnable() {
        @Override
        public void run() {
            for (Ball ball : ballArrayList) {
                move(ball);
            }
            handler.post(startMove);
        }
    };

    private Ball addBall(Context context, FrameLayout field) {
        ballCount += 1;
        Drawable ballDrawable = ContextCompat.getDrawable(context, R.drawable.ball);
        Ball ball = new Ball(context, ballDrawable, ballCount);
        field.addView(ball, new FrameLayout.LayoutParams(ball.getDrawWidth(),ball.getDrawHeight()));
        return ball;
    }

    private void move(Ball b1) {
        for (Ball b2 : ballArrayList) {
            if (b1.getBallId() != b2.getBallId()) {
                checkCollision(b1, b2);
            }
        }
        if (checkCollision(b1, field)) {
            collide(b1);
        }
    }

    private void collide(Ball b1) {

    }

    private void collide(Ball b1, Ball b2) {
        CoordinatePair<Float> vel1 = b1.getVelocity();
        CoordinatePair<Float> vel2 = b2.getVelocity();
    }

    private boolean checkCollision(Ball o1, Ball o2) {
        boolean willCollide = false;

        if (distanceBetweenCoordinates(o1.getCenter(), o2.getCenter()) <= o1.getWidth()) {
            collide(o1,o2);
        }

        return willCollide;
    }

    private float distanceBetweenCoordinates(CoordinatePair<Float> c1, CoordinatePair<Float> c2) {
        float diffX = c2.getX() - c1.getX();
        float diffY = c2.getY() - c1.getY();
        return (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
    }

    private boolean checkCollision(Ball o1, FrameLayout o2) {
        boolean willCollide = false;
        float deltaX = o1.getVx() * t;
        float deltaY = o1.getVy() * t;

        float x = o1.getX() + deltaX;
        float y = o1.getY() + deltaY;

        if (x < o2.getWidth() - o1.getDrawWidth() && x > 0) {
            willCollide = false;
            o1.setX(x);
        } else {
            willCollide = true;
            o1.setVx(o1.getVx() * -1);
        }

        if (y < o2.getHeight() - o1.getDrawHeight() && y > 0) {
            willCollide = false;
            o1.setY(y);
        } else {
            willCollide = true;
            o1.setVy(o1.getVy() * -1);
        }
        return willCollide;
    }
}

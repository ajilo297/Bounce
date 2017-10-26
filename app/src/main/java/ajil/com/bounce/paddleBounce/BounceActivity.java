package ajil.com.bounce.paddleBounce;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.ArrayList;

import ajil.com.bounce.R;

public class BounceActivity extends AppCompatActivity implements OnViewPanListener{

    private FrameLayout field;
    private GestureView touchView;
    private ArrayList<Ball> ballArrayList = new ArrayList<>();
    private Context context;
    private Handler movementHandler;
    private static final float t = 1f;
    public static final int FIELD_NONE = 0;
    public static final int FIELD_HORIZONTAL = 1;
    public static final int FIELD_VERTICAL = 2;
    private static final int PADDLE_TOP = 3;
    private Paddle paddle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounce);

        context = BounceActivity.this;
        movementHandler = new Handler();

        field = findViewById(R.id.field);
        touchView = findViewById(R.id.touch);

        field.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                field.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                movementHandler.post(moveRunnable);
                setupPaddle();
                setUpBall();
            }
        });
    }

    private Runnable moveRunnable = new Runnable() {
        @Override
        public void run() {
            for (Ball ball : ballArrayList) {
                move(ball);
                movementHandler.post(moveRunnable);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        movementHandler.removeCallbacks(moveRunnable);
        field.removeAllViews();
        ballArrayList = new ArrayList<>();
    }

    private void setUpBall() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ball);
        Ball ball = new Ball(context, drawable);

        ValuePair<Float> fieldCenter = new ValuePair<>((float) field.getWidth() / 2, (float) field.getHeight() / 2);
        ValuePair<Float> coordinate = ball.getCoordinateFor(fieldCenter);
        ball.setBallAtCoordinate(coordinate);

        ValuePair<Integer> velocity = new ValuePair<>(0, 0);
        ball.setVelocity(velocity);

        field.addView(ball,new FrameLayout.LayoutParams(ball.getDrawWidth(),ball.getDrawHeight()));
        ball.setVelocity(new ValuePair<>(10, 10));

        startMotion(ball);
    }

    private void setupPaddle() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.paddle);
        paddle = new Paddle(context, drawable);

        float width = field.getWidth() / 3;
        float height = width / 10;
        field.addView(paddle, new FrameLayout.LayoutParams((int) width, (int) height));

        float paddleX = paddle.getX();
        float paddleY = paddle.getY();

        float posX = (field.getWidth() / 2) - (width / 2);
        float posY = (field.getHeight() - height);

        paddle.setX(posX);
        paddle.setY(posY);
    }

    private void startMotion(Ball ball) {
        ballArrayList.add(ball);
    }

    private void setupBlocks() {

    }

    private void move(Ball ball) {
        float initialX = ball.getX();
        float initialY = ball.getY();

        float deltaX = ball.getVelocity().getX() * t;
        float deltaY = ball.getVelocity().getY() * t;

        float finalX = initialX + deltaX;
        float finalY = initialY + deltaY;

        int type = FIELD_NONE;
        if (finalX<= field.getX() || (finalX + ball.getDrawWidth()) >= field.getWidth()) {
            type = FIELD_VERTICAL;
        }
        if (finalY<= field.getY() + 10 /* || (finalY + ball.getDrawHeight()) >= field.getHeight()*/) {
            type = FIELD_HORIZONTAL;
        }
        if (finalY + ball.getDrawHeight() >= paddle.getY() - 10 &&
                finalX + ball.getRadius() > paddle.getX() &&
                finalX + ball.getRadius() < paddle.getX() + paddle.getWidth()) {
            type = PADDLE_TOP;
        }
        collide(ball,type);
        ball.setX(finalX);
        ball.setY(finalY);

        if (ball.getY() + ball.getDrawHeight() > paddle.getY()) {
            disqualify();
        }

//        if (ball.getY() > field.getHeight() + 100 || ball.getX() > field.getWidth() + 100) {
//            disqualify();
//        }

    }

    private void collide(Ball ball, int type) {
        ValuePair<Integer> velocity = ball.getVelocity();
        int ux = velocity.getX();
        int uy = velocity.getY();
        if (type == FIELD_HORIZONTAL) {
            int vy = uy * -1;
            ball.setVelocity(new ValuePair<>(ux,vy));
            ball.hitWall();
        } else if (type == FIELD_VERTICAL) {
            int vx = ux * -1;
            ball.setVelocity(new ValuePair<>(vx,uy));
            ball.hitWall();
        } else if (type == PADDLE_TOP) {
            int vy = uy * -1;
            ball.setVelocity(new ValuePair<>(ux, vy));
//            ball.hitWall();
        }
    }

    @Override
    public void onViewPanned(float x) {
        paddle.setX(x - (paddle.getWidth()/2));
        if (paddle.getX() < field.getX()) {
            paddle.setX(field.getX());
        } else if (paddle.getX() + paddle.getWidth() > field.getWidth()) {
            paddle.setX(field.getWidth()-paddle.getWidth());
        }
    }

    private void reset() {
        ballArrayList = new ArrayList<>();
        movementHandler.post(moveRunnable);
        setupPaddle();
        setUpBall();
    }

    private void disqualify () {
        movementHandler.removeCallbacks(moveRunnable);
        for (Ball ball : ballArrayList) {
            field.removeView(ball);
        }
        ballArrayList = new ArrayList<>();
        touchView.setOnTouchListener(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();

            }
        }, 2000);
    }
}

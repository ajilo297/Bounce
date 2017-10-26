package ajil.com.bounce.obstructionBounce;

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
import ajil.com.bounce.StartActivity;

public class PaddleBounceActivity extends AppCompatActivity implements OnBallReadyForMoveListener{

    private static final String TAG = "PADDLE_BOUNCE";
    private GameField field;
    private Context context;
    private ArrayList<Ball> ballArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paddle_bounce);

        context = PaddleBounceActivity.this;
        field = findViewById(R.id.field);
        field.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                field.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                field.setMinX(field.getX());
                field.setMinY(field.getY());
                field.setMaxX(field.getWidth());
                field.setMaxY(field.getHeight());

                addBall();
            }
        });
    }

    private void addBall() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ball);
        final Ball ball = new Ball(context, drawable);
        field.addView(ball, new FrameLayout.LayoutParams(ball.getDrawWidth(), ball.getDrawHeight()));

        int x = field.getWidth() / 2;
        int y = (int) (field.getMinY() + ball.getRadius());

        ball.setCenter(x, y);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ball.startMove(10,10);
                ball.setGravityMode(false);
                ball.setEnabled(false);
                addBall2();
            }
        }, 500);
        ballArrayList.add(ball);
    }

    private void addBall2() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ball);
        final Ball ball = new Ball(context, drawable);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                field.addView(ball, new FrameLayout.LayoutParams(ball.getDrawWidth(), ball.getDrawHeight()));

                int x = field.getWidth() / 2;
                int y = (int) (field.getMinY() + ball.getRadius());

                ball.setCenter(x, y);
                ball.startMove(-10,10);
                ball.setGravityMode(false);
                ball.setEnabled(true);
            }
        }, 1000);
        ballArrayList.add(ball);
    }

    @Override
    public Obstructor checkForObstruction(Ball ball) {
        Obstructor obstructor = new Obstructor(StartActivity.TYPE_FIELD, field);
        if (ball.getNextX()+ball.getRadius() >= field.getMaxX()) {
            obstructor.setObstruction(StartActivity.TYPE_VERTICAL);
            return obstructor;
        }
        if (ball.getNextX() - ball.getRadius() <= field.getMinX()) {
            obstructor.setObstruction(StartActivity.TYPE_VERTICAL);
            return obstructor;
        }
        if (ball.getNextY() + ball.getRadius() >= field.getMaxY()) {
            obstructor.setObstruction(StartActivity.TYPE_HORIZONTAL);
            return obstructor;
        }
        if (ball.getNextY() - ball.getRadius() <= field.getMinY()) {
            obstructor.setObstruction(StartActivity.TYPE_HORIZONTAL);
            return obstructor;
        }

        for (Ball object : ballArrayList) {
            if (ball != object) {
                obstructor = new Obstructor(StartActivity.TYPE_BALL, object);
                return obstructor;
            }
        }

        return null;
    }
}

package ajil.com.bounce.newBounce;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Random;

import ajil.com.bounce.R;
import ajil.com.bounce.StartActivity;

public class NewBounceActivity extends AppCompatActivity implements OnBallMovedListener, OnPanListener{

    private GameField field;
    private Context context;
    private static final String TAG = "NEWBOUNCE";
    private ArrayList<Obstructor> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bounce);

        context = NewBounceActivity.this;
        field = findViewById(R.id.field);
        field.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                field.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                arrayList.add(new Obstructor(StartActivity.TYPE_FIELD, field));
                addPaddle();
                addBall();
            }
        });
    }

    private void addPaddle() {
        Paddle paddle = new Paddle(context);
        float width = field.getWidth() / 3;
        float height = width / 10;
        field.addView(paddle, new FrameLayout.LayoutParams((int) width, (int) height));
        paddle.setCenterX(field.getWidth() / 2);
        paddle.setFieldOrigin(field.getX());
        paddle.setFieldWidth(field.getWidth());
        paddle.setFieldHeight(field.getHeight());
        paddle.setCenterY(field.getHeight() - (height / 2));
        arrayList.add(new Obstructor(StartActivity.TYPE_PADDLE, paddle));
    }

    private void addBall() {
        final Ball ball = new Ball(context);
        field.addView(ball,new FrameLayout.LayoutParams(ball.getDrawWidth(),ball.getDrawHeight()));
        int min = (int) ball.getRadius();
        Log.e(TAG, "Min" + min);
//        ball.setCenter(getRandomCoordinate(field.getWidth() - 40, min + 40), getRandomCoordinate(field.getHeight() - 40, min + 40));
        ball.setCenter(field.getWidth()/2,field.getHeight()/4);
        arrayList.add(new Obstructor(StartActivity.TYPE_BALL, ball));
        ball.setGravityMode(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ball.startMove();
            }
        }, 1000);
//        ball.startMove();
    }

    private int getRandomCoordinate(int max, int min) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    @Override
    public ArrayList<Obstructor> onMoved(float centerX, float centerY) {
        return arrayList;
    }

    @Override
    public void disqualify(Ball ball, Paddle paddle) {
        ball.stopMove();
        paddle.stopPaddle();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);

    }

    @Override
    public void updatePaddleLocation(float x) {
        for (Obstructor obstructor : arrayList) {
            if (obstructor.getType() == StartActivity.TYPE_PADDLE) {
                Paddle paddle = (Paddle) obstructor.getObject();
                paddle.setmX(x);
            }
        }
    }
}

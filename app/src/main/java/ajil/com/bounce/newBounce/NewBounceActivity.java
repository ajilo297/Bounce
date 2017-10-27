package ajil.com.bounce.newBounce;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.ArrayList;

import ajil.com.bounce.R;
import ajil.com.bounce.StartActivity;

public class NewBounceActivity extends AppCompatActivity implements OnBallMovedListener{

    private GameField field;
    private Context context;
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
                addBall();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addBall2();
                    }
                }, 3000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addBall2();
                    }
                }, 5000);
            }
        });
    }

    private void addBall() {
        final Ball ball = new Ball(context);
        field.addView(ball,new FrameLayout.LayoutParams(ball.getDrawWidth(),ball.getDrawHeight()));
        float frameCenterX = field.getWidth() / 2;
        float frameCenterY = field.getHeight() / 2;
        ball.setCenter(frameCenterX, frameCenterY);
        arrayList.add(new Obstructor(StartActivity.TYPE_BALL,ball));
        ball.setGravityMode(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ball.startMove(30, -20);
            }
        }, 1000);
    }

    private void addBall2() {
        final Ball ball = new Ball(context);
        field.addView(ball,new FrameLayout.LayoutParams(ball.getDrawWidth(),ball.getDrawHeight()));
        float frameCenterX = field.getWidth() / 2;
        float frameCenterY = field.getHeight() / 2;
        ball.setCenter(frameCenterX, frameCenterY);
        arrayList.add(new Obstructor(StartActivity.TYPE_BALL,ball));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ball.startMove(-3, 8);
            }
        }, 1000);
    }

    @Override
    public ArrayList<Obstructor> onMoved(float centerX, float centerY) {
        return arrayList;
    }
}

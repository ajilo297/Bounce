package ajil.com.bounce.newBounce;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import ajil.com.bounce.R;
import ajil.com.bounce.StartActivity;

public class NewBounceActivity extends AppCompatActivity implements OnBallMovedListener, OnPanListener, OnBlockHitListener{

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
                addBlocks();

                int count = 0;
                for (Obstructor obstructor : arrayList) {
                    if (obstructor.getType() == StartActivity.TYPE_BLOCK) {
                        count++;
                    }
                }
                Toast.makeText(context, "Count = " + count, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBlocks() {
        Block block = new Block(context);
//
//        int dim = block.getDrawWidth();
//        field.addView(block,new FrameLayout.LayoutParams(dim,dim));
//        block.setX((field.getWidth() / 2) - (dim / 2));
//        block.setY((field.getHeight() / 2) - (dim / 2));
//        arrayList.add(new Obstructor(StartActivity.TYPE_BLOCK, block));

        int vCount = (int) ((field.getHeight() / block.getDrawHeight()) / 4);
        int dim = block.getDrawWidth();
        int index = 0;
        int y = dim * 2;
        int x = dim * 2;
        while (vCount > 1) {
            Block b = new Block(context);
            b.setX(x);
            b.setY(y);
            if (b.getX() + (dim * 3) > field.getWidth()) {
                y += dim;
                x = dim * 2;
                vCount -= 1;
            } else {
                x += dim;
            }
            b.setIndex(index);
            field.addView(b, new FrameLayout.LayoutParams(dim, dim));
            arrayList.add(new Obstructor(StartActivity.TYPE_BLOCK, b));
            index += 1;
        }
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
        ball.setCenter(field.getWidth() / 2, field.getHeight() * 1.5f / 2);
        arrayList.add(new Obstructor(StartActivity.TYPE_BALL, ball));
        ball.setGravityMode(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ball.startMove(10,-20);
            }
        }, 1000);
    }

    private int getRandomCoordinate(int max, int min) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (Obstructor obstructor : arrayList) {
            if (obstructor.getType() == StartActivity.TYPE_BALL) {
                Ball ball = (Ball) obstructor.getObject();
                ball.stopMove();
            }
        }
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

    @Override
    public boolean onHit(Block block) {
        for (Obstructor obstructor : arrayList) {
            if (obstructor.getType() == StartActivity.TYPE_BLOCK) {
                Block b = (Block) obstructor.getObject();
                if (b.getIndex() == block.getIndex()) {
                    field.removeView(block);
                    return arrayList.remove(obstructor);
                }
            }
        }
        return false;
    }
}

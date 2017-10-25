package ajil.com.bounce;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnBlockHitListener {

    private ArrayList<Ball> ballArrayList = new ArrayList<>();
    private ArrayList<Block> blockArrayList = new ArrayList<>();
    private static final String TAG = "MAIN_ACTIVITY";
    private Handler handler = new Handler();
    private int ballCount = 0;
    private FrameLayout field;
    private static final int t = 1;
    private Context context;
    private int processCompleteCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = findViewById(R.id.field);
        context = MainActivity.this;

        field.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                field.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ballArrayList.add(addBall(context, field, new CoordinatePair<>((float)field.getWidth()/2,
                        (float)(field.getHeight()) - 50),new CoordinatePair<>(-25f,-25f)));
                Log.d(TAG, "All balls released");
                setBlocks();
                addMoreBalls();
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

    private Ball addBall(Context context, FrameLayout field, CoordinatePair<Float> center, CoordinatePair<Float> velocity) {
        ballCount += 1;
        Drawable ballDrawable = ContextCompat.getDrawable(context, R.drawable.ball);
        Ball ball = new Ball(context, ballDrawable, ballCount, center, velocity);
        field.addView(ball, new FrameLayout.LayoutParams(ball.getDrawWidth(),ball.getDrawHeight()));
        return ball;
    }

    private Block addBlock(int id, float x) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.block);
        Block block = new Block(context, drawable, id, (int) x, 290);
        field.addView(block,new FrameLayout.LayoutParams(block.getDrawWidth(),block.getDrawHeight()));
        return block;
    }

    private void move(Ball b1) {

        for (Ball b2 : ballArrayList) {
            if (b1.getBallId() != b2.getBallId()) {
                checkCollision(b1, b2, b1.isGravityOn);
            }
        }

        if (b1.getVx() <= 1) {
            processCompleteCounter += 1;
            if (processCompleteCounter >= 200) {
//                field.removeView(b1);
//                ballArrayList.remove(b1);
                if (ballArrayList.size() == 0) {
                    handler.removeCallbacks(startMove);
                    ballArrayList = new ArrayList<>();
                    blockArrayList = new ArrayList<>();
                    handler = new Handler();
                    Toast.makeText(context, "Process completed", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            processCompleteCounter = 0;
        }
        for (Block block : blockArrayList) {
            checkCollision(b1,block);
        }
        if (b1.isGravityOn) {
            b1.setVy(b1.getVy() + (0.981f * t));
            if (b1.getCenter().getY() + b1.getRadius() >= field.getHeight() - b1.getRadius()) {
                b1.setVx(b1.getVx() * (0.9f));
            }
        } else {
            checkCollision(b1, field);
            return;
        }

        float deltaX = b1.getVx() * t;
        float deltaY = b1.getVy() * t;

        float x = b1.getX() + deltaX;
        float y = b1.getY() + deltaY;

        if (x < field.getWidth() - b1.getDrawWidth() && x > 0) {
            b1.setX(x);
        } else {
            b1.setVx(b1.getVx() * (-0.99f));
        }

        if (y < field.getHeight() - b1.getDrawHeight() && y > 0) {
            b1.setY(y);
        } else {
            b1.setVy(b1.getVy() * (-0.8f));
        }

    }

    private boolean checkCollision(Ball o1, Ball o2, boolean isGravityOn) {
        if (distanceBetweenCoordinates(o1.getCenter(), o2.getCenter()) <= o1.getWidth()) {
            collide(o1, o2, isGravityOn);
            return true;
        }

        return false;
    }

    private float distanceBetweenCoordinates(CoordinatePair<Float> c1, CoordinatePair<Float> c2) {
        float diffX = c2.getX() - c1.getX();
        float diffY = c2.getY() - c1.getY();
        return (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
    }

    private boolean checkCollision(Ball o1, FrameLayout o2) {
        boolean willCollide;
        float deltaX = o1.getVx() * t;
        float deltaY = o1.getVy() * t;

        float x = o1.getX() + deltaX;
        float y = o1.getY() + deltaY;

        if (x < o2.getWidth() - o1.getDrawWidth() && x > 0) {
            o1.setX(x);
        } else {
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

    private void collide(Ball b1, Ball b2, boolean isGravityOn) {
        CoordinatePair<Float> u1 = b1.getVelocity();
        CoordinatePair<Float> u2 = b2.getVelocity();

//        int deflectionSide = b1.getDeflectionSide(b2.getCenter());
//        if (deflectionSide == Block.DELECTION_TOP || deflectionSide == Block.DELECTION_BOTTOM) {
//
//        } else if (deflectionSide == Block.DELECTION_LEFT || deflectionSide == Block.DELECTION_RIGHT) {
//
//        }
//

        if (isGravityOn) {
            CoordinatePair<Float> vg1 = new CoordinatePair<>(u1.getX() * 0.9f, u1.getY() * 0.9f);
            CoordinatePair<Float> vg2 = new CoordinatePair<>(u2.getX() * 0.9f, u2.getY() * 0.9f);
            b1.setVelocity(vg2);
            b2.setVelocity(vg1);
            return;
        }
        b1.setVelocity(u2);
        b2.setVelocity(u1);
    }

    private void checkCollision(Ball ball, Block block) {
//        CoordinatePair<Float> velocity = ball.getVelocity();
        CoordinatePair<Float> center = ball.getCenter();

        if (distanceBetweenCoordinates(center, block.getCenter()) <= (ball.getRadius() + block.getRadius())) {
            int deflectionSide = block.getDeflectionSide(ball.getCenter());
            if (deflectionSide == Block.DELECTION_TOP || deflectionSide == Block.DELECTION_BOTTOM) {
                ball.setVy(ball.getVy() * -1);
            } else if (deflectionSide == Block.DELECTION_LEFT || deflectionSide == Block.DELECTION_RIGHT) {
                ball.setVx(ball.getVx() * -1);
            }
            block.hit();
        }
    }

    private void setBlocks() {
        Block block = new Block(context);
        int blockWidth = block.getDrawWidth();
        int fieldWidth = field.getWidth();
        int blockCount = (fieldWidth / blockWidth) - 4;
        getBlockArrayList(blockCount);
    }

    private ArrayList<Block> getBlockArrayList(int count) {
        ArrayList<Block> list;
        if (isEven(count)) {
            list = placeBlock(count);
        } else {
            list = placeBlock(count - 1);
        }
        return list;
    }

    private ArrayList<Block> placeBlock(int count) {
        int i = 0;
        Block block = new Block(context);
        int blockWidth = block.getDrawWidth();
        float x = ((float)field.getWidth() / 2) - ((float) blockWidth * count / 2);
        while (i < count) {
            blockArrayList.add(addBlock(i,x));
            x += blockWidth;
            i += 1;
        }
        return blockArrayList;
    }

    private boolean isEven (int arg){
        return arg % 2 == 0;
    }

    private void addMoreBalls() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ballArrayList.add(addBall(context, field, new CoordinatePair<>(150f, 110f),new CoordinatePair<>(10f,10f)));
            }
        }, 1000);
    }

    @Override
    public void blockIsHit(Block block) {
        field.removeView(block);
        blockArrayList.remove(block);
        if (blockArrayList.size() == 0) {
            for (Ball ball : ballArrayList) {
                ball.addGravity();
            }
        }
    }
}

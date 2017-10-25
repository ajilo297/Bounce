package ajil.com.bounce.first;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import ajil.com.bounce.R;

/**
 * Created by ajil on 25/10/17.
 */

public class Block extends android.support.v7.widget.AppCompatImageView {

    private Context context;
    private Drawable drawable;
    private int blockId;
    public static final int DELECTION_LEFT = 1;
    public static final int DELECTION_TOP = 2;
    public static final int DELECTION_RIGHT = 3;
    public static final int DELECTION_BOTTOM = 4;
    private OnBlockHitListener hitListener;


    public Block(Context context) {
        super(context);
        this.context = context;
    }

    public Block(Context context, Drawable drawable) {
        super(context);
        this.context = context;
        this.drawable = drawable;
    }

    public Block(Context context, Drawable drawable, int blockId, int x, int y) {
        super(context);
        this.context = context;
        this.drawable = drawable;
        this.blockId = blockId;
        this.setImageDrawable(drawable);
        this.setX(x);
        this.setY(y);
        this.hitListener = (OnBlockHitListener) context;
    }

    public int getDrawWidth() {
        if (this.drawable == null)
            return getDefaultWidth();
        return this.drawable.getIntrinsicWidth();
    }

    public int getDrawHeight() {
        if (this.drawable == null)
            return getDefaultHeight();
        return this.drawable.getIntrinsicHeight();
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    private int getDefaultWidth() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.block);
        return drawable.getIntrinsicWidth();
    }

    private int getDefaultHeight() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.block);
        return drawable.getIntrinsicHeight();
    }

    public int getDeflectionSide(CoordinatePair<Float> coordinatePair) {
        CoordinatePair<Float> c1 = new CoordinatePair<>(this.getX(), this.getY());
        CoordinatePair<Float> c2 = new CoordinatePair<>(this.getX() + this.getWidth(), this.getY());
        CoordinatePair<Float> c3 = new CoordinatePair<>(this.getX(), this.getY() + this.getHeight());
        CoordinatePair<Float> c4 = new CoordinatePair<>(this.getX() + this.getWidth(), this.getY() + this.getHeight());

        if (coordinatePair.getX() < c1.getX() && coordinatePair.getY() < c1.getY()) {
            return DELECTION_TOP;
        } else if (coordinatePair.getX() < c1.getX() && coordinatePair.getY() > c1.getY() && coordinatePair.getY() < c3.getY()) {
            return DELECTION_LEFT;
        } else if (coordinatePair.getX() > c2.getX() && coordinatePair.getY() > c2.getY() && coordinatePair.getY() < c3.getY()) {
            return DELECTION_RIGHT;
        } else {
            return DELECTION_BOTTOM;
        }
    }

    public CoordinatePair<Float> getCenter () {
        float centerX = this.getX() + (getDrawWidth() / 2);
        float centerY = this.getY() + (getDrawHeight() / 2);
        return new CoordinatePair<>(centerX, centerY);
    }

    public float getRadius () {
        return this.getDrawHeight() / 2;
    }

    public void hit() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.block_hit);
        this.setImageDrawable(drawable);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hitListener.blockIsHit(Block.this);
            }
        }, 100);
    }
}

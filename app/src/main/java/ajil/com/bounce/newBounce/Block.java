package ajil.com.bounce.newBounce;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;

import ajil.com.bounce.R;

/**
 * Created by ajilo on 31-10-2017.
 */

public class Block extends AppCompatImageView {
    private Drawable drawable;
    private int index;
    public Block(Context context) {
        super(context);
        drawable = ContextCompat.getDrawable(context, R.drawable.block);
        this.setImageDrawable(drawable);
    }

    public int getDrawWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public int getDrawHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public float getRadius() {
        return this.getDrawWidth() / 2;
    }


    public float getCenterX () {
        return getX() + (getDrawWidth() / 2);
    }

    public float getCenterY() {
        return getY() + (getDrawHeight() / 2);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

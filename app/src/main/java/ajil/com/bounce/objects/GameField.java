package ajil.com.bounce.objects;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import static ajil.com.bounce.StartActivity.TYPE_FIELD;

/**
 * Created by ajil on 27/10/17.
 */

public class GameField extends FrameLayout {
    private float minX, maxX, minY, maxY;
    public int object_type = TYPE_FIELD;
    public GameField(@NonNull Context context) {
        super(context);
    }

    public GameField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameField(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }
}

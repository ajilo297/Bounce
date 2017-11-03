package ajil.com.bounce.helpers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ajilo on 28-10-2017.
 */

public class GestureView extends View implements View.OnTouchListener{
    private OnPanListener onPanListener;
    private float fx = 0;
    public GestureView(Context context) {
        super(context);
        setOnTouchListener(this);
        onPanListener = (OnPanListener) context;
    }

    public GestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        onPanListener = (OnPanListener) context;
    }

    public GestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        onPanListener = (OnPanListener) context;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (fx != motionEvent.getRawX()) {
            fx = motionEvent.getRawX();
            onPanListener.updatePaddleLocation(fx);
            return true;
        }
        return false;
    }
}

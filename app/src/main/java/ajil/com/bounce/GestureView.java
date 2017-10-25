package ajil.com.bounce;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ajilo on 25-10-2017.
 */

public class GestureView extends View implements View.OnTouchListener{
    private static final String TAG = "GESTURE_VIEW";
    private float ix = 0;
    private float fx = 0;
    private OnViewPanListener listener;

    public GestureView(Context context) {
        super(context);
        this.setOnTouchListener(this);
        listener = (OnViewPanListener) context;
    }

    public GestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        listener = (OnViewPanListener) context;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (fx != motionEvent.getRawX()) {
            ix = fx;
            fx = motionEvent.getRawX();
            listener.onViewPanned(fx);
            return true;
        }
        return false;
    }
}

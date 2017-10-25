package ajil.com.bounce;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.widget.FrameLayout;

/**
 * Created by ajilo on 25-10-2017.
 */

public class Paddle extends AppCompatImageView {

    private Context context;
    public Paddle(Context context) {
        super(context);
    }


    public Paddle(Context context, Drawable drawable) {
        super(context);
        this.setImageDrawable(drawable);
        this.setScaleType(ScaleType.FIT_XY);
    }

}

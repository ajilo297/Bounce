package ajil.com.bounce;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ajil.com.bounce.newBounce.NewBounceActivity;

public class StartActivity extends AppCompatActivity {

    public static final int TYPE_BLOCK = 0;
    public static final int TYPE_BALL = 1;
    public static final int TYPE_FIELD = 2;
    public static final int TYPE_PADDLE = 3;

    public static final int TYPE_VERTICAL = 13;
    public static final int TYPE_HORIZONTAL = 14;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        context = StartActivity.this;
    }

    public void startBounce(View view) {
        Intent intent = new Intent(context, NewBounceActivity.class);
        startActivity(intent);
    }
}

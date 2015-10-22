package moe.minori.moviecreditpoc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

public class MainActivity extends Activity {

    ImageView bgImage;
    RelativeLayout fgWrapper;
    ImageView fgImage;
    ScrollView fgScroller;

    int animationSpeedFactor = 10;

    int screenVerticalSize;
    int screenHorizontalSize;

    int currentPosition;

    Handler handler;

    boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgImage = (ImageView) findViewById(R.id.imageView);
        fgWrapper = (RelativeLayout) findViewById(R.id.fgWrapper);
        fgImage = (ImageView) findViewById(R.id.imageView2);
        fgScroller = (ScrollView) findViewById(R.id.scrollView);

        handler = new Handler();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();

        active = true;
        startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();

        active = false;
        initAnimation();
    }


    private void startAnimation() {
        initAnimation(); // to ensure

        // get screen size
        screenVerticalSize = getScreenVerticalSize();
        screenHorizontalSize = getScreenHorizontalSize();

        // set fg image size
        RelativeLayout.LayoutParams params = (LayoutParams) fgImage.getLayoutParams();

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.fg);

        int newWidth = screenHorizontalSize;
        int newHeight = bMap.getHeight() * screenHorizontalSize / bMap.getWidth();

        params.width = newWidth;
        params.height = newHeight;

        params.setMargins(0, screenVerticalSize, 0, screenVerticalSize);

        fgImage.setLayoutParams(params);

        // fgImage visible
        fgWrapper.setVisibility(View.VISIBLE);

        // fgImage start to move upward
        handler.post(new AnimationFrame());
    }

    private void initAnimation() {
        // fgImage invisible
        fgWrapper.setVisibility(View.INVISIBLE);

        // init progressive
        fgScroller.setScrollY(0);

    }

    private int getScreenVerticalSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }

    private int getScreenHorizontalSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }


    class AnimationFrame implements Runnable {
        @Override
        public void run() {
            currentPosition = fgScroller.getScrollY();
            currentPosition++;

            fgScroller.setScrollY(currentPosition);

            if (active)
                handler.postDelayed(this, animationSpeedFactor);
        }
    }

}



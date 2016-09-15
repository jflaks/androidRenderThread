package com.example.julian.graphicbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by julian on 8/29/16.
 */
public class AnimationView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder sh;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private DrawThread thread;
    private Context ctx;


    public AnimationView(Context context) {
        super(context);
        sh = getHolder();
        sh.addCallback(this);
        //paint.setColor(Color.rgb(180,80,10));
        //paint.setStyle(Paint.Style.FILL);
        ctx = context;
        setFocusable(true); // make sure we get key events

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Canvas canvas = sh.lockCanvas();
        //canvas.drawColor(Color.BLACK);
        //canvas.drawCircle(200, 200, 100, paint);
        //sh.unlockCanvasAndPost(canvas);
        thread = new DrawThread(sh, ctx, new Handler());
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}

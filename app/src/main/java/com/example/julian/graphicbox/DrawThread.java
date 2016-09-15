package com.example.julian.graphicbox;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;

/**
 * Created by julian on 9/11/16.
 */
public class DrawThread extends Thread {

    private int canvasWidth = 200;
    private int canvasHeight = 400;
    private static final int SPEED = 2;
    private boolean run = false;
    private boolean isSaved = false;
    private int offset = 0;
    private int r = 0;
    private int g = 0;
    private int b = 0;
    boolean rpos = true;
    boolean gpos = true;
    boolean bpos = true;

    private SurfaceHolder sh;
    private Handler handler;
    private Context ctx;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint4 = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Resources res;
    private Bitmap mBackground;
    Rect rect;

    public DrawThread(SurfaceHolder surfaceHolder, Context context,
                        Handler handler) {
        sh = surfaceHolder;
        this.handler = handler;
        ctx = context;
        Resources res = context.getResources();

        mBackground = BitmapFactory.decodeResource(res,
                R.drawable.octopusfree);
    }
    public void doStart() {
        synchronized (sh) {
            // Start bubble in centre and create some random motion
            paint.setARGB(13,255,0,255);
            paint2.setARGB(10,120,255,80);
            paint3.setARGB(10,255,255,0);


        }
    }
    public void run() {
        while (run) {
            Canvas c = null;
            try {
                c = sh.lockCanvas(null);
                synchronized (sh) {
                    doDraw(c);
                }
            } finally {
                if (c != null) {
                    sh.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setRunning(boolean b) {
        run = b;
    }
    public void setSurfaceSize(int width, int height) {
        synchronized (sh) {
            canvasWidth = width;
            canvasHeight = height;
            doStart();
        }
    }
    private void doDraw(Canvas canvas) {
        if (canvas == null) { return; }
        offset += 5;
        //if (this.isSaved) {
            //canvas.restore();
          //  this.isSaved = false;
        //}
        if (rpos && r +2 > 255) {
            rpos = false;
        }
        if (gpos && g +5 > 255) {
            gpos = false;
        }
        if (bpos && b +1 > 255) {
            bpos = false;
        }
        if (!rpos && r -2 < 0) {
            rpos = true;
        }
        if (!gpos && g -5 < 0) {
            gpos = true;
        }
        if (!bpos && b -1 < 0) {
            bpos = true;
        }

        r += rpos ? 2 : -2 ;
        g += gpos ? 5 : -5 ;
        b += bpos ? 1 : -1 ;
        canvas.drawColor(Color.rgb(r,g,b));
        for (int x =0; x < canvasWidth; x+=15) {
            //canvas.drawPoint(x, canvasHeight / 2, paint);
            //canvas.drawCircle(x, canvasHeight / 2, 20, paint);
            canvas.drawCircle((float) x, (float) Math.sin(Math.toRadians(360 * (x + offset * 3 ) / canvasWidth)) * canvasHeight / 2 + (canvasHeight / 2), 60, paint3);

            canvas.drawCircle((float) x, (float) Math.sin(Math.toRadians(360 * (x + offset) / canvasWidth)) * canvasHeight / 2 + (canvasHeight / 2), 30, paint);
            canvas.drawCircle((float) x, (float) Math.sin(Math.toRadians(360 * (x - offset) / canvasWidth)) * canvasHeight / 2 + (canvasHeight / 2), 20, paint2);

        }
        int widthPixels = canvasWidth / 3;
        int heightPixels = widthPixels * mBackground.getHeight() / mBackground.getWidth() ;
        int pigY  = (int) (Math.sin(Math.toRadians(offset)) * canvasHeight / 4 + canvasHeight / 2 - heightPixels /2);
        rect = new Rect(canvasWidth/4, pigY, (canvasWidth / 4 ) + widthPixels, pigY + heightPixels );
        canvas.drawBitmap(mBackground,null,rect,null);
        //canvas.drawCircle(bubbleX, bubbleY, 50, paint);
        //canvas.save();
        //this.isSaved = true;
    }
}

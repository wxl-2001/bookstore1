package com.jnu.bookstore.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.jnu.bookstore.data.CircleSpriter;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;

    public GameView(Context context) {
        super(context);
        initView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView();
    }

    private void initView() {
        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);
    }

    private DrawThread drawThread;
    private boolean isTouched=false;
    private float touchedX=-1;
    private float touchedY=-1;
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        drawThread = new DrawThread();
        drawThread.start();
        GameView.this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( MotionEvent.ACTION_DOWN==motionEvent.getAction())
                {
                    touchedX=motionEvent.getX();
                    touchedY=motionEvent.getY();
                    isTouched=true;
                }
                return false;
            }
        });
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        drawThread.myStop();
        try {
            drawThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class DrawThread extends Thread
    {
        private final ArrayList<CircleSpriter> spriters=new ArrayList<>();
        private boolean isStopped=false;
        public DrawThread()
        {
            spriters.add(new CircleSpriter(100,100,100,GameView.this.getWidth(),GameView.this.getHeight()));
            spriters.add(new CircleSpriter(300,300,100,GameView.this.getWidth(),GameView.this.getHeight()));
        }

        public void myStop()
        {
            isStopped=true;
        }
        @Override
        public void run() {
            super.run();
            Canvas canvas = null;
            int hitCount=0;

            while(!isStopped) {
                try {
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.GRAY);
                    if(GameView.this.isTouched)
                    {
                        for(int index=0;index<spriters.size();index++)
                        {
                            if(spriters.get(index).isShot(GameView.this.touchedX,GameView.this.touchedY)){
                                hitCount++;
                            }
                        }

                    }
                    for(int index=0;index<spriters.size();index++)
                    {
                        spriters.get(index).move();
                    }
                    for(int index=0;index<spriters.size();index++)
                    {
                        spriters.get(index).draw(canvas);
                    }
                    GameView.this.isTouched=false;
                    Paint paint=new Paint();
                    paint.setTextSize(100);
                    paint.setColor(Color.GREEN);
                    canvas.drawText("hit "+hitCount,100,100,paint);


                } catch (Exception e) {

                } finally {
                    if (null != canvas) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

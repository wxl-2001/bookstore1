package com.jnu.bookstore.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CircleSpriter {
    float x,y,radius;
    double direction;
    float maxWidth,maxHeight;

    public CircleSpriter(float x,float y, float radius,float maxWidth,float maxHeight)
    {
        this.x=x;
        this.y=y;
        this.radius=radius;
        this.direction=Math.random();
        this.maxHeight=maxHeight;
        this.maxWidth=maxWidth;
    }
    public void draw(Canvas canvas)
    {
        Paint paint=new Paint();
        paint.setColor(Color.RED);

        canvas.drawCircle(x,y,radius,paint);
    }
    public void move()
    {
        this.x+=20*Math.cos(direction);
        this.y+=20*Math.sin(direction);
        if(this.x<0) this.x+=maxWidth;
        if(this.y<0) this.y+=maxHeight;
        if(this.x>maxWidth) this.x-=maxWidth;
        if(this.y>maxHeight) this.y-=maxHeight;
    }

    public boolean isShot(float touchedX, float touchedY) {
        double distance=(touchedX-this.x)*(touchedX-this.x)+(touchedY-this.y)*(touchedY-this.y);
        return distance<radius*radius;
    }
}

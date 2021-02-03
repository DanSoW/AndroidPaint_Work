package com.example.androidpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorLong;

public class MyDraw extends View {
    public MyDraw(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        canvas.drawRect(0, getHeight() - 200, getWidth(), getHeight(), paint);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(8);
        canvas.drawCircle(50, 50, 100, paint);
        for(int i = 0; i < 5; i++){
            canvas.drawLine(50, 50, 300-i*50, 50+i*70, paint);
        }

        paint.setStrokeWidth(8);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        Point a = new Point(getWidth() - 800, getHeight() - 500);
        Point b = new Point(getWidth() - 500, getHeight() - 800);
        Point c = new Point(getWidth() - 200, getHeight() - 500);
        Point d = new Point(getWidth()-800, getHeight()-100);
        Point e = new Point(getWidth()-200, getHeight()-100);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, paint);

        path = new Path();
        path.moveTo(a.x, a.y);
        path.lineTo(d.x, d.y);
        path.lineTo(e.x, e.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, paint);

        paint.setColor(Color.BLACK);
        canvas.drawLine(a.x, a.y, c.x, c.y, paint);
        paint.setColor(Color.GRAY);

        paint.setColor(Color.BLACK);
        canvas.drawCircle(getWidth() - 500, getHeight() - 650, 50, paint);
        paint.setColor(Color.LTGRAY);
        canvas.drawLine(getWidth() - 550, getHeight() - 650,
                getWidth() - 450, getHeight() - 650, paint);
        canvas.drawLine(getWidth() - 500, getHeight() - 700,
                getWidth() - 500, getHeight() - 600, paint);

        a = new Point(getWidth() - 750, getHeight() - 200);
        b = new Point(getWidth() - 600, getHeight() - 200);
        c = new Point(getWidth() - 600, getHeight() - 400);
        d = new Point(getWidth() - 750, getHeight() - 400);

        paint.setColor(Color.BLACK);
        path = new Path();
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(d.x, d.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, paint);

        paint.setColor(Color.LTGRAY);
        canvas.drawLine(getWidth() - 675, getHeight() - 200,
                getWidth() - 675, getHeight() - 400, paint);
        canvas.drawLine(getWidth() - 750, getHeight() - 300,
                getWidth() - 600, getHeight() - 300, paint);


        a = new Point(getWidth() - 400, getHeight() - 350);
        b = new Point(getWidth() - 200, getHeight() - 350);
        c = new Point(getWidth() - 200, getHeight() - 100);
        d = new Point(getWidth() - 400, getHeight() - 100);

        paint.setColor(Color.BLACK);
        path = new Path();
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(d.x, d.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, paint);

        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(2);

        c.y = b.y;
        do{
            b.x -= 25;
            c.y += 25;
            canvas.drawLine(b.x, b.y, c.x, c.y, paint);
        }while((b.x - 25) >= a.x);

        c = new Point(getWidth() - 200, getHeight() - 100);
        c.x = d.x;
        do{
            d.y -= 25;
            c.x += 25;
            canvas.drawLine(d.x, d.y, c.x, c.y, paint);
        }while((d.y - 25) >= a.y);
    }
}

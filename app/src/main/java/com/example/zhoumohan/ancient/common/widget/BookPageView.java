package com.example.zhoumohan.ancient.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BookPageView extends View {

    /**
     * 正面画笔
     */
    private Paint frontPaint;
    /**
     * 背面画笔
     */
    private Paint backPaint;
    /**
     * 页角点（触摸点，a点）
     */
    private Point cornerPoint;
    /**
     * 屏幕宽高
     */
    private int screenWidth;
    private int screenHeight;

    private Path path;

    public BookPageView(Context context) {
        this(context, null);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        frontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        frontPaint.setColor(Color.GREEN);
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cornerPoint = new Point();
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        cornerPoint.x = screenWidth;
        cornerPoint.y = screenHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cornerPoint.x = (int) event.getRawX();
                cornerPoint.y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                cornerPoint.x = (int) event.getRawX();
                cornerPoint.y = (int) event.getRawY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //f点
        float fX = screenWidth;
        float fY = screenHeight;
        //g点
        float gX = (cornerPoint.x + fX) / 2;
        float gY = (cornerPoint.y + fY) / 2;
        //m点
        float mX = gX;
        float mY = fY;
        //e点
        float eX = mX - (fY - gY) * (fY - gY) / (fX - gX);
        float eY = fY;
        //h点
        float hX = fX;
        float hY = gY - (fX - gX) * (fX - gX) / (fY - gY);

        //b点
        float bX = (cornerPoint.x + eX) / 2;
        float bY = (cornerPoint.y + eY) / 2;
        //k点
        float kX = (cornerPoint.x + hX) / 2;
        float kY = (cornerPoint.y + hY) / 2;

        //c点
        float cX = (float) (fX - ((fX - cornerPoint.x) + Math.pow(cornerPoint.y, 2) / (4 * (fX - mX))) * 3 / 2);
        float cY = fY;
        //j点
        float jX = fX;
        float jY = (fX - cX) * (fY - hY) / (fX - eX);
        //d点
        float dX = ((cX + bX) / 2 + eX) / 2;
        float dY = ((cY + bY) / 2 + eY) / 2;
        //i点
        float iX = ((jX + kX) / 2 + hX) / 2;
        float iY = ((jY + kY) / 2 + hY) / 2;

        Log.e("坐标", "A:x=" + cornerPoint.x + "y=" + cornerPoint.y);
        Log.e("坐标", "F:x=" + fX + "y=" + fY);
        Log.e("坐标", "G:x=" + gX + "y=" + gY);
        Log.e("坐标", "M:x=" + mX + "y=" + mY);
        Log.e("坐标", "E:x=" + eX + "y=" + eY);
        Log.e("坐标", "H:x=" + hX + "y=" + hY);
        Log.e("坐标", "B:x=" + bX + "y=" + bY);
        Log.e("坐标", "K:x=" + kX + "y=" + kY);
        Log.e("坐标", "C:x=" + cX + "y=" + cY);
        Log.e("坐标", "J:x=" + jX + "y=" + jY);
        Log.e("坐标", "D:x=" + dX + "y=" + dY);
        Log.e("坐标", "I:x=" + iX + "y=" + iY);


        path.reset();
        path.lineTo(0, screenHeight);
        path.lineTo(cX, cY);
        path.quadTo(eX, eY, bX, bY);
        path.lineTo(cornerPoint.x, cornerPoint.y);
        path.lineTo(kX, kY);
        path.quadTo(hX, hY, jX, jY);
        path.lineTo(screenWidth, 0);
        path.close();

        canvas.drawPath(path, frontPaint);

    }
}

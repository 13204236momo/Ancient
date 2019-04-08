package com.example.zhoumohan.ancient.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BookPageView extends View {

    private static final int TOP_LEFT = 0;
    private static final int TOP_MIDDLE = 1;
    private static final int TOP_RIGHT = 2;
    private static final int MIDDLE_LEFT = 3;
    private static final int MIDDLE_MIDDLE = 4;
    private static final int MIDDLE_RIGHT = 5;
    private static final int BOTTOM_LEFT = 6;
    private static final int BOTTOM_MIDDLE = 7;
    private static final int BOTTOM_RIGHT = 8;

    /**
     * 按下的位置
     */
    private static int currentArea = 0;


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

    private float fX, fY, gX, gY, mX, mY, eX, eY, hX, hY, bX, bY, kX, kY, cX, cY, jX, jY, dX, dY, iX, iY;

    /**
     * 屏幕宽高
     */
    private int screenWidth;
    private int screenHeight;

    /**
     * 正面路径
     */
    private Path frontPath;
    /**
     * 反面路径
     */
    private Path backPath;

    /**
     * 每次按下的位置
     */
    private float touchDownX, touchDownY;

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
        backPaint.setColor(Color.YELLOW);
        cornerPoint = new Point();
        frontPath = new Path();
        backPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = (int) event.getRawX();
                touchDownY = (int) event.getRawY();
                touchArea(touchDownX, touchDownY);
                Log.e("zhou2", touchDownX + "&&" + touchDownY);
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

    private void touchArea(float touchDownX, float touchDownY) {
        if (touchDownY <= screenHeight / 3) {  //上层
            if (touchDownX <= screenWidth / 3) {
                currentArea = TOP_LEFT;
                fX = 0;
                fY = 0;
            } else if (touchDownX > screenWidth / 3 && touchDownX < screenWidth * 2 / 3) {
                currentArea = TOP_MIDDLE;
            } else {
                currentArea = TOP_RIGHT;
                fX = screenWidth;
                fY = 0;
            }

        } else if (touchDownY > screenHeight / 3 && touchDownY < screenHeight * 2 / 3) { //中层
            if (touchDownX <= screenWidth / 3) {
                currentArea = MIDDLE_LEFT;
            } else if (touchDownX > screenWidth / 3 && touchDownX < screenWidth * 2 / 3) {
                currentArea = MIDDLE_MIDDLE;
            } else {
                currentArea = MIDDLE_RIGHT;
            }
        } else { //下层
            if (touchDownX <= screenWidth / 3) {
                currentArea = BOTTOM_LEFT;
                fX = 0;
                fY = screenHeight;
            } else if (touchDownX > screenWidth / 3 && touchDownX < screenWidth * 2 / 3) {
                currentArea = BOTTOM_MIDDLE;
            } else {
                currentArea = BOTTOM_RIGHT;
                fX = screenWidth;
                fY = screenHeight;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //g点
        gX = (cornerPoint.x + fX) / 2;
        gY = (cornerPoint.y + fY) / 2;
        //m点
        mX = gX;
        mY = fY;
        //e点
        eX = mX - (fY - gY) * (fY - gY) / (fX - gX);
        eY = fY;
        //h点
        hX = fX;
        hY = gY - (fX - gX) * (fX - gX) / (fY - gY);

        //b点
        bX = (cornerPoint.x + eX) / 2;
        bY = (cornerPoint.y + eY) / 2;
        //k点
        kX = (cornerPoint.x + hX) / 2;
        kY = (cornerPoint.y + hY) / 2;

        //c点
        cX = eX - (fX - eX) / 2;
        cY = fY;
        //j点
        jX = fX;
        jY = hY - (fY - hY) / 2;
        //d点
        dX = ((cX + bX) / 2 + eX) / 2;
        dY = ((cY + bY) / 2 + eY) / 2;
        //i点
        iX = ((jX + kX) / 2 + hX) / 2;
        iY = ((jY + kY) / 2 + hY) / 2;

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


        switch (currentArea) {
            case BOTTOM_RIGHT:
                pathC(canvas);
                pathAFromLowerRight(canvas);
                break;
            case BOTTOM_LEFT:
                pathC(canvas);
                pathAFromLowerLeft(canvas);
                break;
            case TOP_LEFT:
                pathC(canvas);
                pathAFromTopLeft(canvas);
                break;
            case TOP_RIGHT:
                pathC(canvas);
                pathAFromTopRight(canvas);
                break;
        }


    }

    private void pathAFromLowerRight(Canvas canvas) {
        frontPath.reset();
        frontPath.lineTo(0, screenHeight);
        frontPath.lineTo(cX, cY);
        frontPath.quadTo(eX, eY, bX, bY);
        frontPath.lineTo(cornerPoint.x, cornerPoint.y);
        frontPath.lineTo(kX, kY);
        frontPath.quadTo(hX, hY, jX, jY);
        frontPath.lineTo(screenWidth, 0);
        frontPath.close();
        canvas.drawPath(frontPath, frontPaint);
    }

    private void pathAFromLowerLeft(Canvas canvas) {
        frontPath.reset();
        frontPath.lineTo(jX, jY);
        frontPath.quadTo(hX, hY, kX, kY);
        frontPath.lineTo(cornerPoint.x, cornerPoint.y);
        frontPath.lineTo(dX, dY);
        //frontPath.quadTo(eX, eY, cX, cY);
        frontPath.lineTo(screenWidth, screenHeight);
        frontPath.lineTo(screenWidth, 0);
        frontPath.close();
        canvas.drawPath(frontPath, frontPaint);
    }

    private void pathAFromTopRight(Canvas canvas) {
        frontPath.reset();
        frontPath.lineTo(cX, cY);
        frontPath.quadTo(eX, eY, bX, bY);
        frontPath.lineTo(cornerPoint.x, cornerPoint.y);
        frontPath.lineTo(kX, kY);
        frontPath.quadTo(hX, hY, jX, jY);
        frontPath.lineTo(screenWidth, screenHeight);
        frontPath.lineTo(0, screenHeight);
        frontPath.close();
        canvas.drawPath(frontPath, frontPaint);
    }

    private void pathAFromTopLeft(Canvas canvas) {
        frontPath.reset();
        frontPath.moveTo(cX, cY);
        frontPath.quadTo(eX, eY, bX, bY);
        frontPath.lineTo(cornerPoint.x, cornerPoint.y);
        frontPath.lineTo(kX, kY);
        frontPath.quadTo(hX, hY, jX, jY);
        frontPath.lineTo(0, screenHeight);
        frontPath.lineTo(screenWidth, screenHeight);
        frontPath.lineTo(screenWidth, 0);
        frontPath.close();
        canvas.drawPath(frontPath, frontPaint);
    }

    private void pathC(Canvas canvas) {
        backPath.reset();
        backPath.moveTo(iX, iY);
        backPath.lineTo(dX, dY);
        backPath.lineTo(bX, bY);
        backPath.lineTo(cornerPoint.x, cornerPoint.y);
        backPath.lineTo(kX, kY);
        backPath.close();
        canvas.drawPath(backPath, backPaint);
    }
}

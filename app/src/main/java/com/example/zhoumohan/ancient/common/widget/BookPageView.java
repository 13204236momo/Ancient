package com.example.zhoumohan.ancient.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.example.zhoumohan.ancient.R;

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
    private static int currentArea = -1;

    /**
     * 正面画笔
     */
    private Paint aPaint;
    /**
     * 背面画笔
     */
    private Paint cPaint;
    /**
     * 下一页画笔
     */
    private Paint bPaint;

    /**
     * 关键点，a(x,y)为触摸点（页角点）
     */
    private float aX, aY, fX, fY, gX, gY, mX, mY, eX, eY, hX, hY, bX, bY, kX, kY, cX, cY, jX, jY, dX, dY, iX, iY;

    /**
     * 屏幕宽高
     */
    private int screenWidth;
    private int screenHeight;

    /**
     * 正面路径
     */
    private Path aPath;
    /**
     * 反面路径
     */
    private Path cPath;

    private Path bPath;
    /**
     * 每次按下的位置
     */
    private float touchDownX, touchDownY;


    private Bitmap bitmap;
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
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_pager);

        aPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //aPaint.setColor(Color.GREEN);
        bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bPaint.setColor(Color.BLUE);
        cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cPaint.setColor(Color.YELLOW);
        aPath = new Path();
        bPath = new Path();
        cPath = new Path();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
                break;
            case MotionEvent.ACTION_MOVE:
                aX = (int) event.getRawX();
                aY = (int) event.getRawY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                switch (currentArea) {
                    case TOP_LEFT:
                        if (event.getRawX() > screenWidth * 1/ 3 || event.getRawY() > screenHeight * 1 / 3){
                            otherAnimation(event.getRawX(), event.getRawY(),2*screenWidth,0);
                        }else {
                            resumeAnimation(event.getRawX(), event.getRawY());
                        }
                        break;
                    case TOP_RIGHT:
                        if (event.getRawX() < screenWidth * 2 / 3 || event.getRawY() > screenHeight * 1 / 3){
                            otherAnimation(event.getRawX(), event.getRawY(),-screenWidth,0);
                        }else {
                            resumeAnimation(event.getRawX(), event.getRawY());
                        }
                        break;
                    case BOTTOM_LEFT:
                        if (event.getRawX() > screenWidth * 1 / 3 || event.getRawY() < screenHeight * 2 / 3){
                            otherAnimation(event.getRawX(), event.getRawY(),2*screenWidth,screenHeight);
                        }else {
                            resumeAnimation(event.getRawX(), event.getRawY());
                        }
                        break;
                    case BOTTOM_RIGHT:
                        if (event.getRawX() < screenWidth * 2 / 3 || event.getRawY() < screenHeight * 2 / 3) { //翻页
                            otherAnimation(event.getRawX(), event.getRawY(),-screenWidth,screenHeight);
                        } else { //不翻页，恢复
                            resumeAnimation(event.getRawX(), event.getRawY());
                        }
                        break;
                }
                break;
        }
        return true;
    }

    /**
     * 划分触摸区域
     *
     * @param touchDownX
     * @param touchDownY
     */
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

    /**
     * 恢复动画
     *
     * @param rawX
     * @param rawY
     */
    private void resumeAnimation(float rawX, float rawY) {
        //y=kx+b;
        final float k = (fY - rawY) / (fX - rawX);
        final float b = fY - k * fX;

        ValueAnimator animator = ValueAnimator.ofFloat(rawX, fX);
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                aX = (float) animation.getAnimatedValue();
                aY =  aX * k + b;
                invalidate();
            }
        });
        animator.start();
    }


    /**
     * 翻页动画
     * @param rawX
     * @param rawY
     * @param destinationX
     */
    private void otherAnimation(float rawX, float rawY, float destinationX,float destinationY) {
        //y=kx+b;
        final float k = (destinationY - rawY) / (destinationX - rawX);
        final float b = destinationY - k * (destinationX);

        ValueAnimator animator = ValueAnimator.ofFloat(rawX, destinationX);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                aX = (float) animation.getAnimatedValue();
                aY =  aX * k + b;
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(bitmap, 0, 0, aPaint);
        //g点
        gX = (aX + fX) / 2;
        gY = (aY + fY) / 2;
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
        bX = (aX + eX) / 2;
        bY = (aY + eY) / 2;
        //k点
        kX = (aX + hX) / 2;
        kY = (aY + hY) / 2;

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

//        Log.e("坐标", "A:x=" + aX + "y=" + aY);
//        Log.e("坐标", "F:x=" + fX + "y=" + fY);
//        Log.e("坐标", "G:x=" + gX + "y=" + gY);
//        Log.e("坐标", "M:x=" + mX + "y=" + mY);
//        Log.e("坐标", "E:x=" + eX + "y=" + eY);
//        Log.e("坐标", "H:x=" + hX + "y=" + hY);
//        Log.e("坐标", "B:x=" + bX + "y=" + bY);
//        Log.e("坐标", "K:x=" + kX + "y=" + kY);
//        Log.e("坐标", "C:x=" + cX + "y=" + cY);
//        Log.e("坐标", "J:x=" + jX + "y=" + jY);
//        Log.e("坐标", "D:x=" + dX + "y=" + dY);
//        Log.e("坐标", "I:x=" + iX + "y=" + iY);


        switch (currentArea) {
            case BOTTOM_RIGHT:
                pathB(canvas);
                pathC(canvas);
                pathAFromLowerRight(canvas);
                break;
            case BOTTOM_LEFT:
                pathB(canvas);
                pathC(canvas);
                pathAFromLowerLeft(canvas);
                break;
            case TOP_LEFT:
                pathB(canvas);
                pathC(canvas);
                pathAFromTopLeft(canvas);
                break;
            case TOP_RIGHT:
                pathB(canvas);
                pathC(canvas);
                pathAFromTopRight(canvas);
                break;
        }


    }

    private void pathAFromLowerRight(Canvas canvas) {
        aPath.reset();
        aPath.lineTo(0, screenHeight);
        aPath.lineTo(cX, cY);
        aPath.quadTo(eX, eY, bX, bY);
        aPath.lineTo(aX, aY);
        aPath.lineTo(kX, kY);
        aPath.quadTo(hX, hY, jX, jY);
        aPath.lineTo(screenWidth, 0);
        aPath.close();
        canvas.drawPath(aPath, aPaint);
    }

    private void pathAFromLowerLeft(Canvas canvas) {
        aPath.reset();
        aPath.lineTo(jX, jY);
        aPath.quadTo(hX, hY, kX, kY);
        aPath.lineTo(aX, aY);
        aPath.lineTo(bX, bY);
        aPath.quadTo(eX, eY, cX, cY);
        aPath.lineTo(screenWidth, screenHeight);
        aPath.lineTo(screenWidth, 0);
        aPath.close();
        canvas.drawPath(aPath, aPaint);
    }

    private void pathAFromTopRight(Canvas canvas) {
        aPath.reset();
        aPath.lineTo(cX, cY);
        aPath.quadTo(eX, eY, bX, bY);
        aPath.lineTo(aX, aY);
        aPath.lineTo(kX, kY);
        aPath.quadTo(hX, hY, jX, jY);
        aPath.lineTo(screenWidth, screenHeight);
        aPath.lineTo(0, screenHeight);
        aPath.close();
        canvas.drawPath(aPath, aPaint);
    }

    private void pathAFromTopLeft(Canvas canvas) {
        aPath.reset();
        aPath.moveTo(cX, cY);
        aPath.quadTo(eX, eY, bX, bY);
        aPath.lineTo(aX, aY);
        aPath.lineTo(kX, kY);
        aPath.quadTo(hX, hY, jX, jY);
        aPath.lineTo(0, screenHeight);
        aPath.lineTo(screenWidth, screenHeight);
        aPath.lineTo(screenWidth, 0);
        aPath.close();
        canvas.drawPath(aPath, aPaint);

       // aPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, aPaint);
    }

    private void pathC(Canvas canvas) {
        cPath.reset();
        cPath.moveTo(iX, iY);
        cPath.lineTo(dX, dY);
        cPath.lineTo(bX, bY);
        cPath.lineTo(aX, aY);
        cPath.lineTo(kX, kY);
        cPath.close();
        canvas.drawPath(cPath, cPaint);
    }

    private void pathB(Canvas canvas){
        bPath.reset();
        bPath.lineTo(0,screenHeight);
        bPath.lineTo(screenWidth,screenHeight);
        bPath.lineTo(screenWidth,0);
        bPath.close();
        canvas.drawPath(bPath,bPaint);
    }
}

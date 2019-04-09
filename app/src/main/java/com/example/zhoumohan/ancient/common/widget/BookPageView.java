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
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.example.zhoumohan.ancient.R;
import com.example.zhoumohan.ancient.util.BitmapUtil;

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
     * 文字画笔
     */
    private TextPaint textPaint;
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

    /**
     * 每次按下的位置
     */
    private float touchDownX, touchDownY;

String paintText = "1.需要分行的字符串\n" +
        "\n" +
        "2.需要分行的字符串从第几的位置开始\n" +
        "\n" +
        "3.需要分行的字符串到哪里结束\n" +
        "\n" +
        "4.画笔对象\n" +
        "\n" +
        "5.layout的宽度，字符串超出宽度时自动换行。\n" +
        "\n" +
        "6.layout的对其方式，有ALIGN_CENTER， ALIGN_NORMAL， ALIGN_OPPOSITE 三种。\n" +
        "\n" +
        "7.相对行间距，相对字体大小，1.5f表示行间距为1.5倍的字体高度。\n" +
        "\n" +
        "8.在基础行距上添加多少\n" +
        "\n" +
        "实际行间距等于这两者的和。";
    private Bitmap bitmap;
    private Bitmap bitmapB;
    private Bitmap bitmapC;

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
        bitmapB = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_pager);
        bitmapC = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.zhouyue);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bPaint.setColor(Color.GREEN);
       // bPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aPath = new Path();
        cPath = new Path();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;


        initBitmap();
    }

    private void initBitmap() {
        bitmap = BitmapUtil.scaleBitmap(bitmap, screenWidth, screenHeight);
        bitmapB = BitmapUtil.scaleBitmap(bitmapB, screenWidth, screenHeight);
        bitmapC = BitmapUtil.scaleBitmap(bitmapC, screenWidth, screenHeight);

        Canvas canvas = new Canvas(bitmap);
        StaticLayout staticLayout = new StaticLayout(paintText,textPaint,screenWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        canvas.translate(20, 0);
        staticLayout.draw(canvas);
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
                        if (event.getRawX() > screenWidth * 1 / 3 || event.getRawY() > screenHeight * 1 / 3) {
                            otherAnimation(event.getRawX(), event.getRawY(), 2 * screenWidth, 0);
                        } else {
                            resumeAnimation(event.getRawX(), event.getRawY());
                        }
                        break;
                    case TOP_RIGHT:
                        if (event.getRawX() < screenWidth * 2 / 3 || event.getRawY() > screenHeight * 1 / 3) {
                            otherAnimation(event.getRawX(), event.getRawY(), -screenWidth, 0);
                        } else {
                            resumeAnimation(event.getRawX(), event.getRawY());
                        }
                        break;
                    case BOTTOM_LEFT:
                        if (event.getRawX() > screenWidth * 1 / 3 || event.getRawY() < screenHeight * 2 / 3) {
                            otherAnimation(event.getRawX(), event.getRawY(), 2 * screenWidth, screenHeight);
                        } else {
                            resumeAnimation(event.getRawX(), event.getRawY());
                        }
                        break;
                    case BOTTOM_RIGHT:
                        if (event.getRawX() < screenWidth * 2 / 3 || event.getRawY() < screenHeight * 2 / 3) { //翻页
                            otherAnimation(event.getRawX(), event.getRawY(), -screenWidth, screenHeight);
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
                aY = aX * k + b;
                invalidate();
            }
        });
        animator.start();
    }


    /**
     * 翻页动画
     *
     * @param rawX
     * @param rawY
     * @param destinationX
     */
    private void otherAnimation(float rawX, float rawY, float destinationX, float destinationY) {
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
                aY = aX * k + b;
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
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

        canvas.drawBitmap(bitmap, 0, 0, null);
        //使用离屏绘制

        switch (currentArea) {
            case BOTTOM_RIGHT:
                drawContentB(canvas);
                //drawContentC(canvas,pathC());
                drawContentA(canvas, pathAFromLowerRight());
                canvas.drawPath(pathAFromLowerRight(),bPaint);
                break;
            case BOTTOM_LEFT:
                drawContentB(canvas);
                drawContentC(canvas,pathC());
                drawContentA(canvas, pathAFromLowerLeft());
                break;
            case TOP_LEFT:
                drawContentB(canvas);
                drawContentC(canvas,pathC());
                drawContentA(canvas, pathAFromTopLeft());
                break;
            case TOP_RIGHT:
                drawContentB(canvas);
                drawContentC(canvas,pathC());
                drawContentA(canvas, pathAFromTopRight());
                break;
        }

    }

    private Path pathAFromLowerRight() {
        aPath.reset();
        aPath.lineTo(0, screenHeight);
        aPath.lineTo(cX, cY);
        aPath.quadTo(eX, eY, bX, bY);
        aPath.lineTo(aX, aY);
        aPath.lineTo(kX, kY);
        aPath.quadTo(hX, hY, jX, jY);
        aPath.lineTo(screenWidth, 0);
        aPath.close();
        return aPath;
    }

    private Path pathAFromLowerLeft() {
        aPath.reset();
        aPath.lineTo(jX, jY);
        aPath.quadTo(hX, hY, kX, kY);
        aPath.lineTo(aX, aY);
        aPath.lineTo(bX, bY);
        aPath.quadTo(eX, eY, cX, cY);
        aPath.lineTo(screenWidth, screenHeight);
        aPath.lineTo(screenWidth, 0);
        aPath.close();
        return aPath;
    }

    private Path pathAFromTopRight() {
        aPath.reset();
        aPath.lineTo(cX, cY);
        aPath.quadTo(eX, eY, bX, bY);
        aPath.lineTo(aX, aY);
        aPath.lineTo(kX, kY);
        aPath.quadTo(hX, hY, jX, jY);
        aPath.lineTo(screenWidth, screenHeight);
        aPath.lineTo(0, screenHeight);
        aPath.close();
        return aPath;
    }

    private Path pathAFromTopLeft() {
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
        return aPath;
    }

    private Path pathC() {
        cPath.reset();
        cPath.moveTo(iX, iY);
        cPath.lineTo(dX, dY);
        cPath.lineTo(bX, bY);
        cPath.lineTo(aX, aY);
        cPath.lineTo(kX, kY);
        cPath.close();
        return cPath;
    }
    private void drawContentC(Canvas canvas,Path path) {
        canvas.clipPath(path, Region.Op.INTERSECT);
        canvas.drawBitmap(bitmapC, 0, 0, null);
    }

    private void drawContentA(Canvas canvas, Path path) {
        canvas.clipPath(path, Region.Op.INTERSECT);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
    private void drawContentB(Canvas canvas) {
        //canvas.drawBitmap(bitmapB, 0, 0, null);
    }
}

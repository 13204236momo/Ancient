package com.example.zhoumohan.ancient.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
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
     * 关键点，a(x,y)为触摸点（页角点）
     */
    private float aX, aY, fX, fY, gX, gY, mX, mY, eX, eY, hX, hY, bX, bY, kX, kY, cX, cY, jX, jY, dX, dY, iX, iY;

    /**
     * 屏幕宽高
     */
    private int screenWidth;
    private int screenHeight;

    /**
     * A面路径
     */
    private Path aPath;
    /**
     * B面路径
     */
    private Path bPath;
    /**
     * c面路径
     */
    private Path cPath;

    /**
     * 每次按下的位置
     */
    private float touchDownX, touchDownY;

    private Paint strokePaint;

    /**
     * 顶部翻页A区左侧阴影
     */
    private GradientDrawable gradientDrawableATopLeft;
    /**
     * 底部翻页A区左侧阴影
     */
    private GradientDrawable gradientDrawableABottomLeft;
    /**
     * 顶部翻页A区右侧阴影
     */
    private GradientDrawable gradientDrawableATopRight;
    /**
     * 底部翻页A区右侧阴影
     */
    private GradientDrawable gradientDrawableABottomRight;
    /**
     * 水平翻页A区阴影
     */
    private GradientDrawable gradientDrawableAHorizontal;
    /**
     * 顶部翻页B区阴影
     */
    private GradientDrawable gradientDrawableBTop;
    /**
     * 底部翻页B区阴影
     */
    private GradientDrawable gradientDrawableBBottom;
    /**
     * 顶部翻页C区阴影
     */
    private GradientDrawable gradientDrawableCTop;
    /**
     * 底部翻页C区阴影
     */
    private GradientDrawable gradientDrawableCBottom;

    private float lPathAShadowDis = 0;//A区域左阴影矩形短边长度参考值
    private float rPathAShadowDis = 0;//A区域右阴影矩形短边长度参考值

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
            "实际行间距等于这两者的和。" +
            "1.需要分行的字符串\n" +
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
    private String paintText1 = "几个月前，晚上梦到在研究翻页动画，次日上班居然想起了昨天晚上的梦。search了相关信息，大概要用到数学计算、canvas绘图等相关内容。对我来说手动绘图这方面的实践还是0，想想短时间内应该搞不定，便将此事记在了待做清单内。（公司电脑一个单子，自己Mac一个单子，增加行数的速度远比删除的速度快。）\n" +
            "十一长假前夕手头没什么事情，看着清单顶部的第一项（时间排序），便照着例文开始绘图。画到后面，想想就一张图片多无聊。算了！加点功能做个带翻页动画的阅读器吧\n" +
            "\n" +
            "作者：s1991721\n" +
            "链接：https://www.jianshu.com/p/b3f744370a02\n" +
            "来源：简书\n" +
            "简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。";
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
        bitmap = BitmapFactory.decodeResource(getContext()   .getResources(), R.drawable.bg_pager);
        bitmapB = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_pager);
        bitmapC = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_pager_1);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(40);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(Color.GREEN);
        //strokePaint.setShadowLayer(10f, 50f, 50f, Color.GREEN);
        aPath = new Path();
        bPath = new Path();
        cPath = new Path();

        initGradient();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * 初始化阴影
     */
    private void initGradient() {
        int deepColor = 0x55333333;
        int lightColor = 0x01333333;
        int[] gradientColors = new int[]{lightColor, deepColor};

        gradientDrawableATopLeft = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawableATopLeft.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        gradientDrawableABottomLeft = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        gradientDrawableABottomLeft.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55333333;
        lightColor = 0x01333333;
        gradientColors = new int[]{lightColor, lightColor, lightColor, deepColor};
        gradientDrawableATopRight = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradientColors);
        gradientDrawableATopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        gradientDrawableABottomRight = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
        gradientDrawableABottomRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55333333;
        lightColor = 0x01333333;
        gradientColors = new int[]{lightColor, deepColor};
        gradientDrawableAHorizontal = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawableAHorizontal.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55333333;
        lightColor = 0x01111111;
        gradientColors = new int[]{deepColor, lightColor};
        gradientDrawableBTop = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawableBTop.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawableBBottom = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        gradientDrawableBBottom.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x22333333;
        lightColor = 0x00333333;
        gradientColors = new int[]{lightColor, deepColor};
        gradientDrawableCTop = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawableCTop.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawableCBottom = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        gradientDrawableCBottom.setGradientType(GradientDrawable.LINEAR_GRADIENT);
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
        StaticLayout staticLayout = new StaticLayout(paintText, textPaint, screenWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        canvas.translate(20, 0);
        staticLayout.draw(canvas);

        Canvas canvas1 = new Canvas(bitmapB);
        StaticLayout staticLayout1 = new StaticLayout(paintText1, textPaint, screenWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        canvas1.translate(20, 0);
        staticLayout1.draw(canvas1);
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

        float lA = aY - eY;
        float lB = eX - aX;
        float lC = aX * eY - eX * aY;
        lPathAShadowDis = Math.abs((lA * dX + lB * dY + lC) / (float) Math.hypot(lA, lB));

        float rA = aY - hY;
        float rB = hX - aX;
        float rC = aX * hY - hX * aY;
        rPathAShadowDis = Math.abs((rA * iX + rB * iY + rC) / (float) Math.hypot(rA, rB));

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
                drawContentB(canvas, pathAFromLowerRight());
                drawContentC(canvas, pathAFromLowerRight());
                drawContentA(canvas, pathAFromLowerRight());
                break;
            case BOTTOM_LEFT:
                drawContentB(canvas, pathAFromLowerLeft());
                drawContentC(canvas, pathAFromLowerLeft());
                drawContentA(canvas, pathAFromLowerLeft());
                break;
            case TOP_LEFT:
                drawContentB(canvas, pathAFromTopLeft());
                drawContentC(canvas, pathAFromTopLeft());
                drawContentA(canvas, pathAFromTopLeft());
                break;
            case TOP_RIGHT:
                drawContentB(canvas, pathAFromTopRight());
                drawContentC(canvas, pathAFromTopRight());
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

    private Path pathB() {
        bPath.reset();
        bPath.lineTo(0, screenHeight);
        bPath.lineTo(screenWidth, screenHeight);
        bPath.lineTo(screenWidth, 0);
        bPath.close();
        return bPath;
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

    private void drawContentC(Canvas canvas, Path path) {
        canvas.save();
        canvas.clipPath(path);
        canvas.clipPath(pathC(), Region.Op.REVERSE_DIFFERENCE);
        canvas.drawBitmap(bitmapC, 0, 0, null);
        canvas.restore();
        //drawPathCShadow(canvas);
    }

    private void drawContentA(Canvas canvas, Path path) {
        canvas.save();
        canvas.clipPath(path, Region.Op.INTERSECT);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();
        drawPathALeftShadow(canvas);
        drawPathARightShadow(canvas);
    }

    private void drawContentB(Canvas canvas, Path path) {
        canvas.save();
        canvas.clipPath(path);
        canvas.clipPath(pathC(), Region.Op.UNION);
        canvas.clipPath(pathB(), Region.Op.REVERSE_DIFFERENCE);
        canvas.drawBitmap(bitmapB, 0, 0, null);
        canvas.restore();
        drawPathBShadow(canvas);
    }


    //A区左侧
    private void drawPathALeftShadow(Canvas canvas) {
        canvas.save();
        int left;
        int right;
        int top = (int) eY;
        int bottom = (int) (eY + screenHeight);
        GradientDrawable gradientDrawable;
        if (currentArea == TOP_RIGHT) {
            gradientDrawable = gradientDrawableATopLeft;
            left = (int) (eX - lPathAShadowDis / 2);
            right = (int) (eX);
        } else {
            gradientDrawable = gradientDrawableABottomLeft;
            left = (int) (eX);
            right = (int) (eX + lPathAShadowDis / 2);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        //裁剪出我们需要的区域
        Path mPath = new Path();
        mPath.moveTo(aX - Math.max(rPathAShadowDis, lPathAShadowDis) / 2, aY);
        mPath.lineTo(dX, dY);
        mPath.lineTo(eX, eY);
        mPath.lineTo(aX, aY);
        mPath.close();
        canvas.clipPath(aPath);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(eX - aX, aY - eY));
        canvas.rotate(mDegrees, eX, eY);
        gradientDrawable.draw(canvas);
        canvas.restore();
    }

    //A区右侧
    private void drawPathARightShadow(Canvas canvas) {
        canvas.save();
        float viewDiagonalLength = (float) Math.hypot(screenWidth, screenHeight);//view对角线长度
        int left = (int) hX;
        int right = (int) (hX + viewDiagonalLength * 10);//需要足够长的长度
        int top;
        int bottom;

        GradientDrawable gradientDrawable;
        if (currentArea == TOP_RIGHT) {
            gradientDrawable = gradientDrawableATopRight;
            top = (int) (hY - rPathAShadowDis / 2);
            bottom = (int) hY;
        } else {
            gradientDrawable = gradientDrawableABottomRight;
            top = (int) hY;
            bottom = (int) (hY + rPathAShadowDis / 2);
        }
        gradientDrawable.setBounds(left, top, right, bottom);
        //裁剪出我们需要的区域
        Path mPath = new Path();
        mPath.moveTo(aX - Math.max(rPathAShadowDis, lPathAShadowDis) / 2, aY);
        mPath.lineTo(hX, hY);
        mPath.lineTo(aX, aY);
        mPath.close();
        canvas.clipPath(aPath);
        canvas.clipPath(mPath, Region.Op.INTERSECT);
        //canvas.drawPath(mPath,strokePaint);

        float mDegrees = (float) Math.toDegrees(Math.atan2(aY - hY, aX - hX));
        canvas.rotate(mDegrees, hX, hY);
        gradientDrawable.draw(canvas);
        canvas.restore();
    }

    //绘制投在B区域的阴影
    private void drawPathBShadow(Canvas canvas) {
        canvas.save();
        int deepOffset = 0;
        int lightOffset = 0;

        float aTof = (float) Math.hypot((aX - fX), (aY - fY));
        float viewDiagonalLength = (float) Math.hypot(screenWidth, screenHeight);

        int left;
        int right;
        int top = (int) cY;
        int bottom = (int) (viewDiagonalLength + top);

        GradientDrawable gradientDrawable;

        if (currentArea == TOP_RIGHT) {
            gradientDrawable = gradientDrawableBTop;

            left = (int) (cX - deepOffset);
            right = (int) (cX + aTof / 4 + lightOffset);
        } else {
            gradientDrawable = gradientDrawableBBottom;

            left = (int) (cX - aTof / 4 - lightOffset);
            right = (int) (cX + deepOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        float rotateDegrees = (float) Math.toDegrees(Math.atan2(eX - fX, hY - fY));
        canvas.rotate(rotateDegrees, cX, cY);
        gradientDrawable.draw(canvas);
        canvas.restore();
    }


    //绘制投在C区域的阴影
    private void drawPathCShadow(Canvas canvas) {
        canvas.save();
        int deepOffset = 1;//深色端的偏移值
        int lightOffset = -30;//浅色端的偏移值
        float viewDiagonalLength = (float) Math.hypot(screenWidth, screenHeight);//view对角线长度
        int midpoint_ce = (int) (cX + eX) / 2;//ce中点
        int midpoint_jh = (int) (jY + hY) / 2;//jh中点
        float minDisToControlPoint = Math.min(Math.abs(midpoint_ce - eX), Math.abs(midpoint_jh - hY));//中点到控制点的最小值
        int left;
        int right;
        int top = (int) cY;
        int bottom = (int) (viewDiagonalLength + cY);
        GradientDrawable gradientDrawable;
        if (currentArea == TOP_RIGHT) {
            gradientDrawable = gradientDrawableCTop;
            left = (int) (cX - lightOffset);
            right = (int) (cX + minDisToControlPoint + deepOffset);
        } else {
            gradientDrawable = gradientDrawableCBottom;
            left = (int) (cX - minDisToControlPoint - deepOffset);
            right = (int) (cX + lightOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);
        float mDegrees = (float) Math.toDegrees(Math.atan2(eX - fX, hY - fY));
        canvas.rotate(mDegrees, cX, cY);
        gradientDrawable.draw(canvas);
        canvas.restore();
    }

}

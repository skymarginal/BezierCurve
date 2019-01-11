package com.marginal.beziercurve.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by 你的样子 on 2018/12/8.
 */

public class WaterRippleView2 extends View {

    private int width;
    private int height;

    private int y;

    private int waveWidth;
    private int halfWaveWidth;

    private int controlX;

    private ValueAnimator animator;
    private int offset;

    Paint paint = new Paint();
    Path path = new Path();

    public WaterRippleView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        y = height / 2;
        waveWidth = width / 3;
        halfWaveWidth = waveWidth / 2;
        controlX = waveWidth / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        int startX = -width + offset;
        path.moveTo(startX, y);
        for (int i = 0; i < (width / halfWaveWidth) * 2; i++) {
            //每次画半个波浪
            path.rQuadTo(controlX, (isEvenNumber(i) ? 50 : -50), halfWaveWidth, 0);
        }

        path.lineTo(width,height);
        path.lineTo(0,height);
        path.close();

        canvas.drawPath(path, paint);
    }

    private boolean isEvenNumber(int a) {
        return a % 2 == 0;
    }

    public void startAnimation() {
        animator = ValueAnimator.ofInt(0, width);
        animator.setDuration(4000);
        animator.setInterpolator(new LinearInterpolator());
        //无限循环
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    public void endAnimation() {
        animator.cancel();
    }

}
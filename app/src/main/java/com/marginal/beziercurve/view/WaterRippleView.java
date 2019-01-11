package com.marginal.beziercurve.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 你的样子 on 2018/12/8.
 * 水波纹
 *
 * @author gerile
 */

public class WaterRippleView extends View {

    private int width;
    private int height;

    private int minWidth = 50;
    private int minHeight = 10;
    private List<Integer> widthList = new ArrayList<>();
    private List<Integer> heightList = new ArrayList<>();

    private ValueAnimator animator;
    private int dx;

    Paint paint = new Paint();
    Path path = new Path();

    public WaterRippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.GRAY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();

        while (minWidth < width) {
            minWidth += 50;
            widthList.add(minWidth);
            minHeight += 10;
            heightList.add(minHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.moveTo(0, height / 2);

        for (int i = 0; i < widthList.size(); i++) {
            int width = widthList.get(i);
            path.rQuadTo(width / 2, (i % 2 == 0 ? dx : -dx), width, 0);
        }

        canvas.drawPath(path, paint);
    }

    public void startAnimation() {
        animator = ValueAnimator.ofInt(-30, 30, -30);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        //无限循环
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    public void endAnimation() {
        animator.cancel();
    }


}

package com.marginal.beziercurve.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 你的样子 on 2018/12/8.
 * 可拖拽的线条
 *
 * @author gerile
 */

public class DragLineView extends View {

    private int width;
    private int height;

    float controlX;
    float controlY;

    Paint paint = new Paint();
    Path path = new Path();

    public DragLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        controlX = width / 2;
        controlY = height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.moveTo(0, height / 2);
        path.quadTo(controlX, controlY, width, height / 2);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                controlX = event.getX();
                controlY = event.getY();
                invalidate();
            default:
                break;
        }
        return true;
    }

}

package com.marginal.beziercurve.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 你的样子 on 2018/12/14.
 * 音频播放波浪线效果
 *
 * @author gerile
 */

public class AudioWaveView extends View {

    private final int DEFAULT_SAMPLING_SIZE = 64;
    private final float DEFAULT_OFFSET_SPEED = 290F;
    private final int DEFAULT_SENSIBILITY = 5;

    //采样点的数量，越高越精细，但是高于一定限度肉眼很难分辨，越高绘制效率越低
    private int samplingSize = DEFAULT_SAMPLING_SIZE;

    //控制向右偏移速度，越小偏移速度越快
    private float offsetSpeed = DEFAULT_OFFSET_SPEED;

    //平滑改变的音量值
    private float volume = 0;

    //用户设置的音量，[0,100]
    private int targetVolume = 50;

    //每次平滑改变的音量单元
    private float perVolume;

    //灵敏度，越大越灵敏[1,10]
    private int sensibility = DEFAULT_SENSIBILITY;

    //背景色
    private int backGroundColor = Color.WHITE;

    //波浪线颜色
    private int lineColor = Color.BLACK;
    //粗线宽度
    private int thickLineWidth = 6;
    //细线宽度
    private int fineLineWidth = 2;

    private final Paint paint = new Paint();

    {
        //防抖动
        paint.setDither(true);
        //抗锯齿，降低分辨率，提高绘制效率
        paint.setAntiAlias(true);
    }

    private List<Path> paths = new ArrayList<>();

    {
        for (int i = 0; i < 4; i++) {
            paths.add(new Path());
        }
    }

    //不同函数曲线系数
    private float[] pathFuncs = {
            0.6f, 0.35f, 0.1f, -0.1f
    };

    //采样点X坐标
    private float[] samplingX;
    //采样点位置映射到[-2,2]之间
    private float[] mapX;
    //画布宽高
    private int width, height;
    //画布中心的高度
    private int centerHeight;
    //振幅
    private float amplitude;
    //存储衰变系数
    private SparseArray<Double> recessionFuncs = new SparseArray<>();
    //连线动画结束标记
    private boolean isPrepareLineAnimEnd = false;
    //连线动画位移
    private int lineAnimX = 0;
    //渐入动画结束标记
    private boolean isPrepareAlphaAnimEnd = false;
    //渐入动画百分比值[0,1f]
    private float prepareAlpha = 0f;
    //是否开启准备动画
    private boolean isOpenPrepareAnim = false;

    public AudioWaveView(Context context) {
        super(context);
    }

    public AudioWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    int a = 4;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        a += 10;
        float offset = a / offsetSpeed;

        if (null == samplingX || null == mapX || null == pathFuncs) {
            initDraw(canvas);
        }

        if (lineAnim(canvas)) {
            resetPaths();
            softerChangeVolume();

            //波形函数的值
            float curY;
            for (int i = 0; i <= samplingSize; i++) {
                float x = samplingX[i];
                curY = (float) (amplitude * calcValue(mapX[i], offset));
                for (int n = 0; n < paths.size(); n++) {
                    //四条线分别乘以不同的函数系数
                    float realY = curY * pathFuncs[n] * volume * 0.01f;
                    paths.get(n).lineTo(x, centerHeight + realY);
                }
            }

            //连线至终点
            for (int i = 0; i < paths.size(); i++) {
                paths.get(i).moveTo(width, centerHeight);
            }

            //绘制曲线
            for (int n = 0; n < paths.size(); n++) {
                if (n == 0) {
                    paint.setStrokeWidth(thickLineWidth);
                    paint.setAlpha((int) (255 * alphaInAnim()));
                } else {
                    paint.setStrokeWidth(fineLineWidth);
                    paint.setAlpha((int) (100 * alphaInAnim()));
                }
                canvas.drawPath(paths.get(n), paint);
            }
        }
    }

    //初始化绘制参数
    private void initDraw(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();
        centerHeight = height >> 1;
        //振幅为高度的1/4
        amplitude = height / 3.0f;

        //适合View的理论最大音量值，和音量不属于同一概念
        perVolume = sensibility * 0.35f;

        //初始化采样点及映射
        //这里因为包括起点和终点，所以需要+1
        samplingX = new float[samplingSize + 1];
        mapX = new float[samplingSize + 1];
        //确定采样点之间的间距
        float gap = width / (float) samplingSize;
        //采样点的位置
        float x;
        for (int i = 0; i <= samplingSize; i++) {
            x = i * gap;
            samplingX[i] = x;
            //将采样点映射到[-2，2]
            mapX[i] = (x / (float) width) * 4 - 2;
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(lineColor);
        paint.setStrokeWidth(thickLineWidth);
    }

    /**
     * 连线动画
     *
     * @param canvas
     * @return whether animation is end
     */
    private boolean lineAnim(Canvas canvas) {
        if (isPrepareLineAnimEnd || !isOpenPrepareAnim) {
            return true;
        }
        paths.get(0).moveTo(0, centerHeight);
        paths.get(1).moveTo(width, centerHeight);

        for (int i = 1; i <= samplingSize; i++) {
            float x = i * lineAnimX / samplingSize;
            paths.get(0).lineTo(x, centerHeight);
            paths.get(1).lineTo(width - x, centerHeight);

        }

        paths.get(0).moveTo(width / 2, centerHeight);
        paths.get(1).moveTo(width / 2, centerHeight);

        lineAnimX += width / 60;
        canvas.drawPath(paths.get(0), paint);
        canvas.drawPath(paths.get(1), paint);

        if (lineAnimX > width / 2) {
            isPrepareLineAnimEnd = true;
            return true;
        }
        return false;
    }

    /**
     * 重置path
     */
    private void resetPaths() {
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).rewind();
            paths.get(i).moveTo(0, centerHeight);
        }
    }

    //初始化参数
    private void initParameters() {
        lineAnimX = 0;
        prepareAlpha = 0f;
        isPrepareLineAnimEnd = false;
        isPrepareAlphaAnimEnd = false;
        samplingX = null;
    }

    /**
     * 使曲线振幅有较大改变时动画过渡自然
     */
    private void softerChangeVolume() {
        //这里减去perVolume是为了防止volume频繁在targetVolume上下抖动
        if (volume < targetVolume - perVolume) {
            volume += perVolume;
        } else if (volume > targetVolume + perVolume) {
            if (volume < perVolume * 2) {
                volume = perVolume * 2;
            } else {
                volume -= perVolume;
            }
        } else {
            volume = targetVolume;
        }
    }

    /**
     * 计算波形函数中x对应的y值
     * <p>
     * 使用稀疏矩阵进行暂存计算好的衰减系数值，下次使用时直接查找，减少计算量
     *
     * @param mapX   换算到[-2,2]之间的x值
     * @param offset 偏移量
     * @return [-1, 1]
     */
    private double calcValue(float mapX, float offset) {
        int keyX = (int) (mapX * 1000);
        offset %= 2;
        double sinFunc = Math.sin(Math.PI * mapX - offset * Math.PI);
        double recessionFunc;
        if (recessionFuncs.indexOfKey(keyX) >= 0) {
            recessionFunc = recessionFuncs.get(keyX);
        } else {
            recessionFunc = 4 / (4 + Math.pow(mapX, 4));
            recessionFuncs.put(keyX, recessionFunc);
        }
        return sinFunc * recessionFunc;
    }

    /**
     * 渐入动画
     *
     * @return progress of animation
     */
    private float alphaInAnim() {
        if (!isOpenPrepareAnim) {
            return 1;
        }
        if (prepareAlpha < 1f) {
            prepareAlpha += 0.02f;
        } else {
            prepareAlpha = 1;
        }
        return prepareAlpha;
    }

    private ValueAnimator animator;
    private int offset;

    public void startAnimation() {
        animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(2000);
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

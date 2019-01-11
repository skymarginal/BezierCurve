package com.marginal.beziercurve.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.marginal.beziercurve.R;
import com.marginal.beziercurve.view.WaterRippleView;
import com.marginal.beziercurve.view.WaterRippleView2;
import com.marginal.beziercurve.view.WaveView3;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 你的样子 on 2019/1/11.
 * 水波浪线
 * @author gerile
 */

public class WaterRippleActivity extends AppCompatActivity {

    public static void startWaterRippleActivity(Context context) {
        context.startActivity(new Intent(context, WaterRippleActivity.class));
    }

    @BindView(R.id.water_ripple)
    WaveView3 rippleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_ripple);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    //    rippleView.startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
    //    rippleView.endAnimation();
    }
}

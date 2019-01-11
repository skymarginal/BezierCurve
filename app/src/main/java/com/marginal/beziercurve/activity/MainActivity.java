package com.marginal.beziercurve.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.marginal.beziercurve.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bezier_drag, R.id.bezier_water_ripple,R.id.bezier_audio_wave})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.bezier_drag:
                DragLineActivity.startDragLineActivity(this);
                break;
            case R.id.bezier_water_ripple:
                WaterRippleActivity.startWaterRippleActivity(this);
                break;
            case R.id.bezier_audio_wave:
                AudioWaveActivity.startAudioWaveActivity(this);
                break;
            default:
                break;
        }
    }

}

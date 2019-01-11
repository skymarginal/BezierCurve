package com.marginal.beziercurve.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.marginal.beziercurve.R;
import com.marginal.beziercurve.view.AudioWaveView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 你的样子 on 2019/1/11.
 * 音频波浪线
 *
 * @author geriel
 */

public class AudioWaveActivity extends Activity {

    public static void startAudioWaveActivity(Context context) {
        context.startActivity(new Intent(context, AudioWaveActivity.class));
    }

    @BindView(R.id.audio_wave)
    AudioWaveView audioWaveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_wave);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioWaveView.startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioWaveView.endAnimation();
    }
}

package com.marginal.beziercurve.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.marginal.beziercurve.R;

/**
 * Created by 你的样子 on 2019/1/11.
 * 可拖拽
 * @author gerile
 */

public class DragLineActivity extends AppCompatActivity {

    public static void startDragLineActivity(Context context) {
        context.startActivity(new Intent(context, DragLineActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
    }
}

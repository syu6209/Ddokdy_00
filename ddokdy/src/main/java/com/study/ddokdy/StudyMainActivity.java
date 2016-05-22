package com.study.ddokdy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

public class StudyMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_main);
        init();
        holdViews();
        setViews();
    }


    private void init() {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));

    }
    private void holdViews() {
    }

    private void setViews() {
    }
}

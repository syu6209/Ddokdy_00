package com.study.ddokdy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 23..
 */
public class Setup_Activity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        init();

    }

    public void init()
    {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_blackgray));

    }
}

package com.study.ddokdy;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

public class MakeNewStudyActivity extends Activity {
    private int data_openType;
    ImageView type_closed, type_opend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_study);
        init();
        holdViews();
        setViews();
    }

    private void init() {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
    }

    private void holdViews() {
        type_closed = (ImageView)findViewById(R.id.makestudy_iv_closed);
        type_opend = (ImageView)findViewById(R.id.makestudy_iv_opend);
    }

    private void setViews() {
        //초기값 공개
        data_openType = 1;
        type_closed.setVisibility(View.INVISIBLE);

        type_opend.setOnClickListener(new OpenTypeListener(1));
        type_closed.setOnClickListener(new OpenTypeListener(4));
    }
    private class OpenTypeListener implements View.OnClickListener{
        int openType;
        public OpenTypeListener(int openType){
            this.openType = openType;
        }
        @Override
        public void onClick(View v) {
            data_openType = openType;
            if(openType==1){
                type_closed.setVisibility(View.INVISIBLE);
                type_opend.setVisibility(View.VISIBLE);
            }else{
                type_closed.setVisibility(View.VISIBLE);
                type_opend.setVisibility(View.INVISIBLE);
            }
        }
    }

}

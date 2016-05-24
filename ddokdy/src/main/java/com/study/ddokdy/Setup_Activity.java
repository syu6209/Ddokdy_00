package com.study.ddokdy;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 23..
 */
public class Setup_Activity extends Activity {
    private static final String TAG = Setup_Activity.class.getSimpleName();


    RelativeLayout Check_Box_Position_Info_Btn,Check_Box_Notice_Setting_Btn,Cross_Mark_Btn;
    ImageView Check_Box_Notice_Setting_Image,Check_Box_Position_Info_Image;
    Boolean Check_Box_Notice_Setting_state ,Check_Box_Position_Info_state;
    TextView version_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        init();
        holdViews();
        setViews();
    }

    public void init()
    {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_blackgray));
        Check_Box_Notice_Setting_state = false;
        Check_Box_Position_Info_state = false;
    }
    private void holdViews() {
        Cross_Mark_Btn = (RelativeLayout)findViewById(R.id.Cross_Mark_Btn);
        version_text=(TextView)findViewById(R.id.version_text);
        Check_Box_Position_Info_Btn = (RelativeLayout) findViewById(R.id.Check_Box_Position_Info_Btn);
        Check_Box_Notice_Setting_Btn= (RelativeLayout) findViewById(R.id.Check_Box_Notice_Setting_Btn);
        Check_Box_Notice_Setting_Image = (ImageView) findViewById(R.id.Check_Box_Notice_Setting_Image);
        Check_Box_Position_Info_Image = (ImageView) findViewById(R.id.Check_Box_Position_Info_Image);


    }


    private void setViews() {

        Cross_Mark_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cross_Mark_Btn" );

                finish();
            }
        });

        if(Check_Box_Notice_Setting_state)
        {
            Drawable drawable = getResources().getDrawable(R.drawable.check_symbol_true);
            Check_Box_Notice_Setting_Image.setBackground(drawable);
        }
        if(Check_Box_Position_Info_state)
        {
            Drawable drawable = getResources().getDrawable(R.drawable.check_symbol_true);
            Check_Box_Position_Info_Image.setBackground(drawable);
        }
        Check_Box_Notice_Setting_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable;
                if(Check_Box_Notice_Setting_state)
                {
                    drawable= getResources().getDrawable(R.drawable.check_symbol_false);
                }
                else {
                    drawable = getResources().getDrawable(R.drawable.check_symbol_true);
                }
                Check_Box_Notice_Setting_Image.setBackground(drawable);
                Check_Box_Notice_Setting_state = !Check_Box_Notice_Setting_state;

            }
        });
        Check_Box_Position_Info_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable;
                if(Check_Box_Position_Info_state)
                {
                    drawable= getResources().getDrawable(R.drawable.check_symbol_false);
                }
                else {
                    drawable = getResources().getDrawable(R.drawable.check_symbol_true);
                }
                Check_Box_Position_Info_Image.setBackground(drawable);
                Check_Box_Position_Info_state = !Check_Box_Position_Info_state;

            }
        });
    }
}

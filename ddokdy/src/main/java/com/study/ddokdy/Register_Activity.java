package com.study.ddokdy;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 21..
 */
public class Register_Activity extends Activity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = Register_Activity.class.getSimpleName();
    Spinner Email_Spinner;
    RelativeLayout Whole_Check_Box_btn ,DDock_Check_Box_Btn    ,Personal_Info_Check_Box_btn,register_btn;
    ImageView     Whole_Check_Box_Image,DDock_Check_Box_Iamge,Personal_Info_Check_Box_Image;
    Boolean Whole_Check_State, DDock_Check_State,Personal_Info_Check_State;
    String[] ITEMS = {"email.com", "naver.com", "asdfasdfasdfasdfasdfasdfasdf 3", "Item 4", "Item 5", "Item 6","naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6","email", "naver.com", "Item 3", "Item 4", "Item 5", "Item 6","naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6","email", "naver.com", "Item 3", "Item 4", "Item 5", "Item 6","naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"
    };
    ArrayAdapter<String> arrayAdapter;

    Drawable check_true ;
    Drawable check_false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        holdViews();
        setViews();
    }

    private void init() {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spiner_item, ITEMS);
         Whole_Check_State=false;
        DDock_Check_State=false;
        Personal_Info_Check_State=false;
         check_true = getResources().getDrawable(R.drawable.check_box_true_orange);
         check_false = getResources().getDrawable(R.drawable.check_box_false);

    }
    private void holdViews() {

        Email_Spinner = (Spinner)findViewById(R.id.Email_Spinner);
        register_btn = (RelativeLayout)findViewById(R.id.register_btn);


        Whole_Check_Box_btn = (RelativeLayout)findViewById(R.id.Whole_Check_Box_btn);
        DDock_Check_Box_Btn= (RelativeLayout)findViewById(R.id.DDock_Check_Box_Btn);
        Personal_Info_Check_Box_btn= (RelativeLayout)findViewById(R.id.Personal_Info_Check_Box_btn);
        Whole_Check_Box_Image = (ImageView)findViewById(R.id.Whole_Check_Box_Image);
        DDock_Check_Box_Iamge= (ImageView)findViewById(R.id.DDock_Check_Box_Image);
        Personal_Info_Check_Box_Image= (ImageView)findViewById(R.id.Personal_Info_Check_Box_Image);

    }

    private void setViews() {
        register_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {

                    register_btn.setBackground(getResources().getDrawable(R.color.mint_dark));

                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    register_btn.setBackground(getResources().getDrawable(R.color.mint));

                }
                return true;
            }
        });
        Email_Spinner.setAdapter(arrayAdapter);
        Email_Spinner.setOnItemSelectedListener(this);
        Whole_Check_Box_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Whole_Check_State) {
                    DDock_Check_Box_Iamge.setBackground(check_false);
                    Personal_Info_Check_Box_Image.setBackground(check_false);
                    DDock_Check_State=false;
                    Personal_Info_Check_State =false;
                } else {
                    DDock_Check_Box_Iamge.setBackground(check_true);
                    Personal_Info_Check_Box_Image.setBackground(check_true);
                    DDock_Check_State=true;
                    Personal_Info_Check_State =true;
                }

                Whole_CheckBox();


            }
        });
        DDock_Check_Box_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(DDock_Check_State)
                {
                    DDock_Check_Box_Iamge.setBackground(check_false);

                }
                else {
                    DDock_Check_Box_Iamge.setBackground(check_true);
                }
                DDock_Check_State = !DDock_Check_State;
                Whole_CheckBox();




            }
        });

        Personal_Info_Check_Box_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Personal_Info_Check_State)
                {
                    Personal_Info_Check_Box_Image.setBackground(check_false);

                }
                else {
                    Personal_Info_Check_Box_Image.setBackground(check_true);
                }
                Personal_Info_Check_State = !Personal_Info_Check_State;
                Whole_CheckBox();


            }
        });
    }

    public void Whole_CheckBox()
    {
        if(DDock_Check_State&&Personal_Info_Check_State)
        {
            Whole_Check_State = true;
            Whole_Check_Box_Image.setBackground(check_true);
        }
        else
        {
            Whole_Check_State = false;
            Whole_Check_Box_Image.setBackground(check_false);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ((TextView)parent.getChildAt(0)).setTextColor(Color.rgb(96, 91, 85));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

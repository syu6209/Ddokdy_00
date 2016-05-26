package com.study.ddokdy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 24..
 */
public class StudyRoomActivity extends Activity implements  Fragment_StudyRoom_Member.StudyRoom_Listener,Fragment_StudyRoom_Member_Management.Fragment_StudyRoom_Member_Management_Listener{
    private static final String TAG = StudyRoomActivity.class.getSimpleName();
    int type;
    ImageButton StudyRoom_Circle_Btn,StudyRoom_Back_Btn;
    FragmentManager fragmentManager;
    TextView title_centerText;


    @Override
    public void On_Study_Room() {
        StudyRoom_Circle_Btn.setVisibility(View.VISIBLE);

    }


    @Override
    public void Call_StudyRoom_management_Date_View() {
        Fragment frag = new Fragment();


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.StudyRoom_Container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom);
        init();
        holdViews();
        setViews();

        Create_Fragment_view(type);

    }
    private void init() {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        fragmentManager= getFragmentManager();
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

    }
    private void holdViews() {
        StudyRoom_Circle_Btn = (ImageButton)findViewById(R.id.StudyRoom_Circle_Btn);
        StudyRoom_Back_Btn = (ImageButton)findViewById(R.id.topbar_btn_close);
        title_centerText = (TextView)findViewById(R.id.title_centerText);




    }
    private void setViews() {
        StudyRoom_Circle_Btn.setVisibility(View.GONE);
        title_centerText.setText(R.string.text_StudyRoom_activity_title_member);

        StudyRoom_Circle_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_centerText.setText(R.string.text_StudyRoom_activity_title_member_management);

                Fragment frag = new Fragment_StudyRoom_Member_Management();


                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.StudyRoom_Container, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                StudyRoom_Circle_Btn.setVisibility(View.GONE);

            }
        });

        StudyRoom_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }
        });


    }

    public void Create_Fragment_view(int type)
    {
        Fragment frag= new Fragment();


        switch(type)
        {
            case 0:
                //채팅
                break;
            case 1:
                // 과제
                break;
            case 2:
                // 일정
                break;
            case 3:
                // 멤버
                frag = new Fragment_StudyRoom_Member();
                break;
            case 4:
                // 설정
                break;
            default:
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.StudyRoom_Container, frag);
        fragmentTransaction.commit();

    }
}

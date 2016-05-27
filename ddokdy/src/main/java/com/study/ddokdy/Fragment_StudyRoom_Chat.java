package com.study.ddokdy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by ibyeongmu on 16. 5. 27..
 */
public class Fragment_StudyRoom_Chat extends android.app.Fragment{
    private static final String TAG =  Fragment_StudyRoom_Chat.class.getSimpleName();
    private static final int Anim_Speed = 200;
    int anim_height ;

    private Animation alphaAni;

    ImageView chat_option_btn;
    LinearLayout menu_box;
    ResizeWidthAnimation anim;
    boolean btn_flag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_studyroom_chat,container,false);


        setanim(v);



        init();
        holdViews(v);
        setViews(v);


        return v;
    }
    public void init()
    {
        anim_height = (int)((double) (getActivity().getResources().getDisplayMetrics().heightPixels) *0.0864);


    }
    public void holdViews(View v)
    {
        chat_option_btn = (ImageView)v.findViewById(R.id.chat_option_btn);
        menu_box = (LinearLayout)v.findViewById(R.id.menu_box);

    }
    public void setanim(View v)
    {
        menu_box = (LinearLayout)v.findViewById(R.id.menu_box);
        anim = new ResizeWidthAnimation(menu_box, 0);
        menu_box.startAnimation(anim);
    }
    public void setViews(View v)
    {

        chat_option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btn_flag) {
                    Log.d(TAG,"Asdas");
                    anim = new ResizeWidthAnimation(menu_box, anim_height);
                    anim.setDuration(Anim_Speed);
                    menu_box.startAnimation(anim);
                    chat_option_btn.setBackground(getResources().getDrawable(R.drawable.btn_cross_blue));

                }
                else
                {
                    Log.d(TAG,"Asdas");

                    anim = new ResizeWidthAnimation(menu_box, 0);
                    anim.setDuration(Anim_Speed);
                    menu_box.startAnimation(anim);
                    chat_option_btn.setBackground(getResources().getDrawable(R.drawable.btn_plus));


                }
                btn_flag=!btn_flag;


            }
        });

    }
    public void start_anim()
    {

//        ResizeWidthAnimation anim = new ResizeWidthAnimation(menu_box, 200);
//        anim.setDuration(1000);
//        menu_box.startAnimation(anim);

    }



    public class ResizeWidthAnimation extends Animation
    {
        private int mHeight;
        private int mStartHeight;
        private View mView;
        public ResizeWidthAnimation(View view, int width)
        {
            mView = view;
            mHeight = width;
            mStartHeight = view.getHeight();
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            int newWidth = mStartHeight + (int) ((mHeight - mStartHeight) * interpolatedTime);
            mView.getLayoutParams().height = newWidth;
            mView.requestLayout();
        }
        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight)
        {
            super.initialize(width, height, parentWidth, parentHeight);
        }
        @Override
        public boolean willChangeBounds()
        {
            return true;
        }
    }
}

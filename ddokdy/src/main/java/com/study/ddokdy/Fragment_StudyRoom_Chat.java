package com.study.ddokdy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by ibyeongmu on 16. 5. 27..
 */
public class Fragment_StudyRoom_Chat extends android.app.Fragment{
    private static final String TAG =  Fragment_StudyRoom_Chat.class.getSimpleName();
    private static final int Anim_Speed = 200;
    int anim_height ;
    PullToRefreshListView mListview;
    ArrayList<Member_data> mListData;

    ImageView chat_option_btn;
    LinearLayout menu_box;
    ResizeWidthAnimation anim;
    boolean btn_flag = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_studyroom_chat,container,false);
        init();
        holdViews(v);
        setanim(v);

        setViews(v);
        return v;
    }
    public void init()
    {
        anim_height = (int)((double) (getActivity().getResources().getDisplayMetrics().heightPixels) *0.0864);
        mListData = new ArrayList<Member_data>();
        mListData.add(new Member_data(1,"aaasdasdasd"));
        mListData.add(new Member_data(1,"aaasdasdasd"));
        mListData.add(new Member_data(1,"aaasdasdasd"));

        mListData.add(new Member_data(0,"aaasdasdasd"));
        mListData.add(new Member_data(0,"aaasdasdasd"));
        mListData.add(new Member_data(0,"aaasdasdasd"));
        mListData.add(new Member_data(0,"aaasdasdasd"));

    }
    public void holdViews(View v)
    {
        chat_option_btn = (ImageView)v.findViewById(R.id.chat_option_btn);
        menu_box = (LinearLayout)v.findViewById(R.id.menu_box);
        mListview = (PullToRefreshListView)v.findViewById(R.id.StudyRoom_Chat_PullToRefreshListView);

    }
    public void setanim(View v)
    {
        anim = new ResizeWidthAnimation(menu_box, 0);
        menu_box.startAnimation(anim);
    }
    public void setViews(View v)
    {
        ListViewAdapter adapter = new ListViewAdapter(getActivity());
        mListview.setAdapter(adapter);
        mListview.setScrollBarDefaultDelayBeforeFade(10);

        chat_option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btn_flag) {
                    anim = new ResizeWidthAnimation(menu_box, anim_height);
                    anim.setDuration(Anim_Speed);
                    menu_box.startAnimation(anim);
                    chat_option_btn.setBackground(getResources().getDrawable(R.drawable.btn_cross_blue));
                }
                else
                {
                    anim = new ResizeWidthAnimation(menu_box, 0);
                    anim.setDuration(Anim_Speed);
                    menu_box.startAnimation(anim);
                    chat_option_btn.setBackground(getResources().getDrawable(R.drawable.btn_plus));
                }
                btn_flag=!btn_flag;
            }
        });
    }
    private class Member_data{

        int who_flag;
        String text;

        public Member_data(int who_flag, String text) {
            this.who_flag = who_flag;
            this.text = text;
        }
    }
    private class ViewHolder{


        TextView chat_text;


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
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        public ListViewAdapter(Context mContext) {
            this.mContext = mContext;
        }
        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(position < mListData.size()){
                ViewHolder holder = null;
                Member_data mData;
                try{
                    mData = mListData.get(position);
                }catch(Exception e){
                    mData = new Member_data(1,"fail");
                    mData.text = "오류";
                }
                ViewGroup v = null;
                if(mData.who_flag ==1)
                {// 상대
                    convertView = inflater.inflate(R.layout.listbox_yourchat, v);
                }
                else
                {
                    convertView = inflater.inflate(R.layout.listbox_mychat, v);
                }
                if(convertView.getTag()==null){
                    holder = new ViewHolder();

                    holder.chat_text = (TextView)convertView.findViewById(R.id.listbox_chat_text);
                    convertView.setTag(holder);

                }else{
                    holder = (ViewHolder)convertView.getTag();
                }
                if(holder != null){

                    holder.chat_text.setText(mData.text);
//

                }
            }else{
                convertView = inflater.inflate(R.layout.listbox_mainplus, null);
                convertView.setTag(null);
                return convertView;
            }
            return convertView;
        }

    }
}

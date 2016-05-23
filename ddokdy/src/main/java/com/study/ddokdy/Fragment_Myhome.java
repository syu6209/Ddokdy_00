package com.study.ddokdy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 22..
 */
public class Fragment_Myhome extends android.support.v4.app.Fragment{

    TextView tv_topbar_title;
    RelativeLayout setup_text,emblem_text,notice_text;
    ImageButton btn_topbar_mypage;
    PullToRefreshListView mListview;
    ArrayList<StudyData> mListData;
    ViewPager viewpager;




    public Fragment_Myhome(ViewPager viewpager) {
        this.viewpager = viewpager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = null;
        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.fragment_main_myhome, v);
        init();
        holdViews(ll);
        setViews(ll);
        return ll;
    }

    private void init() {
        mListData = new ArrayList<StudyData>();

        StudyData object = new StudyData();

        object.title = "마이호모홈";
        object.subtitle = "5월 2일 경대 스터디룸 씨스페이스";
        object.bossname = "옥수진";
        object.status = "진행중";
        object.isMine = true;
        mListData.add(object);

        object = new StudyData();
        object.title = "어휴 노답";
        object.subtitle = "최대한 빠른 자살";
        object.bossname = "신한수";
        object.status = "가입대기";
        object.isMine = false;
        mListData.add(object);

        object = new StudyData();
        object.title = "님들 빨리 자살하져";
        object.subtitle = "최대한 빠른 자살";
        object.bossname = "신한수";
        object.status = "가입대기";
        object.isMine = false;
        mListData.add(object);

        object = new StudyData();
        object.title = "제목 입력 공간";
        object.subtitle = "최신 일정이나 과제가 입력되는 공간";
        object.bossname = "스터디장";
        object.status = "가입대기";
        object.isMine = false;
        mListData.add(object);
    }

    private void holdViews(LinearLayout ll) {
        tv_topbar_title = (TextView)ll.findViewById(R.id.topbar_leftText);
        btn_topbar_mypage = (ImageButton)ll.findViewById(R.id.topbar_btn_mypage);
        mListview = (PullToRefreshListView)ll.findViewById(R.id.ptr_listview);
        setup_text =(RelativeLayout)ll.findViewById(R.id.setup_text);
        emblem_text = (RelativeLayout)ll.findViewById(R.id.emblem_text);
        notice_text = (RelativeLayout)ll.findViewById(R.id.notice_text);
    }
    private void setViews(LinearLayout ll) {
        ListViewAdapter adapter = new ListViewAdapter(ll.getContext());
        mListview.setAdapter(adapter);
        mListview.setScrollBarDefaultDelayBeforeFade(10);
        setup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Setup_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tv_topbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);
            }
        });
        btn_topbar_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(2);
            }
        });
    }
    private class DetailClickListener implements View.OnClickListener{
        int position;
        public DetailClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            click_detail(position);
        }
    }
    private void click_detail(int position) {
        StudyData mData = mListData.get(position);
        ZMethod.toast(getContext(), mData.title + " 디테일 클릭");
    }
    private class StudyData{
        String title,subtitle,bossname,status;
        boolean isMine;
    }
    private class ViewHolder{
        ImageView iv_list_icon;
        TextView tv_list_title,tv_list_subtitle,tv_list_bossname,tv_list_status;
        ImageButton btn_list_detail;
    }


    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        public ListViewAdapter(Context mContext) {
            this.mContext = mContext;
        }
        @Override
        public int getCount() {
            return mListData.size()+1;
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
                StudyData mData;
                try{
                    mData = mListData.get(position);
                }catch(Exception e){
                    mData = new StudyData();
                    mData.title = "오류";
                }
                ViewGroup v = null;
                convertView = inflater.inflate(R.layout.listbox_mainstudy, v);

                if(convertView.getTag()==null){
                    holder = new ViewHolder();
                    holder.tv_list_title = (TextView)convertView.findViewById(R.id.listbox_study_title);
                    holder.tv_list_subtitle = (TextView)convertView.findViewById(R.id.listbox_study_subtitle);
                    holder.tv_list_bossname = (TextView)convertView.findViewById(R.id.listbox_study_bossname);
                    holder.tv_list_status = (TextView)convertView.findViewById(R.id.listbox_study_status);
                    holder.iv_list_icon = (ImageView)convertView.findViewById(R.id.listbox_icon_mystudy);
                    holder.btn_list_detail = (ImageButton)convertView.findViewById(R.id.listbox_btn_detail);
                    convertView.setTag(holder);

                }else{
                    holder = (ViewHolder)convertView.getTag();
                }
                if(holder != null){
                    holder.tv_list_title.setText(mData.title);
                    holder.tv_list_subtitle.setText(mData.subtitle);
                    holder.tv_list_bossname.setText(mData.bossname);
                    holder.tv_list_status.setText(mData.status);
                    if(mData.isMine){
                        holder.iv_list_icon.setVisibility(View.VISIBLE);
                    }else{
                        holder.iv_list_icon.setVisibility(View.GONE);
                    }
                    holder.btn_list_detail.setOnClickListener(new DetailClickListener(position));
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

package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

public class StudyMainActivity extends Activity {
    ZBadgeView zbv[] = new ZBadgeView[5];
    PullToRefreshListView mListview;
    ArrayList<StudyPostData> mListData;
    ZMethod zmethod = new ZMethod();
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
        mListData = new ArrayList<StudyPostData>();
        StudyPostData mData = new StudyPostData();
        mData.idx="0";
        mData.content = "좋은 자료가 있어 공유해요~\nhttp://naver.com/good";
        mData.like = 0;
        mData.reply = 0;
        mData.name = "김명희";
        mData.time = "2016-05-24 03:18:12";
        mData.isMine = false;

        mListData.add(mData);

        mData = new StudyPostData();
        mData.idx="0";
        mData.content = "오늘 같은 날.. 술한잔 어때요??\n경대 앞에 좋은 술집 아는데~";
        mData.like = 0;
        mData.reply = 0;
        mData.name = "옥수진";
        mData.time = "2016-05-23 03:18:12";
        mData.isMine = false;

        mListData.add(mData);

        mData = new StudyPostData();
        mData.idx="0";
        mData.content = "님들 빨리 자살하죠";
        mData.like = 0;
        mData.reply = 0;
        mData.name = "신한수";
        mData.time = "2016-05-23 07:00:22";
        mData.isMine = false;

        mListData.add(mData);

    }
    private void holdViews() {
        zbv[0] = (ZBadgeView)findViewById(R.id.studyMain_btn_chat);
        zbv[1] = (ZBadgeView)findViewById(R.id.studyMain_btn_homework);
        zbv[2] = (ZBadgeView)findViewById(R.id.studyMain_btn_schedule);
        zbv[3] = (ZBadgeView)findViewById(R.id.studyMain_btn_member);
        zbv[4] = (ZBadgeView)findViewById(R.id.studyMain_btn_setting);
        mListview = (PullToRefreshListView)findViewById(R.id.ptr_listview2);
    }

    private void setViews() {
        zbv[0].setText("채팅");
        zbv[1].setText("과제");
        zbv[2].setText("일정");
        zbv[3].setText("멤버");
        zbv[4].setText("설정");

        zbv[0].setCountNum(1);
        zbv[1].setCountNum(0);
        zbv[2].setCountNum(0);
        zbv[3].setCountNum(0);
        zbv[4].setCountNum(0);
        for(int i=0;i<5;i++){
            zbv[i].setOnClickListener(new ZbadgeViewClickListener(i));
        }
        ListViewAdapter adapter = new ListViewAdapter(this);
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click_post(position-1);
            }
        });
    }

    private void click_reply(int position){
        StudyPostData mData = mListData.get(position);
        ZMethod.toast(getApplication(), mData.name+" 댓글쓰기 클릭");
    }
    private void click_like(int position){
        StudyPostData mData = mListData.get(position);
        ZMethod.toast(getApplication(), mData.name+" 좋아요 클릭");
    }
    private void click_post(int position){
        StudyPostData mData = mListData.get(position);
        ZMethod.toast(getApplication(), mData.name+" 포스트 클릭");
    }
    private void click_detail(int position) {
        StudyPostData mData = mListData.get(position);
        ZMethod.toast(getApplication(), mData.name+" 디테일 클릭");
    }
    private class ZbadgeViewClickListener implements View.OnClickListener{
        int num;
        public ZbadgeViewClickListener(int num){
            this.num = num;
        }
        @Override
        public void onClick(View v) {
//        Intent intent = new Intent(getApplication(), StudyRoom.class);
//        intent.putExtra("type", num);
//        startActivity(intent);
//        StudyRoom 클래스는 액티비티고 그안에서 프레그먼트로 각각 채팅, 과제, 일정 들을 그리기
        }
    }



    private class InnerBtnClickListener implements View.OnClickListener{
        int type, position;
        public InnerBtnClickListener(int type, int position) {
            this.type = type;
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            switch (type){
                case 1:
                    click_detail(position);
                    break;
                case 2:
                    click_like(position);
                    break;
                case 3:
                    click_reply(position);
                    break;
            }

        }
    }
    private class StudyPostData{
        String idx;
        String name,time,content;
        int like,reply;
        boolean isMine;
    }
    private class ViewHolder{
        ImageView iv_list_icon;
        TextView tv_list_name,tv_list_time,tv_list_content;
        ImageButton btn_list_detail;
        Button btn_list_like, btn_list_reply;
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
                StudyPostData mData;
                try{
                    mData = mListData.get(position);
                }catch(Exception e){
                    mData = new StudyPostData();
                    mData.name = "오류";
                }
                ViewGroup v = null;
                convertView = inflater.inflate(R.layout.listbox_studypost, v);

                if(convertView.getTag()==null){
                    holder = new ViewHolder();
                    holder.tv_list_name = (TextView)convertView.findViewById(R.id.listbox_studypost_name);
                    holder.tv_list_time = (TextView)convertView.findViewById(R.id.listbox_studypost_time);
                    holder.tv_list_content = (TextView)convertView.findViewById(R.id.listbox_studypost_content);
                    holder.btn_list_detail = (ImageButton)convertView.findViewById(R.id.listbox_btn_detail);
                    holder.btn_list_like = (Button)convertView.findViewById(R.id.listbox_studypost_btn_like);
                    holder.btn_list_reply = (Button)convertView.findViewById(R.id.listbox_studypost_btn_reply);
                    convertView.setTag(holder);

                }else{
                    holder = (ViewHolder)convertView.getTag();
                }
                if(holder != null){
                    holder.tv_list_name.setText(mData.name);
                    holder.tv_list_time.setText(zmethod.getTimeString(mData.time));
                    holder.tv_list_content.setText(mData.content);

                    holder.btn_list_detail.setOnClickListener(new InnerBtnClickListener(1, position));
                    holder.btn_list_detail.setFocusable(false);
                    holder.btn_list_like.setOnClickListener(new InnerBtnClickListener(2, position));
                    holder.btn_list_like.setFocusable(false);
                    holder.btn_list_reply.setOnClickListener(new InnerBtnClickListener(3, position));
                    holder.btn_list_reply.setFocusable(false);
                }
            }else{
                convertView = inflater.inflate(R.layout.listbox_mainplus, null);
                convertView.setTag(null);
                convertView.setVisibility(View.INVISIBLE);
                return convertView;
            }
            convertView.setVisibility(View.VISIBLE);
            return convertView;
        }

    }
}

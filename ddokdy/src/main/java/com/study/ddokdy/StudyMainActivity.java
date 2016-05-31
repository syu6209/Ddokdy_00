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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZLoginInfo;
import prv.zozi.utils.ZMethod;

public class StudyMainActivity extends Activity {
    private Info_Login loginInfo;

    ZBadgeView zbv[] = new ZBadgeView[5];
    PullToRefreshListView mListview;
    ListViewAdapter adapter;
    ArrayList<StudyPostData> mListData;
    ZMethod zmethod = new ZMethod();

    private TextView tv_title, tv_subtitle, tv_bossname, tv_noticeDate, tv_noticeContent;
    private EditText et_board_input;
    private Button btn_board_submit;
    private int board_pivot_idx_first, board_pivot_idx_last;
    private int data_idx, data_is_open, data_member_curr, data_member_max, data_master_id, data_noti_idx,data_noti_type;
    private String data_title,data_category,data_mname,data_noti_title, data_noti_content, data_noti_registered,data_noti_date;
    private String url_send = "study_board_write.php";
    private String url_getmaininfo = "study_getmaininfo.php";
    private String url_getboardlist = "study_board_list.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_main);
        Intent intent = getIntent();
        data_idx = intent.getIntExtra("idx", 0);
        if(data_idx==0){
            ZMethod.toast(this, "인덱스 정보가 없습니다.");
            finish();
            return;
        }
        init();
        holdViews();
        setViews();
        loadStudyData();
        loadBoardData();
    }

    private void loadStudyData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String postData ="idx="+data_idx;
                String result = ZMethod.getStringHttpPost(Config.url_home+url_getmaininfo, postData);
                //System.out.println("결과 = "+ result);
                if(result!=null){
                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);
                        int rc = json.getInt("resultCode");

                        if(rc==0){
                            data_title = json.getString("title");
                            data_category = json.getString("category");
                            data_mname = json.getString("master_name");
                            data_noti_title = json.getString("noti_title");
                            data_noti_date = json.getString("noti_date");
                            data_noti_content = json.getString("noti_content");
                            data_noti_registered = json.getString("noti_registered");

                            data_is_open = json.getInt("isopen");
                            data_member_curr = json.getInt("member_curr");
                            data_member_max = json.getInt("member_max");
                            data_master_id = json.getInt("master_id");
                            data_noti_idx = json.getInt("noti_idx");
                            data_noti_type = json.getInt("noti_type");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataSet_study();
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ZMethod.toast(StudyMainActivity.this, "스터디룸 정보를 찾을 수 없습니다.");
                                    StudyMainActivity.this.finish();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void dataSet_study() {
        tv_title.setText(data_title);
        tv_subtitle.setText(data_category);
        tv_bossname.setText(data_mname);
        try {
            String[] noti_date = data_noti_date.split("-");
            tv_noticeDate.setText(noti_date[1] + "월" + noti_date[2] + "일");
        }catch (Exception e){
            tv_noticeDate.setText(data_noti_date);
        }

        tv_noticeContent.setText(data_noti_content);
    }
    private void loadBoardData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String postData ="idx="+data_idx;
                String result = ZMethod.getStringHttpPost(Config.url_home+url_getboardlist, postData);
                //System.out.println("값 : " + result);
                mListData.clear();
                if(result!=null) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);
                        if(json.getInt("resultCode")==0){
                            StudyPostData mData = null;
                            int rn = json.getInt("rn");
                            JSONArray jsarr = json.getJSONArray("data");
                            for(int i=0;i<rn;i++){
                                json = jsarr.getJSONObject(i);
                                mData = new StudyPostData();
                                mData.idx = json.getInt("idx");
                                mData.sr_idx = json.getInt("sr_idx");
                                mData.uid = json.getInt("uid");
                                mData.name = json.getString("name");
                                mData.title = json.getString("name");
                                mData.content = json.getString("content");
                                mData.time = json.getString("time");
                                mData.hit = json.getInt("hit");
                                mData.like = json.getInt("like");

                                mListData.add(mData);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataSet_board(true);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataSet_board(false);
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void dataSet_board(boolean b) {
        if(b){

        }else{

        }
        mListview.onRefreshComplete();
        adapter.notifyDataSetChanged();
    }


    private void init() {
        loginInfo = new ZLoginInfo(this).getLoginInfo();
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        mListData = new ArrayList<StudyPostData>();
        /*
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
        */
    }
    private void holdViews() {
        zbv[0] = (ZBadgeView)findViewById(R.id.studyMain_btn_chat);
        zbv[1] = (ZBadgeView)findViewById(R.id.studyMain_btn_homework);
        zbv[2] = (ZBadgeView)findViewById(R.id.studyMain_btn_schedule);
        zbv[3] = (ZBadgeView)findViewById(R.id.studyMain_btn_member);
        zbv[4] = (ZBadgeView)findViewById(R.id.studyMain_btn_setting);
        mListview = (PullToRefreshListView)findViewById(R.id.ptr_listview2);

        tv_title = (TextView)findViewById(R.id.topbar_tv_title);
        tv_subtitle = (TextView)findViewById(R.id.topbar_tv_subtitle);
        tv_bossname = (TextView)findViewById(R.id.topbar_tv_bossname);
        tv_noticeDate = (TextView)findViewById(R.id.studyMain_tv_noticeDate);
        tv_noticeContent = (TextView)findViewById(R.id.studyMain_tv_noticeContent);

        et_board_input = (EditText)findViewById(R.id.studyMain_et_board);
        btn_board_submit = (Button)findViewById(R.id.btn_submit);
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
        adapter = new ListViewAdapter(this);
        mListview.setAdapter(adapter);
        mListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadBoardData();
            }
        });
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click_post(position-1);
            }
        });

        btn_board_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_submit();
            }
        });
    }
    private void board_submit(){
        if(et_board_input.getText().length()==0){
            ZMethod.toast(this, "내용을 입력해 주세요!");
            return;
        }
        final String data = et_board_input.getText().toString();
        et_board_input.setText("업로드 중입니다...");
        et_board_input.setEnabled(false);
        et_board_input.setFocusable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mdata = data;
                try {
                    mdata = URLEncoder.encode(data,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> data_set = new HashMap<String, String>();
                data_set.put("uid", loginInfo.user_id);
                data_set.put("sr_idx", data_idx+"");
                data_set.put("type", "txt");
                data_set.put("name", "0");
                data_set.put("content", mdata);

                String postData = ZMethod.Map_to_String(data_set);
                String result = ZMethod.getStringHttpPost(Config.url_home+url_send, postData);


                try {
                    JSONObject json = new JSONObject(result);
                    if(json.getInt("resultCode")==0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                send_complete(true);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                send_complete(false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            send_complete(false);
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void send_complete(boolean b) {
        if(b){
            ZMethod.toast(this, "글이 등록되었습니다.");
        }else{
            ZMethod.toast(this, "글이 등록중 오류가 발생하였습니다.");
        }
        et_board_input.setEnabled(false);
        et_board_input.setFocusable(false);
        et_board_input.setText("");
        loadBoardData();
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
        Intent intent = new Intent(getApplication(), StudyRoomActivity.class);
            intent.putExtra("type", num);
            startActivity(intent);
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
        String title, name,time,content;
        boolean isMine;
        public int idx, sr_idx, uid, hit,like, reply;
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

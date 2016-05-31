package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZLoginInfo;
import prv.zozi.utils.ZMethod;

public class SearchActivity extends Activity {
    private Info_Login logininfo;
    private ImageButton btn_back, btn_modify;
    private TextView tv_keytitle, tv_keysubtitle, tv_rn;
    private RelativeLayout key_background;

    private PullToRefreshListView mListview;
    private ArrayList<StudyData> mListData;
    private ListViewAdapter adapter;

    private int data_idx, data_background, data_rn;
    private String data_keytitle, data_keysub;
    private String url_search = "keyword_search.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        data_idx = intent.getIntExtra("idx", 0);
        data_background = intent.getIntExtra("background", 0);
        data_keytitle = intent.getStringExtra("title");
        data_keysub = intent.getStringExtra("subtitle");

        if(data_idx==0){
            ZMethod.toast(this, "맞춤스터디 정보를 찾을 수 없습니다.");
            finish();
            return;
        }
        init();
        holdViews();
        setViews();
        loadData();
    }



    private void init() {
        logininfo = new ZLoginInfo(this).getLoginInfo();
        mListData = new ArrayList<StudyData>();
    }
    private void holdViews() {
        btn_back = (ImageButton)findViewById(R.id.topbar_btn_return);
        btn_modify = (ImageButton)findViewById(R.id.topbar_btn_modify);
        tv_keytitle = (TextView)findViewById(R.id.listbox_study_title);
        tv_keysubtitle = (TextView)findViewById(R.id.listbox_study_bossname);
        tv_rn = (TextView)findViewById(R.id.listbox_study_status);
        key_background = (RelativeLayout)findViewById(R.id.listbox_background);
        mListview = (PullToRefreshListView)findViewById(R.id.ptr_listview);
    }
    private void setViews() {
        adapter = new ListViewAdapter(this);
        mListview.setAdapter(adapter);
        mListview.setScrollingWhileRefreshingEnabled(false);
        mListview.setScrollBarDefaultDelayBeforeFade(10);
        mListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click_study(position-1);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
        tv_keytitle.setText(data_keytitle);
        tv_keysubtitle.setText(data_keysub);
        ZMethod.setBoxBackground(key_background, data_background);
    }

    private void modify() {

    }

    private void loadData(){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                mListData.clear();
            }

            @Override
            protected String doInBackground(String... params) {
                String postData = "idx=" + data_idx;
                String result =  ZMethod.getStringHttpPost(Config.url_home+url_search, postData);

                try {
                    JSONObject json = new JSONObject(result);
                    int rn = json.getInt("rn");
                    data_rn = rn;
                    data_keytitle = json.getString("name");
                    data_keysub = json.getString("subtitle");
                    data_background = json.getInt("background");

                    JSONArray jsarr = json.getJSONArray("data");
                    StudyData object = null;
                    for(int i=0;i<rn;i++){
                        json = jsarr.getJSONObject(i);
                        object = new StudyData();
                        object.idx = json.getInt("idx");
                        object.title = json.getString("name");
                        object.subtitle = json.getString("location")+"/"+json.getString("category");
                        object.background = json.getInt("background");
                        object.curr = json.getInt("member_curr");
                        object.max = json.getInt("member_max");

                        mListData.add(object);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "fail";
                }

                return "ok";
            }

            @Override
            protected void onPostExecute(String s) {
                if(Config.pdialog!=null && Config.pdialog.isShowing()){
                    Config.pdialog.dismiss();
                }
                loadComplete();
            }
        };
        task.execute(null,null,null);
    }

    private void loadComplete() {
        adapter.notifyDataSetChanged();
        mListview.onRefreshComplete();
        tv_keytitle.setText(data_keytitle);
        tv_keysubtitle.setText(data_keysub);
        tv_rn.setText("검색결과 : "+data_rn+"건");
        ZMethod.setBoxBackground(key_background, data_background);
    }


    private void click_study(int position) {

        Intent intent = null;
        if(position<mListData.size()) {
            StudyData mData = mListData.get(position);
            intent = new Intent(this, StudyMainActivity.class);
            intent.putExtra("idx", mData.idx);
            startActivity(intent);
        }else{
            intent = new Intent(this, MakeNewStudyActivity.class);
            startActivity(intent);
        }
    }
    private void click_detail(int position) {
        StudyData mData = mListData.get(position);
        ZMethod.toast(this, mData.title+" 디테일 클릭");
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
    private class StudyData{
        int idx;
        String title,subtitle,bossname,status;
        int background, curr, max;
        boolean isMine;
    }
    private class ViewHolder{
        ImageView iv_list_icon;
        TextView tv_list_title,tv_list_subtitle,tv_list_bossname,tv_list_status;
        ImageButton btn_list_detail;
        RelativeLayout background;
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
                convertView = inflater.inflate(R.layout.listbox_mainstudy_bigstyle, v);

                if(convertView.getTag()==null){
                    holder = new ViewHolder();
                    holder.tv_list_title = (TextView)convertView.findViewById(R.id.listbox_study_title);
                    holder.tv_list_subtitle = (TextView)convertView.findViewById(R.id.listbox_study_subtitle);
                    holder.tv_list_bossname = (TextView)convertView.findViewById(R.id.listbox_study_bossname);
                    holder.tv_list_status = (TextView)convertView.findViewById(R.id.listbox_study_status);
                    holder.iv_list_icon = (ImageView)convertView.findViewById(R.id.listbox_icon_mystudy);
                    holder.btn_list_detail = (ImageButton)convertView.findViewById(R.id.listbox_btn_detail);
                    holder.background = (RelativeLayout)convertView.findViewById(R.id.listbox_background);
                    convertView.setTag(holder);

                }else{
                    holder = (ViewHolder)convertView.getTag();
                }
                if(holder != null){
                    holder.tv_list_title.setText(mData.title);
                    holder.tv_list_subtitle.setText(mData.subtitle);
                    holder.tv_list_bossname.setText(mData.subtitle);
                    holder.tv_list_status.setText(mData.curr+"명 /"+mData.max+"명");

                    holder.iv_list_icon.setVisibility(View.GONE);

                    holder.btn_list_detail.setOnClickListener(new DetailClickListener(position));
                    holder.btn_list_detail.setFocusable(false);
                    ZMethod.setBoxBackground(holder.background, mData.background);
                }
            }else{
                convertView = inflater.inflate(R.layout.listbox_mainplus, null);
                convertView.setTag(null);
                return convertView;
            }
            return convertView;
        }

    }//end of ListViewAdapter Class
}

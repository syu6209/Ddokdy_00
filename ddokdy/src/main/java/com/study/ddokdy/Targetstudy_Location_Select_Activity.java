package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 31..
 */
public class Targetstudy_Location_Select_Activity extends Activity {
    private final static String TAG = Targetstudy_Location_Select_Activity.class.getSimpleName();



    PullToRefreshListView mListview;

    ImageButton topbar_btn_close,StudyRoom_Check_Btn;
    ArrayList<location_data> mListData;
    ListViewAdapter adapter;
    TextView select_text;

    String select_name ;
    int loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targetstudy_location_select);
        init();
        holdView();
        setView();


    }

    public void init()
    {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        mListData = new ArrayList<location_data>();
        get_category_list("info_location","0");

    }
    public void holdView()
    {
        topbar_btn_close  =(ImageButton)findViewById(R.id.topbar_btn_close);
        StudyRoom_Check_Btn = (ImageButton)findViewById(R.id.StudyRoom_Check_Btn);

        mListview = (PullToRefreshListView)findViewById(R.id.target_location_list);
        select_text = (TextView)findViewById(R.id.select_text);


    }


    public void setView()
    {

         adapter = new ListViewAdapter(getApplicationContext());
        mListview.setAdapter(adapter);
        mListview.setScrollBarDefaultDelayBeforeFade(10);


        topbar_btn_close.setOnClickListener(new top_btn_listenr(0));
        StudyRoom_Check_Btn.setOnClickListener(new top_btn_listenr(1));

    }



    public class top_btn_listenr implements View.OnClickListener
    {
        int btn_num;

        public top_btn_listenr(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            if(btn_num ==0)
            {
                finish();

            }
            else if (btn_num ==1)
            {
                // 확인 버튼
                exit_location ();


            }

        }
    }
    private class ViewHolder{


        TextView chat_text;



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
                location_data mData;
                try{
                    mData = mListData.get(position);
                }catch(Exception e){
                    mData = new location_data("fail",1);
                    mData.name = "오류";
                }
                ViewGroup v = null;

                convertView = inflater.inflate(R.layout.listbox_location_select, v);

                if(convertView.getTag()==null){
                    holder = new ViewHolder();

                    holder.chat_text = (TextView)convertView.findViewById(R.id.location_name);
                    convertView.setOnClickListener(new location_click(position));
                    convertView.setTag(holder);

                }else{
                    holder = (ViewHolder)convertView.getTag();
                }
                if(holder != null){

                    holder.chat_text.setText(mData.name);
                }
            }else{
                convertView = inflater.inflate(R.layout.listbox_mainplus, null);
                convertView.setTag(null);
                return convertView;
            }
            return convertView;
        }

    }

    class location_data {
        String name;
        int idx;


        public location_data(String name, int idx) {
            this.name = name;
            this.idx = idx;
        }
    }
    public class location_click implements View.OnClickListener
    {
        int btn_idx;

        public location_click(int btn_idx) {
            this.btn_idx = btn_idx;
        }

        @Override
        public void onClick(View v) {

            Log.d(TAG, "  mListData .name == " + mListData.get(btn_idx).name + "mListData.idx == " + mListData.get(btn_idx).idx);

            select_name = mListData.get(btn_idx).name;

            loc = mListData.get(btn_idx).idx;

            if(select_text.getText().toString().equals("스터디 지역") )
            {
                get_category_list("info_location", String.valueOf(mListData.get(btn_idx).idx));
            }
            else
            {


            }


            select_text.setText(mListData.get(btn_idx).name);



//

        }
    }
    public void exit_location ()
    {
        Intent intent = new Intent();
        intent.putExtra("select_name", select_name);
        intent.putExtra("loc", loc);
        setResult(Config.Targetstudy_Location_Select_Activity_RESULT_CODE, intent);
        finish();
    }

    public void get_category_list(final String type, final String code) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> data_set = new HashMap<String, String>();
                data_set.put("type", type);
                data_set.put("code", code);

                String mdata = ZMethod.getStringHttpPost(Config.url_home + "get_infoList.php", ZMethod.Map_to_String(data_set));
                try {
                    JSONObject jobj = new JSONObject(mdata);
                    String rn = jobj.getString("rn");
                    JSONArray Jarray_data = jobj.getJSONArray("data");

                    mListData.clear();

                        for (int i = 0; i < Integer.parseInt(rn); i++) {
                            JSONObject job = Jarray_data.getJSONObject(i);
                            mListData.add(new location_data(job.getString("name"), Integer.parseInt(job.getString("idx"))));
                        }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (mListData.size() !=0) {
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }





}

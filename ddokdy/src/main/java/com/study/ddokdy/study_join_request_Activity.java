package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZLoginInfo;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 6. 1..
 */
public class study_join_request_Activity extends Activity {
    private static final String TAG =study_join_request_Activity.class.getSimpleName();
    private Info_Login logininfo;
    String id_x ,title,address,period_stx,period_etx,weekint,kor_week,w_int,member_curr,member_max,introduce,weektime,cond_num;













    RelativeLayout join_btn;


    LayoutInflater inflater;

    int idx;
    LinearLayout three_option_container;
    ArrayList<String >  option_content ;
    String default_week[]  ={"월","화","수","목","금","토","일"};

    int option_flag [] = {0,0,0};

    TextView period_TextView,location_TextView,group_time_TextView,mem_cnt_TextView,group_intro_TextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_join_request);

        init();
        holdView();
        setView();

    }
    public void init() {
        logininfo = new ZLoginInfo(getApplicationContext()).getLoginInfo();
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));

        Intent intent = getIntent();
         idx = intent.getExtras().getInt("idx");
        get_study_info();
        option_content = new  ArrayList<String>();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        three_option_container = (LinearLayout)findViewById(R.id.three_option_container);

    }

    public void holdView() {

        join_btn = (RelativeLayout)findViewById(R.id.join_btn);

        period_TextView=(TextView)findViewById(R.id.period_TextView);
        location_TextView = (TextView)findViewById(R.id.location_TextView);
        group_time_TextView =(TextView)findViewById(R.id.group_time_TextView);
        mem_cnt_TextView =(TextView)findViewById(R.id.mem_cnt_TextView);
        group_intro_TextView=(TextView)findViewById(R.id.group_intro_TextView);






    }

    public void setView() {


        join_btn.setOnClickListener(new join_listner());

    }

    public class join_listner implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    HashMap<String,String> map = new HashMap<String, String>();

                    map.put("sr_idx",String.valueOf(idx));
                    map.put("uid",logininfo.user_id);
                    map.put("type", "10");
                    int cond =0;



                    for (int i =0;i<option_flag.length;i++)
                    {
                        if(option_flag[i]==1)
                        {
                            cond = (int) (cond + Math.pow(2, i));

                        }
                    }
                    map.put("cond", String.valueOf(cond));



                   String data = ZMethod.getStringHttpPost(Config.url_home+"study_join.php",ZMethod.Map_to_String(map));

                    Log.d(TAG," ZMethod.Map_to_String(map) == "+ZMethod.Map_to_String(map));

                    Log.d(TAG,"data == "+data);
                    if (data != null)
                    {
                        try {
                            JSONObject  jobg = new JSONObject(data);
                            String   resultCode =  jobg.getString("resultCode");
                            String   msg =  jobg.getString("msg");

                            Log.d(TAG,"resultCode  ==  "+resultCode);

                            Log.d(TAG,"msg  ==  "+msg);
                            if(resultCode.equals("0"))
                            {
                                finish();
                                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"실패 "+msg,Toast.LENGTH_SHORT).show();

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }).start();

        }
    }
    public void add_option_view()
    {

        for(int i=0;i<option_content.size();i++) {
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

            LinearLayout optionView = (LinearLayout) inflater.inflate(R.layout.join_option_circle_view, null);
            TextView text = (TextView) optionView.findViewById(R.id.option_view_text);

            optionView.setOnClickListener(new optionView_click_listenr(i));

            text.setText(option_content.get(i));

            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams((dm.widthPixels / 4), (dm.widthPixels/4));

            params.setMargins(20, 20, 20, 20);

            optionView.setLayoutParams(params);

            three_option_container.addView(optionView);
        }

    }
    public class optionView_click_listenr implements View.OnClickListener
    {
        int btn_num;

        public optionView_click_listenr(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {

            ImageView view = (ImageView) v.findViewById(R.id.option_view_Image);
            if(option_flag[btn_num] ==1 )
            {
                view.setBackground(getResources().getDrawable(R.drawable.circle_gray));
                option_flag[btn_num]=0;
            }
            else if(option_flag[btn_num]==0)
            {
                view.setBackground(getResources().getDrawable(R.drawable.circle_check_mint));
                option_flag[btn_num]=1;

            }


        }
    }


    public void get_study_info()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("idx", String.valueOf(idx));



                String data =ZMethod.getStringHttpPost(Config.url_home+"study_getdetail.php",ZMethod.Map_to_String(map));

                Log.d(TAG,""+Config.url_home+"study_getdetail.php"+"&"+ZMethod.Map_to_String(map));

                Log.d(TAG, "data == "+data);

                if( data != null)
                {
                    try {
                        JSONObject jobj  = new JSONObject(data);
                        String resultCode = jobj.getString("resultCode");
                        if( resultCode.equals("0")) {
                             id_x = jobj.getString("idx");

                             title = jobj.getString("title");
                             address = jobj.getString("address");

                             period_stx = jobj.getString("period_stx");
                             period_etx = jobj.getString("period_etx");

                             weekint = jobj.getString("weekint");


                             w_int  = Integer.toBinaryString(Integer.parseInt(weekint));

                            Log.d(TAG, "w_int == " + w_int);

                             kor_week ="";


                            for (int i=w_int.length()-1 ;i>=0;i--)
                            {
                                if(w_int.charAt(i) == '1')
                                {
                                    kor_week =kor_week + " "+default_week[i];
                                }

                            }
                             member_curr = jobj.getString("member_curr");
                             member_max = jobj.getString("member_max");
                             introduce = jobj.getString("introduce");

                             weektime = jobj.getString("weektime");


                             cond_num = jobj.getString("cond_num");
                            Log.d(TAG, "cond_num == " + cond_num);

//
                            for(int i=0;i<Integer.parseInt(cond_num);i++)
                            {
                                option_content.add(jobj.getString("cond" + i));




                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    location_TextView.setText(address);

                                    mem_cnt_TextView.setText(member_curr+"명 / "+member_max+" 명");

                                    group_intro_TextView.setText(introduce);
                                    group_time_TextView.setText("주 "+weektime+"회 / "+kor_week);

                                    period_TextView.setText( period_stx  +"   ~   "+period_etx );

                                    add_option_view();

                                }
                            });














                        }
                        else
                        {
                            Log.d(TAG,"aaa");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            }
        }).start();
    }


}

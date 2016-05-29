package com.study.ddokdy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZHandler;
import prv.zozi.utils.ZHandlerInterFacce;
import prv.zozi.utils.ZMethod;

public class MakeNewStudyActivity extends Activity implements ZHandlerInterFacce {
    private static final String TAG = MakeNewStudyActivity.class.getSimpleName();
    private ZHandler handler = new ZHandler(this);
    private int data_openType;
    private DialogCategoryActivity dca;
    private int cat1, cat2,loc_code1,loc_code2;
    Spinner large_field_spinner, small_field_spinner;
    ImageView type_closed, type_opend,makestudy_ibtn_location;
    TextView makestudy_iv_closed_text, makestudy_iv_opened_text, field_select_text_btn,makestudy_et_c1_loction,On_line_btn,Off_line_btn;

    LinearLayout location_btn;
    RelativeLayout recruit_btn;

    ArrayList<category> large_category;
    ArrayList<category> small_category;


    String field_str = "";

    int on_off_line_flag;


    @Override
    public void handleMessage(Message msg) {
        switch (msg.arg1) {
            case 600:
                field_str = "";
                int position = msg.arg2;
                int idx = large_category.get(position).idx;

                get_category_list("info_category", String.valueOf(idx), 1);

                field_str = field_str+ large_category.get(position).name;
                field_select_text_btn.setText(field_str);

                cat1 =idx;
                cat2 =0;


                break;
            case 601:
                position = msg.arg2;
                cat2 =small_category.get(position).idx;


                field_str = field_str +"  <  "+ small_category.get(position).name;
                field_select_text_btn.setText(field_str);

                break;
            case 700:

                field_str = "";
                Log.d(TAG,"700");
                 position = msg.arg2;
                 idx = large_category.get(position).idx;

                get_category_list("info_location", String.valueOf(idx), 1);

                field_str = field_str+ large_category.get(position).name;
                makestudy_et_c1_loction.setText(field_str);

                loc_code1 =idx;
                loc_code2 =0;


                break;


            case 701:
                Log.d(TAG,"701");

                position = msg.arg2;
                loc_code2 =small_category.get(position).idx;
                field_str = field_str +"  <  "+ small_category.get(position).name;
                makestudy_et_c1_loction.setText(field_str);

                break;


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_study);
        init();
        holdViews();
        setViews();
    }

    private void init() {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        large_category = new ArrayList<category>();
        small_category = new ArrayList<category>();
        int on_off_line_flag=2; // 선택 x




        // 대분류 받아오기

    }

    private void holdViews() {
        location_btn = (LinearLayout)findViewById(R.id.location_btn);
        On_line_btn = (TextView) findViewById(R.id.On_line_btn);
        Off_line_btn = (TextView )findViewById(R.id.Off_line_btn);


        type_closed = (ImageView) findViewById(R.id.makestudy_iv_closed);
        type_opend = (ImageView) findViewById(R.id.makestudy_iv_opend);

        makestudy_ibtn_location = (ImageView)findViewById(R.id.makestudy_ibtn_location);
        recruit_btn = (RelativeLayout)findViewById(R.id.recruit_btn);

        makestudy_et_c1_loction = (TextView )findViewById(R.id.makestudy_et_c1_loction);
        makestudy_iv_closed_text = (TextView) findViewById(R.id.makestudy_iv_closed_text);
        makestudy_iv_opened_text = (TextView) findViewById(R.id.makestudy_iv_opened_text);
        field_select_text_btn = (TextView) findViewById(R.id.field_select_text_btn);


    }

    private void openListDialog(int type, String info) {
        int msgnum = 0;

        if (info.equals("info_category")) {
            msgnum = 600;
        } else if (info.equals("info_location")) {
            msgnum = 700;

        } else if (info.equals("status")) {
            msgnum = 800;

        } else {
            msgnum = 1000;

        }

        if (type == 0) {
            //ㄷ대대분류
            dca = new DialogCategoryActivity(MakeNewStudyActivity.this, handler, msgnum, "대분류 선택", large_category);
            dca.show();

        } else {
            // 소분류

            dca = new DialogCategoryActivity(MakeNewStudyActivity.this, handler, msgnum + 1, "소분류 선택", large_category);
            dca.show();

        }
        dca.setCancelable(false);
    }

    private void setViews() {


//        small_field_spinner.setVisibility(View.GONE);
//        large_field_spinner.setVisibility(View.GONE);

        //초기값 공개

        data_openType = 1;
        type_closed.setVisibility(View.INVISIBLE);
        makestudy_iv_closed_text.setOnClickListener(new OpenTypeListener(1));

        makestudy_iv_opened_text.setOnClickListener(new OpenTypeListener(2));
        field_select_text_btn.setOnClickListener(new onclicklistener(0));
        location_btn.setOnClickListener(new onclicklistener(1));
        recruit_btn.setOnClickListener(new onclicklistener(2));
        On_line_btn.setOnClickListener(new onclicklistener(3));
        Off_line_btn.setOnClickListener(new onclicklistener(4));


    }
    public class onclicklistener implements View.OnClickListener
    {
        int btn_num;

        public onclicklistener(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            if( btn_num ==0)
            {
                openListDialog(0, "info_category");
                get_category_list("info_category", "0", 0);

            }
            else if (btn_num == 1)
            {
                openListDialog(0, "info_location");
                get_category_list("info_location", "0", 0);
            }
            else if (btn_num == 2)
            {
                openListDialog(0, "info_location");
                get_category_list("info_location", "0", 0);
            }
            else if(btn_num ==3)
            {
                 on_off_line_flag =1;
                On_line_btn.setTextColor(getResources().getColorStateList(R.color.mint_dark));
                Off_line_btn.setTextColor(getResources().getColorStateList(R.color.blackgray));

            }
            else if (btn_num ==4)
            {
                on_off_line_flag =0;
                On_line_btn.setTextColor(getResources().getColor(R.color.blackgray));
                Off_line_btn.setTextColor(getResources().getColor(R.color.mint_dark));
            }

        }
    }

    public void get_category_list(final String type, final String code, final int cat_flag) {

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

                    large_category.clear();
                    small_category.clear();
                    if (cat_flag == 0) {

                        for (int i = 0; i < Integer.parseInt(rn); i++) {
                            JSONObject job = Jarray_data.getJSONObject(i);
                            large_category.add(new category(job.getString("name"), Integer.parseInt(job.getString("idx"))));
                        }
                        for (int i = 0; i < Integer.parseInt(rn); i++) {
                            Log.d(TAG, "large_category== " + large_category.get(i).name);
                        }
                    } else if (cat_flag == 1) {
                        for (int i = 0; i < Integer.parseInt(rn); i++) {
                            JSONObject job = Jarray_data.getJSONObject(i);
                            small_category.add(new category(job.getString("name"), Integer.parseInt(job.getString("idx"))));
                        }
                        for (int i = 0; i < Integer.parseInt(rn); i++) {
                            Log.d(TAG, "sma== " + small_category.get(i).name);
                        }
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Log.d(TAG,"cat_flag == "+cat_flag);

                                if (cat_flag == 0) {

                                    Log.d(TAG,"large_category.size()  == "+large_category.size() );

                                    if (large_category.size() != 0) {


                                        dca.setArrayList(large_category);
                                    }
                                }
                                else
                                {
                                    Log.d(TAG,"small_category.size()  == "+small_category.size() );

                                    if(small_category.size()!=0) {

                                        openListDialog(1, type);
                                        dca.setArrayList(small_category);
                                    }

                                }



                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }


    public void large_Spinner_info() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                //로그인
                HashMap<String, String> data_set = new HashMap<String, String>();
                data_set.put("type", "info_category");
                data_set.put("code", "0");


                String mdata = ZMethod.getStringHttpPost(Config.url_home + "get_infoList.php", ZMethod.Map_to_String(data_set));

                Log.d(TAG, "data== " + mdata);

                if (mdata != null) {

                    try {
                        JSONObject jobj = new JSONObject(mdata);
                        Log.d(TAG, "jobj ==  " + jobj);


                        String rn = jobj.getString("rn");

                        JSONArray jarr = jobj.getJSONArray("data");

                        Log.d(TAG, "jarr .toString" + jarr.toString());

                        ArrayList<String> Large_field = new ArrayList<String>();

                        for (int i = 0; i < Integer.parseInt(rn); i++) {

                            JSONObject jo = jarr.getJSONObject(i);


                            Log.d(TAG, "aa" + jo.getString("idx"));
                            Log.d(TAG, "aa" + jo.getString("name"));
                            Log.d(TAG, "aa" + jo.getString("code"));
                        }

//
//                        String idx  = jarr.getString(0);
//
//                        String name  = jarr.getString(1);
//
//                        String code  = jarr.getString(2);


//
//                        Log.d(TAG,"rn ==  "+rn);
//                        Log.d(TAG,"idx ==  "+idx);
//                        Log.d(TAG,"name ==  "+name);
//                        Log.d(TAG,"code ==  "+code);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        }).start();
    }


    private class OpenTypeListener implements View.OnClickListener {
        int openType;

        public OpenTypeListener(int openType) {
            this.openType = openType;
        }

        @Override
        public void onClick(View v) {

            data_openType = openType;


            if (openType == 1) {
                type_closed.setVisibility(View.VISIBLE);
                type_opend.setVisibility(View.INVISIBLE);
            } else if (openType == 2) {
                type_closed.setVisibility(View.INVISIBLE);
                type_opend.setVisibility(View.VISIBLE);
            }
        }
    }


    public static class category {
        String name;
        int idx;

        public category(String name, int idx) {
            this.name = name;
            this.idx = idx;
        }
    }

}

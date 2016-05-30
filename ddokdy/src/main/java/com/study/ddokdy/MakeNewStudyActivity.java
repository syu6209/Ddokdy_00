package com.study.ddokdy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZHandler;
import prv.zozi.utils.ZHandlerInterFacce;
import prv.zozi.utils.ZLoginInfo;
import prv.zozi.utils.ZMethod;

public class MakeNewStudyActivity extends Activity implements ZHandlerInterFacce {
    private static final String TAG = MakeNewStudyActivity.class.getSimpleName();
    private Info_Login logininfo;
    private ZHandler handler = new ZHandler(this);
    private int is_open;
    private DialogCategoryActivity dca;
    Spinner large_field_spinner, small_field_spinner;
    ImageView type_closed, type_opend,makestudy_ibtn_location;
    TextView time_first,time_second,goal_time_first,goal_time_second,makestudy_iv_closed_text, makestudy_iv_opened_text, field_select_text_btn,makestudy_et_c1_loction,On_line_btn,Off_line_btn,recruit_text;
    LayoutInflater inflater;
    LinearLayout container_Layout ;
    LinearLayout location_btn;
    RelativeLayout recruit_btn;
    Button makestudy_btn;

    TextWatcher watcher;



    EditText makestudy_et_studyname,makestudy_et_c3_member,study_intro;

    ArrayList<category> large_category;
    ArrayList<category> small_category;

    ArrayList<LinearLayout> option_text;


    String field_str = "";
    int year, month, day, hour, minute;
    int on_off_line_flag;
    boolean info_category_flag,info_location_flag;



    private int cat1, cat2,loc_code1,loc_code2,weekint,background;


    String loc_detail,sr_name,introduce,posting_etx,period_stx,period_etx,time_stx,time_etx,max_member;




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
        logininfo = new ZLoginInfo(getApplicationContext()).getLoginInfo();
        large_category = new ArrayList<category>();
        small_category = new ArrayList<category>();
        on_off_line_flag=2; // 선택 x

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);



        option_text = new ArrayList<LinearLayout>();
        info_category_flag = false;
        info_location_flag =false;

         inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 대분류 받아오기
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                full_check();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

    }

    private void holdViews() {

        study_intro = (EditText)findViewById(R.id.study_intro);

        makestudy_btn = (Button) findViewById(R.id.makestudy_btn);
        makestudy_et_c3_member = (EditText) findViewById(R.id.makestudy_et_c3_member);


        makestudy_et_studyname = (EditText) findViewById(R.id.makestudy_et_studyname);

        container_Layout = (LinearLayout)findViewById(R.id.option_container);

        location_btn = (LinearLayout)findViewById(R.id.location_btn);
        On_line_btn = (TextView) findViewById(R.id.On_line_btn);
        Off_line_btn = (TextView )findViewById(R.id.Off_line_btn);
        recruit_text = (TextView) findViewById(R.id.recruit_text);
        goal_time_second =(TextView)findViewById(R.id.goal_time_second);

        time_first = (TextView)findViewById(R.id.time_first);
        time_second =(TextView)findViewById(R.id.time_second);
        goal_time_first = (TextView )findViewById(R.id.goal_time_first);


        type_closed = (ImageView) findViewById(R.id.makestudy_iv_closed);
        type_opend = (ImageView) findViewById(R.id.makestudy_iv_opend);

        makestudy_ibtn_location = (ImageView)findViewById(R.id.makestudy_ibtn_location);
        recruit_btn = (RelativeLayout)findViewById(R.id.recruit_btn);

        makestudy_et_c1_loction = (TextView )findViewById(R.id.makestudy_et_c1_loction);
        makestudy_iv_closed_text = (TextView) findViewById(R.id.makestudy_iv_closed_text);
        makestudy_iv_opened_text = (TextView) findViewById(R.id.makestudy_iv_opened_text);
        field_select_text_btn = (TextView) findViewById(R.id.field_select_text_btn);


    }
    public void full_check()
    {
     //   private int cat1, cat2,loc_code1,loc_code2,is_open;


//        String loc_detail,sr_name,introduce,posting_etx,period_stx,period_etx,weekint,time_stx,time_etx,max_member,background;
        sr_name =makestudy_et_studyname.getText().toString();
        loc_detail = makestudy_et_c1_loction.getText().toString();
        introduce = study_intro.getText().toString();
        posting_etx = recruit_text.getText().toString();
        period_stx = goal_time_first.getText().toString();
        period_etx = goal_time_second.getText().toString();
        time_stx = time_first.getText().toString();
        time_etx = time_second .getText().toString();
        max_member = makestudy_et_c3_member.getText().toString();
        boolean study_name_flag = !(makestudy_et_studyname.getText().toString().equals(""));
        boolean open_flag =  !(is_open ==2);
        boolean recluit_flag = !(recruit_text.getText().toString().equals("모집기간선택"));
        boolean on_off_flag = !(on_off_line_flag == 2);
        boolean person_num_flag = !(makestudy_et_c3_member.getText().toString().equals(""));
        boolean goal_time_first_flag  = !(goal_time_first.getText().toString().equals("시작 선택"));
        boolean goal_time_second_flag  = !(goal_time_second.getText().toString().equals("끝 선택"));
        boolean time_first_flag  = !(time_first.getText().toString().equals("시작 선택"));
        boolean time_second_flag  = !(time_second.getText().toString().equals("끝 선택"));
        boolean study_intro_flag = !(study_intro.getText().toString().equals(""));

        Iterator<LinearLayout> iterator = option_text.iterator();
        boolean option_flag = true;
        while (iterator.hasNext()) {
            LinearLayout parent =(LinearLayout) iterator.next();
            EditText edit = (EditText)parent.findViewById(R.id.option_edit_text);
            if(edit.getText().toString().equals(""))
            {
                option_flag = false;
                break;
            }
        }
        Log.d(TAG," study_name_flag == "+study_name_flag);
        Log.d(TAG," open_flag == "+open_flag);
        Log.d(TAG," info_category_flag == "+info_category_flag);
        Log.d(TAG,"recluit_flag == "+recluit_flag);
        Log.d(TAG,"on_off_flag  == "+on_off_flag);
        Log.d(TAG," info_location_flag == "+info_location_flag);
        Log.d(TAG,"person_num_flag  ==  "+person_num_flag);
        Log.d(TAG,"goal_time_first_flag  ==  "+goal_time_first_flag);
        Log.d(TAG,"goal_time_second_flag  ==  "+goal_time_second_flag);
        Log.d(TAG,"time_first_flag  ==  "+time_first_flag);
        Log.d(TAG,"time_second_flag  ==  "+time_second_flag);
        Log.d(TAG,"option_flag  ==  "+option_flag);
        Log.d(TAG,"study_intro_flag  ==  "+study_intro_flag);

        if(study_name_flag
                &&open_flag
                &&recluit_flag
                &on_off_flag
                &&person_num_flag
                &&info_location_flag
                &&goal_time_first_flag
                &&goal_time_second_flag
                &&time_first_flag
                &&time_second_flag
                &&option_flag
                &&study_intro_flag)
        {
            makestudy_btn.setBackground(getResources().getDrawable(R.color.mint_dark));
            makestudy_btn.setEnabled(true);

        }
        else
        {
            makestudy_btn.setBackground(getResources().getDrawable(R.color.mint));
            makestudy_btn.setEnabled(false);
        }


    }
    public void study_make()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> map = new HashMap<String, String>();


                map.put("uid",logininfo.user_id);
                map.put("sr_name",sr_name);
                map.put("loc_code1",String.valueOf(loc_code1));
                map.put("loc_code2",String.valueOf(loc_code2));
                map.put("loc_detail",loc_detail);
                map.put("cat1",String.valueOf(cat1));
                map.put("cat2",String.valueOf(cat2));
                map.put("is_open",String.valueOf(is_open));
                map.put("introduce",introduce);
                map.put("posting_etx",posting_etx);
                map.put("period_stx",period_stx);
                map.put("period_etx",period_etx);

                map.put("is_online",String.valueOf(on_off_line_flag));


                weekint=1;
                map.put("weekint",String.valueOf(weekint));
                map.put("time_stx",time_stx);
                map.put("time_etx",time_etx);
                map.put("max_member",String.valueOf(max_member));
                background=1;
                map.put("background",String.valueOf(background));









                String data = ZMethod.getStringHttpPost(Config.url_home+"study_make.php",ZMethod.Map_to_String(map));

                Log.d(TAG,"ZMethod.Map_to_String(map) == "+ZMethod.Map_to_String(map) );
                Log.d(TAG,"data ==  "+data);

                if(data != null)
                {
                    JSONObject jobj = null;
                    try {
                        jobj = new JSONObject(data);



                        Log.d(TAG,"jobj ==  "+jobj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }





            }
        }).start();
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
        makestudy_et_studyname.addTextChangedListener(watcher);
        makestudy_et_c3_member.addTextChangedListener(watcher);
        study_intro.addTextChangedListener(watcher);

        LinearLayout route_info_tab = (LinearLayout) inflater.inflate(R.layout.makestudy_option_view, null);

        EditText edit = (EditText)route_info_tab.findViewById(R.id.option_edit_text);
        edit.addTextChangedListener(watcher);

        ImageButton imgbtn = (ImageButton)  route_info_tab.findViewById(R.id.makestudy_plus_minus_btn);

        imgbtn.setOnClickListener(new option_btn_click_listenr(0));
        container_Layout.addView(route_info_tab);
        option_text.add(route_info_tab);
//        small_field_spinner.setVisibility(View.GONE);
//        large_field_spinner.setVisibility(View.GONE);

        //초기값 공개

        is_open = 2;
        type_closed.setVisibility(View.INVISIBLE);
        makestudy_iv_closed_text.setOnClickListener(new OpenTypeListener(0));

        makestudy_iv_opened_text.setOnClickListener(new OpenTypeListener(1));
        field_select_text_btn.setOnClickListener(new onclicklistener(0));
        location_btn.setOnClickListener(new onclicklistener(1));
        recruit_btn.setOnClickListener(new onclicklistener(2));
        On_line_btn.setOnClickListener(new onclicklistener(3));
        Off_line_btn.setOnClickListener(new onclicklistener(4));
        goal_time_first.setOnClickListener(new onclicklistener(5));
        goal_time_second.setOnClickListener(new onclicklistener(6));
        time_first.setOnClickListener(new onclicklistener(7));
        time_second.setOnClickListener(new onclicklistener(8));
        makestudy_btn.setOnClickListener(new onclicklistener(9));

    }
    public class option_btn_click_listenr implements View.OnClickListener
    {
        int option_btn_num;

        public option_btn_click_listenr(int option_btn_num) {
            this.option_btn_num = option_btn_num;
        }

        @Override
        public void onClick(View v) {

            if(option_btn_num ==0) {
                    if(option_text.size() <3) {

                        Log.d(TAG, "option_btn_num == " + option_btn_num);
                        LinearLayout route_info_tab = (LinearLayout) inflater.inflate(R.layout.makestudy_option_view, null);
                        ImageButton imgbtn = (ImageButton) route_info_tab.findViewById(R.id.makestudy_plus_minus_btn);
                        EditText edit = (EditText)route_info_tab.findViewById(R.id.option_edit_text);
                        edit.addTextChangedListener(watcher);
                        imgbtn.setBackground(getResources().getDrawable(R.drawable.btn_minus));
                        imgbtn.setOnClickListener(new option_btn_click_listenr(option_btn_num + 1));
                        container_Layout.addView(route_info_tab);
                        option_text.add(route_info_tab);
                    }
                }
                else
                {
                    Log.d(TAG, "dd == " );

                    container_Layout.removeView((View) v.getParent().getParent());

                    option_text.remove(option_btn_num);

                }
            full_check();



        }
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
                info_category_flag = true;
                full_check();
                openListDialog(0, "info_category");
                get_category_list("info_category", "0", 0);

            }
            else if (btn_num == 1)
            {

                info_location_flag = true;
                full_check();
                openListDialog(0, "info_location");
                get_category_list("info_location", "0", 0);
            }
            else if (btn_num == 2)
            {
                new DatePickerDialog(MakeNewStudyActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);

                        recruit_text.setText(msg);
                        full_check();

                    }
                }, year, month, day).show();

            }
            else if(btn_num ==3)
            {
                 on_off_line_flag =1;
                On_line_btn.setTextColor(getResources().getColorStateList(R.color.mint_dark));
                Off_line_btn.setTextColor(getResources().getColorStateList(R.color.blackgray));
                full_check();


            }
            else if (btn_num ==4)
            {
                on_off_line_flag =0;
                On_line_btn.setTextColor(getResources().getColor(R.color.blackgray));
                Off_line_btn.setTextColor(getResources().getColor(R.color.mint_dark));
                full_check();

            }
            else if ( btn_num ==5)
            {
                new DatePickerDialog(MakeNewStudyActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);

                        goal_time_first.setText(msg);
                        full_check();

                    }
                }, year, month, day).show();
            }
            else if ( btn_num ==6)
            {
                new DatePickerDialog(MakeNewStudyActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);

                        goal_time_second.setText(msg);
                        full_check();

                    }
                }, year, month, day).show();
            }

            else if(btn_num==7)
            {
                new TimePickerDialog(MakeNewStudyActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String msg = String.format("%d:%d",  hourOfDay, minute);
                        time_first.setText(msg);
                        full_check();


                    }
                }, hour, minute, false).show();
            }

            else if (btn_num==8)
            {
                new TimePickerDialog(MakeNewStudyActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String msg = String.format("%d:%d",hourOfDay, minute);
                        time_second.setText(msg);
                        full_check();


                    }
                }, hour, minute, false).show();
            }
            else if ( btn_num ==9)
            {
                full_check();
                study_make();

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
//                        for (int i = 0; i < Integer.parseInt(rn); i++) {
//                            Log.d(TAG, "large_category== " + large_category.get(i).name);
//                        }
                    } else if (cat_flag == 1) {
                        for (int i = 0; i < Integer.parseInt(rn); i++) {
                            JSONObject job = Jarray_data.getJSONObject(i);
                            small_category.add(new category(job.getString("name"), Integer.parseInt(job.getString("idx"))));
                        }
//                        for (int i = 0; i < Integer.parseInt(rn); i++) {
//                            Log.d(TAG, "sma== " + small_category.get(i).name);
//                        }
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






    private class OpenTypeListener implements View.OnClickListener {
        int openType;

        public OpenTypeListener(int openType) {
            this.openType = openType;
        }

        @Override
        public void onClick(View v) {

            is_open = openType;


            if (openType == 0) {
                type_closed.setVisibility(View.VISIBLE);
                type_opend.setVisibility(View.INVISIBLE);
                full_check();

            } else if (openType == 1) {
                type_closed.setVisibility(View.INVISIBLE);
                type_opend.setVisibility(View.VISIBLE);
                full_check();

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

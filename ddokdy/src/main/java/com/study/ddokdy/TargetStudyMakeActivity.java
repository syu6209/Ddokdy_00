package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZLoginInfo;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 31..
 */
public class TargetStudyMakeActivity extends Activity {

    private static final String TAG = TargetStudyMakeActivity.class.getSimpleName();
    private Info_Login logininfo;

    int is_online;
    int Image_list[];
    int background;
    Button make_target_btn;
    String name ;

    EditText target_study_title;


    ImageButton image_rotate_btn;
    ImageView study_location_image, study_search_image, study_field_image;
    LayoutInflater inflater;
    TextView On_line_btn, Off_line_btn;
    Button mon_btn, tue_btn, wed_btn, thr_btn, fri_btn, sat_btn, sun_btn;

    LinearLayout location_container, field_container, search_container;
    ArrayList<cate_info> location_result, field_result, search_result, temp;
    RelativeLayout rotate_image, study_location_select, study_search_select, study_field_select;
    String select_name;
    int loc, weekint;
    int week_array[] = {0, 0, 0, 0, 0, 0, 0};
    boolean week_flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targetstudymake);

        init();
        holdViews();
        setViews();


    }

    public void init() {
        logininfo = new ZLoginInfo(getApplicationContext()).getLoginInfo();

        is_online = 2;

        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        background = 0;
        Image_list = new int[]{R.drawable.box_background_00, R.drawable.box_background_01, R.drawable.box_background_02, R.drawable.box_background_03};
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        temp = new ArrayList<cate_info>();
        location_result = new ArrayList<cate_info>();
        field_result = new ArrayList<cate_info>();
        search_result = new ArrayList<cate_info>();
        week_flag = false;

    }

    public void holdViews() {

        On_line_btn = (TextView) findViewById(R.id.On_line_btn);
        Off_line_btn = (TextView) findViewById(R.id.Off_line_btn);
        target_study_title = (EditText) findViewById(R.id.target_study_title);


        make_target_btn = (Button) findViewById(R.id.make_target_btn);
        mon_btn = (Button) findViewById(R.id.mon_btn);
        tue_btn = (Button) findViewById(R.id.tue_btn);
        wed_btn = (Button) findViewById(R.id.wed_btn);
        thr_btn = (Button) findViewById(R.id.thr_btn);
        fri_btn = (Button) findViewById(R.id.fri_btn);
        sat_btn = (Button) findViewById(R.id.sat_btn);
        sun_btn = (Button) findViewById(R.id.sun_btn);


        search_container = (LinearLayout) findViewById(R.id.search_container);

        field_container = (LinearLayout) findViewById(R.id.field_container);
        location_container = (LinearLayout) findViewById(R.id.location_container);
        image_rotate_btn = (ImageButton) findViewById(R.id.image_rotate_btn);
        rotate_image = (RelativeLayout) findViewById(R.id.rotate_image);


        study_location_select = (RelativeLayout) findViewById(R.id.study_location_select);
        study_search_select = (RelativeLayout) findViewById(R.id.study_search_select);
        study_field_select = (RelativeLayout) findViewById(R.id.study_field_select);


        study_location_image = (ImageView) findViewById(R.id.study_location_image);
        study_search_image = (ImageView) findViewById(R.id.study_search_image);
        study_field_image = (ImageView) findViewById(R.id.study_field_image);


    }

    public class make_target_btn_listner implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Log.d(TAG,"Asd");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HashMap<String,String> map = new HashMap<String, String>();
                    try {
                    map.put("idx","0");


                        map.put("name", URLEncoder.encode(name, "utf-8"));


                    map.put("uid",logininfo.user_id);
                    for (int i =0; i< location_result.size();i++)
                    {
                        map.put("loc"+(i+1),String.valueOf(location_result.get(i).idx));

                    }
                    for (int i =0; i< field_result.size();i++)
                    {
                        map.put("cat"+(i+1),String.valueOf(field_result.get(i).idx));

                    }
                    for (int i =0; i< search_result.size();i++)
                    {
                        map.put("keyword"+(i+1),URLEncoder.encode(String.valueOf(search_result.get(i).name), "utf-8"));

                    }
                    map.put("is_online",String.valueOf(is_online));
                    map.put("weekint",String.valueOf(weekint));
                    map.put("background",String.valueOf(background));

                        Log.d(TAG, "  is_online == " + is_online);
                        Log.d(TAG,"  weekint == "+weekint);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }



                    String data =ZMethod.getStringHttpPost(Config.url_home + "keyword_save.php",ZMethod.Map_to_String(map));
                    Log.d(TAG,",ZMethod.Map_to_String(map) === "+ ZMethod.Map_to_String(map));

                    Log.d(TAG,"data === "+ data);

                    if(data !=null)
                    {
                        try {
                            JSONObject jobj = new JSONObject(data);

                            String resultCode = jobj.getString("resultCode");
                            String msg = jobj.getString("msg");

                            Log.d(TAG,"resultCode == "+resultCode);
                            Log.d(TAG,"msg == "+msg);
                            if(resultCode.equals("0"))
                            {
                                finish();
                            }
                            else
                            {
                                Log.d(TAG,"실패 ");

                            }






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                }
            }).start();

        }
    }

    public void full_check() {

         name = target_study_title.getText().toString();

        boolean title_flag = !name.equals("");
        boolean online_flag = (is_online != 2);
        if(title_flag&&week_flag&&online_flag)
        {
            make_target_btn.setBackground(getResources().getDrawable(R.color.mint_dark));
            make_target_btn.setEnabled(true);

        }
        else
        {
            make_target_btn.setBackground(getResources().getDrawable(R.color.mint));
            make_target_btn.setEnabled(false);
        }


    }

    public void setViews() {

        target_study_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                full_check();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        make_target_btn.setOnClickListener(new make_target_btn_listner());
        On_line_btn.setOnClickListener(new on_off_listend(0));
        Off_line_btn.setOnClickListener(new on_off_listend(1));
        mon_btn.setOnClickListener(new week_btn_click_listenr(0));
        tue_btn.setOnClickListener(new week_btn_click_listenr(1));
        wed_btn.setOnClickListener(new week_btn_click_listenr(2));
        thr_btn.setOnClickListener(new week_btn_click_listenr(3));
        fri_btn.setOnClickListener(new week_btn_click_listenr(4));
        sat_btn.setOnClickListener(new week_btn_click_listenr(5));
        sun_btn.setOnClickListener(new week_btn_click_listenr(6));
        image_rotate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background = (background + 1) % Image_list.length;
                rotate_image.setBackground(getResources().getDrawable(Image_list[background]));
            }
        });
        study_location_select.setOnClickListener(new option_select_listner(0));
        study_field_select.setOnClickListener(new option_select_listner(1));
        study_search_select.setOnClickListener(new option_select_listner(2));


    }

    public class on_off_listend implements View.OnClickListener {
        int btn_num;

        public on_off_listend(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            if (btn_num == 0) {

                is_online = 1;
                On_line_btn.setTextColor(getResources().getColorStateList(R.color.Test_red));
                Off_line_btn.setTextColor(getResources().getColorStateList(R.color.blackgray));
            } else if (btn_num == 1) {

                is_online = 0;
                On_line_btn.setTextColor(getResources().getColor(R.color.blackgray));
                Off_line_btn.setTextColor(getResources().getColor(R.color.Test_red));
            }

            full_check();


        }
    }

    public class week_btn_click_listenr implements View.OnClickListener {
        int btn_num;

        public week_btn_click_listenr(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            if (week_array[btn_num] == 1) {
                week_array[btn_num] = 0;
                Button btn = (Button) v;
                btn.setTextColor(getResources().getColor(R.color.blackgray));
            } else if (week_array[btn_num] == 0) {
                week_array[btn_num] = 1;

                Button btn = (Button) v;
                btn.setTextColor(getResources().getColor(R.color.Test_red));

            }

            weekint = 0;

            for (int i = 0; i < 7; i++) {
                if (week_array[i] == 1) {
                    weekint = (int) (weekint + Math.pow(2, i));
                    week_flag = true;

                }
            }
            full_check();


        }
    }


    public class option_select_listner implements View.OnClickListener {
        int btn_num;

        public option_select_listner(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            if (btn_num == 0) {


                Intent intent = new Intent(TargetStudyMakeActivity.this, Targetstudy_Location_Select_Activity.class);
                startActivityForResult(intent, 0);

            } else if (btn_num == 1) {
                Intent intent = new Intent(TargetStudyMakeActivity.this, Targetstudy_Field_Activity.class);
                startActivityForResult(intent, 0);

//                startActivityForResult(intent, 0);

            } else if (btn_num == 2) {
                Intent intent = new Intent(TargetStudyMakeActivity.this, Targetstudy_Search_Activity.class);
                intent.putExtra("search_result_size", search_result.size());
//                startActivity(intent);
                startActivityForResult(intent, 0);

            }

        }
    }

    public static class cate_info implements Serializable {
        String name;
        int idx;

        public cate_info(String name, int idx) {
            this.name = name;
            this.idx = idx;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {


            switch (resultCode) {
                case Config.Targetstudy_Location_Select_Activity_RESULT_CODE:

                    select_name = data.getExtras().getString("select_name");
                    loc = data.getExtras().getInt("loc");
                    addArray(location_result, select_name, loc);
                    add_cate_process(resultCode, study_location_select, location_result, study_location_image, location_container);
                    break;
                case Config.Targetstudy_Field_Activity_RESULT_CODE:
                    select_name = data.getExtras().getString("select_name");
                    loc = data.getExtras().getInt("loc");

                    addArray(field_result, select_name, loc);

                    add_cate_process(resultCode, study_field_select, field_result, study_field_image, field_container);

                    break;
                case Config.Targetstudy_Search_Select_Activity_RESULT_CODE:


                    Log.d(TAG, "temp.size() ==" + temp.size());


                    temp = (ArrayList<cate_info>) (data.getExtras().getSerializable("Search_array"));

                    for (int i = 0; i < temp.size(); i++) {
                        search_result.add(new cate_info(temp.get(i).name, temp.get(i).idx));
                    }
                    add_cate_process(resultCode, study_search_select, search_result, study_search_image, search_container);


                    break;


                default:


                    break;
            }
        } else {
            Log.d(TAG, "걍 나옴");

        }
    }

    public void addArray(ArrayList<cate_info> array_l, String select_name, int loc) {

        boolean flag = false;
        if (array_l.size() < 3) {
            for (int i = 0; i < array_l.size(); i++) {
                if (array_l.get(i).idx == loc) {
                    flag = true;
                    Toast.makeText(getApplicationContext(), "이미 존재 하는 자료 입니다", Toast.LENGTH_SHORT).show();
                }
            }
            if (!flag) {
                array_l.add(new cate_info(select_name, loc));
            }
        }

    }

    public void add_cate_process(int flag, RelativeLayout study_select, ArrayList<cate_info> array_l, ImageView image_btn, LinearLayout container) {
        container.removeAllViews();
        if (array_l.size() > 0) {
            image_btn.setBackground(getResources().getDrawable(R.drawable.btn_arrow_down));
            study_select.setBackground(getResources().getDrawable(R.color.gray1));
            container.setVisibility(View.VISIBLE);
            for (int i = 0; i < array_l.size(); i++) {

                RelativeLayout target_box = (RelativeLayout) inflater.inflate(R.layout.target_box, null);
//                if(flag ==0) {
//                    target_box.setOnClickListener(new location_box_clicked(i));
//                }
//                else if(flag ==1)
//                {
//                    target_box.setOnClickListener(new field_box_clicked(i));
//
//                }
//                else if(flag==2)
//                {
                target_box.setOnClickListener(new remove_box(i, flag, array_l));

//                }

                TextView target_box_title = (TextView) target_box.findViewById(R.id.target_box_title);
                target_box_title.setText(array_l.get(i).name);
                container.addView(target_box);

            }
        } else if (array_l.size() == 0) {
            study_select.setBackground(getResources().getDrawable(R.color.white));

            image_btn.setBackground(getResources().getDrawable(R.drawable.btn_arrow_right));
            container.setVisibility(View.GONE);


        }


    }

    public class location_box_clicked implements View.OnClickListener {
        int btn_num;

        public location_box_clicked(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            location_result.remove(btn_num);
            add_cate_process(Config.Targetstudy_Location_Select_Activity_RESULT_CODE, study_location_select, location_result, study_location_image, location_container);
        }
    }

    public class field_box_clicked implements View.OnClickListener {
        int btn_num;

        public field_box_clicked(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            field_result.remove(btn_num);
            add_cate_process(Config.Targetstudy_Field_Activity_RESULT_CODE, study_field_select, field_result, study_field_image, field_container);
        }
    }

    public class remove_box implements View.OnClickListener {
        int btn_num;
        int state;
        ArrayList<cate_info> arr;


        public remove_box(int btn_num, int state, ArrayList<cate_info> arr) {
            this.btn_num = btn_num;
            this.state = state;
            this.arr = arr;
        }

        @Override
        public void onClick(View v) {
            arr.remove(btn_num);
            if (state == 0) {
                add_cate_process(Config.Targetstudy_Location_Select_Activity_RESULT_CODE, study_location_select, location_result, study_location_image, location_container);

            } else if (state == 1) {
                add_cate_process(Config.Targetstudy_Field_Activity_RESULT_CODE, study_field_select, field_result, study_field_image, field_container);

            } else if (state == 2) {
                add_cate_process(Config.Targetstudy_Search_Select_Activity_RESULT_CODE, study_search_select, search_result, study_search_image, search_container);

            }

        }
    }
}




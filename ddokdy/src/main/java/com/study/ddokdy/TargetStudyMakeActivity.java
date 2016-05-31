package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 31..
 */
public class TargetStudyMakeActivity extends Activity {

    private static final String TAG = TargetStudyMakeActivity.class.getSimpleName();

    int Image_list[];
    int background;
    ImageButton image_rotate_btn;
    ImageView study_location_image, study_search_image, study_field_image;
    LayoutInflater inflater;
    LinearLayout location_container, field_container, search_container;
    ArrayList<cate_info> location_result, field_result,search_result, temp;
    RelativeLayout rotate_image, study_location_select, study_search_select, study_field_select;
    String select_name ;
    int loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targetstudymake);

        init();
        holdViews();
        setViews();


    }

    public void init() {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        background = 0;
        Image_list = new int[]{R.drawable.box_background_00, R.drawable.box_background_01, R.drawable.box_background_02, R.drawable.box_background_03};


        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        temp=new ArrayList<cate_info>();
        location_result = new ArrayList<cate_info>();
        field_result = new ArrayList<cate_info>();
        search_result = new ArrayList<cate_info>();


    }

    public void holdViews() {

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

    public void setViews() {
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

    public static class cate_info  implements Serializable{
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
                    add_cate_process(resultCode,study_location_select, location_result, study_location_image, location_container);
                    break;
                case Config.Targetstudy_Field_Activity_RESULT_CODE:
                    select_name = data.getExtras().getString("select_name");
                    loc = data.getExtras().getInt("loc");

                    addArray(field_result, select_name, loc);

                    add_cate_process(resultCode,study_field_select, field_result, study_field_image, field_container);

                    break;
                case Config.Targetstudy_Search_Select_Activity_RESULT_CODE:


                    Log.d(TAG,"temp.size() ==" +temp.size());


                    temp =(ArrayList<cate_info>) (data.getExtras().getSerializable("Search_array"));

                    for(int i =0 ; i<temp.size();i++)
                    {
                        search_result.add(new cate_info(temp.get(i).name,temp.get(i).idx));
                    }
                    add_cate_process(resultCode,study_search_select, search_result, study_search_image, search_container);


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
            for (int i =0; i<array_l.size();i++) {
                if(array_l.get(i).idx  == loc) {
                    flag = true;
                    Toast.makeText(getApplicationContext(),"이미 존재 하는 자료 입니다",Toast.LENGTH_SHORT).show();
                }
            }
            if(!flag ) {
                array_l.add(new cate_info(select_name, loc));
            }
        }

    }

    public void add_cate_process(int flag ,RelativeLayout study_select, ArrayList<cate_info> array_l, ImageView image_btn, LinearLayout container) {
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
                    target_box.setOnClickListener(new remove_box(i,flag,array_l));

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
            add_cate_process(Config.Targetstudy_Location_Select_Activity_RESULT_CODE,study_location_select, location_result, study_location_image, location_container);
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
            add_cate_process(Config.Targetstudy_Field_Activity_RESULT_CODE,study_field_select, field_result, study_field_image, field_container);
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
            if(state == 0) {
                add_cate_process(Config.Targetstudy_Location_Select_Activity_RESULT_CODE,study_location_select, location_result, study_location_image, location_container);

            }
            else if (state == 1)
            {
                add_cate_process(Config.Targetstudy_Field_Activity_RESULT_CODE,study_field_select, field_result, study_field_image, field_container);

            }
            else if (state == 2){
                add_cate_process(Config.Targetstudy_Search_Select_Activity_RESULT_CODE, study_search_select, search_result, study_search_image, search_container);

            }

        }
    }
}




package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

import prv.zozi.utils.Config;

/**
 * Created by ibyeongmu on 16. 5. 31..
 */
public class Targetstudy_Search_Activity extends Activity {

    private static final String TAG =Targetstudy_Search_Activity.class.getSimpleName();
    TextView add_btn;
    EditText search_Edittext;
    ImageButton topbar_btn_close, StudyRoom_Check_Btn;
    ArrayList<TargetStudyMakeActivity.cate_info> Search_array;
    LinearLayout search_container;
    int search_result_size;

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targetstudy_search);

        init();
        holdView();
        setView();


    }

    public void init() {

        Search_array = new ArrayList<TargetStudyMakeActivity.cate_info>();

        Intent intet = getIntent();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        search_result_size = intet.getExtras().getInt("search_result_size");
    }

    public void holdView() {
        add_btn = (TextView) findViewById(R.id.add_btn);
        search_Edittext = (EditText) findViewById(R.id.search_Edittext);
        search_container = (LinearLayout)findViewById(R.id.Li_container);


        topbar_btn_close = (ImageButton) findViewById(R.id.topbar_btn_close);
        StudyRoom_Check_Btn = (ImageButton) findViewById(R.id.StudyRoom_Check_Btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "asda");
                Log.d(TAG, "search_Edittext == "+ search_Edittext.getText().toString());



                if(!search_Edittext.getText().toString().equals("")) {

                    if(Search_array.size()<4) {
                        Search_array.add(new TargetStudyMakeActivity.cate_info(search_Edittext.getText().toString(), 0));
                        add_target_view();
                        search_Edittext.setText("");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"키워드 최대수",Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"내용을 입력하세요",Toast.LENGTH_SHORT).show();

                }


            }
        });
    }


    public void setView() {
        topbar_btn_close.setOnClickListener(new top_btn_listenr(0));
        StudyRoom_Check_Btn.setOnClickListener(new top_btn_listenr(1));


    }

    public class top_btn_listenr implements View.OnClickListener {
        int btn_num;

        public top_btn_listenr(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            if (btn_num == 0) {
                finish();

            } else if (btn_num == 1) {
                // 확인 버튼
                if ((3 - search_result_size) >= Search_array.size())
                {
                    //가능
                    exit_location();

                }
                else {
                    //불가능
                    Toast.makeText(getApplicationContext(),"실패!추가 가능한 수 : "+(3 - search_result_size),Toast.LENGTH_SHORT).show();


                }


            }

        }
    }
    public void add_target_view()
    {

        search_container.removeAllViews();

            for(int i=0;i<Search_array.size();i++) {
                RelativeLayout target_box = (RelativeLayout) inflater.inflate(R.layout.target_box, null);
                target_box.setOnClickListener(new search_clicked(i));
                TextView target_box_title = (TextView) target_box.findViewById(R.id.target_box_title);
                target_box_title.setText(Search_array.get(i).name);
                search_container.addView(target_box);
            }

    }
    public class search_clicked implements View.OnClickListener
    {
        int btn_num;

        public search_clicked(int btn_num) {
            this.btn_num = btn_num;
        }

        @Override
        public void onClick(View v) {
            Search_array.remove(btn_num);

            add_target_view();

        }
    }





    public void exit_location() {
        Intent intent = new Intent();
        intent.putExtra("Search_array",Search_array);

        setResult(Config.Targetstudy_Search_Select_Activity_RESULT_CODE, intent);


        finish();
    }

}

package com.study.ddokdy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jnm.android.widget.ScalableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 21..
 */
public class Register_Activity extends Activity {
    private static final String TAG = Register_Activity.class.getSimpleName();
    private static final int Anim_Speed = 200;
    String mode, enc_id;

    int anim_height;

    Spinner Email_Spinner, gender_Spinner;
    EditText email_host_Edittext, Name_Edittext, email_front_text, tel_edittext, first_pwd_edittext, second_pwd_edittext, age_edittext;
    ImageView Whole_Check_Box_Image, DDock_Check_Box_Iamge, Personal_Info_Check_Box_Image;
    RelativeLayout Whole_Check_Box_btn, DDock_Check_Box_Btn, Personal_Info_Check_Box_btn, DDock_clause_btn, personal_clause_btn;
    LinearLayout Ddock_clause, personal_info;
    ResizeWidthAnimation anim;
    View.OnFocusChangeListener onfocuslistener;
    TextWatcher watcher;
    Button register_btn;
    ScalableLayout first_pwd_view, second_pwd_view;


    boolean Whole_Check_State, DDock_Check_State, Personal_Info_Check_State, DDock_btn_flag, personal_info_btn_flag, email_select_flag;
    String[] Email_Spinner_Item = {"이메일선택", "직접입력", "asdfasdfasdfasdfasdfasdfasdf 3", "Item 4", "Item 5", "Item 6", "naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "email", "naver.com", "Item 3", "Item 4", "Item 5", "Item 6", "naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "email", "naver.com", "Item 3", "Item 4", "Item 5", "Item 6", "naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"
    };

    String[] Gender_Spinner_Item = {"성별선택", "남", "여"};

    ArrayAdapter<String> email_adapter;
    ArrayAdapter<String> gender_adapter;

    Drawable check_true;
    Drawable check_false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        holdViews();
        setanim();
        setViews();


        register_info_setting();


    }

    public void register_info_setting() {
        Intent intent = getIntent();
        mode = intent.getExtras().getString("mode");
        enc_id = intent.getExtras().getString("enc_id");
        String name = intent.getExtras().getString("name");
        String email = intent.getExtras().getString("email");
        if (mode.equals("social")) {
            Name_Edittext.setText(name);
            Name_Edittext.setEnabled(false);
            if (email != null) {
                String[] email_address = email.split("@");
                set_email_host_state();
                email_front_text.setText(email_address[0]);
                email_front_text.setEnabled(false);
                email_host_Edittext.setText(email_address[1]);
                email_host_Edittext.setEnabled(false);
            }

            first_pwd_view.setVisibility(View.GONE);
            second_pwd_view.setVisibility(View.GONE);
        }


    }

    public void setanim() {
        anim = new ResizeWidthAnimation(Ddock_clause, 0);
        Ddock_clause.startAnimation(anim);
        anim = new ResizeWidthAnimation(personal_info, 0);
        personal_info.startAnimation(anim);
    }

    private void init() {

        onfocuslistener = new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {


            }
        };
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                full_check();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                full_check();
            }

            @Override
            public void afterTextChanged(Editable s) {
                full_check();

            }
        };
        email_select_flag = false;
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        anim_height = (int) ((double) (getApplicationContext().getResources().getDisplayMetrics().heightPixels) * 0.5);

        email_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spiner_item, Email_Spinner_Item);
        gender_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spiner_item, Gender_Spinner_Item);

        Whole_Check_State = false;
        DDock_Check_State = false;
        Personal_Info_Check_State = false;
        check_true = getResources().getDrawable(R.drawable.check_box_true_orange);
        check_false = getResources().getDrawable(R.drawable.check_box_false);

    }

    private void holdViews() {

        first_pwd_view = (ScalableLayout) findViewById(R.id.first_pwd_view);
        second_pwd_view = (ScalableLayout) findViewById(R.id.second_pwd_view);
        age_edittext = (EditText) findViewById(R.id.age_edittext);
        tel_edittext = (EditText) findViewById(R.id.tel_edittext);
        first_pwd_edittext = (EditText) findViewById(R.id.first_pwd_edittext);
        second_pwd_edittext = (EditText) findViewById(R.id.second_pwd_edittext);
        email_front_text = (EditText) findViewById(R.id.email_front_text);
        Name_Edittext = (EditText) findViewById(R.id.Name_Edittext);

        email_host_Edittext = (EditText) findViewById(R.id.Email_Host_Text);
        DDock_clause_btn = (RelativeLayout) findViewById(R.id.DDock_clause_btn);
        personal_clause_btn = (RelativeLayout) findViewById(R.id.personal_clause_btn);
        Ddock_clause = (LinearLayout) findViewById(R.id.DDock_clause_content_view);
        personal_info = (LinearLayout) findViewById(R.id.personal_clause_content_view);
        gender_Spinner = (Spinner) findViewById(R.id.gender_Spinner);
        Email_Spinner = (Spinner) findViewById(R.id.Email_Host_Spinner);
        register_btn = (Button) findViewById(R.id.register_btn);
        Whole_Check_Box_btn = (RelativeLayout) findViewById(R.id.Whole_Check_Box_btn);
        DDock_Check_Box_Btn = (RelativeLayout) findViewById(R.id.DDock_Check_Box_Btn);
        Personal_Info_Check_Box_btn = (RelativeLayout) findViewById(R.id.Personal_Info_Check_Box_btn);
        Whole_Check_Box_Image = (ImageView) findViewById(R.id.Whole_Check_Box_Image);
        DDock_Check_Box_Iamge = (ImageView) findViewById(R.id.DDock_Check_Box_Image);
        Personal_Info_Check_Box_Image = (ImageView) findViewById(R.id.Personal_Info_Check_Box_Image);

    }

    public boolean full_check() {
        boolean first_pwd = !first_pwd_edittext.getText().toString().equals("");
        boolean second_pwd = !second_pwd_edittext.getText().toString().equals("");
        boolean email_host = false;
        boolean gender_flag = false;

        if (mode.equals("social")) {
            first_pwd = true;
            second_pwd = true;
        }
        if (!email_select_flag) {
            email_host = !Email_Spinner.getSelectedItem().toString().equals(Email_Spinner_Item[0]);
        } else {
            email_host = !email_host_Edittext.getText().toString().equals("");
        }
        if (!gender_Spinner.getSelectedItem().toString().equals(Gender_Spinner_Item[0])) {
            gender_flag = true;
        }


        if (second_pwd && first_pwd &&
                email_host && gender_flag &&
                !age_edittext.getText().toString().equals("") &&
                !tel_edittext.getText().toString().equals("") &&
                !email_front_text.getText().toString().equals("") &&
                !Name_Edittext.getText().toString().equals("") && Whole_Check_State) {
            register_btn.setBackground(getResources().getDrawable(R.color.mint_dark));
            register_btn.setEnabled(true);
            return true;
        } else {
            register_btn.setBackground(getResources().getDrawable(R.color.mint));
            register_btn.setEnabled(false);
            return false;
        }
    }

    private void setViews() {
        Name_Edittext.addTextChangedListener(watcher);
        email_host_Edittext.addTextChangedListener(watcher);
        email_front_text.addTextChangedListener(watcher);
        first_pwd_edittext.addTextChangedListener(watcher);
        second_pwd_edittext.addTextChangedListener(watcher);
        tel_edittext.addTextChangedListener(watcher);
        age_edittext.addTextChangedListener(watcher);

        DDock_clause_btn.setOnClickListener(new onbtnclick(6));
        personal_clause_btn.setOnClickListener(new onbtnclick(5));
        register_btn.setOnClickListener(new onbtnclick(4));


        Email_Spinner.setAdapter(email_adapter);
        gender_Spinner.setAdapter(gender_adapter);
        Email_Spinner.setOnItemSelectedListener(new onSpinnerSelectListener(1));
        gender_Spinner.setOnItemSelectedListener(new onSpinnerSelectListener(2));
        Whole_Check_Box_btn.setOnClickListener(new onbtnclick(1));
        DDock_Check_Box_Btn.setOnClickListener(new onbtnclick(2));
        Personal_Info_Check_Box_btn.setOnClickListener(new onbtnclick(3));
    }

    private class onbtnclick implements View.OnClickListener {
        int btn_num;

        public onbtnclick(int btn_num) {
            this.btn_num = btn_num;

        }

        @Override
        public void onClick(View v) {

            if (btn_num == 1) {
                if (Whole_Check_State) {
                    DDock_Check_Box_Iamge.setBackground(check_false);
                    Personal_Info_Check_Box_Image.setBackground(check_false);
                    DDock_Check_State = false;
                    Personal_Info_Check_State = false;
                } else {
                    DDock_Check_Box_Iamge.setBackground(check_true);
                    Personal_Info_Check_Box_Image.setBackground(check_true);
                    DDock_Check_State = true;
                    Personal_Info_Check_State = true;
                }

                Whole_CheckBox();
                full_check();
            } else if (btn_num == 2) {
                if (DDock_Check_State) {
                    DDock_Check_Box_Iamge.setBackground(check_false);

                } else {
                    DDock_Check_Box_Iamge.setBackground(check_true);
                }
                DDock_Check_State = !DDock_Check_State;
                Whole_CheckBox();
                full_check();
            } else if (btn_num == 3) {
                if (Personal_Info_Check_State) {
                    Personal_Info_Check_Box_Image.setBackground(check_false);

                } else {
                    Personal_Info_Check_Box_Image.setBackground(check_true);
                }
                Personal_Info_Check_State = !Personal_Info_Check_State;
                Whole_CheckBox();
                full_check();
            } else if (btn_num == 4) {
                if (first_pwd_edittext.getText().toString().equals(second_pwd_edittext.getText().toString())) {


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //로그인
                            HashMap<String, String> data_set = new HashMap<String, String>();
                            data_set.put("mode", mode);
                            try {
                                data_set.put("user_name", URLEncoder.encode(Name_Edittext.getText().toString(), "utf-8"));

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            data_set.put("email_id", email_front_text.getText().toString());
                            if (email_select_flag) {
                                data_set.put("email_host", email_host_Edittext.getText().toString());
                            } else {
                                data_set.put("email_host", Email_Spinner.getSelectedItem().toString());
                            }
                            data_set.put("user_age", age_edittext.getText().toString());
                            if (gender_Spinner.getSelectedItem().toString().equals(Gender_Spinner_Item[1])) {
                                //남
                                data_set.put("user_gender", "1");
                            } else if (gender_Spinner.getSelectedItem().toString().equals(Gender_Spinner_Item[2])) {
                                //여
                                data_set.put("user_gender", "2");
                            }
                            data_set.put("user_mobile", tel_edittext.getText().toString());

                            if (mode.equals("social")) {
                                data_set.put("loginkey", enc_id);
                            } else if (mode.equals("normal")) {
                                data_set.put("user_pw", first_pwd_edittext.getText().toString());
                            }
                            String postData = ZMethod.Map_to_String(data_set);
                            String url_str = Config.url_home + "join.php/" + postData;
                            Log.d(TAG, "url_str== " + url_str);
                            String data = ZMethod.getStringHttpPost(Config.url_home + "join.php", postData);


                            Log.d(TAG, "data == " + data);

                            if (data != null) {

                                try {
                                    JSONObject json = new JSONObject(data);
                                    Log.d(TAG, "json==" + json.getString("msg"));
                                    final String msg = json.getString("msg");
                                    String resultCode = json.getString("resultCode");

                                    if (resultCode.equals("0")) {
                                        finish();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();

                                            }
                                        });


                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }


                        }
                    }).start();
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인 하세요", Toast.LENGTH_SHORT).show();

                }
            } else if (btn_num == 5) {
                if (!personal_info_btn_flag) {
                    anim = new ResizeWidthAnimation(personal_info, anim_height);
                    anim.setDuration(Anim_Speed);
                    personal_info.startAnimation(anim);
                } else {
                    anim = new ResizeWidthAnimation(personal_info, 0);
                    anim.setDuration(Anim_Speed);
                    personal_info.startAnimation(anim);
                }
                personal_info_btn_flag = !personal_info_btn_flag;
            } else if (btn_num == 6) {
                if (!DDock_btn_flag) {
                    anim = new ResizeWidthAnimation(Ddock_clause, anim_height);
                    anim.setDuration(Anim_Speed);
                    Ddock_clause.startAnimation(anim);
                } else {
                    anim = new ResizeWidthAnimation(Ddock_clause, 0);
                    anim.setDuration(Anim_Speed);
                    Ddock_clause.startAnimation(anim);
                }
                DDock_btn_flag = !DDock_btn_flag;


            }
        }
    }

    public void Whole_CheckBox() {
        if (DDock_Check_State && Personal_Info_Check_State) {
            Whole_Check_State = true;
            Whole_Check_Box_Image.setBackground(check_true);
        } else {
            Whole_Check_State = false;
            Whole_Check_Box_Image.setBackground(check_false);
        }
    }


    public void set_email_host_state() {
        Email_Spinner.setVisibility(View.GONE);
        email_host_Edittext.setVisibility(View.VISIBLE);
        email_select_flag = true;
    }


    public class ResizeWidthAnimation extends Animation {
        private int mHeight;
        private int mStartHeight;
        private View mView;

        public ResizeWidthAnimation(View view, int width) {
            mView = view;
            mHeight = width;
            mStartHeight = view.getHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newWidth = mStartHeight + (int) ((mHeight - mStartHeight) * interpolatedTime);
            mView.getLayoutParams().height = newWidth;
            mView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private class onSpinnerSelectListener implements AdapterView.OnItemSelectedListener {
        int Spinner_num;

        public onSpinnerSelectListener(int spinner_num) {
            this.Spinner_num = spinner_num;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            full_check();

            if (Spinner_num == 1) {
                if (Email_Spinner_Item[position].equals("직접입력")) {
                    set_email_host_state();
                }
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}

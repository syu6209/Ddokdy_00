package com.study.ddokdy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

/**
 * Created by ibyeongmu on 16. 5. 21..
 */
public class Register_Activity extends Activity implements AdapterView.OnItemSelectedListener{
    Spinner Email_Spinner;
    String[] ITEMS = {"email.com", "naver.com", "asdfasdfasdfasdfasdfasdfasdf 3", "Item 4", "Item 5", "Item 6","naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6","email", "naver.com", "Item 3", "Item 4", "Item 5", "Item 6","naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6","email", "naver.com", "Item 3", "Item 4", "Item 5", "Item 6","naver.com", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"
    };
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        holdViews();
        setViews();







    }

    private void init() {
        ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spiner_item, ITEMS);

    }
    private void holdViews() {

        Email_Spinner = (Spinner)findViewById(R.id.Email_Spinner);
    }


    private void setViews() {
        Email_Spinner.setAdapter(arrayAdapter);
        Email_Spinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ((TextView)parent.getChildAt(0)).setTextColor(Color.rgb(96, 91, 85));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

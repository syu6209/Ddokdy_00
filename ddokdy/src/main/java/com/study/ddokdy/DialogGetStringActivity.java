package com.study.ddokdy;

import java.util.regex.Pattern;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZHandler;
import prv.zozi.utils.ZMethod;

public class DialogGetStringActivity extends Dialog {
	Context context;
	ZHandler handler;
	TextView tv_title, tv_label;
	EditText input;
	Button btn;
	LinearLayout line;
	int msgnum;
	String title=null,label = null;
	private int Theme, inputType = 0;
	private String init=null;
	
	public static final int INPUTTYPE_NUMBER = 1;
	// ���ڸ� ���
	protected InputFilter filterNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                Spanned dest, int dstart, int dend) {

                Pattern ps = Pattern.compile("^[0-9]+$");
                if (!ps.matcher(source).matches()) {
                        return "";
                } 
                return null;
        } 
	}; 
	public DialogGetStringActivity(Context context, ZHandler handler, int msgnum) {
		super(context);
		this.context = context;
		this.handler = handler;
		this.msgnum = msgnum;
		title="����� �Է�";
		label = "�Է�";
		Theme = 0;
	}
	public DialogGetStringActivity(Context context, ZHandler handler, int msgnum, String title, String label) {
		super(context);
		this.context = context;
		this.handler = handler;
		this.msgnum = msgnum;
		this.title = title;
		this.label = label;
		Theme = 0;
	}
	public DialogGetStringActivity(Context context, ZHandler handler, int msgnum, String title, String label, int Theme) {
		super(context);
		this.context = context;
		this.handler = handler;
		this.msgnum = msgnum;
		this.title = title;
		this.label = label;
		this.Theme = Theme;
	}
	public void setInputType(int inputType){
		this.inputType = inputType;
	}
	public void setInitializeString(String init){
		this.init = init;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setContentView(R.layout.dialog_get_string);
		
		holdViews();
		setViews();
	}
	public void holdViews(){
		tv_title = (TextView)findViewById(R.id.dialog_gets_title);
		tv_label = (TextView)findViewById(R.id.dialog_gets_label);
		if(inputType==INPUTTYPE_NUMBER){
			input = (EditText)findViewById(R.id.dialog_gets_input);
			input.setVisibility(View.GONE);
			input = (EditText)findViewById(R.id.dialog_gets_input_number);
		}else{
			input = (EditText)findViewById(R.id.dialog_gets_input_number);
			input.setVisibility(View.GONE);
			input = (EditText)findViewById(R.id.dialog_gets_input);	
		}
		btn = (Button)findViewById(R.id.dialog_gets_btn);
		line = (LinearLayout)findViewById(R.id.dialog_getstring_line);
	}
	public void setViews(){
		tv_title.setText(title);
		tv_label.setText(label);
		if(Theme!=0){
			btn.setBackgroundResource(R.drawable.shape_round);
			line.setBackgroundColor(Color.parseColor("#F3927D"));
		}
		if(inputType==INPUTTYPE_NUMBER){
			input.setFilters(new InputFilter[] { filterNum });
		}
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(inputType==INPUTTYPE_NUMBER){
					myOnClick();
				}else{
					if(input.getText().toString().length()>0){
						myOnClick();
					}else{
						ZMethod.toast(context, "������ �Է����ּ���.");
						dismiss();
					}
				}
			}
		});
		if(init!=null){
			if(!"0".equals(init)){
				input.setText(init);
			}
		}
	}
	public void myOnClick(){
		Message msg = handler.obtainMessage();
		msg.arg1 = msgnum;
		msg.obj = input.getText().toString();
		handler.sendMessage(msg);
		dismiss();
	}
	
	
		
	}
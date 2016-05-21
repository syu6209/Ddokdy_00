package com.study.ddokdy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import prv.zozi.utils.Config;
import prv.zozi.utils.ZHandler;
import prv.zozi.utils.ZMethod;

public class SplashActivity extends Activity {
	
	ZHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		init();
		
		holdViews();
		
	}
	
	
	private void init() {
		ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
		handler = new ZHandler(this);
		handler.postDelayed(new MovePage(), 1500);
	}


	private void holdViews() {
		
	}


	private class MovePage implements Runnable{
		public MovePage() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void run() {
			Intent intent = new Intent(getApplication(), LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			SplashActivity.this.finish();
		}
	}
}

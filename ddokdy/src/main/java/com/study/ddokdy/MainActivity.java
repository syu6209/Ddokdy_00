package com.study.ddokdy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

public class MainActivity extends android.support.v4.app.FragmentActivity {
	
	ViewPager viewpager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		holdViews();
		setViews();

		Log.d("Asdsad","Asdasd");


	}



	private void init() {
		ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
		
	}
	private void holdViews() {
		
		viewpager = (ViewPager)findViewById(R.id.viewpager);
	}

	
	private void setViews() {
		viewpager.setAdapter(new VPAdapter(getSupportFragmentManager()));
		viewpager.setCurrentItem(1);
	}
	public class VPAdapter extends FragmentPagerAdapter{
		private final int MAX_PAGE = 3;
		Fragment current = new Fragment();
		public VPAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if(position<0 || MAX_PAGE<=position){
				return null;
			}
			switch(position){
			case 0:
				current = new Fragment_MainSearch(viewpager);
				break;
			case 1:
				current = new Fragment_MainStudyList(viewpager);
				break;
			case 2:
				current = new Fragment_Myhome(viewpager);
				break;
			default:
				return null;
			}
			return current;
		}

		@Override
		public int getCount() {
			return MAX_PAGE;
		}
		
	}
	
	
}

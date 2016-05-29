package com.study.ddokdy;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import prv.zozi.utils.ZHandler;

public class DialogCategoryActivity extends Dialog {
	Activity context;
	ZHandler handler;
	ListView listview;
	MyAdapter adapter;
	int msgnum;
	ArrayList<MakeNewStudyActivity.category> lists;
	String title = null;
	public DialogCategoryActivity(Activity context, ZHandler handler, int msgnum, String title, ArrayList<MakeNewStudyActivity.category> lists) {
		super(context);
		this.context = context;
		this.handler = handler;
		this.msgnum = msgnum;
		this.title = title;
		this.lists = lists;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		setContentView(R.layout.dialog_category);
		holdViews();
	}
	public void holdViews(){
		listview = (ListView)findViewById(R.id.dialog_category_listview);
		
		adapter = new MyAdapter(this.getContext());
		listview.setAdapter(adapter);
		
		if(title!=null){
			TextView tv = (TextView)findViewById(R.id.dialog_category_title);
			tv.setText(title);
		}
		/*
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Message msg = handler.obtainMessage();
				msg.arg1 = msgnum;
				msg.arg2 = position-1;
				handler.sendMessage(msg);
				dismiss();
			}
		});
		*/
		
		
	}
	public void myOnClick(int position){
		Message msg = handler.obtainMessage();
		msg.arg1 = msgnum;
		msg.arg2 = position;
		handler.sendMessage(msg);
		dismiss();
	}
	public void setArrayList(ArrayList<MakeNewStudyActivity.category> arr){
		lists = arr;
		adapter.notifyDataSetChanged();
	}
	private class ViewHolder {
		public TextView title;
		public TextView count;
		public TextView right;
		public ImageView lock;
	}
	private class MyAdapter extends BaseAdapter{
		Context mContext;
		public MyAdapter(Context mContext) {
			this.mContext = mContext;
		}
		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		private class ocl implements View.OnClickListener{
			int position;
			public ocl(int position) {
				this.position = position; 
			}
			@Override
			public void onClick(View v) {
				myOnClick(position);
			}
		}
		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//listview_board_normalbox
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_board_normalbox, null);

			holder.title = (TextView) convertView.findViewById(R.id.boardbox_normal_txt);
			holder.count = (TextView) convertView.findViewById(R.id.boardbox_countbox);
			holder.right = (TextView) convertView.findViewById(R.id.boardbox_normal_right);
			holder.lock = (ImageView) convertView.findViewById(R.id.boardbox_lcon_lock);
		
			convertView.setTag(holder);
			convertView.setClickable(true);

			
			convertView.setOnClickListener(new ocl(position));
			if (holder != null) {

				holder.title.setText(lists.get(position).name);

				holder.lock.setVisibility(View.GONE);
				holder.count.setVisibility(View.GONE);
				holder.right.setVisibility(View.GONE);
			}
			return convertView;
		}
		
	}

}
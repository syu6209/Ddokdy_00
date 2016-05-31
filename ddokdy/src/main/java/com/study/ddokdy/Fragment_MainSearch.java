package com.study.ddokdy;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZLoginInfo;
import prv.zozi.utils.ZMethod;

public class Fragment_MainSearch extends android.support.v4.app.Fragment{
	private Info_Login logininfo;
	TextView tv_topbar_title;
	ImageButton btn_topbar_search;
	PullToRefreshListView mListview;
	ArrayList<KeywordData> mListData;
	ViewPager viewpager;

	private String url_listload = "keyword_list.php";
	private ListViewAdapter adapter;


	private int del_position;
	public Fragment_MainSearch(){

	}

	public Fragment_MainSearch(ViewPager viewpager) {
		this.viewpager = viewpager;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//this.viewpager = getArguments().getParcelable("viewpager");
		ViewGroup v = null;
		LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.fragment_main_search, v);
		init();
		holdViews(ll);
		setViews(ll);
		loadData();
		return ll;
	}

	private void loadData() {
		AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
			@Override
			protected void onPreExecute() {
				mListData.clear();
			}

			@Override
			protected String doInBackground(String... params) {
				String postData = "uid="+logininfo.user_id;
				String result =  ZMethod.getStringHttpPost(Config.url_home+url_listload, postData);

				try {
					JSONObject json = new JSONObject(result);
					int rn = json.getInt("rn");
					JSONArray jsarr = json.getJSONArray("data");
					KeywordData object = null;
					for(int i=0;i<rn;i++){
						json = jsarr.getJSONObject(i);
						object = new KeywordData();
						object.idx = json.getInt("idx");
						object.name = json.getString("name");
						object.subtitle = json.getString("subtitle");
						object.background = json.getInt("background");
						object.count = json.getInt("count");

						mListData.add(object);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return "fail";
				}

				return "ok";
			}

			@Override
			protected void onPostExecute(String s) {
				if(Config.pdialog!=null && Config.pdialog.isShowing()){
					try {
						Config.pdialog.dismiss();
					}catch (Exception e){

					}
				}
				adapter.notifyDataSetChanged();
				mListview.onRefreshComplete();
			}
		};
		task.execute(null,null,null);
	}


	private void init() {
		logininfo = new ZLoginInfo(getContext()).getLoginInfo();
		mListData = new ArrayList<KeywordData>();
		/*
		StudyData object = new StudyData();

		object.name = "졸작 파티";
		object.subtitle = "5월 2일 경대 스터디룸 씨스페이스";
		object.bossname = "옥수진";
		object.status = "진행중";
		object.isMine = true;
		mListData.add(object);

		object = new StudyData();
		object.name = "어휴 노답";
		object.subtitle = "최대한 빠른 자살";
		object.bossname = "신한수";
		object.status = "가입대기";
		object.isMine = false;
		mListData.add(object);

		object = new StudyData();
		object.name = "님들 빨리 자살하져";
		object.subtitle = "최대한 빠른 자살";
		object.bossname = "신한수";
		object.status = "가입대기";
		object.isMine = false;
		mListData.add(object);

		object = new StudyData();
		object.name = "제목 입력 공간";
		object.subtitle = "최신 일정이나 과제가 입력되는 공간";
		object.bossname = "스터디장";
		object.status = "가입대기";
		object.isMine = false;
		mListData.add(object);
		*/
	}
	private void holdViews(LinearLayout ll) {
		tv_topbar_title = (TextView)ll.findViewById(R.id.topbar_centerText);
		btn_topbar_search = (ImageButton)ll.findViewById(R.id.topbar_btn_search);
		mListview = (PullToRefreshListView)ll.findViewById(R.id.ptr_listview);
	}

	
	private void setViews(LinearLayout ll) {
		adapter = new ListViewAdapter(ll.getContext());
		mListview.setAdapter(adapter);
		mListview.setScrollBarDefaultDelayBeforeFade(10);
		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				click_study(position -1);
			}
		});
		btn_topbar_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(2);
			}
		});
		tv_topbar_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(1);
			}
		});
	}

	private void click_study(int position) {
		KeywordData mData = mListData.get(position);
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		intent.putExtra("idx", mData.idx);
		intent.putExtra("title", mData.name);
		intent.putExtra("subtitle", mData.subtitle);
		intent.putExtra("background", mData.background);
		startActivity(intent);
	}

	private void click_detail(int position) {
		KeywordData mData = mListData.get(position);
		//ZMethod.toast(getContext(), mData.name +" 디테일 클릭");
		del_position = position;
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(mData.name+" 삭제");
		dialog.setMessage("선택한 맞춤스터디를 삭제하시겠습니까?");
		dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				delete_keyword();
			}
		});
		dialog.setNegativeButton("취소", null);
		dialog.show();
	}

	private void delete_keyword() {
	}

	private class DetailClickListener implements View.OnClickListener{
		int position;
		public DetailClickListener(int position) {
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			click_detail(position);
		}
	}
	private class KeywordData{
		int idx, count;
		String name,subtitle;
		int background;
		boolean isMine;
	}
	private class ViewHolder{
		ImageView iv_list_icon;
		TextView tv_list_title,tv_list_subtitle,tv_list_bossname,tv_list_status;
		ImageButton btn_list_detail;
		RelativeLayout background;
	}
	private class ListViewAdapter extends BaseAdapter{
		private Context mContext = null;
		public ListViewAdapter(Context mContext) {
			this.mContext = mContext;
		}
		@Override
		public int getCount() {
			return mListData.size()+1;
		}

		@Override
		public Object getItem(int position) {
			return mListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			if(position < mListData.size()){
				ViewHolder holder = null;
				KeywordData mData;
				try{
					mData = mListData.get(position);
				}catch(Exception e){
					mData = new KeywordData();
					mData.name = "오류";
				}
				convertView = inflater.inflate(R.layout.listbox_mainstudy, null);
				
				if(convertView.getTag()==null){
					holder = new ViewHolder();
					holder.tv_list_title = (TextView)convertView.findViewById(R.id.listbox_study_title);
					holder.tv_list_subtitle = (TextView)convertView.findViewById(R.id.listbox_study_subtitle);
					holder.tv_list_bossname = (TextView)convertView.findViewById(R.id.listbox_study_bossname);
					holder.tv_list_status = (TextView)convertView.findViewById(R.id.listbox_study_status);
					holder.iv_list_icon = (ImageView)convertView.findViewById(R.id.listbox_icon_mystudy);
					holder.btn_list_detail = (ImageButton)convertView.findViewById(R.id.listbox_btn_detail);
					holder.background = (RelativeLayout)convertView.findViewById(R.id.listbox_background);
					convertView.setTag(holder);
					
				}else{
					holder = (ViewHolder)convertView.getTag();
				}
				if(holder != null){
					holder.tv_list_title.setText(mData.name);
					//holder.tv_list_subtitle.setText(mData.subtitle);
					holder.tv_list_bossname.setText(mData.subtitle);
					holder.tv_list_status.setText("검색결과 : "+mData.count+"건");

					holder.iv_list_icon.setVisibility(View.GONE);

					holder.btn_list_detail.setOnClickListener(new DetailClickListener(position));
					holder.btn_list_detail.setImageResource(R.drawable.btn_close_black);
					holder.btn_list_detail.setFocusable(false);
					ZMethod.setBoxBackground(holder.background, mData.background);
				}
			}else{
				convertView = inflater.inflate(R.layout.listbox_mainplus, null);
				convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),TargetStudyMakeActivity.class);

						startActivity(intent);

					}
				});
				convertView.setTag(null);
				return convertView;
			}
			return convertView;
		}
		
	}
}

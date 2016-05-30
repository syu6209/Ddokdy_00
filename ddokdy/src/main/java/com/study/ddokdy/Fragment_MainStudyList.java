package com.study.ddokdy;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZLoginInfo;
import prv.zozi.utils.ZMethod;

public class Fragment_MainStudyList extends android.support.v4.app.Fragment{
	private Info_Login logininfo;
	private TextView tv_topbar_title;
	private ImageButton btn_topbar_search,btn_topbar_mypage;
	private PullToRefreshListView mListview;
	private ArrayList<StudyData> mListData;
	private ListViewAdapter adapter;
	private ViewPager viewpager;

	private String url_getStudyList = "study_getlist.php";

	private String jsondata;

	public Fragment_MainStudyList(ViewPager viewpager) {
		this.viewpager = viewpager;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup v = null;
		LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.fragment_main_mystudylist, v);
		init();
		holdViews(ll);
		setViews(ll);
//		loadData();

		return ll;
	}


	@Override
	public void onResume() {
		super.onResume();
	loadData();
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
				String result =  ZMethod.getStringHttpPost(Config.url_home+url_getStudyList, postData);

				try {
					JSONObject json = new JSONObject(result);
					int rn = json.getInt("rn");
					JSONArray jsarr = json.getJSONArray("data");
					StudyData object = null;
					for(int i=0;i<rn;i++){
						json = jsarr.getJSONObject(i);
						object = new StudyData();
						object.title = json.getString("title");
						object.subtitle = json.getString("subtitle");
						object.bossname = json.getString("nick");
						object.status = json.getString("status");
						object.background = json.getInt("background");
						if(json.getInt("ismine")==1) {
							object.isMine = true;
						}else{
							object.isMine = false;
						}
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
					Config.pdialog.dismiss();
				}
				adapter.notifyDataSetChanged();
				mListview.onRefreshComplete();
			}
		};
		task.execute(null,null,null);
	}


	private void init() {
		logininfo = new ZLoginInfo(getContext()).getLoginInfo();
		mListData = new ArrayList<StudyData>();
		
		StudyData object = new StudyData();
		/*
		object.title = "졸작 파티";
		object.subtitle = "5월 2일 경대 스터디룸 씨스페이스";
		object.bossname = "옥수진";
		object.status = "진행중";
		object.isMine = true;
		mListData.add(object);

		object = new StudyData();
		object.title = "어휴 노답";
		object.subtitle = "최대한 빠른 자살";
		object.bossname = "신한수";
		object.status = "가입대기";
		object.isMine = false;
		mListData.add(object);

		object = new StudyData();
		object.title = "님들 빨리 자살하져";
		object.subtitle = "최대한 빠른 자살";
		object.bossname = "신한수";
		object.status = "가입대기";
		object.isMine = false;
		mListData.add(object);

		object = new StudyData();
		object.title = "제목 입력 공간";
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
		btn_topbar_mypage = (ImageButton)ll.findViewById(R.id.topbar_btn_mypage);
		mListview = (PullToRefreshListView)ll.findViewById(R.id.ptr_listview);
	}

	
	private void setViews(LinearLayout ll) {
		adapter = new ListViewAdapter(ll.getContext());
		mListview.setAdapter(adapter);
		mListview.setScrollingWhileRefreshingEnabled(false);
		mListview.setScrollBarDefaultDelayBeforeFade(10);
		mListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData();
			}
		});
		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				click_study(position-1);
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
		btn_topbar_mypage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(0);
			}
		});
	}
	private void click_study(int position) {

		Intent intent = null;
		if(position<mListData.size()) {
			StudyData mData = mListData.get(position);
			intent = new Intent(getActivity(), StudyMainActivity.class);
			intent.putExtra("idx", mData.idx);
			startActivity(intent);
		}else{
			intent = new Intent(getActivity(), MakeNewStudyActivity.class);
			startActivity(intent);
		}
	}
	private void click_detail(int position) {
		StudyData mData = mListData.get(position);
		ZMethod.toast(getContext(), mData.title+" 디테일 클릭");
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
	private class StudyData{
		String idx;
		String title,subtitle,bossname,status;
		int background;
		boolean isMine;
	}
	private class ViewHolder{
		ImageView iv_list_icon;
		TextView tv_list_title,tv_list_subtitle,tv_list_bossname,tv_list_status;
		ImageButton btn_list_detail;
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
				StudyData mData;
				try{
					mData = mListData.get(position);
				}catch(Exception e){
					mData = new StudyData();
					mData.title = "오류";
				}
				ViewGroup v = null;
				convertView = inflater.inflate(R.layout.listbox_mainstudy, v);

				if(convertView.getTag()==null){
					holder = new ViewHolder();
					holder.tv_list_title = (TextView)convertView.findViewById(R.id.listbox_study_title);
					holder.tv_list_subtitle = (TextView)convertView.findViewById(R.id.listbox_study_subtitle);
					holder.tv_list_bossname = (TextView)convertView.findViewById(R.id.listbox_study_bossname);
					holder.tv_list_status = (TextView)convertView.findViewById(R.id.listbox_study_status);
					holder.iv_list_icon = (ImageView)convertView.findViewById(R.id.listbox_icon_mystudy);
					holder.btn_list_detail = (ImageButton)convertView.findViewById(R.id.listbox_btn_detail);
					convertView.setTag(holder);

				}else{
					holder = (ViewHolder)convertView.getTag();
				}
				if(holder != null){
					holder.tv_list_title.setText(mData.title);
					holder.tv_list_subtitle.setText(mData.subtitle);
					holder.tv_list_bossname.setText(mData.bossname);
					holder.tv_list_status.setText(mData.status);
					if(mData.isMine){
						holder.iv_list_icon.setVisibility(View.VISIBLE);
					}else{
						holder.iv_list_icon.setVisibility(View.GONE);
					}
					holder.btn_list_detail.setOnClickListener(new DetailClickListener(position));
					holder.btn_list_detail.setFocusable(false);
				}
			}else{
				convertView = inflater.inflate(R.layout.listbox_mainplus, null);
				convertView.setTag(null);
				return convertView;
			}
			return convertView;
		}

	}//end of ListViewAdapter Class
}

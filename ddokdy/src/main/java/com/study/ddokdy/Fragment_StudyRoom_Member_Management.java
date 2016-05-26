package com.study.ddokdy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ibyeongmu on 16. 5. 26..
 */
public class Fragment_StudyRoom_Member_Management extends Fragment {
    private static final String TAG = Fragment_StudyRoom_Member_Management.class.getSimpleName();
    PullToRefreshListView mListview;
    ArrayList<Member_Mangement_data> mListData;
    TextView management_select_date ;
    RelativeLayout StudyRoom_Member_Management_Date_Btn;
    Fragment_StudyRoom_Member_Management_Listener mListener;
    ListViewAdapter adapter;

    public interface Fragment_StudyRoom_Member_Management_Listener
    {
        public void Call_StudyRoom_management_Date_View();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (Fragment_StudyRoom_Member_Management_Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }


    private class DetailClickListener implements View.OnClickListener{
        int position;
        public DetailClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            mListData.set(position, new Member_Mangement_data(mListData.get(position).Name, !mListData.get(position).attend_flag));
            adapter.notifyDataSetChanged();


        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_studyroom_member_management,container,false);
        init();
        holdViews(v);
        setViews(v);

        return v;
    }

    private void init() {
        mListData = new ArrayList<Member_Mangement_data>();
        mListData.add(new Member_Mangement_data("카",true));
        mListData.add(new Member_Mangement_data("ㅁㄴㅁㄴㅇ",false));

        mListData.add(new Member_Mangement_data("ㅇ", true));

        mListData.add(new Member_Mangement_data("ㅈ", true));

        mListData.add(new Member_Mangement_data("ㅂ", true));





    }
    private void holdViews(View ll) {
        mListview = (PullToRefreshListView)ll.findViewById(R.id.StudyRoom_Member_Management_PullToRefreshListView);
        StudyRoom_Member_Management_Date_Btn = (RelativeLayout)ll.findViewById(R.id.StudyRoom_Member_Management_Date_Btn);

        management_select_date = (TextView)ll.findViewById(R.id.management_select_date);

    }
    private void setViews(View ll) {
         adapter = new ListViewAdapter(getActivity());
        mListview.setAdapter(adapter);
        mListview.setScrollBarDefaultDelayBeforeFade(10);
        set_today_date(management_select_date);

        StudyRoom_Member_Management_Date_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.Call_StudyRoom_management_Date_View();


            }
        });


    }

    public void set_today_date(TextView v)
    {
        v.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date(System.currentTimeMillis())));
    }
    private class Member_Mangement_data{
        String Name;
        Boolean attend_flag;

        public Member_Mangement_data(String name, Boolean attend_flag) {
            Name = name;
            this.attend_flag = attend_flag;
        }
    }
    private class ViewHolder{
        ImageView profile_Image;
        RelativeLayout attend_flag_Image,listbox_studyroom_member_management_attend_Btn;
        TextView attend_flag_text,Name_text;


    }
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        public ListViewAdapter(Context mContext) {
            this.mContext = mContext;
        }
        @Override
        public int getCount() {
            return mListData.size();
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
                    Member_Mangement_data mData;
                    try{
                        mData = mListData.get(position);
                    }catch(Exception e){
                        mData = new Member_Mangement_data("asd",true);
                        mData.Name = "오류";
                    }
                    ViewGroup v = null;
                    convertView = inflater.inflate(R.layout.listbox_studyroom_member_management, v);

                    if(convertView.getTag()==null){
                        holder = new ViewHolder();
                        holder.listbox_studyroom_member_management_attend_Btn = (RelativeLayout)convertView.findViewById(R.id.listbox_studyroom_member_management_attend_Btn);
                        holder.profile_Image = (ImageView)convertView.findViewById(R.id.listbox_studyroom_member_management_profile_Image);
                        holder.attend_flag_Image=(RelativeLayout)convertView.findViewById(R.id.listbox_studyroom_member_management_attend_Image);
                        holder.attend_flag_text=(TextView)convertView.findViewById(R.id.listbox_studyroom_member_management_attend_text);
                        holder.Name_text=(TextView)convertView.findViewById(R.id.listbox_studyroom_member_management_name_text);
                        convertView.setTag(holder);

                    }else{
                        holder = (ViewHolder)convertView.getTag();
                    }
                    if(holder != null){
                        if(mData.attend_flag)
                        {
                            holder.attend_flag_Image.setBackground(getResources().getDrawable(R.drawable.shape_round_mint));
                            holder.attend_flag_text.setText(R.string.text_attend);

                        }
                        else
                        {
                            holder.attend_flag_Image.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                            holder.attend_flag_text.setText(R.string.text_do_not_attend);
                        }
                        holder.listbox_studyroom_member_management_attend_Btn.setOnClickListener(new DetailClickListener(position));


                        holder.Name_text.setText(mData.Name);

                    }
            }else{
                convertView = inflater.inflate(R.layout.listbox_mainplus, null);
                convertView.setTag(null);
                return convertView;
            }
            return convertView;
        }

    }


}

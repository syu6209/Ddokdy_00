package com.study.ddokdy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by ibyeongmu on 16. 5. 24..
 */
public class Fragment_StudyRoom_Member extends Fragment{
    private static final String TAG = Fragment_StudyRoom_Member.class.getSimpleName();


    StudyRoom_Listener mListener;
    ArrayList<Member_data> mListData;
    PullToRefreshListView mListview;




    public interface StudyRoom_Listener
    {
        public void On_Study_Room();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_studyroom_member,container,false);

        mListener.On_Study_Room();
        ///// study_room_activity     circle 버튼 안보이게
        init();
        holdViews(v);
        setViews( v);




        return v;
    }
    private void init() {
        mListData = new ArrayList<Member_data>();
        mListData.add(new Member_data(1,10,"이름","내q용",true));
        mListData.add(new Member_data(35,40,"ads","내asdasdasd용",false));
        mListData.add(new Member_data(342,1000,"zxcv","내용",true));
        mListData.add(new Member_data(134,200,"이름","내용",false));
        mListData.add(new Member_data(10,10,"이름","내용",true));
        mListData.add(new Member_data(1,10,"이름","내asdasdasd용",false));
        mListData.add(new Member_data(1, 10, "이asdasd름", "내용", true));
        mListData.add(new Member_data(1, 10, "이asd름", "내용asdasd", false));
        mListData.add(new Member_data(1, 10, "이asd름", "내용", true));
        mListData.add(new Member_data(1, 10, "이asd름", "내용", false));






    }
    private void holdViews(View v) {
        mListview = (PullToRefreshListView)v.findViewById(R.id.StudyRoom_Member_PullToRefreshListView);
    }
    private void setViews(View ll) {
        ListViewAdapter adapter = new ListViewAdapter(getActivity());
        mListview.setAdapter(adapter);
        mListview.setScrollBarDefaultDelayBeforeFade(10);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Click_Member(position - 1);
            }
        });

    }

    public void Click_Member(int position)
    {
        Toast.makeText(getActivity(),"position== "+position,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (StudyRoom_Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    private class Member_data{
        double attendance_count,attendance_total;
        String name,content;
        boolean master_flag;

        public Member_data(int attendance_count, int attendance_total, String name, String content, boolean master_flag) {
            this.attendance_count = attendance_count;
            this.attendance_total = attendance_total;
            this.name = name;
            this.content = content;
            this.master_flag = master_flag;
        }
    }
    private class ViewHolder{
        ImageView StudyRoom_Memeber_listbox_Profile_Image;

        TextView StudyRoom_Memeber_listbox_Name_Text,StudyRoom_Memeber_listbox_Content_Text,StudyRoom_Memeber_listbox_attendance_text;
        RelativeLayout StudyRoom_Memeber_listbox_Btn,StudyRoom_Memeber_listbox_attendance_graph,StudyRoom_Memeber_listbox_Master_Flag;


    }

    private class DetailClickListener implements View.OnClickListener{
        int position;
        public DetailClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            Log.d(TAG,"asdas");

        }
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
                Member_data mData;
                try{
                    mData = mListData.get(position);
                }catch(Exception e){
                    mData = new Member_data(1,10,"이름","내용",true);
                    mData.name = "오류";
                }
                ViewGroup v = null;
                convertView = inflater.inflate(R.layout.listbox_studyroom_member, v);

                if(convertView.getTag()==null){
                    holder = new ViewHolder();
                    holder.StudyRoom_Memeber_listbox_Profile_Image = (ImageView)convertView.findViewById(R.id.StudyRoom_Memeber_listbox_Profile_Image);
                    holder.StudyRoom_Memeber_listbox_attendance_text=(TextView)convertView.findViewById(R.id.StudyRoom_Memeber_listbox_attendance_text);
                    holder.StudyRoom_Memeber_listbox_attendance_graph=(RelativeLayout)convertView.findViewById(R.id.StudyRoom_Memeber_listbox_attendance_graph);
                    holder.StudyRoom_Memeber_listbox_Master_Flag=(RelativeLayout)convertView.findViewById(R.id.StudyRoom_Memeber_listbox_Master_Flag);
                    holder.StudyRoom_Memeber_listbox_Content_Text=(TextView)convertView.findViewById(R.id.StudyRoom_Memeber_listbox_Content_Text);
                    holder.StudyRoom_Memeber_listbox_Btn= (RelativeLayout) convertView.findViewById(R.id.StudyRoom_Memeber_listbox_Btn);
                    holder.StudyRoom_Memeber_listbox_Name_Text= (TextView) convertView.findViewById(R.id.StudyRoom_Memeber_listbox_Name_Text);
                    convertView.setTag(holder);

                }else{
                    holder = (ViewHolder)convertView.getTag();
                }
                if(holder != null){
//                   holder.StudyRoom_Memeber_listbox_Profile_Image = (ImageView)convertView.findViewById(R.id.StudyRoom_Memeber_listbox_Profile_Image);

                    Double rate = (mData.attendance_count/mData.attendance_total);
                    holder.StudyRoom_Memeber_listbox_attendance_text.setText("출석(" + (int) (rate * 100) + "%)");


                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,148, getActivity().getResources().getDisplayMetrics());
                    int attendance_total_width =  getActivity().getResources().getDisplayMetrics().widthPixels- px;
                    attendance_total_width  = (int) (attendance_total_width*(rate));
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(attendance_total_width, ViewGroup.LayoutParams.MATCH_PARENT);
                    holder.StudyRoom_Memeber_listbox_attendance_graph.setLayoutParams(params);

                    holder.StudyRoom_Memeber_listbox_Btn.setOnClickListener(new DetailClickListener(position));


                    if(mData.master_flag) {
                        holder.StudyRoom_Memeber_listbox_Master_Flag.setVisibility(View.VISIBLE);
                    }
                    else {
                        holder.StudyRoom_Memeber_listbox_Master_Flag.setVisibility(View.GONE);
                    }


                    holder.StudyRoom_Memeber_listbox_Content_Text.setText(mData.content);
                    holder.StudyRoom_Memeber_listbox_Name_Text.setText(mData.name);

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

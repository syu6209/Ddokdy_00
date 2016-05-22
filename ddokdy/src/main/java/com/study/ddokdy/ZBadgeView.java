package com.study.ddokdy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import prv.zozi.utils.ZoziBitmapUtils;

/**
 * Created by syu62 on 2016-05-22.
 */
public class ZBadgeView extends LinearLayout {

    RelativeLayout rl;
    TextView title,count;

    public ZBadgeView(Context context) {
        super(context);
        init();
    }

    public ZBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_zbadgeview, this, false);
        addView(v);

        rl = (RelativeLayout)findViewById(R.id.badge_background);
        title = (TextView)findViewById(R.id.badge_tv_title);
        count = (TextView)findViewById(R.id.badge_tv_count);


    }
    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZBadgeView);
        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ZBadgeView, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {


        int bg_color = typedArray.getColor(R.styleable.ZBadgeView_backgroundColor, Color.parseColor("#605B55"));
        rl.setBackgroundColor(bg_color);

        int text_color = typedArray.getColor(R.styleable.ZBadgeView_badgeTextColor, Color.WHITE);
        float text_size = typedArray.getFloat(R.styleable.ZBadgeView_badgeTextSize, ZoziBitmapUtils.dpToPx(getContext(), 11));

        count.setTextColor(text_color);
        count.setTextSize(text_size);
        count.setText("");

        text_color = typedArray.getColor(R.styleable.ZBadgeView_textColor, Color.WHITE);
        text_size = typedArray.getFloat(R.styleable.ZBadgeView_textSize, ZoziBitmapUtils.dpToPx(getContext(), 11));
        String text =  typedArray.getString(R.styleable.ZBadgeView_text);
        title.setTextColor(text_color);
        title.setTextSize(text_size);
        title.setText(text);

        typedArray.recycle();

    }

    /**
     *
     * @param num 뱃지 카운트
     *            same as {@link #setCountNum(int)} and {@link #setCountNum(String)}
     *           if num==0 then hide badgeText.
     */
    public void setBadgeNum(int num){
        setCountNum(num);
    }
    /**
     * @param num 뱃지 카운트
     *            same as {@link #setCountNum(int)} and {@link #setCountNum(String)}
     *            if num==0 then hide badgeText.
     */
    public void setBadgeNum(String num){
        setCountNum(num);
    }
    /**
     * @param num 뱃지 카운트
     *            same as {@link #setBadgeNum(int)} and {@link #setBadgeNum(String)}
     *            if num==0 then hide badgeText.
     */
    public void setCountNum(String num){
        if("0".equals(num)){
            count.setVisibility(View.GONE);
        }else{
            count.setVisibility(View.VISIBLE);
            count.setText(num);
        }
    }
    /**
     * @param num 뱃지 카운트
     *            same as {@link #setBadgeNum(int)} and {@link #setBadgeNum(String)}
     *            if num==0 then hide badgeText.
     */
    public void setCountNum(int num){
        if(num==0){
            count.setVisibility(View.GONE);
        }else{
            count.setVisibility(View.VISIBLE);
            count.setText(""+num);
        }
    }
    public void setText(String name){
        title.setText(name);
    }
    public void setTextColor(int color){
        title.setTextColor(color);
    }
    public void setBadgeTextColor(int color){
        count.setTextColor(color);
    }
    public void setTextSize(float size){
        title.setTextSize(size);
    }
    public void setBadgeTextSize(float size){
        count.setTextSize(size);
    }
}

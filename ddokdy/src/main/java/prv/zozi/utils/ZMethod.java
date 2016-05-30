package prv.zozi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.study.ddokdy.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class ZMethod {

	public static void setBoxBackground(View v, int n){
		switch (n){
			case 1:
				v.setBackgroundResource(R.drawable.box_background_01);
				break;
			case 2:
				v.setBackgroundResource(R.drawable.box_background_02);
				break;
			case 3:
				v.setBackgroundResource(R.drawable.box_background_03);
				break;
			default:
				v.setBackground(null);
				break;
		}
	}
	public static void toast(Context ctx, String msg) {
		try {
			Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
			toast.show();
		} catch (Exception e) {

		}
	}
	@SuppressLint("NewApi")
	public static void setStatusColor(Activity a, int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
			a.getWindow().setStatusBarColor(color);
		}
	}

	/**
	 * sql 형식의 dateTime 을 넣으면 (Ex 2016-05-06 14:42:00 )
	 * 방금막, 1분전, 2분전....1시간전전으로 리턴
	 * @param sqlTime sql 형식의 datetime
	 * @return 결과 스트링
     */
	public String getTimeString(String sqlTime){
		final int SEC = 60;
		final int MIN = 60;
		final int HOUR = 24;
		final int DAY = 30;
		final int MONTH = 12;
		String msg = sqlTime;
		try {
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
			java.util.Date tempDate = format.parse(sqlTime);

			long curTime = System.currentTimeMillis();
			long regTime = tempDate.getTime();
			long diffTime = (curTime - regTime) / 1000;

			if (diffTime < SEC) {
				// sec
				msg = "방금 전";
			} else if ((diffTime /= SEC) < MIN) {
				// min
				msg = diffTime + "분 전";
			} else if ((diffTime /= MIN) < HOUR) {
				// hour
				msg = (diffTime) + "시간 전";
			} else if ((diffTime /= HOUR) < DAY) {
				// day
				msg = (diffTime) + "일 전";
			} else if ((diffTime /= DAY) < MONTH) {
				// day
				msg = (diffTime) + "달 전";
			} else {
				msg = (diffTime) + "년 전";
			}

		}catch (ParseException e) {
			e.printStackTrace();
		}
		return msg;
	}
	public static String  Map_to_String (HashMap<String,String>  data_set)
	{
		String result_data = "";
		Iterator<String> keys = data_set.keySet().iterator();

		while( keys.hasNext() ){
			String key = keys.next();
			result_data =result_data+key+"="+data_set.get(key);
			if(keys.hasNext())
			{
				result_data=result_data+"&";
			}
		}
		return result_data;



	}

	public static String getStringHttpPost(String url, String postData) {
		StringBuilder jsonHtml = new StringBuilder();

		try {

			URL target_url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) target_url.openConnection();
			if (conn != null) {

				conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);

				OutputStream os = conn.getOutputStream();
				os.write(postData.getBytes("UTF-8"));
				os.flush();
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
					for (;;) {
						String line = br.readLine();

						if (line == null)
							break;
						jsonHtml.append(line + "\n");
					}
					br.close();
				}
				os.close();
				conn.disconnect();
			}
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

		return jsonHtml.toString();
	}
}
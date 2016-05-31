package prv.zozi.utils;

import android.app.ProgressDialog;

public class Config {
	public static ProgressDialog pdialog = null;
	public static int loadingState = 0;
	public static int downloadQue = 0,downloadQueLimit=2;//2 max : 3

	public static String url_home = "http://syu6209.cafe24.com/study/";



	public static String Color_orange = "#FFBC91";
	public static String Color_blackgray = "#605B55";


	/////////////// 네이버 info
	public static String NAVER_CLIENT_ID = "YrmSUYmdbnPnuTogsfSG";
	public static String NAVER_CLIENT_SECRET = "nqaHKanINo";
	public static String NAVER_CLIENT_NAME = "똑디";
	public static String NAVER_CALLBACK_INTENT = "com.study.ddockdy";
	/////////////// 네이버 info


	public final static int Targetstudy_Location_Select_Activity_RESULT_CODE =0;
	public final static int Targetstudy_Field_Activity_RESULT_CODE =1;
	public final static int Targetstudy_Search_Select_Activity_RESULT_CODE =2;




}

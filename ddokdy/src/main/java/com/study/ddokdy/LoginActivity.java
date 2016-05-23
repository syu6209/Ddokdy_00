package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

public class LoginActivity extends Activity {
	private final static String TAG = LoginActivity.class.getSimpleName();

	LinearLayout btn_login_kakao, btn_login_naver;
	private static OAuthLogin mOAuthLoginInstance;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;
	public Context mContext;

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.mContext = getApplicationContext();
		init();
		holdViews();
		setListeners();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	private void init() {
		ZMethod.setStatusColor(this, Color.parseColor(Config.Color_orange));
	}

	private void holdViews() {
		btn_login_kakao = (LinearLayout) findViewById(R.id.btnll_login_kakao);
		btn_login_naver = (LinearLayout) findViewById(R.id.btnll_login_naver);
	}

	private void setListeners() {
		btn_login_naver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				login_with_naver();
			}
		});
		btn_login_kakao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				login_with_kakao();
			}
		});
	}

	private void login_with_naver() {
		initNaverData();
		mOAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);


	}

	private void login_with_kakao() {

		Intent intent = new Intent(getApplication(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
//		gotoMain();

	}

	private void gotoMain() {
		Intent intent = new Intent(getApplication(), Register_Activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}

	private void initNaverData() {
		Log.d(TAG,"initNaverData");

		mOAuthLoginInstance = OAuthLogin.getInstance();
		Log.d(TAG,"2");

		mOAuthLoginInstance.init(LoginActivity.this, Config.NAVER_CLIENT_ID, Config.NAVER_CLIENT_SECRET, Config.NAVER_CLIENT_NAME, Config.NAVER_CALLBACK_INTENT);
		Log.d(TAG, "3");

	}

	private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
		@Override
		public void run(boolean success) {

			if (success) {
				Log.d(TAG, "refreshToken == " );

				String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
				String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
				long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
				String tokenType = mOAuthLoginInstance.getTokenType(mContext);
				Log.d(TAG, "accessToken == " + accessToken);
				Log.d(TAG, "refreshToken == " + refreshToken);
				Log.d(TAG, "expiresAt == " + expiresAt);
				Log.d(TAG, "tokenType == " + tokenType);
				new RequestApiTask().execute();
			} else {
				Log.d(TAG, "failfail");

				String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
				String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
				Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
			}
		}

		;
	};

	public class RequestApiTask extends AsyncTask<Void, Void, Void> {

		String state;
		String enc_id;
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			String url = "https://apis.naver.com/nidlogin/nid/getHashId_v2.xml";
			String at = mOAuthLoginInstance.getAccessToken(mContext);
			Pasingversiondata(mOAuthLoginInstance.requestApi(mContext, at, url));
			return null;
		}

		protected void onPostExecute(Void content) {
			if(state.equals("success")) {
				gotoMain();
			}
			else
			{
				Log.d(TAG,"asdasd");

			}

		}

		private void Pasingversiondata(String data) { // xml 파싱
			String f_array[] = new String[2];
			try {
				XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
				XmlPullParser parser = parserCreator.newPullParser();
				InputStream input = new ByteArrayInputStream(data.getBytes("UTF-8"));
				parser.setInput(input, "UTF-8");
				int parserEvent = parser.getEventType();
				String tag;
				boolean inText = false;
				int colIdx = 0;
				while (parserEvent != XmlPullParser.END_DOCUMENT) {

					switch (parserEvent) {
						case XmlPullParser.START_TAG:
							tag = parser.getName();
							if (tag.compareTo("xml") == 0) {
								inText = false;
							} else if (tag.compareTo("data") == 0) {
								inText = false;
							} else if (tag.compareTo("result") == 0) {
								inText = false;
							} else if (tag.compareTo("resultcode") == 0) {
								inText = false;
							} else if (tag.compareTo("response") == 0) {
								inText = false;
							} else {
								inText = true;

							}
							break;
						case XmlPullParser.TEXT:
							if (inText) {
								if (parser.getText() == null) {
									f_array[colIdx] = "";
								} else {
									f_array[colIdx] = parser.getText().trim();
								}
								colIdx++;
							}
							inText = false;
							break;
						case XmlPullParser.END_TAG:
							inText = false;
							break;
					}
					parserEvent = parser.next();
				}
			} catch (Exception e) {
				Log.e("dd", "Error in network call", e);
			}
			state =f_array[0];
			enc_id = f_array[1];
			Log.d(TAG, "state == " + f_array[0]);
			Log.d(TAG, "enc_id == " + f_array[1]);


		}

	}
}

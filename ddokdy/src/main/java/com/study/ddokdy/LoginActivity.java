package com.study.ddokdy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import prv.zozi.utils.Config;
import prv.zozi.utils.ZMethod;

public class LoginActivity extends Activity {
    private final static String TAG = LoginActivity.class.getSimpleName();


    LinearLayout btn_login_kakao, btn_login_naver, btnll_login_facebook;
    RelativeLayout Auto_Login_Btn;
    ImageView Auto_Login_Image;

    boolean Auto_Login_Flag = false;

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
        Auto_Login_Btn = (RelativeLayout) findViewById(R.id.Auto_Login_Btn);
        Auto_Login_Image = (ImageView) findViewById(R.id.Auto_Login_Image);
        btnll_login_facebook = (LinearLayout) findViewById(R.id.btnll_login_facebook);


    }

    private void setListeners() {
        Auto_Login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Auto_Login_Flag) {
                    Auto_Login_Image.setBackground(getResources().getDrawable(R.drawable.check_box_false));
                } else {
                    Auto_Login_Image.setBackground(getResources().getDrawable(R.drawable.check_box_true_gray));

                }
                Auto_Login_Flag = !Auto_Login_Flag;

            }
        });
        btn_login_naver.setOnClickListener(new btn_click(1));
        btn_login_kakao.setOnClickListener(new btn_click(2));
        btnll_login_facebook.setOnClickListener(new btn_click(3));
    }
    private class btn_click implements View.OnClickListener {
        int btn_num;
        public btn_click(int btn_num) {
            this.btn_num = btn_num;

        }
        @Override
        public void onClick(View v) {
            if (btn_num == 1) {
                login_with_naver();

            } else if (btn_num == 2) {
                login_with_kakao();
            } else if (btn_num == 3) {
                Intent intent = new Intent(getApplication(), Register_Activity.class);
                intent.putExtra("mode", "normal");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
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

        }


        private void initNaverData() {
            Log.d(TAG, "initNaverData");

            mOAuthLoginInstance = OAuthLogin.getInstance();
            Log.d(TAG, "2");

            mOAuthLoginInstance.init(LoginActivity.this, Config.NAVER_CLIENT_ID, Config.NAVER_CLIENT_SECRET, Config.NAVER_CLIENT_NAME, Config.NAVER_CALLBACK_INTENT);
            Log.d(TAG, "3");

        }

        private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {

                if (success) {
                    Log.d(TAG, "refreshToken == ");

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
            String email;
            String name;

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Void doInBackground(Void... params) {
//			String url = "https://apis.naver.com/nidlogin/nid/getHashId_v2.xml";
//			String at = mOAuthLoginInstance.getAccessToken(mContext);
//			Pasingversiondata(mOAuthLoginInstance.requestApi(mContext, at, url));
                String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
                String at = mOAuthLoginInstance.getAccessToken(mContext);
                Pasingversiondata(mOAuthLoginInstance.requestApi(mContext, at, url));
                return null;
            }

            protected void onPostExecute(Void content) {
                if (state.equals("success")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //로그인
                            HashMap<String, String> data_set = new HashMap<String, String>();
                            data_set.put("mode", "social");
                            data_set.put("user_login", enc_id);
                            String postData = ZMethod.Map_to_String(data_set);
                            String url_str = Config.url_home + "login.php/" + postData;
                            Log.d(TAG, "url_str== " + url_str);
                            String data = ZMethod.getStringHttpPost(Config.url_home + "login.php", postData);


                            if (data != null) {
                                try {
                                    JSONObject json = new JSONObject(data);
                                    String resultCode = json.getString("resultCode");
                                    String msg = json.getString("msg");
                                    Log.d(TAG, " resultCode ==" + resultCode);
                                    Log.d(TAG, " msg ==" + msg);
                                    if (resultCode.equals("4")) {
                                        // 회원 가입 으로 이동
                                        Intent intent = new Intent(getApplication(), Register_Activity.class);
                                        intent.putExtra("mode", "social");
                                        intent.putExtra("enc_id", enc_id);
                                        intent.putExtra("email", email);
                                        intent.putExtra("name", name);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    } else if (resultCode.equals("0")) {
                                        String user_id = json.getString("user_id");
                                        String user_age = json.getString("user_age");
                                        String user_gender = json.getString("user_gender");

                                        String user_agree = json.getString("user_agree");
                                        String user_name = json.getString("user_name");

                                        String user_nick = json.getString("user_mobile");
                                        String user_profile = json.getString("user_profile");

                                        String user_registered = json.getString("user_registered");
                                        Log.d(TAG, "user_id ==      " + user_id);
                                        Log.d(TAG, "user_age ==      " + user_age);
                                        Log.d(TAG, "user_gender ==      " + user_gender);
                                        Log.d(TAG, "user_agree ==      " + user_agree);
                                        Log.d(TAG, "user_name ==      " + user_name);
                                        Log.d(TAG, "user_nick ==      " + user_nick);
                                        Log.d(TAG, "user_registered ==      " + user_registered);
                                        Log.d(TAG, "user_profile ==      " + user_profile);


                                    } else if (resultCode.equals("1")) {
                                        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                    }).start();

//				gotoMain();

                } else {
                    Log.d(TAG, "asdasd");
                }
            }

            private void Pasingversiondata(String data) { // xml 파싱
                Log.d(TAG, "data == " + data);
                String f_array[] = new String[10];
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
                state = f_array[0];
                email = f_array[1];
                enc_id = f_array[3];
                name = f_array[8];

                Log.d(TAG, "state == " + f_array[0]);
                Log.d(TAG, "email == " + f_array[1]);
                Log.d(TAG, "nickname == " + f_array[2]);
                Log.d(TAG, "enc_id == " + f_array[3]);
                Log.d(TAG, "profile_image == " + f_array[4]);
                Log.d(TAG, "age == " + f_array[5]);
                Log.d(TAG, "gender == " + f_array[6]);
                Log.d(TAG, "id == " + f_array[7]);
                Log.d(TAG, "name == " + f_array[8]);

            }

        }

        public class ResizeWidthAnimation extends Animation {
            private int mHeight;
            private int mStartHeight;
            private View mView;

            public ResizeWidthAnimation(View view, int width) {
                mView = view;
                mHeight = width;
                mStartHeight = view.getHeight();
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int newWidth = mStartHeight + (int) ((mHeight - mStartHeight) * interpolatedTime);
                mView.getLayoutParams().height = newWidth;
                mView.requestLayout();
            }

            @Override
            public void initialize(int width, int height, int parentWidth, int parentHeight) {
                super.initialize(width, height, parentWidth, parentHeight);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        }


    }

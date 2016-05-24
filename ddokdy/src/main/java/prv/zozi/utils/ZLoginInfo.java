package prv.zozi.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by syu62 on 2016-05-24.
 */
public class ZLoginInfo {
    Context mContext;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    /**
     * 로그인 정보를 저장하고 가져오는 클래스
     * 서로 무슨 key를 사용했는지 사용한 key값을 여기다가 적으셈
     *
     * 자동로그인 : isAuto
     * @param context
     */
    public ZLoginInfo(Context context){
        this.mContext = context;
        init();
    }
    public void init(){
        sp = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * Login정보에 파일에
     * key라는 이름의 String data 를 저장함.
     * 한번 저장하면 앱을 삭제하거나 캐시삭제하기전까지
     * getString(key)로 가져올 수 있음
     * @param key 키
     * @param data 데이터
     */
    public void saveData(String key, String data){

        editor.putString(key, data);

        editor.commit();
    }
    public void saveData(String key, boolean data){
        editor.putBoolean(key, data);

        editor.commit();
    }


    /**
     * Login정보에 파일에
     * key라는 이름의 Int data 를 저장함.
     * 한번 저장하면 앱을 삭제하거나 캐시삭제하기전까지
     * getInt(key)로 가져올 수 있음.
     * @param key
     * @param data
     */
    public void saveData(String key, int data){

        editor.putInt(key, data);

        editor.commit();
    }

    /**
     * Login정보파일에 있는 데이터중
     * key라는 이름으로 저장한 String data 를 가져옴.
     * 없으면 null을 리턴하니까 try-catch나 if문으로 null 체크를 해야함.
     * @param key
     * @return
     */
    public String getString(String key){
        return sp.getString(key, null);
    }
    /**
     * Login정보파일에 있는 데이터중
     * key라는 이름으로 저장한 Int data 를 가져옴.
     * 없으면 -44를 리턴. try-catch나 if문으로 -44를 체크를 해야함.
     * @param key
     * @return
     */
    public int getInt(String key){
        return sp.getInt(key, -44);
    }
    /**
     * 값이 없으면 def(두번째 파라미터)를 리턴.
     * @param key
     * @return
     */
    public int getInt(String key, int def){
        return sp.getInt(key, def);
    }
    /**
     * 값이 null 일때 기본값 def (두번째 파라미터)
     * @param key
     * @return
     */
    public boolean getData(String key, boolean def){
        return sp.getBoolean(key, def);
    }
    /**
     * 값이 null 일때 기본값 false
     * @param key
     * @return
     */
    public boolean getData(String key){
        return sp.getBoolean(key, false);
    }


    /**
     * Login정보파일에 있는 데이터중 자료형 안따지고 key라는 이름의 data 삭제
     * @param key
     */
    public void deleteData(String key){
        editor.remove(key);
        editor.commit();
    }

    /**
     * Login정보파일에 저장된 모든 데이터 삭제
     */
    public void delereAll(){
        editor.clear();
        editor.commit();
    }
}

package prv.zozi.utils;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;

public class ZHandler extends Handler {
	private final WeakReference<Activity> activity;
	private WeakReference<Dialog> dialog = null;
	public ZHandler(Activity activity) {
		this.activity = new WeakReference<Activity>(activity);
		dialog = null;
	}
	
	public ZHandler(Dialog dialog) {
		this.dialog = new WeakReference<Dialog>(dialog);
		activity = null;
	}
	@Override
	public void handleMessage(Message msg) {
		if(dialog == null){
			Activity activity = this.activity.get();
	
			if (activity != null && !activity.isFinishing()) {
				((ZHandlerInterFacce) activity).handleMessage(msg);
			}
		}else if(activity == null){
			Dialog dialog = this.dialog.get();
			
			if(dialog!=null && dialog.isShowing()){
				((ZHandlerInterFacce)dialog).handleMessage(msg);
			}
		}
	}
}

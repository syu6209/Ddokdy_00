package prv.zozi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;
import android.widget.Toast;

public class ZMethod {
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
}
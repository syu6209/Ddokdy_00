package prv.zozi.utils;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ZoziBitmapUtils {
	static public String getImagePathToUri(Context context, Uri data) {
		String[] obj = { MediaStore.Images.Media.DATA };
		// Cursor cursor = managedQuery(data, obj, nullll, nulll, nullll);
		Cursor cursor = context.getContentResolver().query(data, obj, null, null, null);
		int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		String imgPath = cursor.getString(column_idx);
		cursor.close();
		return imgPath;
	}

	static public String getImageNameToUri(Context context, Uri data) {
		String[] obj = { MediaStore.Images.Media.DATA };
		// Cursor cursor = managedQuery(data, obj, nullll, nulll, nullll);
		Cursor cursor = context.getContentResolver().query(data, obj, null, null, null);
		int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		String imgPath = cursor.getString(column_idx);
		String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
		cursor.close();
		return imgName;
	}

	public static Bitmap centerCrop(Bitmap srcBmp, int w, int h){
		int stx,etx,sty,ety;
		stx = (srcBmp.getWidth() / 2) - (w / 2);
		etx= (srcBmp.getWidth() / 2) + (w / 2);
		sty = (srcBmp.getHeight() / 2) - (h / 2);
		ety = (srcBmp.getHeight() / 2) + (h / 2);
		if(stx<0 || etx>srcBmp.getWidth()){
			stx = 0;
			etx = srcBmp.getWidth();
		}
		if(sty<0 || ety>srcBmp.getHeight()){
			sty = 0;
			ety = srcBmp.getWidth();
		}
		
		return Bitmap.createBitmap(srcBmp, stx, sty, etx, ety);
	}
	public static Bitmap centerCrop(Bitmap srcBmp){
		if (srcBmp.getWidth() > srcBmp.getHeight()) {
			
			srcBmp = Bitmap.createBitmap(srcBmp, srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2, 0,
					srcBmp.getHeight(), srcBmp.getHeight());
		} else {

			srcBmp = Bitmap.createBitmap(srcBmp, 0, srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
					srcBmp.getWidth(), srcBmp.getWidth());
		}
		return srcBmp;
	}
	public static int getDeviceDPI(Context context){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager mgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		mgr.getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi;
	}
	public static double getDeviceXDPI(Context context){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager mgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		mgr.getDefaultDisplay().getMetrics(metrics);
		return metrics.xdpi;
	}
	public static double getDeviceYDPI(Context context){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager mgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		mgr.getDefaultDisplay().getMetrics(metrics);
		return metrics.ydpi;
	}
	public static int pxToDp(Context context, int px) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	    return dp;
	}
	public static int dpToPx(Context context, int dp) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	public synchronized static int GetExifOrientation(String filepath) 
	{
	    int degree = 0;
	    ExifInterface exif = null;
	    
	    try 
	    {
	        exif = new ExifInterface(filepath);
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    
	    if (exif != null) 
	    {
	        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
	        
	        if (orientation != -1) 
	        {
	            // We only recognize a subset of orientation tag values.
	            switch(orientation) 
	            {
	                case ExifInterface.ORIENTATION_ROTATE_90:
	                    degree = 90;
	                    break;
	                    
	                case ExifInterface.ORIENTATION_ROTATE_180:
	                    degree = 180;
	                    break;
	                    
	                case ExifInterface.ORIENTATION_ROTATE_270:
	                    degree = 270;
	                    break;
	            }

	        }
	    }
	    
	    return degree;
	}
	public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) 
	{
	    if ( degrees != 0 && bitmap != null ) 
	    {
	        Matrix m = new Matrix();
	        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2 );
	        try 
	        {
	            Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
	            if (bitmap != b2) 
	            {
	            	bitmap.recycle();
	            	bitmap = b2;
	            }
	        } 
	        catch (OutOfMemoryError ex) 
	        {
	            // We have no memory to rotate. Return the original bitmap.
	        }
	    }
	    
	    return bitmap;
	}
}

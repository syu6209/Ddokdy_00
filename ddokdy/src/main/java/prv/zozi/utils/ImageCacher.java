package prv.zozi.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCacher {
	private LruCache<String, Bitmap> imageCache;
	public ImageCacher(int maxMemory, int cacheSize){
		imageCache = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap bitmap){
				return bitmap.getByteCount()/1024;
			}
		};
	}
	public Bitmap loadImageByUrl(String key){
		Bitmap bitmap = null;
		System.out.println("url = "+key);
		if(getImageCache(key)==null){
			DownlaodImageInBackground downloader = new DownlaodImageInBackground();
			downloader.execute(key);
			bitmap = downloader.bit;
			//new DownlaodImageInBackground(iv).execute(url);
			//Drawable d = iv.getDrawable();
			//bitmap = ((BitmapDrawable)d).getBitmap();
			/*
			try {
				URL url = new URL(key);
				URLConnection conn = url.openConnection();
				conn.connect();

				int nSize = conn.getContentLength();
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
				bitmap = BitmapFactory.decodeStream(bis);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			if(bitmap!=null){
				imageCache.put(key, bitmap);
			}
		}else{
			bitmap = imageCache.get(key);
		}
		return bitmap;
	}
	public void addImageCache(String key, Bitmap bitmap){
		//imageCache.remove(key);
		if(getImageCache(key)==null){
			imageCache.put(key, bitmap);
		}
	}
	public Bitmap getImageCache(String key) {
		return imageCache.get(key);
	}
}

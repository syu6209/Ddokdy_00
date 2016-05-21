package prv.zozi.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.study.ddokdy.R;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageButton;
import android.widget.ImageView;

public class DiskImageCache
{
  private Context context;
  private LruCache<String, Bitmap> memoryCache;
  private DiskLruCache diskLruCache;
  private final Object diskCacheLock = new Object();
  private boolean diskCacheStarting = true;
  private final String DISK_CACHE_SUBDIR = "thumnails";
  private final int IMAGE_QUALITY = 70;
  private final int DISK_CACHE_SIZE = 1024 * 1024 * 70; //70MB

  public DiskImageCache(Context context)
  {
    this.context = context;
    final int cacheSize = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    int maxSize = 1024 * 1024 * cacheSize / 4; //앱 메모리의 1/4를 사용
    memoryCache = new LruCache<String, Bitmap>(maxSize)
    {
      protected int sizeOf(String key, Bitmap bitmap)
      {
        return bitmap.getByteCount();
      }
    };
    if(diskCacheStarting)
      new InitDiskCacheTask().execute();
  }


  public DiskLruCache getDiskLruCache() {
    return diskLruCache;
  }

  public Bitmap getImageBitmap(String imageURL, String key_token, boolean isDiskCacheImmediately){
    int stx = imageURL.lastIndexOf('/');
    String key = imageURL.substring(stx)+key_token;

    System.gc();
    if (imageURL.isEmpty())
    {
      return null;
    }
    else
    {
      //Bitmap bitmap = getBitmapFromDiskCache(imageURL); //디스크캐시 확인
      Bitmap bitmap = getBitmapFromDiskCache(key);
      if (bitmap != null){
        System.out.println(key+"디스크 캐시 성공");
        return bitmap;
      }else
      {
        while(Config.downloadQue>Config.downloadQueLimit){

        }
        Config.downloadQue++;
        System.out.println(key+"디스크 캐시 실패 다운로드 "+ Config.downloadQue+"/"+ Config.downloadQueLimit);
        bitmap = problemImageDownload(imageURL);
        if(Config.downloadQue>0){
          Config.downloadQue--;
        }
        System.out.println(key+"다운로드완료"+ Config.downloadQue+"/"+ Config.downloadQueLimit);
        if (bitmap == null){
          return bitmap;
        }else
        {
          if(isDiskCacheImmediately){
            addBitmapToCache(key, bitmap);

            System.out.println(key+"다운로드완료 디스크 캐시완료");
          }
          return bitmap;
        }

      }
    }
  }
  //이미지 가져오기
  public Bitmap getImageBitmap(String imageURL, boolean isDiskCacheImmediately){
    int stx = imageURL.lastIndexOf('/');
    String key = imageURL.substring(stx);

    System.gc();
    if (imageURL.isEmpty())
    {
      return null;
    }
    else
    {
      //Bitmap bitmap = getBitmapFromDiskCache(imageURL); //디스크캐시 확인
      Bitmap bitmap = getBitmapFromDiskCache(key);
      if (bitmap != null){
        System.out.println(key+"디스크 캐시 성공");
        return bitmap;
      }else
      {
        while(Config.downloadQue>Config.downloadQueLimit){

        }
        Config.downloadQue++;
        System.out.println(key+"디스크 캐시 실패 다운로드 "+ Config.downloadQue+"/"+ Config.downloadQueLimit);
        bitmap = problemImageDownload(imageURL);
        if(Config.downloadQue>0){
          Config.downloadQue--;
        }
        System.out.println(key+"다운로드완료"+ Config.downloadQue+"/"+ Config.downloadQueLimit);
        if (bitmap == null){
          return bitmap;
        }else
        {
          if(isDiskCacheImmediately){
            addBitmapToCache(key, bitmap);

            System.out.println(key+"다운로드완료 디스크 캐시완료");
          }
          return bitmap;
        }

      }
    }
  }
  public void setImageButton(ImageButton view, String imageURL){
    int stx = imageURL.lastIndexOf('/');
    String key = imageURL.substring(stx);
    if (imageURL.isEmpty())
    {
      view.setImageResource(R.drawable.no_image);
    }
    else
    {
      //Bitmap bitmap = getBitmapFromDiskCache(imageURL); //디스크캐시 확인
      Bitmap bitmap = getBitmapFromDiskCache(key);
      if (bitmap != null)
        view.setImageBitmap(bitmap);
      else
      {
        //bitmap = problemImageDownload(imageURL);


        bitmap = problemImageDownload(imageURL);
        if (bitmap == null)
          view.setImageResource(R.drawable.no_image);
        else
        {
          view.setImageBitmap(bitmap);
          addBitmapToCache(key, bitmap);
        }
      }
    }
  }
  public void setImageView(ImageView view, String imageURL, boolean isDiskCacheImmediately)
  {
    int stx = imageURL.lastIndexOf('/');
    String key = imageURL.substring(stx);
    if (imageURL.isEmpty())
    {
      view.setImageResource(R.drawable.no_image);
    }
    else
    {
      //Bitmap bitmap = getBitmapFromDiskCache(imageURL); //디스크캐시 확인
      Bitmap bitmap = getBitmapFromDiskCache(key);
      if (bitmap != null)
        view.setImageBitmap(bitmap);
      else
      {
        //bitmap = problemImageDownload(imageURL);


        bitmap = problemImageDownload(imageURL);
        if (bitmap == null)
          view.setImageResource(R.drawable.no_image);
        else
        {
          view.setImageBitmap(bitmap);
          addBitmapToCache(key, bitmap);
        }
      }
    }
  }


  private Bitmap problemImageDownload(String imageURL)
  {
    try
    {
      //LogUtil.i("imageURL", imageURL);
      return new ImageDownloadAsync().execute(imageURL,null,null).get();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  private class ImageDownloadAsync extends AsyncTask<String, String, Bitmap>
  {
    @Override
    protected Bitmap doInBackground(String... urls)
    {
      Bitmap bitmap = null;
      try
      {
        URL ImageUrl = new URL(urls[0]);
        HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream inputStream = conn.getInputStream();

        bitmap = BitmapFactory.decodeStream(inputStream);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      return bitmap;
    }
  }

  //디스크캐시 생성
  private class InitDiskCacheTask extends AsyncTask<File, Void, Void>
  {

    @Override
    protected Void doInBackground(File... params)
    {
      synchronized (diskCacheLock)
      {
        File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
        try
        {
          diskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE);
          diskCacheStarting = false;
          diskCacheLock.notifyAll();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
      return null;
    }
  }


  //이미지 캐시에 저장
  public void addBitmapToCache(String key, Bitmap bitmap)
  {
    synchronized (diskCacheLock)
    {
      DiskLruCache.Editor editor = null;
      try
      {
        //key = SetBase64.encodeString(key); //키값이 url형태라 불안정하므로 base64로 변환.
        //key = Base64.encodeToString(key.getBytes(), 0);
        //하지않음ㅇㅇ
        memoryCache.put(key, bitmap); //메모리캐시에 저장
        editor = diskLruCache.edit(key);
        if (editor == null)
        {
          return;
        }

        if (diskLruCache != null && diskLruCache.get(key) == null)
        {
          if (writeBitmapToFile(bitmap, editor))
          {
            diskLruCache.flush();
            editor.commit();
            //LogUtil.d("cache_test_DISK_", "image put on disk cache " + key);
          }
        }
        else
        {
          editor.abort();
          // LogUtil.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      System.out.println(key+"디스크 캐시 완료");
    }
  }


  public LruCache<String, Bitmap> getMemoryCache() {
    return memoryCache;
  }


  private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor) throws IOException, FileNotFoundException
  {
    OutputStream out = null;
    try
    {
      out = new BufferedOutputStream(editor.newOutputStream(0), 8 * 1024);
      return bitmap.compress(CompressFormat.PNG, IMAGE_QUALITY, out);
    }
    finally
    {
      if (out != null)
        out.close();
    }
  }

  //이미지를 가져온다
  private Bitmap getBitmapFromDiskCache(String key)
  {
    //key = Base64.encodeToString(key.getBytes(), 0); //url을 base64로 먼저 변환
    Bitmap bitmap = memoryCache.get(key); //먼저 메모리에 있는지 확인.
    if (bitmap == null)
    {
      if (diskLruCache != null)
      {
        DiskLruCache.Snapshot snapshot = null;
        try
        {
          snapshot = diskLruCache.get(key);
          if (snapshot == null)
            return null;
          final InputStream in = snapshot.getInputStream(0);
          if (in != null)
          {
            final BufferedInputStream buffIn = new BufferedInputStream(in, 8 * 1024);
            bitmap = BitmapFactory.decodeStream(buffIn);
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        finally
        {
          if (snapshot != null)
            snapshot.close();
        }
      }

    /*
      synchronized (diskCacheLock)
      {
        while (diskCacheStarting)
        {
          try
          {
            diskCacheLock.wait();
          }
          catch (InterruptedException e)
          {
          }
        }
        if (diskLruCache != null)
        {
          DiskLruCache.Snapshot snapshot = null;
          try
          {
            snapshot = diskLruCache.get(key);
            if (snapshot == null)
              return null;
            final InputStream in = snapshot.getInputStream(0);
            if (in != null)
            {
              final BufferedInputStream buffIn = new BufferedInputStream(in, 8 * 1024);
              bitmap = BitmapFactory.decodeStream(buffIn);
            }
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
          finally
          {
            if (snapshot != null)
              snapshot.close();
          }
        }

       // if (BuildConfig.DEBUG){
          //LogUtil.d("cache_test_DISK_", bitmap == null ? "" : "image read from disk " + key);
       // }
      }
      */
    }
    return bitmap;
  }


  private static File getDiskCacheDir(Context context, String uniqueName)
  {
    final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir()
            .getPath();
    return new File(cachePath + File.separator + uniqueName);
  }


  public static boolean isExternalStorageRemovable()
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
    {
      return Environment.isExternalStorageRemovable();
    }
    return true;
  }


  public static File getExternalCacheDir(Context context)
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)
    {
      return context.getExternalCacheDir();
    }
    final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
    return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
  }
}
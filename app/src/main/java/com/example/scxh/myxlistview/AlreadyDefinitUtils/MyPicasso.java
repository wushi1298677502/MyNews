package com.example.scxh.myxlistview.AlreadyDefinitUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;


import com.example.scxh.myxlistview.Logs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by scxh on 2016/8/4.
 */
public class MyPicasso {
    public static final String STRING_CACHE_NAME = "com.example.scxh.myapp.Utils.MyPicasso";
    private static boolean isCache = true;
    private Context mContext;
    public enum Mothod{
        GET,POST
    }
    public enum Cache{
        TRUE,FALSE
    }
    public interface CallBack{
        void excute(Bitmap bitmap);
    }
    public MyPicasso(Context context){
        this.mContext = context;

    }


    public void AsyncDownload(final String url, final MyPicasso.Mothod mothod, final Cache isCache, final MyPicasso.CallBack callBack){
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                String urls = strings[0];

                return doGetPostHttp(urls,mothod,isCache);
            }
            protected void onPostExecute(Bitmap bitmap){
                callBack.excute(bitmap);
            }
        }.execute(url);
    }

    public Bitmap doGetPostHttp(String httpUrl,Mothod mothod,Cache isCache){
        DiskLruCacheUtil mDiskLruCacheUtil = DiskLruCacheUtil.getInstance(mContext);
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(httpUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if(mothod == Mothod.GET){
                httpURLConnection.setRequestMethod("GET");
            }else {
                httpURLConnection.setRequestMethod("POST");
            }
            httpURLConnection.setConnectTimeout(5000);//连接超时时间
            httpURLConnection.setReadTimeout(5000); //读数据超时
            httpURLConnection.connect();

            int code = httpURLConnection.getResponseCode();
            Logs.e("code>>>>"+code);
            if(code == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                if(isCache == Cache.TRUE){
                    Logs.e("从网络获取图片保存到内存缓存");
                    LruCacheUtil.getInsantance().addBitmapToMemoryCache(httpUrl,bitmap);
                    mDiskLruCacheUtil.putBitmapFile(httpUrl,bitmap); //保存图片到文件缓存
//                    mContext.getSharedPreferences(STRING_CACHE_NAME,Context.MODE_PRIVATE).edit().putString(httpUrl,inputStream).commit();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

            if(bitmap == null){
                bitmap = LruCacheUtil.getInsantance().getBitmapFromMemoryCache(httpUrl); //从内存缓存取图片
                Logs.e("bitmap//从内存缓存取图片"+bitmap);
                if(bitmap == null){
                    bitmap = mDiskLruCacheUtil.getBitmaFile(httpUrl);//从文件缓存获取图片
                    Logs.e("bitmap从文件缓存获取图片"+bitmap);

                }
            }

//            String cacheContent = mContext.getSharedPreferences(STRING_CACHE_NAME,Context.MODE_PRIVATE).getString(httpUrl,null);
//            return cacheContent;
            return bitmap;
        }finally {
            httpURLConnection.disconnect();
        }
        return bitmap;
    }
    /**
     * 从网络获取图片
     *
     * @param httpUrl
     * @return
     */
    public Bitmap doDownLoadPictrue(String httpUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     * 检查网络连接是否可用
     */

    public boolean isWiFiActive(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

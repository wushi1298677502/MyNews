package com.example.scxh.myxlistview.AlreadyDefinitUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by scxh on 2016/7/19.
 */
public class DiskLruCacheUtil {
    com.example.scxh.myxlistview.AlreadyDefinitUtils.DiskLruCache mDiskLruCache = null;
    private static DiskLruCacheUtil CacheUtil = null;
    public static DiskLruCacheUtil getInstance(Context context) {
        if (CacheUtil == null) {
            CacheUtil = new DiskLruCacheUtil(context);
        }
            return CacheUtil;
    }
    public DiskLruCacheUtil(Context context) {
        try {
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            com.example.scxh.myxlistview.AlreadyDefinitUtils.DiskLruCache.open(cacheDir, 1001, 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获得路径
    public File getDiskCacheDir(Context context,String uniqueName){
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||
                !Environment.isExternalStorageRemovable()){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
     *
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmaFile(String imageUrl) {
        Bitmap bitmap = null;
        try {
            String key = hashKeyForDisk(imageUrl);
            com.example.scxh.myxlistview.AlreadyDefinitUtils.DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public void putBitmapFile(String imageUrl,Bitmap bitmap) {
        try {
            String key = hashKeyForDisk(imageUrl);
            com.example.scxh.myxlistview.AlreadyDefinitUtils.DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                //compress()的三个参数，1 图片格式 2 int num 0--100，越大保存质量越高 3 输出流
                boolean isSaveSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                if(isSaveSuccess){
                    editor.commit();
                } else {
                    editor.abort();//忽略
                }
                mDiskLruCache.flush();//刷新
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
     *
     * @param imageUrl
     */
    public void putBitmapFile(final String imageUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String key = hashKeyForDisk(imageUrl);
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        //根据imageUrl从网络加载图片写入outPutStream
                        if (downloadUrlToStream(imageUrl, outputStream)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                    mDiskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    /**
     * 建立HTTP请求，并获取Bitmap对象。 写入指定输出流
     *
     *  imageUrl 图片的URL地址
     * @return 解析后的Bitmap对象
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}


package com.example.scxh.myxlistview.AlreadyDefinitUtils;

import android.content.Context;
import android.os.AsyncTask;


import com.example.scxh.myxlistview.Logs;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by scxh on 2016/7/28.
 */
public class ConnectionUtil {
    public static final String STRING_CACHE_NAME = "com.example.scxh.myapp.Utils";
    private Context mContext;
    private static boolean isCache = true;
    public enum Mothod {
        GET,POST
    }
    public enum Cache {
        TRUE,FALSE
    }
    public ConnectionUtil(Context context){
        mContext = context;
    }
    public interface HttpConnectionInterface {
        void excute(String content) throws JSONException;
    }

    /**
     * 无参无缓存请求
     *
     * @param httpUrl
     * @param method
     * @param httpConnectionInterface
     */
    public void asyncConnect(final String httpUrl, final Mothod method, final HttpConnectionInterface httpConnectionInterface) {
        asyncConnect(httpUrl, null, method, Cache.FALSE, httpConnectionInterface);
    }

    /**
     * 无参有缓存
     *
     * @param httpUrl
     * @param method
     * @param httpConnectionInterface
     */
    public void asyncConnect(final String httpUrl, final Mothod method, Cache isCache, final HttpConnectionInterface httpConnectionInterface) {
        asyncConnect(httpUrl, null, method, isCache, httpConnectionInterface);
    }

    /**
     * 有参无缓存
     *
     * @param httpUrl
     * @param paramtMap
     * @param method
     * @param httpConnectionInterface
     */
    public void asyncConnect(final String httpUrl, final HashMap<String, String> paramtMap, final Mothod method, final HttpConnectionInterface httpConnectionInterface) {
        asyncConnect(httpUrl, paramtMap, method, Cache.FALSE, httpConnectionInterface);
    }

    /**
     * 有参有缓存请求
     * 异步联网获取Http响应字符串数据
     *
     * @param httpUrl
     * @param
     * @param method
     * @param httpConnectionInterface
     */
    public void asyncConnect(final String httpUrl, final HashMap<String,String> paramMap, final ConnectionUtil.Mothod method, final Cache isCache,
                             final ConnectionUtil.HttpConnectionInterface httpConnectionInterface){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                String Urls = strings[0];
                return doHttpConnection(Urls,paramMap,method,isCache);
            }
            protected void onPostExecute(String content){
                try {
                    httpConnectionInterface.excute(content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(httpUrl);
    }
    public String doHttpConnection(String httpUrl, HashMap<String,String> paramMap, ConnectionUtil.Mothod mothod,Cache isCache){
        if(mothod == Mothod.GET){
            if(paramMap != null){
                String paramUrl = "?";
                paramUrl = doParameterHttp(paramUrl,paramMap);
                httpUrl = httpUrl + paramUrl;
            }
            return doGetPostHttp(httpUrl,paramMap,mothod,isCache);
        }else {
            return doGetPostHttp(httpUrl,paramMap,mothod,isCache);
        }
    }
    public String doParameterHttp(String httpUrl,HashMap<String,String> paramMap){
        for(String key:paramMap.keySet()){
            String value = paramMap.get(key);
            httpUrl = httpUrl + key + "=" + value + "&";
        }
        httpUrl = httpUrl.substring(0,httpUrl.length()-1);
        return httpUrl;
    }
    public String doGetPostHttp(String httpUrl,HashMap<String,String> paramMap,Mothod mothod,Cache isCache){
        String message = "";
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(httpUrl);
            Logs.e("httpUrl>>>"+httpUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if(mothod == Mothod.GET){
                httpURLConnection.setRequestMethod("GET");
            }else {
                httpURLConnection.setRequestMethod("POST");
            }
           httpURLConnection.setConnectTimeout(5000);//连接超时时间
            httpURLConnection.setReadTimeout(5000); //读数据超时
            httpURLConnection.connect();

            if(mothod == Mothod.POST){
                // post请求的参数
                if(paramMap != null){
                    String data = doParameterHttp("",paramMap);//userName=admin&passWord=123456
                    // 获得一个输出流,向服务器写数据,默认情况下,系统不允许向服务器输出内容
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    outputStream.close();
                }
            }

            int code = httpURLConnection.getResponseCode();
            Logs.e("code>>>>"+code);
            if(code == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpURLConnection.getInputStream();
                message = readInput(inputStream);
                if(isCache == Cache.TRUE){
                    mContext.getSharedPreferences(STRING_CACHE_NAME,Context.MODE_PRIVATE).edit().putString(httpUrl,message).commit();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            String cacheContent = mContext.getSharedPreferences(STRING_CACHE_NAME,Context.MODE_PRIVATE).getString(httpUrl,null);
            return cacheContent;
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }

        }
        return message;
    }
    /**
     * 输入流转字符串
     *
     * @param is
     * @return
     */
    public String readInput(InputStream is) {
        Reader reader = new InputStreamReader(is);  //字节转字符流
        BufferedReader br = new BufferedReader(reader); //字符缓存流

        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                reader.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}

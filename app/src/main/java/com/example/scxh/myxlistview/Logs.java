package com.example.scxh.myxlistview;

import android.util.Log;

/**
 * Created by scxh on 2016/5/26.
 */
public class Logs {
    public static final String tag="MyApp";
    public static final boolean isdebug=true;//控制程序日志输出状态

    public static void v(String str){
        if(isdebug)
        Log.v(tag,str);
    }
    public static void d(String str){
        if(isdebug)
        Log.d(tag,str);
    }
    public static void i(String str){
        if(isdebug)
        Log.i(tag,str);
    }
    public static void w(String str){
        if(isdebug)
        Log.w(tag,str);
    }
    public static void e(String str){
        if(isdebug)
        Log.e(tag,str);
    }

}

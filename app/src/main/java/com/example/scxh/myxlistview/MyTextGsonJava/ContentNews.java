package com.example.scxh.myxlistview.MyTextGsonJava;

import com.example.scxh.myxlistview.MyTextGsonJava.ContentNewsImg;

import java.util.ArrayList;

/**
 * Created by scxh on 2016/8/11.
 */
public class ContentNews {
    String body;
    String title;
    String source;
    ArrayList<ContentNewsImg> img;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<ContentNewsImg> getImg() {
        return img;
    }

    public void setImg(ArrayList<ContentNewsImg> img) {
        this.img = img;
    }
}

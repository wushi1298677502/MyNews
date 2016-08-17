package com.example.scxh.myxlistview.MyPicGsonJava;

import java.util.ArrayList;

/**
 * Created by scxh on 2016/8/15.
 */
public class Pics {
    int total;
    int picTemplate;
    ArrayList<PicsList> list;

    public ArrayList<PicsList> getList() {
        return list;
    }

    public void setList(ArrayList<PicsList> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPicTemplate() {
        return picTemplate;
    }

    public void setPicTemplate(int picTemplate) {
        this.picTemplate = picTemplate;
    }
}

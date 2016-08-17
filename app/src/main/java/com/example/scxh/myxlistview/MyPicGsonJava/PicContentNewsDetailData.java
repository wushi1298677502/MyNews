package com.example.scxh.myxlistview.MyPicGsonJava;

import java.util.ArrayList;

/**
 * Created by scxh on 2016/8/14.
 */
public class PicContentNewsDetailData {
    String title;
    String lead;
    ArrayList<PicContentNewsDetaiPics> pics;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public ArrayList<PicContentNewsDetaiPics> getPics() {
        return pics;
    }

    public void setPics(ArrayList<PicContentNewsDetaiPics> pics) {
        this.pics = pics;
    }
}

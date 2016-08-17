package com.example.scxh.myxlistview.MyTextGsonJava;

import java.util.ArrayList;

/**
 * Created by scxh on 2016/7/29.
 */
public class news {
    String title;
    String imgsrc;
    String digest;
    String docid;
    ArrayList<Ads> ads;
    ArrayList<Imgextra> imgextra;
    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public ArrayList<Imgextra> getImgextra() {
        return imgextra;
    }

    public void setImgextra(ArrayList<Imgextra> imgextra) {
        this.imgextra = imgextra;
    }

    public ArrayList<Ads> getAds() {
        return ads;
    }

    public void setAds(ArrayList<Ads> ads) {
        this.ads = ads;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}

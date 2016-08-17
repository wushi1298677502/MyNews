package com.example.scxh.myxlistview.MyPicGsonJava;

import com.example.scxh.myxlistview.MyPicGsonJava.Pics;

/**
 * Created by scxh on 2016/8/14.
 */
public class PicContentNews {
        String id;
        String title;
        String source;
        String pic;
        String kpic;
        int comment;
        Pics pics;

    public Pics getPics() {
        return pics;
    }

    public void setPics(Pics pics) {
        this.pics = pics;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getKpic() {
        return kpic;
    }

    public void setKpic(String kpic) {
        this.kpic = kpic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}

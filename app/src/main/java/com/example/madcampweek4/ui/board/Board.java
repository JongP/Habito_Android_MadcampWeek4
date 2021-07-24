package com.example.madcampweek4.ui.board;

import android.graphics.Bitmap;

public class Board {
    String post_id;
    String user_id;
    String user_profilePic;
    String group_id;
    String post_content;
    Bitmap post_bitmap;
    String post_date;
    int like;

    public Board(){}

    public Board(String post_id,
                 String user_id,
                 String user_profilePic,
                 String group_id,
                 String post_content,
                 Bitmap post_bitmap,
                 String post_date,
                 int like ){
        this.post_id = post_id;
        this.user_id = user_id;
        this.user_profilePic = user_profilePic;
        this.group_id = group_id;
        this.post_content = post_content;
        this.post_bitmap = post_bitmap;
        this.post_date = post_date;
        this.like = like;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public void setUser_profilePic(String user_profilePic) {
        this.user_profilePic = user_profilePic;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public void setPost_bitmap(Bitmap post_bitmap) {
        this.post_bitmap = post_bitmap;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public String getUser_profilePic() {
        return user_profilePic;
    }

    public String getPost_content() {
        return post_content;
    }

    public Bitmap getPost_bitmap() {
        return post_bitmap;
    }

    public String getPost_date() {
        return post_date;
    }

    public int getLike() {
        return like;
    }
}

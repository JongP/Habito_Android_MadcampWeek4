package com.example.madcampweek4.ui.board;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.HashMap;

public class Board {
    private String post_id;
    private String user_id;
    private String user_name;
    private String user_profilePic;
    private String group_id;
    private String post_content;

    private String post_uri;
    private String post_date;
    private int like;
    private HashMap<String,Object> likes;

    public Board(){}

    public Board(String post_id,
                 String user_id,
                 String user_name,
                 String user_profilePic,
                 String group_id,
                 String post_content,
                 String post_uri,
                 String post_date,
                 int like ){
        this.post_id = post_id;
        this.user_id = user_id;
        this.user_name=user_name;
        this.user_profilePic = user_profilePic;
        this.group_id = group_id;
        this.post_content = post_content;
        this.post_uri = post_uri;
        this.post_date = post_date;
        this.like = like;
        this.likes=null;
    }


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_profilePic() {
        return user_profilePic;
    }

    public void setUser_profilePic(String user_profilePic) {
        this.user_profilePic = user_profilePic;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_uri() {
        return post_uri;
    }

    public void setPost_uri(String post_uri) {
        this.post_uri = post_uri;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public HashMap<String, Object> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, Object> likes) {
        this.likes = likes;
    }
}

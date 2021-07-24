package com.example.madcampweek4.ui.Board;

public class Board {
    String post_id;
    String user_id;
    String group_id;
    String post_title;
    String post_content;
    String post_url;
    String post_date;
    int like;

    public Board(){

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

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
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

    public String getPost_title() {
        return post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public String getPost_url() {
        return post_url;
    }

    public String getPost_date() {
        return post_date;
    }

    public int getLike() {
        return like;
    }
}

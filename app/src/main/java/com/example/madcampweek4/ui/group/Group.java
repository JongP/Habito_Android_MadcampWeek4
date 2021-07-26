package com.example.madcampweek4.ui.group;

import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;

public class Group {
    private String id;
    private String profile;
    private String groupName;
    private String groupInfo;
    private HashMap<String,String> users;

    public Group(){
    }

    public Group(String id, String profile, String groupName, String groupInfo) {
        this.id = id;
        this.profile = profile;
        this.groupName = groupName;
        this.groupInfo = groupInfo;
    }

    public Group(String profile, String groupName, String groupInfo) {
        this.profile = profile;
        this.groupName = groupName;
        this.groupInfo = groupInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupInfo() {
        return groupInfo;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupInfo(String groupInfo) {
        this.groupInfo = groupInfo;
    }

    public HashMap<String, String > getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, String> users) {
        this.users = users;
    }
}

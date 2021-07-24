package com.example.madcampweek4.ui.group;

public class Group {
    private String profile;
    private String groupName;
    private String groupInfo;

    public Group(String profile, String groupName, String groupInfo){
        this.profile = profile;
        this.groupName = groupName;
        this.groupInfo = groupInfo;
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
}

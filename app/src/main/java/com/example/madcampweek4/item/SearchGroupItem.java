package com.example.madcampweek4.item;

public class SearchGroupItem {

    private String groupName, groupContent;

    public SearchGroupItem(String groupName, String groupContent) {
        this.groupName = groupName;
        this.groupContent = groupContent;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupContent() {
        return groupContent;
    }

    public void setGroupContent(String groupContent) {
        this.groupContent = groupContent;
    }
}

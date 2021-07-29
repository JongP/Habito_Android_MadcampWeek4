package com.example.madcampweek4;

//사용자 계정 정보 모델 클래스

import java.util.ArrayList;

public class UserAccount {
    private String idToken; //firebase uid
    private String emailId;
    private String password;
    private String name;
    private String profileURL;
    private int points;
    private static ArrayList<Boolean> display_fish;
    private static ArrayList<Boolean> fish;

    public static ArrayList<Boolean> getFish() {
        return fish;
    }

    public static void setFish(ArrayList<Boolean> fish) {
        UserAccount.fish = fish;
    }
//별명, 프로필 이미지 등으로 확장 가능

    public int getPoints() {
        return points;
    }

    public static ArrayList<Boolean> getDisplay_fish() {
        return display_fish;
    }

    public static void setDisplay_fish(ArrayList<Boolean> display_fish) {
        UserAccount.display_fish = display_fish;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public UserAccount() { }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}

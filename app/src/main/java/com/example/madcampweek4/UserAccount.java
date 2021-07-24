package com.example.madcampweek4;

//사용자 계정 정보 모델 클래스

public class UserAccount {
    private String idToken; //firebase uid
    private String emailId;
    private String password;
    private String name;
    //별명, 프로필 이미지 등으로 확장 가능

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

}

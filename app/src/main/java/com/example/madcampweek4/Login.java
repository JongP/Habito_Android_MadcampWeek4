package com.example.madcampweek4;

public class Login {
    private static String name, email;
    private static int groupNum, postNum;

    Login(String name, String email){
        this.name=name;
        this.email=email;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Login.name = name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Login.email = email;
    }

    public static int getGroupNum() {
        return groupNum;
    }

    public static void setGroupNum(int groupNum) {
        Login.groupNum = groupNum;
    }

    public static int getPostNum() {
        return postNum;
    }

    public static void setPostNum(int postNum) {
        Login.postNum = postNum;
    }
}

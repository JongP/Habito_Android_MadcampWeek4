package com.example.madcampweek4;

public class Login {
    private static String name, email;
    private static int groupNum, postNum, points;
    private  static String Uid;

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


    public static int getPoints() {
        return points;
    }

    public static void setPoints(int points) {
        Login.points = points;
    }

    public static String getUid() {
        return Uid;
    }

    public static void setUid(String uid) {
        Uid = uid;
    }
}

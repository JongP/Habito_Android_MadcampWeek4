package com.example.madcampweek4;

public class Login {
    private static String name, email;

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
}

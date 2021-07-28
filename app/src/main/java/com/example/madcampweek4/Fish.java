package com.example.madcampweek4;

import java.util.ArrayList;

public class Fish {
    private static ArrayList<Boolean> own;
    private static int[] head= {-1,-1,1,1,1,-1,-1,1};

    public static ArrayList<Boolean> getOwn() {
        return own;
    }

    public static void setOwn(ArrayList<Boolean> own) {
        Fish.own = own;
    }

    public static void setHead(int[] head) {
        Fish.head = head;
    }

    public static int[] getHead() {
        return head;
    }

}

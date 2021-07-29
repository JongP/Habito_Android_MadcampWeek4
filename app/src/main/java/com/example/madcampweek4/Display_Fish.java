package com.example.madcampweek4;

import java.util.ArrayList;

public class Display_Fish {
    private static ArrayList<Boolean> own;
    private static int MAX_FISH = 7;
    private static String[] names= new String[]{"Cutefish", "Shark", "SpinJelly", "Turtle", "Whale", "Balloon", "Blue"};;

    public static String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public static ArrayList<Boolean> getOwn() {
        return own;
    }

    public static void setOwn(ArrayList<Boolean> own) {
        Display_Fish.own = own;
    }

    public static int getMaxFish() {
        return MAX_FISH;
    }
}

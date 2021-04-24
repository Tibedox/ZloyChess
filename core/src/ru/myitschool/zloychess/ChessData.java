package ru.myitschool.zloychess;

import com.google.gson.annotations.SerializedName;

public class ChessData {
    @SerializedName("id")
    int id;
    int gamecode;
    int color;
    int turn;
    int state;
    String gamedate;
    String action;
    String name1;
    String name2;
    int wins1;
    int wins2;
}

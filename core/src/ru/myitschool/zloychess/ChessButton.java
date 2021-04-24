package ru.myitschool.zloychess;

public class ChessButton {
    float x, y;
    float width, height;
    int pressed;

    public ChessButton(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    boolean isHit(float tx, float ty){
        return tx>x && ty>y && tx<x+width && ty<y+height;
    }
}

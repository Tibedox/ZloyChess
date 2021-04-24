package ru.myitschool.zloychess;

public class Cell {
    int x, y;
    int color; // 0 -белый, 1 - чёрный
    Figure figure;

    public Cell(int x, int y, int color, Figure figure) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.figure = figure;
    }
}

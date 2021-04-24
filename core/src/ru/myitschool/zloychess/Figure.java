package ru.myitschool.zloychess;

public class Figure {
    int boardX, boardY;
    float scrX, scrY;
    static float scrXUnderBoard[] = new float[2];
    int side;
    int color;
    int type;
    boolean isAlive;
    boolean isMove;

    public Figure(int boardX, int boardY, int color, int type, int side) {
        this.boardX = boardX;
        this.boardY = boardY;
        this.color = color;
        this.type = type;
        this.side = side;
        isAlive = true;
        isMove = false;
        boardToScreen();
    }

    void boardToScreen(){
        scrX = boardX *Main.size;
        scrY = boardY *Main.size + Main.paddingBottom;
    }

    void screenToBoard(){
        boardX = (int)((scrX + Main.size/2)/Main.size);
        boardY = (int)((scrY + Main.size/2 - Main.paddingBottom)/Main.size);
    }

    boolean isHit(float touchX, float touchY){
        return touchX > scrX && touchX < scrX + Main.size && touchY > scrY && touchY < scrY + Main.size;
    }

    void move(float touchX, float touchY){
        scrX = touchX-Main.size/2;
        scrY = touchY-Main.size/2;
    }

    boolean isDropCorrect(float scrX, float scrY, Cell[][] board){
        int x = (int)(scrX/Main.size);
        int y = (int)((scrY- Main.paddingBottom)/Main.size);
        System.out.println("x = "+x+" y = "+y);
        if(x<0 || x>7 || y<0 || y>7) return false;

        if(board[x][y].figure != null)
            if(board[x][y].figure.side == side) return false;
            else {
                if(board[x][y].figure.type == Main.KING) return false;
                board[x][y].figure.isAlive = false;
                board[x][y].figure.scrX = scrXUnderBoard[board[x][y].figure.side];
                if(side == Main.OUR_SIDE) board[x][y].figure.scrY = Main.paddingBottom - Main.size;
                else board[x][y].figure.scrY = Main.paddingBottom + Main.SCR_WIDTH + Main.size/6;
                scrXUnderBoard[board[x][y].figure.side] += Main.size/2;
            }

        return true;
    }
}

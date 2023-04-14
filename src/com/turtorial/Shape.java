package com.turtorial;

import java.awt.*;
import java.awt.event.KeyListener;

public class Shape{

    private int[][] coords;
    private Color color;
    private Board board;
    private static int score = 0;
    private int x = 4, y = 0;
    private int deltaX = 0;
    private boolean collision = false;
    private boolean moveX = true;
    private final int BLOCK_SIZE = 30;
    public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20;
    private int normal = 500;
    private int fast = 50;
    private int delayTimeForMovement = normal;
    private long beginTime;

    public Shape(int[][] coords, Board board, Color color) {
        this.coords = coords;
        this.color = color;
        this.board = board;
    }

    public static int getScore() {
        return score;
    }

    public void resetShapePosition(){
        collision = false;
        this.x = 4;
        this.y = 0;
    }
    public void reset(){
        this.score = 0;
    }
    public void update(){

        if (collision){
            for (int i = 0; i < coords.length; i++) {
                for (int j =0; j < coords[i].length;j++){
                    if (coords[i][j] != 0){
                        this.board.getBoard()[y+i][x+j] = color;
                    }
                }
            }
            checkLine();
            resetShapePosition();
            board.generateShape();
        return;
        }
        // Check movement horizontal
        if(x+deltaX+ coords[0].length <= BOARD_WIDTH && x+deltaX >= 0 &&  y<=BOARD_HEIGHT- coords.length){
            for (int i = 0; i < coords.length; i++) {
                for (int j =0; j < coords[i].length;j++){
                    if (coords[i][j] != 0){
                        if(this.board.getBoard()[i+y][x+j+deltaX] != null){
                            moveX = false;
                        }
                    }
                }
            }
            if (moveX){
                x += deltaX;
            }
            moveX = true;

        }
        deltaX = 0;
        if(System.currentTimeMillis() - beginTime > delayTimeForMovement){
            if (y<BOARD_HEIGHT- coords.length){

                if (!checkForCollision()){
                    y++;
                }

            } else{
                collision = true;
            }
            beginTime = System.currentTimeMillis();
        }
    }

    private boolean checkForCollision() {
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords[i].length; j++) {
                if (coords[i][j] != 0) {
                    if (this.board.getBoard()[i + y + 1][x + j + deltaX] != null) {
                        {
                            collision = true;

                        }
                    }
                }
            }

        }
        return collision;
    }

    public void drawShape(Graphics g){
        for (int i = 0; i < coords.length; i++) {
            for (int j =0; j < coords[0].length;j++){
                if (coords[i][j] != 0){
                    g.setColor(this.color);
                    g.fillRect((j+x)*BLOCK_SIZE, (i+y)*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    public void rotateLeft(){
        if(checkForCollisonsWhileRotating()){
            coords = rotateToLeft(coords);
        }
    }
    public void rotateRight(){
        if(x +coords.length > BOARD_WIDTH || y + coords[0].length > BOARD_HEIGHT){
            return;
        }

        // check for collisions
        if(checkForCollisonsWhileRotating()){
            coords = rotateToRight(coords);
        }


    }

    public boolean checkForCollisonsWhileRotating(){
        // return true if rotating is possible without collision
        for (int row =0; row <coords[0].length; row++){
            for(int col = 0; col < coords.length; col++){
                if(coords[col][row] !=0){
                    if(this.board.getBoard()[y+row+1][x+col+deltaX] != null){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int[][] rotateToRight(int[][] matrix){
        int[][] temp = new int[matrix[0].length][matrix.length];
        int k = 0;
        for (int i = 0; i < temp.length; i++) {
            int r = matrix.length-1;
            for(int j = 0 ; j < temp[0].length; j++){
                temp[i][j] = matrix[r--][k];
            }
            k++;
        }
        return temp;

    }
    private int[][] rotateToLeft(int[][] matrix){
        int[][] temp = new int[matrix[0].length][matrix.length];
        int k = matrix[0].length-1;
        for (int i = 0; i < temp.length; i++) {
            int r = 0;
            for(int j = 0 ; j < temp[0].length; j++){
                temp[i][j] = matrix[r++][k];
            }
            k--;
        }
        return temp;
    }

    private void printMatrix(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for(int j = 0 ; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



    public int[][] getCoords() {
        return coords;
    }

    private void checkLine() {

        for(int row = board.getBoard().length-1; row >0 ; row--){
            int colCounter = 0;
            for (int col =0; col < board.getBoard()[0].length; col++){
                if (board.getBoard()[row][col] != null){
                    colCounter++;
                    if(colCounter == board.getBoard()[0].length ){
                        score++;
                        System.out.println("Score: " + score);
                        updateLines(row);
                        row++;
                    }
                }
            }
        }
    }

    private void updateLines(int rowNum){
        for(int row = rowNum; row >0 ; row--){
            int colCounter = 0;
            for (int col =0; col < board.getBoard()[0].length; col++){
                    board.getBoard()[row][col] = board.getBoard()[row-1][col];
            }
        }
    }


    public void speedUp(){
        delayTimeForMovement = fast;

    }
    public void goNormal(){
        delayTimeForMovement = normal;
    }

    public void moveLeft(){
        deltaX = -1;
    }

    public void moveRight(){
        deltaX = 1;
    }
}

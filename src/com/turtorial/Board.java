package com.turtorial;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Board extends JPanel implements KeyListener {

    private static int STATE_GAME_PLAY = 0;
    private static int STATE_GAME_PAUSE = 1;
    private static int STATE_GAME_OVER = 2;

    private int state = STATE_GAME_PLAY;
    private  static int FPS = 60;
    private static int delay = FPS/1000;

    public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20;
    private Timer looper;
    private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH ];
    private final int BLOCK_SIZE = 30;


    private Shape[] shapes = new Shape[7];
    private Color[] colors = {Color.RED,Color.ORANGE, Color.GREEN, Color.cyan,Color.BLUE, Color.lightGray,Color.magenta};

    private Shape shape;




    public Board(){
        shapes[0] = new Shape(new int[][] {{1,1,1,1}}, this,colors[0]);
        shapes[1] = new Shape(new int[][] {{1,1,1},{0,1,0}},this,colors[1]);
        shapes[2] = new Shape(new int[][] {{1,1,1},{1,0,0}},this,colors[2]);
        shapes[3] = new Shape(new int[][] {{1,1,1},{0,0,1}},this,colors[3]);
        shapes[4] = new Shape(new int[][] {{0,1,1},{1,1,0}},this,colors[4]);
        shapes[5] = new Shape(new int[][] {{1,1},{1,1}},this,colors[5]);
        shapes[6] = new Shape(new int[][] {{1}},this,colors[6]);


        generateShape();
        looper = new Timer(delay , new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateBoard();
                repaint();


            }
            });

        looper.start();


    }

    public void setBoard(Color[][] board) {
        this.board = board;
    }

    public Color[][] getBoard(){
        return board;
    }


    private void updateBoard(){
        if (state == STATE_GAME_PLAY){
            shape.update();
        }

        checkGameOver();

    }

    public void generateShape(){
        Random r = new Random();
        shape = shapes[r.nextInt(7)];;

    }

    private void checkGameOver(){
    int[][] s = shape.getCoords();
        for(int row =0; row< s.length; row++){
            for(int col=0; col< s[0].length;col++){
                if(s[row][col] !=0){
                    if(board[row+shape.getY()][col+shape.getX()] != null){
                        state = STATE_GAME_OVER;
                    }
                }
            }
        }


    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());
        shape.drawShape(g);
        drawBoard(g);
    }



    public void drawBoard(Graphics g){
        // draw board

        renderBoard(g);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + shape.getScore(), (BOARD_WIDTH+1)*BLOCK_SIZE, BLOCK_SIZE*2);

        if(state == STATE_GAME_OVER){
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", (BOARD_WIDTH*BLOCK_SIZE)/2, ((BOARD_HEIGHT-2)*BLOCK_SIZE)/2);
        } else if(state == STATE_GAME_PAUSE) {
            g.setColor(Color.WHITE);
            g.drawString("GAME PAUSED", (BOARD_WIDTH * BLOCK_SIZE) / 2, ((BOARD_HEIGHT-2) * BLOCK_SIZE) / 2);
        }

    }
private void renderBoard(Graphics g){
    for (int row = 0; row <board.length; row++) {
        for (int col = 0; col <board[0].length; col++){
            if(board[row][col] != null){
                g.setColor(board[row][col]);
                g.fillRect((col)*BLOCK_SIZE, (row)*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }
    g.setColor(Color.WHITE);
    for (int row = 0; row <=BOARD_HEIGHT; row++){
        g.drawLine(0,row*BLOCK_SIZE,BOARD_WIDTH*BLOCK_SIZE,row*BLOCK_SIZE);
    }
    for (int col = 0; col <=BOARD_WIDTH; col++){
        g.drawLine(col*BLOCK_SIZE,0,col*BLOCK_SIZE,BOARD_HEIGHT*BLOCK_SIZE);
    }

}

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            shape.speedUp();

        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            shape.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            shape.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_A){
            shape.rotateLeft();
        }

        if (e.getKeyCode() == KeyEvent.VK_D){
            shape.rotateRight();
        }

        if(state == STATE_GAME_OVER){
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                for (int row = 0; row <board.length; row++) {
                    for (int col = 0; col <board[0].length; col++){
                            board[row][col] = null;
                    }
                }
                shape.reset();

                state = STATE_GAME_PLAY;

            }
            updateBoard();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if(state == STATE_GAME_PLAY){
                state = STATE_GAME_PAUSE;
            } else if(state == STATE_GAME_PAUSE){
                state = STATE_GAME_PLAY;
            }


        }



    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            shape.goNormal();
        }

    }
}

package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player extends Actor {
	private String selectCharacter;
	
	MyTimer mytimer;
	
    public Player(int x, int y, String selectCharacter) {
        super(x, y);
        this.selectCharacter = selectCharacter;
        initPlayer();
    }

    private void initPlayer() {

        ImageIcon iicon = new ImageIcon("src/resources/"+selectCharacter+"/StartPlayer.png");
        Image image = iicon.getImage();
        setImage(image);
    }
    
    public void changePlayerVector(int direction) {
    	ImageIcon iicon = new ImageIcon("src/resources/"+selectCharacter+"/"+direction+"Player.png");
        Image image = iicon.getImage();
        setImage(image);
    }

    public void move(int x, int y) {
    	
    	mytimer.notMoveTime=0;
        int dx = x() + x;
        int dy = y() + y;
        setX(dx);
        setY(dy);
        
    
    }
    
}

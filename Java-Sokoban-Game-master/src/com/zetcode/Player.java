package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player extends Actor {

    public Player(int x, int y) {
        super(x, y);

        initPlayer();
    }

    private void initPlayer() {

        ImageIcon iicon = new ImageIcon("src/resources/sokoban/StartPlayer.png");
        Image image = iicon.getImage();
        setImage(image);
    }
    
    public void changePlayerVector(int direction) {
    	ImageIcon iicon = new ImageIcon("src/resources/sokoban/"+direction+"Player.png");
        Image image = iicon.getImage();
        setImage(image);
    }

    public void move(int x, int y) {

        int dx = x() + x;
        int dy = y() + y;
        
        setX(dx);
        setY(dy);
    }
}

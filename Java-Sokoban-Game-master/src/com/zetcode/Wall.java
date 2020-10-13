package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Wall extends Actor {

	private Image image;

	public Wall(int x, int y, int mode) {
		super(x, y);
		this.mode = mode;
		initWall();
	}

	private void initWall() {
		if(mode==1) {
			ImageIcon iicon = new ImageIcon("src/resources/wall1.png");
			image = iicon.getImage();
			setImage(image);
		}
		else {
			ImageIcon iiicon = new ImageIcon("src/resources/wall.png");
			image = iiicon.getImage();
			setImage(image);
		}
	}
}

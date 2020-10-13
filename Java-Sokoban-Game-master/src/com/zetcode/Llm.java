package com.zetcode;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Llm extends Wall {

	private Image image;
	private ImageIcon llm;
	private ImageIcon wall;
	private ImageIcon dWall;
	Actor actor;

	public Llm(int x, int y) {
		super(x, y, mode);

		initllm();
	}

	private void initllm() {
		
		llm = new ImageIcon("src/recources/llm");
		wall = new ImageIcon("src/resources/wall.png");
		dWall = new ImageIcon("src/resources/wall1.png");
		if (mode == 1) {
			image = dWall.getImage();
			setImage(image);
		} else {
			image = wall.getImage();
			setImage(image);
		}

	}

	public void rellm() {
		
		setImage(null);
		
	}


}

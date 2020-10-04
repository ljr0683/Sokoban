package com.zetcode;

import java.awt.*;

import javax.swing.JFrame;

public class Sokoban {
	
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			GameStart game = new GameStart();
			game.setVisible(true);
		});
	}
}

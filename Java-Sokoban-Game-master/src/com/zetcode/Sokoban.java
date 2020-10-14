package com.zetcode;

import java.awt.*;
import javax.swing.JFrame;

public class Sokoban {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(() -> {
			UIManager game = new UIManager();
			game.setVisible(true);
		});
	}
}

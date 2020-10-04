package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

import com.zetcode.GameStart.*;

public class LevelPanel extends JPanel {

	private JButton[] levelButton = new JButton[5];
	private GameStart frame;

	private int width, height;

	public LevelPanel(GameStart frame) {
		this.frame = frame;
		for (int i = 0; i < levelButton.length; i++) {
			levelButton[i] = new JButton(Integer.toString(i + 1));
			add(levelButton[i]);
			levelButton[i].addActionListener(new MyActionListener());
		}
	}

	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			if (b.equals(levelButton[0])) {
				Board board = new Board(0);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
			if (b.equals(levelButton[1])) {
				Board board = new Board(1);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
			if (b.equals(levelButton[2])) {
				Board board = new Board(2);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
			if (b.equals(levelButton[3])) {
				Board board = new Board(3);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
			if (b.equals(levelButton[4])) {
				Board board = new Board(4);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
		}
	}

}

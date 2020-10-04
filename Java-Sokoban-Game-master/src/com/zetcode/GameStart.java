package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GameStart extends JFrame {
	private final int OFFSET = 30;
	
	private final int LEVEL_SELECT_BUTTON = 1;
	private final int SELECT_CHRACTER_BUTTON = 2;
	private final int DRAW_CHARACTER_BUTTON = 3;
	
//	private final int LEVEL_SELECT_PANEL = 1;
//	private final int SELECT_CHARACTER_PANEL = 2;
//	private final int DRAW_CHARACTER_PANEL = 3;

	private JPanel mainPanel = new JPanel();
	private JButton levelSelectButton = new JButton("레벨 선택");
	private JButton selectCharacterButton = new JButton("캐릭터 선택");
	private JButton drawCharacterButton = new JButton("캐릭터 그리기");
	
	private LevelPanel levelPanel = new LevelPanel(this);

	Container c = getContentPane();

	public GameStart() {
		mainPanel.add(levelSelectButton);
		mainPanel.add(selectCharacterButton);
		mainPanel.add(drawCharacterButton);
		levelSelectButton.addActionListener(new MyActionListener());
		selectCharacterButton.addActionListener(new MyActionListener());
		drawCharacterButton.addActionListener(new MyActionListener());
		
		initUI();
	}

	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Sokoban");

		c.add(mainPanel);

		setSize(500, 300);
		setLocationRelativeTo(null);
		
	}
	
	void changePanel(int i) {
		switch (i) {
			case LEVEL_SELECT_BUTTON :
				c.removeAll();
				c.add(levelPanel);
				c.revalidate();
				c.repaint();
				levelPanel.requestFocusInWindow();
				
			case SELECT_CHRACTER_BUTTON :
				
			case DRAW_CHARACTER_BUTTON :
				
			default:
		}
				
	}
	
	void changePanel( JPanel panel, int width, int height) {
		c.removeAll();
		c.add(panel);
		c.revalidate();
		c.repaint();
		setSize(width + OFFSET, height + 2 * OFFSET);
		panel.requestFocusInWindow();
	}


	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			if (b.equals(levelSelectButton)) {
				changePanel(LEVEL_SELECT_BUTTON);
			}
		}
	}
}

package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

public class DrawCharacterPanel extends JPanel {
	JLabel la = new JLabel("캐릭터 그리는 패널임");

	private GameStart frame;
	private MainPanel previousPanel;
	private JButton backSpaceButton = new JButton("<-");

	public DrawCharacterPanel(GameStart frame, MainPanel previousPanel) {
		setLayout(null);
		this.frame = frame;
		this.previousPanel = previousPanel;

		add(la);
		la.setBounds(100, 100, 110, 20);

		add(backSpaceButton);
		backSpaceButton.addActionListener(new MyActionListener());
		backSpaceButton.setBounds(25, 20, 45, 20);
	}

	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();

			if (b.equals(backSpaceButton)) {
				frame.changePanel(previousPanel);
			}
		}
	}

}

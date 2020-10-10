package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

public class SelectCharacterPanel extends JPanel {
	JLabel la = new JLabel("캐릭터 선택 패널임");

	private UIManager frame;
	private MainPanel previousPanel;
	private JButton backSpaceButton = new JButton("<-");

	public SelectCharacterPanel(UIManager frame, MainPanel previousPanel) {
		setLayout(null);
		this.frame = frame;
		this.previousPanel = previousPanel;

		add(la);
		la.setBounds(100, 100, 110, 20);

		add(backSpaceButton);
		backSpaceButton.setBounds(25, 20, 45, 20);
		backSpaceButton.addActionListener(new MyActionListener());

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

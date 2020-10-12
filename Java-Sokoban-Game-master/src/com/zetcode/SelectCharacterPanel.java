package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

public class SelectCharacterPanel extends JPanel {
	JLabel la = new JLabel("캐릭터 선택 패널임");

	private UIManager frame;
	private LevelPanel previousPanel;
	private SelectCharacterPanel panel;
	
	private ImageIcon defaultMarioImage = new ImageIcon("src/resources/Mario/StartPlayer.png");
	private ImageIcon defaultYellowHatImage = new ImageIcon("src/resources/YellowHat/StartPlayer.png");
	private ImageIcon enteredMouseMarioImage = new ImageIcon("src/resources/Mario/2Player.png");
	private ImageIcon enteredMouseYellowImage = new ImageIcon("src/resources/YellowHat/2Player.png");
	
	private JButton backSpaceButton = new JButton("<-");
	private JButton MarioButton = new JButton(defaultMarioImage);
	private JButton YellowHatButton = new JButton(defaultYellowHatImage);
	
	
	private int levelSelected;

	public SelectCharacterPanel(UIManager frame, LevelPanel previousPanel, int levelSelected) {
		setLayout(null);
		this.frame = frame;
		this.previousPanel = previousPanel;
		this.levelSelected = levelSelected;
		this.panel = this;
		
		add(la);
		la.setBounds(100, 100, 110, 20);

		add(backSpaceButton);
		add(YellowHatButton);
		add(MarioButton);
		YellowHatButton.setBounds(750, 450, 200, 200);
		MarioButton.setBounds(450, 450, 200, 200);
		backSpaceButton.setBounds(25, 20, 45, 20);
		
		backSpaceButton.addActionListener(new MyActionListener());
		YellowHatButton.addActionListener(new MyActionListener());
		MarioButton.addActionListener(new MyActionListener());
		
		YellowHatButton.addMouseListener(new MyMouseListener());
		MarioButton.addMouseListener(new MyMouseListener());
	}

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();

			if (b.equals(backSpaceButton)) {
				frame.changePanel(previousPanel);
			}
			
			if(b.equals(YellowHatButton)) {
				LevelSelectPanel level = new LevelSelectPanel(frame, panel, levelSelected, "YellowHat");
				
				frame.changePanel(level);
			}
			
			if(b.equals(MarioButton)) {
				LevelSelectPanel level = new LevelSelectPanel(frame, panel, levelSelected, "Mario");
				
				frame.changePanel(level);
			}
		}
	}
	
	private class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseEntered(MouseEvent e) {
			JButton b = (JButton)e.getSource();
			if(b.equals(MarioButton)) {
				MarioButton.setIcon(enteredMouseMarioImage);
			}
			
			if(b.equals(YellowHatButton)) {
				YellowHatButton.setIcon(enteredMouseYellowImage);
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			MarioButton.setIcon(defaultMarioImage);
			YellowHatButton.setIcon(defaultYellowHatImage);
		}
	}

}

package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SelectCharacterPanel extends JPanel {

	private UIManager frame;
	private LevelPanel previousPanel;
	private SelectCharacterPanel panel;
	
	private ImageIcon defaultMarioImage = new ImageIcon("src/resources/Mario/StartPlayer.png");
	private ImageIcon defaultYellowHatImage = new ImageIcon("src/resources/YellowHat/StartPlayer.png");
	private ImageIcon enteredMouseMarioImage = new ImageIcon("src/resources/Mario/2Player.png");
	private ImageIcon enteredMouseYellowImage = new ImageIcon("src/resources/YellowHat/2Player.png");
	
	private JLabel backSpaceLabel;
	private JButton MarioButton = new JButton(" : Mario",defaultMarioImage);
	private JButton YellowHatButton = new JButton(" : YellowHat",defaultYellowHatImage);
	private ImageIcon backGroundImage;
	
	
	private int levelSelected;

	public SelectCharacterPanel(UIManager frame, LevelPanel previousPanel, int levelSelected) {
		setLayout(null);
		this.frame = frame;
		this.previousPanel = previousPanel;
		this.levelSelected = levelSelected;
		this.panel = this;
		
		ImageIcon backSpaceIcon = new ImageIcon("src/resources/BackSpace/BackSpace.png");
		backSpaceLabel = new JLabel(backSpaceIcon);
		
		backGroundImage = new ImageIcon("src/resources/Background/SelectCharacterBackGround.png");
		
		add(backSpaceLabel);
		add(YellowHatButton);
		add(MarioButton);
		YellowHatButton.setBounds(850, 450, 150, 64);
		MarioButton.setBounds(550, 450, 150, 64);
		backSpaceLabel.setBounds(25, 20, 128, 128);
		
		YellowHatButton.setHorizontalAlignment(SwingConstants.LEFT);
		MarioButton.setHorizontalAlignment(SwingConstants.LEFT);
		
		backSpaceLabel.addMouseListener(new MyMouseListener());
		YellowHatButton.addActionListener(new MyActionListener());
		MarioButton.addActionListener(new MyActionListener());
		
		YellowHatButton.setRolloverIcon(enteredMouseYellowImage);
		MarioButton.setRolloverIcon(enteredMouseMarioImage);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(backGroundImage.getImage(), 0, 0, this);
	}

	private class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();

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
		public void mouseClicked(MouseEvent e) {
			JLabel la = (JLabel)e.getSource();
			if(la.equals(backSpaceLabel)) {
				frame.changePanel(previousPanel);
			}
		}
	}

}

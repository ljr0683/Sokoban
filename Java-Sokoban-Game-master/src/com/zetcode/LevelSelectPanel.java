package com.zetcode;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class LevelSelectPanel extends JPanel {
	private JButton completedReplayButton = new JButton("Completed Replay");
	private JButton failedReplayButton = new JButton("Failed Replay");
	private JButton startButton = new JButton("Start");
	private JButton randomStartButton = new JButton("Random Mode Start");
	private JLabel score[] = new JLabel[5];
	private String selectCharacter;
	private JLabel backSpaceLabel;
	private ImageIcon backGroundImage;
	
	private File file;
	
	private UIManager frame;
	private SelectCharacterPanel previousPanel;
	private LevelSelectPanel panel;
	
	private int width, height; // 게임의 크기 설정하기 위함
	private int levelSelected;
	
	public LevelSelectPanel(UIManager frame, SelectCharacterPanel previousPanel, int levelSelected, String selectCharacter) {
		
		setLayout(null);
		
		panel = this;
		
		this.frame=frame;
		this.previousPanel=previousPanel;
		this.levelSelected=levelSelected;
		this.selectCharacter = selectCharacter;
		
		ImageIcon backSpaceIcon = new ImageIcon("src/resources/BackSpace/BackSpace.png");
		backSpaceLabel = new JLabel(backSpaceIcon);
		
		backGroundImage = new ImageIcon("src/resources/Background/DefaultBackground.png");
		
		add(backSpaceLabel);
		add(completedReplayButton);
		add(failedReplayButton);
		add(startButton);
		add(randomStartButton);
		score[levelSelected] = new JLabel(Integer.toString(levelSelected));
		add(score[levelSelected]);
		
		backSpaceLabel.setBounds(25, 20, 128, 128);
		completedReplayButton.setBounds(400, 700, 120, 50);
		failedReplayButton.setBounds(600, 700, 120, 50);
		startButton.setBounds(800, 700, 120, 50);
		randomStartButton.setBounds(1000, 700, 120, 50);
		
		backSpaceLabel.addMouseListener(new MyMouseListener()); // 뒤로가기 버튼에 액션리스너 등록
		completedReplayButton.addActionListener(new MyActionListener());
		failedReplayButton.addActionListener(new MyActionListener());
		startButton.addActionListener(new MyActionListener());
		randomStartButton.addActionListener(new MyActionListener());
	}
	
	public void setScore(int levelSelected, int score) {
		this.score[levelSelected].setText(Integer.toString(score));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(backGroundImage.getImage(), 0, 0, this);
	}
	
	class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel la = (JLabel)e.getSource();
			if(la.equals(backSpaceLabel)) {
				frame.changePanel(previousPanel);
			}
		}
	}
	
	class MyActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			
			if(b.equals(completedReplayButton)) {
				
				String s = "Completed";
				String filePath = "src\\replay\\"+s+"_replay_"+levelSelected+".txt";
				file = new File(filePath);
				
				if(file.exists()) {
					Replay replay = new Replay(levelSelected, panel, frame, file, selectCharacter);
				}
				
			}
			
			if(b.equals(failedReplayButton)) {
				
				String s = "Failed";
				String filePath = "src\\replay\\"+s+"_replay_"+levelSelected+".txt";
				file = new File(filePath);
				
				if(file.exists()) {
					Replay replay = new Replay(levelSelected, panel, frame, file, selectCharacter);
				}
			}
			
			if(b.equals(startButton)) {
				Board board = new Board(levelSelected, panel, frame, selectCharacter, 0);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
			
			if(b.equals(randomStartButton)) {
				Random rand = new Random(System.currentTimeMillis());
				int mode = rand.nextInt(3);
				System.out.println(mode);
				Board board = new Board(levelSelected, panel, frame, selectCharacter, mode);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
			
			
		}
	}
}

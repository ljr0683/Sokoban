package com.zetcode;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class LevelSelectPanel extends JPanel {
	private JLabel completedReplayLabel;
	private JLabel failedReplayLabel;
	private JLabel startLabel;
	private JLabel randomStartLabel;
	private String selectCharacter;
	private JLabel backSpaceLabel;
	private ImageIcon backGroundImage;
	private JLabel scoreLabel;
	private JLabel notExitsReplayLabel;
	
	ImageIcon backSpaceIcon = new ImageIcon("src/resources/BackSpace/BackSpace.png");
	ImageIcon startIcon = new ImageIcon("src/resources/GameStartImage/Start.png");
	ImageIcon enteredStartIcon = new ImageIcon("src/resources/GameStartImage/EnteredStart.png");
	ImageIcon randomStartIcon = new ImageIcon("src/resources/GameStartImage/RandomStart.png");
	ImageIcon enteredRandomStartIcon = new ImageIcon("src/resources/GameStartImage/EnteredRandomStart.png");
	ImageIcon completedIcon = new ImageIcon("src/resources/GameStartImage/Completed.png");
	ImageIcon failedIcon = new ImageIcon("src/resources/GameStartImage/Failed.png");
	
	
	private File file;
	private File scoreFile;
	
	private UIManager frame;
	private SelectCharacterPanel previousPanel;
	private LevelSelectPanel panel;
	
	private int width, height; // 게임의 크기 설정하기 위함
	private int levelSelected;
	private int score;
	
	public LevelSelectPanel(UIManager frame, SelectCharacterPanel previousPanel, int levelSelected, String selectCharacter) {
		
		setLayout(null);
		
		panel = this;
		
		this.frame=frame;
		this.previousPanel=previousPanel;
		this.levelSelected=levelSelected;
		this.selectCharacter = selectCharacter;
		scoreFile = new File("src/score/score_"+levelSelected+".txt");
		if(!scoreFile.exists()) {
			score = 0;
		}
		else {
			try {
				FileReader fr = new FileReader(scoreFile);
				BufferedReader bufReader = new BufferedReader(fr);
				String stringScore;
				do {
					stringScore = bufReader.readLine();
				}while((bufReader.readLine()) != null);
				
				score = Integer.parseInt(stringScore);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		startLabel = new JLabel(startIcon);
		randomStartLabel = new JLabel(randomStartIcon);
		completedReplayLabel = new JLabel(completedIcon);
		failedReplayLabel = new JLabel(failedIcon);
		
		backSpaceLabel = new JLabel(backSpaceIcon);
		backGroundImage = new ImageIcon("src/resources/Background/DefaultBackground.png");
		
		Font scoreFont = new Font("배달의민족 도현", Font.BOLD, 50);
		scoreLabel = new JLabel("BestScore : "+score);
		scoreLabel.setFont(scoreFont);
		
		Font replayFileNotExitsFont = new Font("배달의 민족 도현", Font.BOLD, 50);
		notExitsReplayLabel = new JLabel("Replay File Not Exits");
		notExitsReplayLabel.setFont(replayFileNotExitsFont);
		
		add(backSpaceLabel);
		add(completedReplayLabel);
		add(failedReplayLabel);
		add(startLabel);
		add(randomStartLabel);
		add(scoreLabel);
		add(notExitsReplayLabel);
		
		backSpaceLabel.setBounds(25, 20, 128, 128);
		completedReplayLabel.setBounds(400, 700, 96, 96);
		failedReplayLabel.setBounds(600, 700, 96, 96);
		startLabel.setBounds(800, 700, 96, 96);
		randomStartLabel.setBounds(1000, 700, 96, 96);
		scoreLabel.setBounds(530, 150, 500, 60);
		notExitsReplayLabel.setBounds(500, 250, 700, 200);
		
		
		backSpaceLabel.addMouseListener(new MyMouseListener()); // 뒤로가기 버튼에 액션리스너 등록
		completedReplayLabel.addMouseListener(new MyMouseListener());
		failedReplayLabel.addMouseListener(new MyMouseListener());
		startLabel.addMouseListener(new MyMouseListener());
		randomStartLabel.addMouseListener(new MyMouseListener());
		
		notExitsReplayLabel.setForeground(new Color(255, 0, 0));
		notExitsReplayLabel.setVisible(false);
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
			
			if(la.equals(startLabel)) {
				Board board = new Board(levelSelected, panel, frame, selectCharacter, 0);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
				startLabel.setIcon(startIcon);
				notExitsReplayLabel.setVisible(false);
			}
			
			if(la.equals(randomStartLabel)) {
				Random rand = new Random(System.currentTimeMillis());
				int mode = rand.nextInt(5);
				System.out.println(mode);
				Board board = new Board(levelSelected, panel, frame, selectCharacter, mode);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
				randomStartLabel.setIcon(randomStartIcon);
				notExitsReplayLabel.setVisible(false);
			}
			
			if(la.equals(completedReplayLabel)) {
				String s = "Completed";
				String filePath = "src\\replay\\"+s+"_replay_"+levelSelected+".txt";
				file = new File(filePath);
				
				if(file.exists()) {
					Replay replay = new Replay(levelSelected, panel, frame, file, selectCharacter);
					notExitsReplayLabel.setVisible(false);
				}
				else {
					notExitsReplayLabel.setVisible(true);
				}
				
			}
			
			if(la.equals(failedReplayLabel)) {
				String s = "Failed";
				String filePath = "src\\replay\\"+s+"_replay_"+levelSelected+".txt";
				file = new File(filePath);
				
				if(file.exists()) {
					Replay replay = new Replay(levelSelected, panel, frame, file, selectCharacter);
					notExitsReplayLabel.setVisible(false);
				}
				else {
					notExitsReplayLabel.setVisible(true);
				}
				
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel la= (JLabel)e.getSource();
			if(la.equals(startLabel)) {
				startLabel.setIcon(enteredStartIcon);
			}
			
			if(la.equals(randomStartLabel)) {
				randomStartLabel.setIcon(enteredRandomStartIcon);
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			startLabel.setIcon(startIcon);
			randomStartLabel.setIcon(randomStartIcon);
		}
		
		
	}
}

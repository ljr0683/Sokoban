package com.zetcode;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class LevelSelectPanel extends JPanel {
	private JButton completedReplayButton = new JButton("Completed Replay");
	private JButton failedReplayButton = new JButton("Failed Replay");
	private JButton startButton = new JButton("start");
	private JButton backSpaceButton = new JButton("<-");
	private JLabel score[] = new JLabel[6];
	private String selectCharacter;
	
	private File file;
	
	private UIManager frame;
	private SelectCharacterPanel previousPanel;
	private LevelSelectPanel panel;
	
	private int width, height; // ������ ũ�� �����ϱ� ����
	private int levelSelected;
	
	public LevelSelectPanel(UIManager frame, SelectCharacterPanel previousPanel, int levelSelected, String selectCharacter) {
		
		panel = this;
		
		this.frame=frame;
		this.previousPanel=previousPanel;
		this.levelSelected=levelSelected;
		this.selectCharacter = selectCharacter;
		
		add(backSpaceButton);
		add(completedReplayButton);
		add(failedReplayButton);
		add(startButton);
		score[levelSelected] = new JLabel(Integer.toString(levelSelected));
		add(score[levelSelected]);
		
		backSpaceButton.addActionListener(new MyActionListener()); // �ڷΰ��� ��ư�� �׼Ǹ����� ���
		completedReplayButton.addActionListener(new MyActionListener());
		failedReplayButton.addActionListener(new MyActionListener());
		startButton.addActionListener(new MyActionListener());
		//backSpaceButton.setBounds(25, 20, 45, 20); // �ڷΰ��� ��ư ��ġ, ũ�� ����
	}
	
	public void setScore(int levelSelected, int score) {
		this.score[levelSelected].setText(Integer.toString(score));
	}
	
	class MyActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			if(b.equals(backSpaceButton)) {
				frame.changePanel(previousPanel);
			}
			
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
				Board board = new Board(levelSelected, panel, frame, selectCharacter);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
			
			
		}
	}
}

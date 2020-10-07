package com.zetcode;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class LevelSelectPanel extends JPanel {
	private JButton completedReplayButton = new JButton("Completed Replay");
	private JButton failedReplayButton = new JButton("Failed Replay");
	private JButton startButton = new JButton("start");
	private JButton backSpaceButton = new JButton("<-");
	private JLabel score[] = new JLabel[6];
	
	private InsertQueue[] completedReplayQueue = new InsertQueue[5];
	private InsertQueue[] failedReplayQueue = new InsertQueue[5];
	
	
	private GameStart frame;
	private LevelPanel previousPanel;
	private LevelSelectPanel panel;
	
	private int width, height; // ������ ũ�� �����ϱ� ����
	private int levelSelected;
	
	public LevelSelectPanel(GameStart frame, LevelPanel previousPanel, int levelSelected) {
		panel = this;
		
		this.frame=frame;
		this.previousPanel=previousPanel;
		this.levelSelected=levelSelected;
		
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
	
	public void setCompletedReplayQueue(InsertQueue completedReplayQueue, int i) {
		this.completedReplayQueue[i] = completedReplayQueue;
		System.out.println(i);
	}
	
	public void setFailedReplayQueue(InsertQueue failedReplayQueue, int i) {
		this.failedReplayQueue[i] = failedReplayQueue;
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
				
				if(completedReplayQueue != null) {
					Board board = new Board(levelSelected, panel, frame, completedReplayQueue[levelSelected]);
					width = board.getBoardWidth();
					height = board.getBoardHeight();
					frame.changePanel(board, width, height);
					}
				else
					System.out.println("true");
			}
			
			if(b.equals(failedReplayButton)) {
				System.out.println("failedReplayButton");
				if(failedReplayQueue != null) {
					Board board = new Board(levelSelected, panel, frame, failedReplayQueue[levelSelected]);
					width = board.getBoardWidth();
					height = board.getBoardHeight();
					frame.changePanel(board, width, height);
				}
			}
			
			if(b.equals(startButton)) {
				Board board = new Board(levelSelected, panel, frame);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}
			
			
		}
	}
}

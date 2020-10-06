package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

import com.zetcode.GameStart.*;

public class LevelPanel extends JPanel {

	private JButton[] levelButton = new JButton[6]; // 버튼 5개 선언

	private GameStart frame; // GameStart 클래스의 change 메소드를 사용하기 위해 선언
	private MainPanel previousPanel; // 뒤로가기 버튼을 위해 선언(전 패널 객체를 얻기 위해)
	private JButton backSpaceButton = new JButton("<-"); // 뒤로가기 버튼
	private LevelPanel panel;

	private int width, height; // 게임의 크기 설정하기 위함

	public LevelPanel(GameStart frame, MainPanel previousPanel) { // 생성자
		setLayout(null); // 레이아웃을 설정 x 내 마음대로 배치 가능
		this.frame = frame; // GameStart 객체를 받아옴 change 메소드를 사용하기 위함
		this.previousPanel = previousPanel; // 뒤로가기를 위함
		panel = this;

		add(backSpaceButton); // 뒤로가기 버튼 패널에 추가
		backSpaceButton.addActionListener(new MyActionListener()); // 뒤로가기 버튼에 액션리스너 등록
		backSpaceButton.setBounds(25, 20, 45, 20); // 뒤로가기 버튼 위치, 크기 조정
		for (int i = 0; i < levelButton.length; i++) { // 레벨 버튼 초기화 및 버튼 등록, 액션리스너 등록, 위치, 크기 조정
			levelButton[i] = new JButton(Integer.toString(i + 1));
			levelButton[i].setBounds(93 + i * 60, 200, 50, 30);
			add(levelButton[i]);
			levelButton[i].addActionListener(new MyActionListener());
		}

	}

	class MyActionListener implements ActionListener { // 액션리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource(); // 어떤 버튼이 눌렸는지 알아냄
			
			for(int i=0; i<levelButton.length; i++) {
				if(b.equals(levelButton[i])) {
					Board board = new Board(i, panel, frame);
					width = board.getBoardWidth();
					height = board.getBoardHeight();
					frame.changePanel(board, width, height);
				}
			}

			if (b.equals(backSpaceButton)) { // 뒤로가기 버튼이 눌리면
				frame.changePanel(previousPanel);
			}
		}
	}

}

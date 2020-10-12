package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LevelPanel extends JPanel {

	private JButton[] levelButton = new JButton[5]; // 버튼 5개 선언

	private UIManager frame; // GameStart 클래스의 change 메소드를 사용하기 위해 선언
	private MainPanel previousPanel; // 뒤로가기 버튼을 위해 선언(전 패널 객체를 얻기 위해)
	private LevelPanel panel; //board에서 뒤로가기 위한 패널 파라미터로 넘겨주는 패널
	private SelectCharacterPanel selectCharacterPanel;
	private Score Score;
	private ImageIcon backGroundImage;

	public LevelPanel(UIManager frame, MainPanel previousPanel) { //생성자
		setLayout(null); // 레이아웃을 설정 x 내 마음대로 배치 가능
		this.frame = frame; // GameStart 객체를 받아옴 change 메소드를 사용하기 위함
		this.previousPanel = previousPanel; // 뒤로가기를 위함
		panel = this;
		Score = new Score();
		
		ImageIcon normalIcon = new ImageIcon("src/resources/ButtonIcon/Button1.png");
		
		
		for (int i = 0; i < levelButton.length; i++) { // 레벨 버튼 초기화 및 버튼 등록, 액션리스너 등록, 위치, 크기 조정
			levelButton[i] = new JButton(normalIcon);
			levelButton[i].setBounds(500 + i * 120, 700, 64, 64);
			add(levelButton[i]);
			levelButton[i].addActionListener(new MyActionListener());
			
		}
		
		backGroundImage = new ImageIcon("src/resources/Background/MainPng.png");
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(backGroundImage.getImage(), 0, 0, this);
	}
	
	


	private class MyActionListener implements ActionListener { // 액션리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource(); // 어떤 버튼이 눌렸는지 알아냄
			
			for(int i=0; i<levelButton.length; i++) {
				if(b.equals(levelButton[i])) {
					selectCharacterPanel  = new SelectCharacterPanel(frame, panel, i);
					frame.changePanel(selectCharacterPanel);
				}
			}
			
//			if(b.equals(backSpaceButton)) { // 뒤로가기 버튼이 눌리면
//				frame.changePanel(previousPanel);
//			}
		}
	}

}

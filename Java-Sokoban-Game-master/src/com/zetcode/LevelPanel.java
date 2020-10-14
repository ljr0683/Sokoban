package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LevelPanel extends JPanel {

	private JLabel[] levelJLabel = new JLabel[5];

	private UIManager frame; // GameStart 클래스의 change 메소드를 사용하기 위해 선언
	private LevelPanel panel; //board에서 뒤로가기 위한 패널 파라미터로 넘겨주는 패널
	private SelectCharacterPanel selectCharacterPanel;
	private ImageIcon backGroundImage;
	private ImageIcon[] normalIcon = new ImageIcon[5];
	private ImageIcon[] mouseEnteredIcon = new ImageIcon[5];


	public LevelPanel(UIManager frame) { //생성자
		setLayout(null); // 레이아웃을 설정 x 내 마음대로 배치 가능
		this.frame = frame; // GameStart 객체를 받아옴 change 메소드를 사용하기 위함
		panel = this;
		
		
		for (int i = 0; i < levelJLabel.length; i++) { // 레벨 버튼 초기화 및 버튼 등록, 액션리스너 등록, 위치, 크기 조정
			normalIcon[i] = new ImageIcon("src/resources/ButtonIcon/defaultButton/Button"+i+".png");
			mouseEnteredIcon[i] = new ImageIcon("src/resources/ButtonIcon/EnteredButton/Button"+i+".png");
			levelJLabel[i] = new JLabel(normalIcon[i]);
			levelJLabel[i].setBounds(500 + i * 120, 700, 64, 64);
			add(levelJLabel[i]);
			levelJLabel[i].addMouseListener(new MyMouseListener());
			
		}
		
		backGroundImage = new ImageIcon("src/resources/Background/SelectLevelBackground.png");
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(backGroundImage.getImage(), 0, 0, this);
	}
	
	private class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel la = (JLabel)e.getSource();
			
			for(int i=0; i<levelJLabel.length; i++) {
				if(la.equals(levelJLabel[i])) {
					la.setIcon(normalIcon[i]);

					selectCharacterPanel  = new SelectCharacterPanel(frame, panel, i);
					frame.changePanel(selectCharacterPanel);
				}
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel la = (JLabel)e.getSource();
			
			for(int i=0; i<levelJLabel.length; i++) {
				if(la.equals(levelJLabel[i])) {
					
					la.setIcon(mouseEnteredIcon[i]);
					
				}
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			JLabel la = (JLabel)e.getSource();
			
			for(int i=0; i<levelJLabel.length; i++) {
				if(la.equals(levelJLabel[i])) {
					la.setIcon(normalIcon[i]);
				}
			}
		}
	}
	
}

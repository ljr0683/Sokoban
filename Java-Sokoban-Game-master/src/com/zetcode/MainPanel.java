package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainPanel extends JPanel {

	private UIManager frame; // UIManager 클래스의 change 메소드를 사용하기 위해 선언
	private MainPanel panel; // LevelPanel, SelectCharacterPanel, DrawCharacterPanel 객체에 자기 자신을 인자로 넘겨주기 위한
	// 패널 선언
	
	private ImageIcon titleImage = new ImageIcon("src/resources/Title/Title.png");
	private ImageIcon startImage = new ImageIcon("src/resources/Title/Start.png");

	public MainPanel(UIManager frame) { // 생성자
		
		setLayout(null); // 레이아웃을 설정 x 내 마음대로 배치 가능
		this.frame = frame; // GameStart 객체를 받아옴 change 메소드를 사용하기 위함
		panel = this; // panel변수를 자기 자신으로 초기화하여 LevelPanel, SelectCharacterPanel, DrawCharacterPanel
						// 객체에 자기 자신의 정보를 넘겨줌
		
		addMouseListener(new MyMouseListener());
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(titleImage.getImage(), 0, 0, this);
		g.drawImage(startImage.getImage(), 0, 0, this);
		setOpaque(true);
	}
	
	class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			frame.changePanel(new LevelPanel(frame));
		}
	}

	
}

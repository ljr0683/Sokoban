package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GameStart extends JFrame {
	private final int OFFSET = 30;
	
	private MainPanel mainPanel = new MainPanel(this);

	private JPanel previousPanel;
	
	Container c = getContentPane();

	public GameStart() {

		initUI();
	}

	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Sokoban");

		c.add(mainPanel);

		setSize(500, 300);
		setLocationRelativeTo(null);

	}

	void changePanel(JPanel panel) { // 메인화면에서 사용하는 changePanel, Panel 객체를 받아와 그 패널로 컨텐트팬을 채움
		c.removeAll();
		c.add(panel);
		c.revalidate();
		c.repaint();
	} // 이 메소드는 메인화면에서 작동, 레벨 선택 버튼, 캐릭터 선택 버튼, 캐릭터 그리는 버튼 중 하나를 선택하면 작동

	void changePanel(JPanel panel, int width, int height) { // 레벨 바꿀때 쓰는 changePanel, Panel 객체를 받아와 그 패널로 컨텐트팬을 채움
		c.removeAll();
		c.add(panel);
		c.revalidate();
		c.repaint();
		setSize(width + OFFSET, height + 2 * OFFSET);
		panel.requestFocusInWindow();
	} // 이 메소드는 레벨 선택화면에서 작동, 레벨 1,2,3,4,5중 하나를 선택하면 작동
	

}

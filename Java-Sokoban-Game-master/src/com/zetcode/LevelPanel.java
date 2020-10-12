package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LevelPanel extends JPanel {

	private JButton[] levelButton = new JButton[5]; // ��ư 5�� ����

	private UIManager frame; // GameStart Ŭ������ change �޼ҵ带 ����ϱ� ���� ����
	private MainPanel previousPanel; // �ڷΰ��� ��ư�� ���� ����(�� �г� ��ü�� ��� ����)
	private LevelPanel panel; //board���� �ڷΰ��� ���� �г� �Ķ���ͷ� �Ѱ��ִ� �г�
	private SelectCharacterPanel selectCharacterPanel;
	private Score Score;
	private ImageIcon backGroundImage;

	public LevelPanel(UIManager frame, MainPanel previousPanel) { //������
		setLayout(null); // ���̾ƿ��� ���� x �� ������� ��ġ ����
		this.frame = frame; // GameStart ��ü�� �޾ƿ� change �޼ҵ带 ����ϱ� ����
		this.previousPanel = previousPanel; // �ڷΰ��⸦ ����
		panel = this;
		Score = new Score();
		
		ImageIcon normalIcon = new ImageIcon("src/resources/ButtonIcon/Button1.png");
		
		
		for (int i = 0; i < levelButton.length; i++) { // ���� ��ư �ʱ�ȭ �� ��ư ���, �׼Ǹ����� ���, ��ġ, ũ�� ����
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
	
	


	private class MyActionListener implements ActionListener { // �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource(); // � ��ư�� ���ȴ��� �˾Ƴ�
			
			for(int i=0; i<levelButton.length; i++) {
				if(b.equals(levelButton[i])) {
					selectCharacterPanel  = new SelectCharacterPanel(frame, panel, i);
					frame.changePanel(selectCharacterPanel);
				}
			}
			
//			if(b.equals(backSpaceButton)) { // �ڷΰ��� ��ư�� ������
//				frame.changePanel(previousPanel);
//			}
		}
	}

}

package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainPanel extends JPanel {

	private UIManager frame; // UIManager Ŭ������ change �޼ҵ带 ����ϱ� ���� ����
	private MainPanel panel; // LevelPanel, SelectCharacterPanel, DrawCharacterPanel ��ü�� �ڱ� �ڽ��� ���ڷ� �Ѱ��ֱ� ����
	// �г� ����
	
	private ImageIcon titleImage = new ImageIcon("src/resources/Title/Title.png");
	private ImageIcon startImage = new ImageIcon("src/resources/Title/Start.png");

	public MainPanel(UIManager frame) { // ������
		
		setLayout(null); // ���̾ƿ��� ���� x �� ������� ��ġ ����
		this.frame = frame; // GameStart ��ü�� �޾ƿ� change �޼ҵ带 ����ϱ� ����
		panel = this; // panel������ �ڱ� �ڽ����� �ʱ�ȭ�Ͽ� LevelPanel, SelectCharacterPanel, DrawCharacterPanel
						// ��ü�� �ڱ� �ڽ��� ������ �Ѱ���
		
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

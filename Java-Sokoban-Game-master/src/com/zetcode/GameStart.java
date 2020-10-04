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

	void changePanel(JPanel panel) { // ����ȭ�鿡�� ����ϴ� changePanel, Panel ��ü�� �޾ƿ� �� �гη� ����Ʈ���� ä��
		c.removeAll();
		c.add(panel);
		c.revalidate();
		c.repaint();
	} // �� �޼ҵ�� ����ȭ�鿡�� �۵�, ���� ���� ��ư, ĳ���� ���� ��ư, ĳ���� �׸��� ��ư �� �ϳ��� �����ϸ� �۵�

	void changePanel(JPanel panel, int width, int height) { // ���� �ٲܶ� ���� changePanel, Panel ��ü�� �޾ƿ� �� �гη� ����Ʈ���� ä��
		c.removeAll();
		c.add(panel);
		c.revalidate();
		c.repaint();
		setSize(width + OFFSET, height + 2 * OFFSET);
		panel.requestFocusInWindow();
	} // �� �޼ҵ�� ���� ����ȭ�鿡�� �۵�, ���� 1,2,3,4,5�� �ϳ��� �����ϸ� �۵�
	

}

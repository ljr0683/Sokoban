package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

public class LevelPanel extends JPanel {

	private JButton[] levelButton = new JButton[6]; // ��ư 5�� ����

	private UIManager frame; // GameStart Ŭ������ change �޼ҵ带 ����ϱ� ���� ����
	private MainPanel previousPanel; // �ڷΰ��� ��ư�� ���� ����(�� �г� ��ü�� ��� ����)
	private JButton backSpaceButton = new JButton("<-"); // �ڷΰ��� ��ư
	private LevelPanel panel; //board���� �ڷΰ��� ���� �г� �Ķ���ͷ� �Ѱ��ִ� �г�
	private SelectCharacterPanel selectCharacterPanel;
	private Score Score;


	public LevelPanel(UIManager frame, MainPanel previousPanel) { //������
		setLayout(null); // ���̾ƿ��� ���� x �� ������� ��ġ ����
		this.frame = frame; // GameStart ��ü�� �޾ƿ� change �޼ҵ带 ����ϱ� ����
		this.previousPanel = previousPanel; // �ڷΰ��⸦ ����
		panel = this;
		Score = new Score();
		
		add(backSpaceButton); // �ڷΰ��� ��ư �гο� �߰�
		backSpaceButton.addActionListener(new MyActionListener()); // �ڷΰ��� ��ư�� �׼Ǹ����� ���
		backSpaceButton.setBounds(25, 20, 45, 20); // �ڷΰ��� ��ư ��ġ, ũ�� ����
		
		for (int i = 0; i < levelButton.length; i++) { // ���� ��ư �ʱ�ȭ �� ��ư ���, �׼Ǹ����� ���, ��ġ, ũ�� ����
			levelButton[i] = new JButton(Integer.toString(i + 1));
			levelButton[i].setBounds(93 + i * 60, 200, 50, 30);
			add(levelButton[i]);
			levelButton[i].addActionListener(new MyActionListener());
		}

	}

	class MyActionListener implements ActionListener { // �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource(); // � ��ư�� ���ȴ��� �˾Ƴ�
			
			for(int i=0; i<levelButton.length; i++) {
				if(b.equals(levelButton[i])) {
					selectCharacterPanel  = new SelectCharacterPanel(frame, panel, i);
					frame.changePanel(selectCharacterPanel);
				}
			}
			
			if(b.equals(backSpaceButton)) { // �ڷΰ��� ��ư�� ������
				frame.changePanel(previousPanel);
			}
		}
	}

}

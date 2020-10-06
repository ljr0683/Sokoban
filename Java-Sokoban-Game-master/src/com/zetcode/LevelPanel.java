package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

import com.zetcode.GameStart.*;

public class LevelPanel extends JPanel {

	private JButton[] levelButton = new JButton[5]; // ��ư 5�� ����

	private GameStart frame; // GameStart Ŭ������ change �޼ҵ带 ����ϱ� ���� ����
	private MainPanel previousPanel; // �ڷΰ��� ��ư�� ���� ����(�� �г� ��ü�� ��� ����)
	private JButton backSpaceButton = new JButton("<-"); // �ڷΰ��� ��ư
	private LevelPanel panel;

	private int width, height; // ������ ũ�� �����ϱ� ����

	public LevelPanel(GameStart frame, MainPanel previousPanel) { // ������
		setLayout(null); // ���̾ƿ��� ���� x �� ������� ��ġ ����
		this.frame = frame; // GameStart ��ü�� �޾ƿ� change �޼ҵ带 ����ϱ� ����
		this.previousPanel = previousPanel; // �ڷΰ��⸦ ����
		panel = this;

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

			if (b.equals(levelButton[0])) { // ù���� ��ư�� ������
				Board board = new Board(0, panel, frame);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}

			if (b.equals(levelButton[1])) { // �ι�° ��ư�� ������
				Board board = new Board(1, panel, frame);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}

			if (b.equals(levelButton[2])) { // ����° ��ư�� ������
				Board board = new Board(2, panel, frame);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}

			if (b.equals(levelButton[3])) { // �׹�° ��ư�� ������
				Board board = new Board(3, panel, frame);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}

			if (b.equals(levelButton[4])) { // �ټ���° ��ư�� ������
				Board board = new Board(4, panel, frame);
				width = board.getBoardWidth();
				height = board.getBoardHeight();
				frame.changePanel(board, width, height);
			}

			if (b.equals(backSpaceButton)) { // �ڷΰ��� ��ư�� ������
				frame.changePanel(previousPanel);
			}
		}
	}

}

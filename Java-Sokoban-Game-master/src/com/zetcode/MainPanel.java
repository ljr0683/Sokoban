package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

public class MainPanel extends JPanel {
	private JButton levelSelectButton = new JButton("���� ����");
	private JButton selectCharacterButton = new JButton("ĳ���� ����");
	private JButton drawCharacterButton = new JButton("ĳ���� �׸���");
	// ��ư ����

	private GameStart frame; // GameStart Ŭ������ change �޼ҵ带 ����ϱ� ���� ����
	private MainPanel panel; // LevelPanel, SelectCharacterPanel, DrawCharacterPanel ��ü�� �ڱ� �ڽ��� ���ڷ� �Ѱ��ֱ� ����
	// �г� ����

	public MainPanel(GameStart frame) { // ������
		setLayout(null); // ���̾ƿ��� ���� x �� ������� ��ġ ����
		this.frame = frame; // GameStart ��ü�� �޾ƿ� change �޼ҵ带 ����ϱ� ����
		panel = this; // panel������ �ڱ� �ڽ����� �ʱ�ȭ�Ͽ� LevelPanel, SelectCharacterPanel, DrawCharacterPanel
						// ��ü�� �ڱ� �ڽ��� ������ �Ѱ���

		add(levelSelectButton);
		add(selectCharacterButton);
		add(drawCharacterButton);
		// �гο� ��ư �߰�

		levelSelectButton.setBounds(80, 200, 100, 30);
		selectCharacterButton.setBounds(190, 200, 100, 30);
		drawCharacterButton.setBounds(300, 200, 110, 30);
		// ��ư�� ��ġ, ũ�� ����

		levelSelectButton.addActionListener(new MyActionListener());
		selectCharacterButton.addActionListener(new MyActionListener());
		drawCharacterButton.addActionListener(new MyActionListener());
		// ��ư�� ������ ���
	}

	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource(); // � ��ư�� ���ȴ��� �˾Ƴ�

			if (b.equals(levelSelectButton)) { // ���� ���� ��ư�� �����ٸ�
				frame.changePanel(new LevelPanel(frame, panel)); // GameStart�� changePanel �޼ҵ带 ȣ���Ͽ� LevePanel�� �г�
																		// ��ȯ
			}

			if (b.equals(selectCharacterButton)) {
				frame.changePanel(new SelectCharacterPanel(frame, panel)); // ���� ����
			}

			if (b.equals(drawCharacterButton)) {
				frame.changePanel(new DrawCharacterPanel(frame, panel)); // ���� ����
			}

		}
	}

}

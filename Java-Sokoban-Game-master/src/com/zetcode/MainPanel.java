package com.zetcode;

import java.awt.event.*;

import javax.swing.*;

public class MainPanel extends JPanel {
	private JButton levelSelectButton = new JButton("레벨 선택");
	private JButton selectCharacterButton = new JButton("캐릭터 선택");
	private JButton drawCharacterButton = new JButton("캐릭터 그리기");
	// 버튼 생성

	private GameStart frame; // GameStart 클래스의 change 메소드를 사용하기 위해 선언
	private MainPanel panel; // LevelPanel, SelectCharacterPanel, DrawCharacterPanel 객체에 자기 자신을 인자로 넘겨주기 위한
	// 패널 선언

	public MainPanel(GameStart frame) { // 생성자
		setLayout(null); // 레이아웃을 설정 x 내 마음대로 배치 가능
		this.frame = frame; // GameStart 객체를 받아옴 change 메소드를 사용하기 위함
		panel = this; // panel변수를 자기 자신으로 초기화하여 LevelPanel, SelectCharacterPanel, DrawCharacterPanel
						// 객체에 자기 자신의 정보를 넘겨줌

		add(levelSelectButton);
		add(selectCharacterButton);
		add(drawCharacterButton);
		// 패널에 버튼 추가

		levelSelectButton.setBounds(80, 200, 100, 30);
		selectCharacterButton.setBounds(190, 200, 100, 30);
		drawCharacterButton.setBounds(300, 200, 110, 30);
		// 버튼의 위치, 크기 조정

		levelSelectButton.addActionListener(new MyActionListener());
		selectCharacterButton.addActionListener(new MyActionListener());
		drawCharacterButton.addActionListener(new MyActionListener());
		// 버튼에 리스너 등록
	}

	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource(); // 어떤 버튼이 눌렸는지 알아냄

			if (b.equals(levelSelectButton)) { // 레벨 선택 버튼을 눌렀다면
				frame.changePanel(new LevelPanel(frame, panel)); // GameStart의 changePanel 메소드를 호출하여 LevePanel로 패널
																		// 전환
			}

			if (b.equals(selectCharacterButton)) {
				frame.changePanel(new SelectCharacterPanel(frame, panel)); // 위와 동일
			}

			if (b.equals(drawCharacterButton)) {
				frame.changePanel(new DrawCharacterPanel(frame, panel)); // 위와 동일
			}

		}
	}

}

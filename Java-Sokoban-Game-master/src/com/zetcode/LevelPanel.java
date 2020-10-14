package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LevelPanel extends JPanel {

	private JLabel[] levelJLabel = new JLabel[5];

	private UIManager frame; // GameStart Ŭ������ change �޼ҵ带 ����ϱ� ���� ����
	private LevelPanel panel; //board���� �ڷΰ��� ���� �г� �Ķ���ͷ� �Ѱ��ִ� �г�
	private SelectCharacterPanel selectCharacterPanel;
	private ImageIcon backGroundImage;
	private ImageIcon[] normalIcon = new ImageIcon[5];
	private ImageIcon[] mouseEnteredIcon = new ImageIcon[5];


	public LevelPanel(UIManager frame) { //������
		setLayout(null); // ���̾ƿ��� ���� x �� ������� ��ġ ����
		this.frame = frame; // GameStart ��ü�� �޾ƿ� change �޼ҵ带 ����ϱ� ����
		panel = this;
		
		
		for (int i = 0; i < levelJLabel.length; i++) { // ���� ��ư �ʱ�ȭ �� ��ư ���, �׼Ǹ����� ���, ��ġ, ũ�� ����
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

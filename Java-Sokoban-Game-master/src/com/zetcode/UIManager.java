package com.zetcode;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class UIManager extends JFrame {
	private final int OFFSET = 30;

	private MainPanel mainPanel = new MainPanel(this);

	private Container c = getContentPane();

	public UIManager() {

		initUI();
	}

	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Sokoban");

		c.add(mainPanel);

		setSize(1500, 900);
		setLocationRelativeTo(null);
	}

	void changePanel(JPanel panel) { 
		c.removeAll();
		c.add(panel);
		c.revalidate();
		c.repaint();
		setSize(1500, 900);
		setLocationRelativeTo(null);
		
	} 

	void changePanel(JPanel panel, int width, int height) { 
		c.removeAll();
		c.add(panel);
		c.revalidate();
		c.repaint();
		setSize(width + OFFSET, height + 2 * OFFSET);
		panel.requestFocusInWindow();
		setLocationRelativeTo(null);
	} 

}

package com.zetcode;

import javax.swing.*;

public class Score {
	private int score;
	private int moveCount;
	private int timerCount;
	
	public Score() {
		score = 0;
		
	}
	
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	
	public void setTimerCount(int timerCount) {
		this.timerCount = timerCount;
	}
	
	public void setScore(int i, int score) { // ������ ���� �������� setScore ��
		this.score = score;
	}
	
	public int getScore(int i) {
		return score;
	}
	
}

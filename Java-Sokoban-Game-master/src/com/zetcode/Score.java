package com.zetcode;

import javax.swing.*;

public class Score {
	private int score[] = new int[5];
	
	public Score() {
		for(int i=0; i<score.length; i++) {
			score[i]=0;
		}
	}
	
	public void setScore(int i, int score) { // ������ ���� �������� setScore ��
		this.score[i] = score;
	}
	
	public int getScore(int i) {
		return score[i];
	}
	
}

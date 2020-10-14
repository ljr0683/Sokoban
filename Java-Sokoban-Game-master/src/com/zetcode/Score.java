package com.zetcode;

import java.io.*;

public class Score {
	private int score;
	private int maxScore;
	private int moveCount;
	private int timerCount;
	private int levelSelected;
	private FileIO createFile;
	
	public Score(int levelSelected, int moveCount, int timerCount, File file) {
		maxScore = 5000;
		this.levelSelected = levelSelected;
		this.moveCount = moveCount;
		this.timerCount = timerCount;
		
		if(!file.exists()) {
			score = 0;
		}
		else {
			try {
				FileReader fr = new FileReader(file);
				BufferedReader bufReader = new BufferedReader(fr);
				
				do {
					score = Integer.parseInt(bufReader.readLine()) - 48;
					
				} while(bufReader.readLine() != null);
				
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		computeScore();
	}
	
	private void computeScore() {
		int currentScore = maxScore - (moveCount * 3) - (timerCount * 2);
		
		if(currentScore>score) {
			createFile = new FileIO();
			score = currentScore;
			createFile.scoreFileInput(levelSelected, score);
		}
	}
	
}

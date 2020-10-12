package com.zetcode;

import java.io.*;
import java.util.*;

public class FileIO {
	private Deque<Integer> replay_Queue = new LinkedList<Integer>();
	private String filePath;
	private String folderPath;
	
	public void scoreFileInput(int levelSelected) {
		
		
	}
	
	public void replayFileInput(int levelSelected, String s) {
		
		filePath = "src\\replay\\"+s+"_replay_"+levelSelected+".txt";
		folderPath = "src\\replay";
		
		File folder = new File(folderPath);
		File file = new File(filePath);
		
		FileInput(levelSelected, s, folder, file);
	}
	
	public void FileInput(int levelSelected, String s, File folder, File file) {

		FileWriter fw = null;
		
		try {
			if(!folder.exists())
				folder.mkdir();
			fw = new FileWriter(file,false);
			int size = replay_Queue.size();
			for(int j=0; j<size; j++) {
				fw.write(Integer.toString(replay_Queue.poll()));
			}
			fw.flush();
			fw.close();
		}catch(IOException e) {
			
		}
	}
	
	public void enqueue(int i) {
		replay_Queue.offer(i);
	}
	
	public String getFilePath() {
		return filePath;
	}
}

package com.zetcode;

import java.io.*;
import java.util.*;

public class FileIO {
	private Deque<Integer> replay_Queue = new LinkedList<Integer>();
	private String filePath;
	
	public void FileInput(int levelSelected, String s) {
		FileWriter fw = null;
		
		filePath = "src\\replay\\"+s+"_replay_"+levelSelected+".txt";
		String folderPath = "src\\replay";
		
		File folder = new File(folderPath);
		File file = new File(filePath);
		
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

package com.zetcode;

import java.io.*;
import java.util.*;

public class FileIO {
	private Deque<Integer> replay_Queue = new LinkedList<Integer>();
	private String filePath;
	
	public void FileInput(int levelSelected, String s) {
		FileWriter fw = null;
		
		filePath = "src/replay/"+s+"_replay_"+levelSelected+".txt";
		
		File file = new File(filePath);
		
		try {
			fw = new FileWriter(filePath,false);
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

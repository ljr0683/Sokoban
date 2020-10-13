package com.zetcode;

import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;

public class MyTimer {
	private Image image;
	private ImageIcon boom1;
    private ImageIcon boom2;
    private ImageIcon boom3;
    
	static int boom=0;
	static int notMoveTime=0;
	static int time;
	
	private boolean isfinished = false;
	
	public MyTimer() {
		time = 0;
		runTimer();
	}
	
	private void runTimer() {
		
		Timer m_timer = new Timer();
		TimerTask m_task = new TimerTask() {
			
			@Override
			public void run() {
				if(!isfinished) {
					time++;
					notMoveTime++;
					System.out.println(time);
					if(notMoveTime>3) {
						boom+=1;
					}
					if(notMoveTime==0) {
						boom=0;
					}
					
				}
				else {
					m_timer.cancel();
				}
			}
		};
	
		m_timer.schedule(m_task, 1000, 1000);
		
	}
	
	public void setIsFinished(boolean isfinished) {
		this.isfinished = isfinished;
	}
	
	public int getTime() {
		return time;
	}


	private void setImage(Image image2) {
	// TODO Auto-generated method stub
	
	}
}
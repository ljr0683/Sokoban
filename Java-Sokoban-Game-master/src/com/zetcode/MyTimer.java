package com.zetcode;

import java.util.*;

public class MyTimer {
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
					System.out.println(time);
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
}

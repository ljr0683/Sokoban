package com.zetcode;

import java.util.*;

public class InsertQueue {
	private Queue<Integer> Myque = new LinkedList<Integer>();
	private int size=0;
	
	public void enQueue(int i) {
		Myque.offer(i);
		size++;
	}
	
	public int deQueue() {
		size--;
		return Myque.poll();
		
	}
	
	public int getSize() {
		return size;
	}
}

package com.zetcode;

import java.io.*;
import java.util.*;

public class Replay {
	private Deque<Integer> replay_Deque = new LinkedList<>();
	private Stack<Integer> replay_Stack = new Stack<>();
	private File file;
	private Board board;
	private boolean undo = false;

	private int backCounter = 0;
	
	Replay(int levelSelected, LevelSelectPanel previousPanel, UIManager frame, File file, String selectCharacter){
		replay_Deque = new LinkedList<>();
		
		this.file = file;
		
		try {
			FileReader fr = new FileReader(file);
			int c;
			while((c = fr.read()) != -1) {
				replay_Deque.offer(c-48);
			}
			fr.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		createBoard(levelSelected, previousPanel, frame, file, selectCharacter);
	}
	
	Replay(Board board){
		this.board=board;
		undo = true;
	}
	
	private void createBoard(int levelSelected, LevelSelectPanel previousPanel, UIManager frame, File file, String selectCharacter) {
		board = new Board(levelSelected, previousPanel, frame, file, this, selectCharacter);
		int width = board.getBoardWidth();
		int height = board.getBoardHeight();
		frame.changePanel(board, width, height);
	}
	
	public void goBack() {
		int key3 = 0 ;
		 
		if(!undo) {
			if (!replay_Stack.isEmpty()) {
				key3 = replay_Stack.pop();
				backCounter++;
			}
		}
		else {
			if(!replay_Deque.isEmpty()) {
				key3 = replay_Deque.poll();
			}
		}
		if(key3 == 5 || key3 == 6) {
			switch (key3) {
			
			case 5:
				board.setIsCollision(false);
				if(!undo) {
					replay_Deque.offerFirst(key3);
					key3 = replay_Stack.pop();
				}
				else {
					key3 = replay_Deque.poll();
				}
				
				break;
	
			case 6:
				board.setIsCollision(true);
				if(!undo) {
					replay_Deque.offerFirst(key3);
					key3 = replay_Stack.pop();
				}
				else {
					key3 = replay_Deque.poll();
				}
				
				break;
	
			}
			
			DoingGoBack(key3);
		}
		else {
			DoingGoBack(key3);
		}
	}
	
	private void DoingGoBack(int key3) {
		switch (key3) {
		case Board.LEFT_COLLISION:
			if (board.getIsCollision()) {
				for(int i=0; i<board.getBaggsSize(); i++) {
					Baggage collisionBag = board.getBaggs(i);
					if(board.getSoko().isLeftCollision(collisionBag))
						board.setBags(collisionBag);
				}
				board.getBags().move(Board.SPACE, 0);
			}
			board.getSoko().move(Board.SPACE, 0);
			board.getSoko().changePlayerVector(Board.LEFT_COLLISION);
			
			if(!undo)
				replay_Deque.offerFirst(key3);
			break;
			
		case Board.RIGHT_COLLISION:
			if (board.getIsCollision()) {
				for(int i=0; i<board.getBaggsSize(); i++) {
					Baggage collisionBag = board.getBaggs(i);
					if(board.getSoko().isRightCollision(collisionBag))
						board.setBags(collisionBag);
				}
				
				board.getBags().move(-Board.SPACE, 0);
			}

			board.getSoko().move(-Board.SPACE, 0);
			board.getSoko().changePlayerVector(Board.RIGHT_COLLISION);
			
			if(!undo)
				replay_Deque.offerFirst(key3);
			break;

		case Board.TOP_COLLISION:
			if (board.getIsCollision()) {
				for(int i=0; i<board.getBaggsSize(); i++) {
					Baggage collisionBag = board.getBaggs(i);
					if(board.getSoko().isTopCollision(collisionBag))
						board.setBags(collisionBag);
				}
				board.getBags().move(0, Board.SPACE);
			}

			board.getSoko().move(0, Board.SPACE);
			board.getSoko().changePlayerVector(Board.TOP_COLLISION);
			
			if(!undo)
				replay_Deque.offerFirst(key3);
			break;

		case Board.BOTTOM_COLLISION:
			if (board.getIsCollision()) {
				for(int i=0; i<board.getBaggsSize(); i++) {
					Baggage collisionBag = board.getBaggs(i);
					if(board.getSoko().isBottomCollision(collisionBag))
						board.setBags(collisionBag);
				}
				board.getBags().move(0, -Board.SPACE);
			}

			board.getSoko().move(0, -Board.SPACE);
			board.getSoko().changePlayerVector(Board.BOTTOM_COLLISION);
			
			if(!undo)
				replay_Deque.offerFirst(key3);
			break;
			
		default : 
			break;
		}
	}
	
	
	public void goAhead() {
		int key2 = replay_Deque.poll();

		replay_Stack.push(key2);
		if (backCounter > 0) {
			backCounter--;
		} else {
			replay_Deque.offer(key2);
		}
		
		if(key2 == 5 || key2 == 6) {
		
			switch (key2) {
				
			case 5:
				board.setIsCollision(true);
				key2 = replay_Deque.poll();
				replay_Stack.push(key2);
				replay_Deque.offer(key2);
				break;
				
			case 6:
				board.setIsCollision(false);
				key2 = replay_Deque.poll();
				replay_Stack.push(key2);
				replay_Deque.offer(key2);
			break;
				
			default:
				break;
			}
			
			DoingGoAhead(key2);
		}
		else {
			DoingGoAhead(key2);
		}
	}
	
	public void DoingGoAhead(int key2) {
		switch (key2) {

		case Board.LEFT_COLLISION:

			if (board.getCheckWallCollision(board.getSoko(), Board.LEFT_COLLISION)) { // soko객체 왼쪽에 벽이 있다면 움직이지 않고 키 이벤트를 끝냄
				return;
			}

			if (board.getCheckBagCollision(Board.LEFT_COLLISION)) {
				return;
			}

			board.getSoko().move(-Board.SPACE, 0); 
			board.getSoko().changePlayerVector(Board.LEFT_COLLISION);

			if (board.getBags()!= null) {
				board.callIsEntered(board.getBags());
				if (board.getBags().getIsEntered()) {
					break;
				}
			}
			
			if (board.callIsFailedDetected(board.getBags())) {
				board.callIsFailed();
			}
			

			break;

		case Board.RIGHT_COLLISION:

			if (board.getCheckWallCollision(board.getSoko(),Board.RIGHT_COLLISION)) {
				return;
			}

			if (board.getCheckBagCollision(Board.RIGHT_COLLISION)) {
				return;
			}

			board.getSoko().move(Board.SPACE, 0);
			board.getSoko().changePlayerVector(Board.RIGHT_COLLISION);

			if (board.getBags() != null) {
				board.callIsEntered(board.getBags());
				if (board.getBags().getIsEntered()) {
					break;
				}
			}

			
			if (board.callIsFailedDetected(board.getBags())) {
				board.callIsFailed();
			}
			
			break;

		case Board.TOP_COLLISION:

			if (board.getCheckWallCollision(board.getSoko(), Board.TOP_COLLISION)) {
				return;
			}

			if (board.getCheckBagCollision(Board.TOP_COLLISION)) {
				return;
			}

			board.getSoko().move(0, -Board.SPACE);
			board.getSoko().changePlayerVector(Board.TOP_COLLISION);

			if (board.getBags() != null) {
				board.callIsEntered(board.getBags());
				if (board.getBags().getIsEntered()) {
					break;
				}
			}

		
			if (board.callIsFailedDetected(board.getBags())) {
				board.callIsFailed();
			}
			

			break;

		case Board.BOTTOM_COLLISION:
			if (board.getCheckWallCollision(board.getSoko(), Board.BOTTOM_COLLISION)) {
				return;
			}

			if (board.getCheckBagCollision(Board.BOTTOM_COLLISION)) {
				return;
			}

			board.getSoko().move(0, Board.SPACE);
			board.getSoko().changePlayerVector(Board.BOTTOM_COLLISION);

			if (board.getBags() != null) {
				board.callIsEntered(board.getBags());
				if (board.getBags().getIsEntered()) {
					break;
				}
			}

			
			if (board.callIsFailedDetected(board.getBags())) {
				board.callIsFailed();
			}
			

			break;
		
		default :
			break;
		}
		
	}
	
	public void offerReplay_Deque(int key) {
		replay_Deque.offer(key);
	}
}
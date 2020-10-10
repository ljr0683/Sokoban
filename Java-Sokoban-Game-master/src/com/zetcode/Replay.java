package com.zetcode;

import java.io.*;
import java.util.*;

public class Replay {
	private Deque<Integer> replay_Deque;
	private Stack<Integer> replay_Stack = new Stack<>();
	private File file;
	private Board board;
	

	private int backCounter = 0;
	
	Replay(int levelSelected, LevelSelectPanel previousPanel, UIManager frame, File file){
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
		
		createBoard(levelSelected, previousPanel, frame, file);
	}
	
	Replay(Board board, int key){
		this.board=board;
		replay_Stack.add(key);
	}
	
	private void createBoard(int levelSelected, LevelSelectPanel previousPanel, UIManager frame, File file) {
		board = new Board(levelSelected, previousPanel, frame, file, this);
		int width = board.getBoardWidth();
		int height = board.getBoardHeight();
		frame.changePanel(board, width, height);
	}
	
	public void goBack() {
		int key3 = 0 ;
		if (!replay_Stack.isEmpty()) {
			key3 = replay_Stack.pop();
			backCounter++;
		} 

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

			replay_Deque.offerFirst(key3);
			break;

		case 5:
			board.setIsCollision(false);
			replay_Deque.offerFirst(key3);

			break;

		case 6:
			board.setIsCollision(true);
			replay_Deque.offerFirst(key3);
			
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
		
		switch (key2) {

		case Board.LEFT_COLLISION:

			if (board.getCheckWallCollision(board.getSoko(), Board.LEFT_COLLISION)) { // soko객체 왼쪽에 벽이 있다면 움직이지 않고 키 이벤트를 끝냄
				return;
			}

			if (board.getCheckBagCollision(Board.LEFT_COLLISION)) {
				return;
			}

			board.getSoko().move(-Board.SPACE, 0); 

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
			
		case 5:
			board.setIsCollision(true);
			break;
			
		case 6:
			board.setIsCollision(false);
			break;
			
		default:
			break;
		}
	}
	
}

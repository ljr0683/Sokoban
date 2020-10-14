package com.zetcode;

public class FailedDetected {
	private Board board;
	
	public FailedDetected(Board board) {
		this.board=board;	
	}
	
	public boolean isFailedDetected(Baggage bag) { // failed ���� detected �޼ҵ�

		if (bag != null) {

			if (board.getCheckWallCollision(bag, Board.TOP_COLLISION) || board.getCheckWallCollision(bag, Board.BOTTOM_COLLISION)) { // ��, �Ʒ� �� �ΰ� // �����丵 �Ϸ�

				for (int i = 0; i < board.getWallsSize(); i++) {
					Wall item1 = board.getWalls(i); //item1 = ���� �ִ� ��
					if (bag.isTopCollision(item1) || bag.isBottomCollision(item1)) {

						for (int j = 0; j < board.getWallsSize(); j++) {
							Wall item2 = board.getWalls(j);
							if (!item2.equals(item1) && item1.isLeftCollision(item2) || item1.isRightCollision(item2)) { // item2�� ���� �� ���� ��

								for (int k = 0; k < board.getBaggsSize(); k++) {
									Baggage item3 = board.getBaggs(k);
									if (!item3.equals(bag) && item2.isBottomCollision(item3) || item2.isTopCollision(item3)) { // item3�� item2 �Ʒ��� bag

										return true;
									}
								}
							}
						}
					}
				}
				if (board.getCheckWallCollision(bag, Board.LEFT_COLLISION) || board.getCheckWallCollision(bag, Board.RIGHT_COLLISION)) {

					return true;
				}
			}

			if (board.getCheckWallCollision(bag, Board.LEFT_COLLISION) || board.getCheckWallCollision(bag, Board.RIGHT_COLLISION)) { // ��, ������ �� 2�� �����丵 ��
				for (int i = 0; i < board.getWallsSize(); i++) {

					Wall item1 = board.getWalls(i);

					if (bag.isLeftCollision(item1) || bag.isRightCollision(item1)) {

						for (int j = 0; j < board.getWallsSize(); j++) {

							Wall item2 = board.getWalls(j);

							if (!item2.equals(item1) && item1.isTopCollision(item2) || item1.isBottomCollision(item2)) {
								for (int k = 0; k < board.getBaggsSize(); k++) {
									Baggage item3 = board.getBaggs(k);
									if (!item3.equals(bag) && item2.isRightCollision(item3)
											|| item2.isLeftCollision(item3)) {
										return true;
									}
								}
							}
						}
					}
				}
			} //

			if (board.getCheckWallCollision(bag, Board.TOP_COLLISION) || board.getCheckWallCollision(bag, Board.BOTTOM_COLLISION)) { // ���� �� 1�� , �Ʒ� �� 1�� �����Ѱ��� �����丵
				for (int i = 0; i < board.getBaggsSize(); i++) {
					Baggage item1 = board.getBaggs(i);
					if (!item1.equals(bag) && bag.isLeftCollision(item1) || bag.isRightCollision(item1)) { // item1 = bag �翷�� bag ��ü

						for (int j = 0; j < board.getWallsSize(); j++) {
							Wall item2 = board.getWalls(j);

							if (bag.isBottomCollision(item2) || bag.isTopCollision(item2)) { // item2�� bag��ü ���� �Ʒ��� ��
								for (int k = 0; k < board.getBaggsSize(); k++) {
									Baggage item3 = board.getBaggs(k);

									if (!item3.equals(item1) && !item3.equals(bag)) { // item3�� item2(��) �� ���� ��ü
										if (item2.isLeftCollision(item3) || item2.isRightCollision(item3))
											return true;

										if (bag.isTopCollision(item2) && item1.isBottomCollision(item3)
												|| bag.isBottomCollision(item2) && item1.isTopCollision(item3)) {
											for (int h = 0; h < board.getWallsSize(); h++) {
												Wall item4 = board.getWalls(h);
												if (!item4.equals(item2) && item3.isRightCollision(item4)
														|| item3.isLeftCollision(item4)) {
													return true;
												}
											}
										}
									}
								}
							}

						}
					}
				}
			} //

			if (board.getCheckWallCollision(bag, Board.LEFT_COLLISION) || board.getCheckWallCollision(bag, Board.RIGHT_COLLISION)) { // ���� �� �ϳ� ������ �� �ϳ� �����Ѱ��� �����丵
				for (int i = 0; i < board.getBaggsSize(); i++) {
					Baggage item1 = board.getBaggs(i);
					if (!item1.equals(bag) && bag.isTopCollision(item1) || bag.isBottomCollision(item1)) { //item1�� bag ���� ��

						for (int j = 0; j < board.getWallsSize(); j++) {
							Wall item2 =  board.getWalls(j);

							if (bag.isRightCollision(item2) || bag.isBottomCollision(item2)) { //item2�� 
								for (int k = 0; k < board.getBaggsSize(); k++) {
									Baggage item3 = board.getBaggs(k);

									if (!item3.equals(item1) && !item3.equals(bag)) {
										if (item2.isTopCollision(item3) || item2.isRightCollision(item3))
											return true;

										if (bag.isRightCollision(item2) && item1.isLeftCollision(item3)
												|| bag.isLeftCollision(item2) && item1.isRightCollision(item3)) {
											for (int h = 0; h <  board.getWallsSize(); h++) {
												Wall item4 = board.getWalls(h);
												if (!item4.equals(item2) && item3.isTopCollision(item4)
														|| item3.isBottomCollision(item4)) {
													return true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int i = 0; i < board.getBaggsSize(); i++) { // 4���� bag�϶�
				Baggage item1 = board.getBaggs(i);
				if (!item1.equals(bag) && bag.isTopCollision(item1)) {

					for (int j = 0; j < board.getBaggsSize(); j++) {

						Baggage item2 =board.getBaggs(j);

						if (!item2.equals(item1) && !item2.equals(bag)) {

							if (item1.isLeftCollision(item2) || item1.isRightCollision(item2)) {

								for (int k = 0; k < board.getBaggsSize(); k++) {

									Baggage item3 = board.getBaggs(k);

									if (item2.isBottomCollision(item3)) {

										return true;
									}
								}
							}
						}
					}
				}
			}
			
			for (int i = 0; i < board.getBaggsSize(); i++) { // 4���� bag�϶�
				Baggage item1 = board.getBaggs(i);
				if (!item1.equals(bag) && bag.isBottomCollision(item1)) {

					for (int j = 0; j < board.getBaggsSize(); j++) {

						Baggage item2 = board.getBaggs(j);

						if (!item2.equals(item1) && !item2.equals(bag)) {

							if (item1.isLeftCollision(item2) || item1.isRightCollision(item2)) {

								for (int k = 0; k < board.getBaggsSize(); k++) {

									Baggage item3 = board.getBaggs(k);

									if (item2.isTopCollision(item3)) {

										return true;
									}
								}
							}
						}
					}
				}
			}

		}

		return false;

	}
}
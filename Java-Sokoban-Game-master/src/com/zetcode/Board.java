package com.zetcode;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class Board extends JPanel {

	private Deque<Integer> replay_Deque = new LinkedList<>();
	private Stack<Integer> replay_Stack = new Stack<>();
	private Stack<Integer> test_Stack = new Stack<>();

	private JButton backSpaceButton = new JButton("<-");
	private GameStart frame;
	private LevelSelectPanel previousPanel;
	private Baggage bags = null;
	private boolean isReplay = false;
	private int size;
	private File file;
	private boolean isCollision = false;
	private int levelSelected;
	private int backCounter = 0;
	private boolean flag = false; // �и鼭 ������ Ȯ���ϴ� �Լ�

	private final int OFFSET = 30;
	private final int SPACE = 20;
	private final int LEFT_COLLISION = 1;
	private final int RIGHT_COLLISION = 2;
	private final int TOP_COLLISION = 3;
	private final int BOTTOM_COLLISION = 4;

	private ArrayList<Wall> walls;
	private ArrayList<Baggage> baggs;
	private ArrayList<Area> areas;

	private Player soko;
	private int w = 0;
	private int h = 0;

	private boolean isCompleted = false;
	private boolean isFailed = false;

	private String level[] ={
            "    ######\n"
          + "    ##   #\n"
          + "    ##   #\n"
          + "  ####   ##\n"
          + "  ##      #\n"
          + "#### # ## #   ######\n"
          + "##   # ## #####    #\n"
          + "## $              .#\n"
          + "###### ### #@##    #\n"
          + "    ##     #########\n"
          + "    ########\n",
          
      		"    ######\n"
          + "############\n"
          + "#    #     ###\n"
          + "#    #       #\n"
          + "#.   # ####  #\n"
          + "# $    @ ##  #\n"
          + "#    # #    ##\n"
          + "###### ##    #\n"
          + "  #          #\n"
          + "  #    #     #\n"
          + "  ############\n",
          
      	  "        ######## \n"
          + "        #     @# \n"
          + "        # $#$ ## \n"
          + "        # $  $# \n"
          + "        ##$ $ # \n"
          + "######### $ # ###\n"
          + "#....  ## $  $  #\n"
          + "##...    $  $   #\n"
          + "#....  ##########\n"
          + "########         \n", 
          
            "              ########\n"
          + "              #  ....#\n"
          + "   ############  ....#\n"
          + "   #    #  $ $   ....#\n"
          + "   # $$$#$  $ #  ....#\n"
          + "   #  $     $ #  ....#\n"
          + "   # $$ #$ $ $########\n"
          + "####  $ #     #       \n"
          + "#   # #########       \n"
          + "#    $  ##            \n"
          + "# $$#$$ @#            \n"
          + "#   #   ##            \n"
          + "#########             \n",
          
            "        #####    \n"
          + "        #   #####\n"
          + "        # #$##  #\n"
          + "        #     $ #\n"
          + "######### ###   #\n"
          + "#....  ## $  $###\n"
          + "#....    $ $$ ## \n"
          + "#....  ##$  $ @# \n"
          + "#########  $  ## \n"
          + "        # $ $  # \n"
          + "        ### ## # \n"
          + "          #    # \n"
          + "          ###### \n",
          
            "#################\n"
          + "#               #\n"
          + "#        #      #\n"
          + "#          $@   #\n"
          + "#      #        #\n"
          + "#         .     #\n"
          + "#               #\n"
          + "#               #\n"
          + "#################\n"
 
          };

	public Board(int levelSelected, LevelSelectPanel previousPanel, GameStart frame) {

		setLayout(null);

		this.previousPanel = previousPanel;
		this.levelSelected = levelSelected;
		this.frame = frame;

		add(backSpaceButton);
		backSpaceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton) e.getSource();

				if (b.equals(backSpaceButton)) {
					frame.changePanel(previousPanel);
					frame.setSize(500, 300);
				}

			}
		}); // �ڷΰ��� ��ư�� �׼Ǹ����� ���
		backSpaceButton.setBounds(25, 20, 45, 20);
		initBoard();
	}

	public Board(int levelSelected, LevelSelectPanel previousPanel, GameStart frame, File file) {

		setLayout(null);

		this.previousPanel = previousPanel;
		this.levelSelected = levelSelected;
		this.frame = frame;
		this.file = file;

		add(backSpaceButton);
		backSpaceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton) e.getSource();

				if (b.equals(backSpaceButton)) {
					frame.changePanel(previousPanel);
					frame.setSize(500, 300);
				}

			}
		}); // �ڷΰ��� ��ư�� �׼Ǹ����� ���

		backSpaceButton.setBounds(25, 20, 45, 20);

		try {
			FileReader fr = new FileReader(file);
			int c;
			while ((c = fr.read()) != -1) {
				replay_Deque.offer(c - 48);
			}
		} catch (IOException e) {
			System.out.println("����");
		}

		isReplay = true;
		initBoard();

	}

	private void initBoard() {

		addKeyListener(new TAdapter());
		setFocusable(true);
		initWorld();
	}

	public int getBoardWidth() {
		return this.w;
	}

	public int getBoardHeight() {
		return this.h;
	}

	private void initWorld() { // �ʱ�ȭ

		walls = new ArrayList<>();
		baggs = new ArrayList<>();
		areas = new ArrayList<>();

		int x = OFFSET;
		int y = OFFSET;

		Wall wall; // ��
		Baggage b; // �̴°�
		Area a; // �����°�

		for (int i = 0; i < level[levelSelected].length(); i++) {

			char item = level[levelSelected].charAt(i);

			switch (item) {

			case '\n':
				y += SPACE;

				if (this.w < x) { // width�� ũ�⸦ x�� ���� ū�ɷ� ��
					this.w = x;
				}

				x = OFFSET; // �ѹ� �ٹٲ��� �Ǹ� x=OFF���� �ٽ� �ʱ�ȭ
				break;

			case '#':
				wall = new Wall(x, y); // x,y ������ Wall ��ü ����
				walls.add(wall); // Wall ��ü�� Wall ��� ����Ʈ�� ����
				x += SPACE; // x�� ��ĭ(SPACE)�� ����
				break;

			case '$':
				b = new Baggage(x, y);
				baggs.add(b); // b == Baggage ��ü
				x += SPACE;
				break;

			case '.':
				a = new Area(x, y); // ������ ����
				areas.add(a); // a == Area ��ü
				x += SPACE;
				break;

			case '@':
				soko = new Player(x, y); // �÷��̾���
				x += SPACE;
				break;

			case ' ':
				x += SPACE;
				break;

			default:
				break;
			}

			h = y; // ���̸� ����.
		}
	}

	private void buildWorld(Graphics g) {

		g.setColor(new Color(250, 240, 170));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		ArrayList<Actor> world = new ArrayList<>();

		world.addAll(walls);
		world.addAll(areas);
		world.addAll(baggs);
		world.add(soko);

		for (int i = 0; i < world.size(); i++) {

			Actor item = world.get(i);

			if (item instanceof Player || item instanceof Baggage) {

				g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
			} else {

				g.drawImage(item.getImage(), item.x(), item.y(), this);
			}

			if (isCompleted) { // isCompleted�� true�� ȭ�鿡 completed�� ���

				g.setColor(new Color(0, 0, 0));
				g.drawString("Completed", w / 2 - 35, 20);
			}

			if (isFailed) {

				g.setColor(new Color(0, 0, 0));
				g.drawString("Failed", w / 2 - 35, 20);
			}

		}
	}

	@Override
	public void paintComponent(Graphics g) { // ������Ʈ�� �׸�
		super.paintComponent(g);

		buildWorld(g);
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			if (isCompleted) { // ������ ����.

				return;
			}

			if (isFailed) {

				return;
			}

			if (isReplay != true) {
				int key = e.getKeyCode();
				flag=false;

				switch (key) {

				case KeyEvent.VK_LEFT:

					if (checkWallCollision(soko, LEFT_COLLISION)) { // soko��ü ���ʿ� ���� �ִٸ� �������� �ʰ� Ű �̺�Ʈ�� ����
						return;
					}

					if (checkBagCollision(LEFT_COLLISION)) {
						// �������� ����������, soko��ü ���ʿ� bag��ü�� �����ϰ� �� ���ʿ� �Ǵٸ� Bag ��ü�� �����ϰų�,
						// soko ��ü ���� Bag ��ü�� ���ʿ� Wall ��ü�� �����ϸ� �������� �ʰ� �̺�Ʈ ����
						return;
					}

					soko.move(-SPACE, 0); // ���� �� ��Ȳ�� �������� �ʴ´ٸ� �������� ��ĭ ������.

					if (flag) {
						if (!isCollision) {
							replay_Deque.offer(5);
						}
						isCollision = true;
					} else {
						if (isCollision) {
							replay_Deque.offer(6);
						}

						isCollision = false;
					}

					replay_Deque.offer(LEFT_COLLISION);

					if (bags != null) {
						isEntered(bags);
						if (bags.getIsEntered()) {
							break;
						}
					}

					if (isFailedDetected(bags)) {
						isFailed();
					}

					break;

				case KeyEvent.VK_RIGHT:

					if (checkWallCollision(soko, RIGHT_COLLISION)) {
						return;
					}

					if (checkBagCollision(RIGHT_COLLISION)) {
						return;
					}

					soko.move(SPACE, 0);

					if (flag) {
						if (!isCollision) {
							replay_Deque.offer(5);
						}
						isCollision = true;
					} else {
						if (isCollision) {
							replay_Deque.offer(6);
						}

						isCollision = false;
					}

					replay_Deque.offer(RIGHT_COLLISION);

					if (bags != null) {
						isEntered(bags);
						if (bags.getIsEntered()) {
							break;
						}
					}

					if (isFailedDetected(bags)) {
						isFailed();
					}

					break;

				case KeyEvent.VK_UP:

					if (checkWallCollision(soko, TOP_COLLISION)) {
						return;
					}

					if (checkBagCollision(TOP_COLLISION)) {
						return;
					}

					soko.move(0, -SPACE);

					if (flag) {
						if (!isCollision) {
							replay_Deque.offer(5);
						}
						isCollision = true;
					} else {
						if (isCollision) {
							replay_Deque.offer(6);
						}

						isCollision = false;
					}

					replay_Deque.offer(TOP_COLLISION);

					if (bags != null) {
						isEntered(bags);
						if (bags.getIsEntered()) {
							break;
						}
					}

					if (isFailedDetected(bags)) {
						isFailed();
					}

					break;

				case KeyEvent.VK_DOWN:


					if (checkWallCollision(soko, BOTTOM_COLLISION)) {
						return;
					}

					if (checkBagCollision(BOTTOM_COLLISION)) {
						return;
					}

					soko.move(0, SPACE);

					if (flag) {
						if (!isCollision) {
							replay_Deque.offer(5);
						}
						isCollision = true;
					} else {
						if (isCollision) {
							replay_Deque.offer(6);
						}

						isCollision = false;
					}

					replay_Deque.offer(BOTTOM_COLLISION);

					if (bags != null) {
						isEntered(bags);
						if (bags.getIsEntered()) {
							break;
						}
					}

					if (isFailedDetected(bags)) {
						isFailed();
					}

					break;

				case KeyEvent.VK_R:

					restartLevel();

					break;

				default:
					break;
				}

				isCompleted();
				repaint();
			} else {
				size = replay_Deque.size();

				int key1 = e.getKeyCode();

				switch (key1) { // ���� ���� �Ǻ��ؾߵʤ�!@#!@$!@$%#!#$%

				case KeyEvent.VK_LEFT:
					int key3 = replay_Stack.pop();
					if (!replay_Stack.isEmpty()) {
						backCounter++;
					}

					switch (key3) {
					case LEFT_COLLISION:
						if (isCollision) {
							bags.move(SPACE, 0);
						}

						soko.move(SPACE, 0);

						replay_Deque.offerFirst(key3);
						break;
					case RIGHT_COLLISION:
						if (isCollision) {
							bags.move(-SPACE, 0);
						}

						soko.move(-SPACE, 0);

						replay_Deque.offerFirst(key3);
						break;

					case TOP_COLLISION:
						if (isCollision) {
							bags.move(0, SPACE);
						}

						soko.move(0, SPACE);

						replay_Deque.offerFirst(key3);
						break;

					case BOTTOM_COLLISION:
						if (isCollision) {
							bags.move(0, -SPACE);
						}

						soko.move(0, -SPACE);

						replay_Deque.offerFirst(key3);
						break;

					case 5:
						isCollision = false;
						replay_Deque.offerFirst(key3);

						break;

					case 6:
						isCollision = true;
						replay_Deque.offerFirst(key3);
						break;

					}
					break;

				case KeyEvent.VK_RIGHT:

					int key2 = replay_Deque.poll();

					replay_Stack.push(key2);
					if (backCounter > 0) {
						backCounter--;
					} else {
						replay_Deque.offer(key2);
						test_Stack.add(key2);
					}
					
					switch (key2) {

					case LEFT_COLLISION:

						if (checkWallCollision(soko, LEFT_COLLISION)) { // soko��ü ���ʿ� ���� �ִٸ� �������� �ʰ� Ű �̺�Ʈ�� ����
							return;
						}

						if (checkBagCollision(LEFT_COLLISION)) {
							// �������� ����������, soko��ü ���ʿ� bag��ü�� �����ϰ� �� ���ʿ� �Ǵٸ� Bag ��ü�� �����ϰų�,
							// soko ��ü ���� Bag ��ü�� ���ʿ� Wall ��ü�� �����ϸ� �������� �ʰ� �̺�Ʈ ����
							return;
						}

						soko.move(-SPACE, 0); // ���� �� ��Ȳ�� �������� �ʴ´ٸ� �������� ��ĭ ������.

						if (bags != null) {
							isEntered(bags);
							if (bags.getIsEntered()) {
								break;
							}
						}

						if (isFailedDetected(bags)) {
							isFailed();
						}

						break;

					case RIGHT_COLLISION:

						if (checkWallCollision(soko, RIGHT_COLLISION)) {
							return;
						}

						if (checkBagCollision(RIGHT_COLLISION)) {
							return;
						}

						soko.move(SPACE, 0);

						if (bags != null) {
							isEntered(bags);
							if (bags.getIsEntered()) {
								break;
							}
						}

						if (isFailedDetected(bags)) {
							isFailed();
						}

						break;

					case TOP_COLLISION:

						if (checkWallCollision(soko, TOP_COLLISION)) {
							return;
						}

						if (checkBagCollision(TOP_COLLISION)) {
							return;
						}

						soko.move(0, -SPACE);

						if (bags != null) {
							isEntered(bags);
							if (bags.getIsEntered()) {
								break;
							}
						}

						if (isFailedDetected(bags)) {
							isFailed();
						}

						break;

					case BOTTOM_COLLISION:
						if (checkWallCollision(soko, BOTTOM_COLLISION)) {
							return;
						}

						if (checkBagCollision(BOTTOM_COLLISION)) {
							return;
						}

						soko.move(0, SPACE);

						if (bags != null) {
							isEntered(bags);
							if (bags.getIsEntered()) {
								break;
							}
						}

						if (isFailedDetected(bags)) {
							isFailed();
						}

						break;

					default:
						break;
					}

					break;

				default:
					break;
				}
				isCompleted();
				repaint();
			}
		}
	}

	private boolean checkWallCollision(Actor actor, int type) {

		switch (type) { // type == ���� ������ �� �Ʒ����� ���� 1,2,3,4

		case LEFT_COLLISION: // ���� Ű�� ��������

			for (int i = 0; i < walls.size(); i++) {

				Wall wall = walls.get(i); // walls ��� ����Ʈ���� �ϳ��� wall�� Wall��ü����

				if (actor.isLeftCollision(wall)) { // actor ���ʿ� ���� �ִٸ� true ����

					return true;
				}
			}

			return false; // actor ���ʿ� ���� ���ٸ� false ����

		case RIGHT_COLLISION:

			for (int i = 0; i < walls.size(); i++) {

				Wall wall = walls.get(i);

				if (actor.isRightCollision(wall)) {

					return true;
				}
			}

			return false;

		case TOP_COLLISION:

			for (int i = 0; i < walls.size(); i++) {

				Wall wall = walls.get(i);

				if (actor.isTopCollision(wall)) {

					return true;
				}
			}

			return false;

		case BOTTOM_COLLISION:

			for (int i = 0; i < walls.size(); i++) {

				Wall wall = walls.get(i);

				if (actor.isBottomCollision(wall)) {

					return true;
				}
			}

			return false;

		default:
			break;
		}

		return false;
	}

	private boolean checkBagCollision(int type) { // Bag ��ü�� �浹

		switch (type) {

		case LEFT_COLLISION: // ���� Ű�� ��������,

			for (int i = 0; i < baggs.size(); i++) {

				Baggage bag = baggs.get(i);

				if (soko.isLeftCollision(bag)) { // soko ���ʿ� bag�� �����ϸ�

					for (int j = 0; j < baggs.size(); j++) {

						Baggage item = baggs.get(j); // bag ��� ����Ʈ�� �ϳ��� �˻��Ͽ� item�� ����

						if (!bag.equals(item)) { // item�� bag�� �ٸ� ��ü���

							if (bag.isLeftCollision(item)) {// bag ���ʿ� item(�� �ٸ� Baggage)�� �����ϸ� �ȿ�����
								return true;
							}
						}

						if (checkWallCollision(bag, LEFT_COLLISION)) {// bag��ü ���ʿ� wall ��ü�� �����ϸ� �ȿ�����.
							return true;
						}
					}
					// ���� if���� �� �������� ������ �������� bag��ü�� ������.
					bag.move(-SPACE, 0);
					flag = true;
					this.bags = bag;
					// bag��ü�� ������ �� ������ �������� �˻���.

				}

			}

			return false;

		case RIGHT_COLLISION: // ������ Ű�� ��������

			for (int i = 0; i < baggs.size(); i++) {

				Baggage bag = baggs.get(i); // ���� bag�� Baggage ��� ����Ʈ���� ��ü�� �ϳ��� �˻��� �˻縦 �ϴٰ�

				if (soko.isRightCollision(bag)) { // soko�� �����ʿ� bag��ü�� �����ϸ�

					for (int j = 0; j < baggs.size(); j++) {

						Baggage item = baggs.get(j); // Baggage ��� ����Ʈ���� ��ü�� �ϳ��� �˻��Ͽ� item�� ����

						if (!bag.equals(item)) { // item(�� �ٸ� Baggage)��ü�� bag��ü�� ���� ������

							if (bag.isRightCollision(item)) { // bag ��ü �����ʿ� item ��ü�� �����ϸ� �ȿ�����
								return true;
							}
						}

						if (checkWallCollision(bag, RIGHT_COLLISION)) { // bag��ü �����ʿ� wall ��ü�� �����ϸ� �ȿ�����
							return true;
						}
					}
					// ���� if���� �� �������� ������ �������� bag ��ü�� ������.
					bag.move(SPACE, 0);
					flag = true;
					this.bags = bag;
					// bag��ü�� ������ �� ������ �������� �˻���.
				}

			}

			return false;

		case TOP_COLLISION:

			for (int i = 0; i < baggs.size(); i++) {

				Baggage bag = baggs.get(i);

				if (soko.isTopCollision(bag)) {

					for (int j = 0; j < baggs.size(); j++) {

						Baggage item = baggs.get(j);

						if (!bag.equals(item)) {

							if (bag.isTopCollision(item)) {
								return true;
							}
						}

						if (checkWallCollision(bag, TOP_COLLISION)) {
							return true;
						}
					}

					bag.move(0, -SPACE);
					flag = true;
					this.bags = bag;

				}

			}

			return false;

		case BOTTOM_COLLISION:

			for (int i = 0; i < baggs.size(); i++) {

				Baggage bag = baggs.get(i);

				if (soko.isBottomCollision(bag)) {

					for (int j = 0; j < baggs.size(); j++) {

						Baggage item = baggs.get(j);

						if (!bag.equals(item)) {

							if (bag.isBottomCollision(item)) {
								return true;
							}
						}

						if (checkWallCollision(bag, BOTTOM_COLLISION)) {

							return true;
						}
					}

					bag.move(0, SPACE);
					flag = true;
					this.bags = bag;

				}

			}

			break;

		default:
			break;
		}

		return false;
	}

	private boolean isFailedDetected(Baggage bag) { // failed ���� detected �޼ҵ�

		if (bag != null) {

			if (checkWallCollision(bag, TOP_COLLISION) || checkWallCollision(bag, BOTTOM_COLLISION)) { // ��, �Ʒ� �� �ΰ� // �����丵 �Ϸ�

				for (int i = 0; i < walls.size(); i++) {
					Wall item1 = walls.get(i); //item1 = ���� �ִ� ��
					if (bag.isTopCollision(item1) || bag.isBottomCollision(item1)) {

						for (int j = 0; j < walls.size(); j++) {
							Wall item2 = walls.get(j);
							if (!item2.equals(item1) && item1.isLeftCollision(item2) || item1.isRightCollision(item2)) { // item2�� ���� �� ���� ��

								for (int k = 0; k < baggs.size(); k++) {
									Baggage item3 = baggs.get(k);
									if (!item3.equals(bag) && item2.isBottomCollision(item3)
											|| item2.isTopCollision(item3)) { // item3�� item2 �Ʒ��� bag

										return true;
									}
								}
							}
						}
					}
				}
				if (checkWallCollision(bag, LEFT_COLLISION) || checkWallCollision(bag, RIGHT_COLLISION)) {

					return true;
				}
			}

			if (checkWallCollision(bag, LEFT_COLLISION) || checkWallCollision(bag, RIGHT_COLLISION)) { // ��, ������ �� 2�� �����丵 ��
				for (int i = 0; i < walls.size(); i++) {

					Wall item1 = walls.get(i);

					if (bag.isLeftCollision(item1) || bag.isRightCollision(item1)) {

						for (int j = 0; j < walls.size(); j++) {

							Wall item2 = walls.get(j);

							if (!item2.equals(item1) && item1.isTopCollision(item2) || item1.isBottomCollision(item2)) {
								for (int k = 0; k < baggs.size(); k++) {
									Baggage item3 = baggs.get(k);
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

			if (checkWallCollision(bag, TOP_COLLISION) || checkWallCollision(bag, BOTTOM_COLLISION)) { // ���� �� 1�� , �Ʒ� �� 1�� �����Ѱ��� �����丵
				for (int i = 0; i < baggs.size(); i++) {
					Baggage item1 = baggs.get(i);
					if (!item1.equals(bag) && bag.isLeftCollision(item1) || bag.isRightCollision(item1)) { // item1 = bag �翷�� bag ��ü

						for (int j = 0; j < walls.size(); j++) {
							Wall item2 = walls.get(j);

							if (bag.isBottomCollision(item2) || bag.isTopCollision(item2)) { // item2�� bag��ü ���� �Ʒ��� ��
								for (int k = 0; k < baggs.size(); k++) {
									Baggage item3 = baggs.get(k);

									if (!item3.equals(item1) && !item3.equals(bag)) { // item3�� item2(��) �� ���� ��ü
										if (item2.isLeftCollision(item3) || item2.isRightCollision(item3))
											return true;

										if (bag.isTopCollision(item2) && item1.isBottomCollision(item3)
												|| bag.isBottomCollision(item2) && item1.isTopCollision(item3)) {
											for (int h = 0; h < walls.size(); h++) {
												Wall item4 = walls.get(h);
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

			if (checkWallCollision(bag, LEFT_COLLISION) || checkWallCollision(bag, RIGHT_COLLISION)) { // ���� �� �ϳ� ������ �� �ϳ� �����Ѱ��� �����丵
				for (int i = 0; i < baggs.size(); i++) {
					Baggage item1 = baggs.get(i);
					if (!item1.equals(bag) && bag.isTopCollision(item1) || bag.isBottomCollision(item1)) { //item1�� bag ���� ��

						for (int j = 0; j < walls.size(); j++) {
							Wall item2 = walls.get(j);

							if (bag.isRightCollision(item2) || bag.isBottomCollision(item2)) { //item2�� 
								for (int k = 0; k < baggs.size(); k++) {
									Baggage item3 = baggs.get(k);

									if (!item3.equals(item1) && !item3.equals(bag)) {
										if (item2.isTopCollision(item3) || item2.isRightCollision(item3))
											return true;

										if (bag.isRightCollision(item2) && item1.isLeftCollision(item3)
												|| bag.isLeftCollision(item2) && item1.isRightCollision(item3)) {
											for (int h = 0; h < walls.size(); h++) {
												Wall item4 = walls.get(h);
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

			for (int i = 0; i < baggs.size(); i++) { // 4���� bag�϶� �����Ѱ���
				Baggage item1 = baggs.get(i);
				if (!item1.equals(bag) && bag.isTopCollision(item1) || bag.isBottomCollision(item1)) {

					for (int j = 0; j < baggs.size(); j++) {

						Baggage item2 = baggs.get(j);

						if (!item2.equals(item1) && !item2.equals(bag)) {

							if (item1.isLeftCollision(item2) || item1.isRightCollision(item2)) {

								for (int k = 0; k < baggs.size(); k++) {

									Baggage item3 = baggs.get(k);

									if (item2.isBottomCollision(item3)) {

										return true;
									}

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

	public void isCompleted() { // �� ���������� �־������ isCompleted=true

		int nOfBags = baggs.size(); // Bag ��ü�� ����
		int finishedBags = 0; // Bag��ü�� ���ڿ� finishedBags�� isCompleted=ture == ���� ����

		for (int i = 0; i < nOfBags; i++) {

			Baggage bag = baggs.get(i);

			for (int j = 0; j < nOfBags; j++) {

				Area area = areas.get(j); // ������ ����

				if (bag.x() == area.x() && bag.y() == area.y()) { // bag x,y�� area x,y�� ������ finishedBags +1����

					finishedBags += 1;
				}
			}
		}

		if (finishedBags == nOfBags) { // finishedBag�� nOfbags�� ������ ��� ���������� �־��ٴ� ��
			String s = "Completed";

			FileIO fileio = new FileIO();
			int size = replay_Deque.size();

			for (int i = 0; i < size; i++) {
				fileio.enqueue(replay_Deque.poll());
			}

			fileio.FileInput(levelSelected, s);

			isCompleted = true; // ���� ����
			repaint(); // ������Ʈ�� ��� ������� �ٲ������ ���
		}
	}

	public void isFailed() {
		isFailed = true;
		if (isFailed) {
			String s = "Failed";

			FileIO fileio = new FileIO();
			int size = replay_Deque.size();

			for (int i = 0; i < size; i++) {
				fileio.enqueue(replay_Deque.poll());
			}

			fileio.FileInput(levelSelected, s);
		}
		repaint();

	}

	public void isEntered(Baggage bag) {
		for (int i = 0; i < areas.size(); i++) {
			Area area = areas.get(i);
			if (bag.x() == area.x() && bag.y() == area.y()) {
				bag.setIsEntered();
			}
		}
	}

	private void restartLevel() {

		areas.clear();
		baggs.clear();
		walls.clear();

		initWorld();

		if (isCompleted) {
			isCompleted = false;
		}

		if (isFailed) {
			isFailed = false;
		}

	}

}

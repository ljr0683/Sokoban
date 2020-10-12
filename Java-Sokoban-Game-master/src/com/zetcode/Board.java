package com.zetcode;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class Board extends JPanel {

	private Deque<Integer> replay_Deque = new LinkedList<>();

	private JButton backSpaceButton = new JButton("<-");
	private int undoCount = 3;
	private JLabel undoCountText = new JLabel("ExtraUndo : "+Integer.toString(undoCount));
	private UIManager frame;
	private LevelSelectPanel previousPanel;
	private Baggage bags = null;
	private boolean isReplay = false;
	private int size;
	private File file;
	private boolean isCollision = false;
	private int levelSelected;
	private boolean flag = false; // �и鼭 ������ Ȯ���ϴ� �Լ�
	private Replay replay;
	private FailedDetected failed;
	private String selectCharacter;

	private final int OFFSET = 30;
	public static int SPACE = 64;
	public static final int LEFT_COLLISION = 1;
	public static final int RIGHT_COLLISION = 2;
	public static final int TOP_COLLISION = 3;
	public static final int BOTTOM_COLLISION = 4;

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
		  + "    ##$  #\n"
		  + "  ####  $##\n"
		  + "  ##  $ $ #\n"
		  + "#### # ## #   ######\n"
		  + "##   # ## #####  ..#\n"
		  + "## $  $          ..#\n"
		  + "###### ### #@##  ..#\n"
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

	public Board(int levelSelected, LevelSelectPanel previousPanel, UIManager frame, String selectCharacter) {

		setLayout(null);

		this.previousPanel = previousPanel;
		this.levelSelected = levelSelected;
		this.frame = frame;
		this.selectCharacter = selectCharacter;

		add(backSpaceButton);
		add(undoCountText);
		backSpaceButton.addActionListener(new MyActionListener()); 
		backSpaceButton.setBounds(25, 20, 45, 20);
		failed = new FailedDetected(this);
		
		initBoard();
	}

	public Board(int levelSelected, LevelSelectPanel previousPanel, UIManager frame, File file, Replay replay, String selectCharacter) {

		setLayout(null);

		this.previousPanel = previousPanel;
		this.levelSelected = levelSelected;
		this.frame = frame;
		this.file = file;
		this.selectCharacter = selectCharacter;

		add(backSpaceButton);
		backSpaceButton.addActionListener(new MyActionListener()); 
		backSpaceButton.setBounds(25, 20, 45, 20);

		try {
			FileReader fr = new FileReader(file);
			int c;
			while ((c = fr.read()) != -1) {
				replay_Deque.offer(c - 48);
			}
			fr.close();
		} catch (IOException e) {
			System.out.println("����");
		}
		
		this.replay = replay;
		failed = new FailedDetected(this);
		
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
				soko = new Player(x, y, selectCharacter); // �÷��̾���
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

		g.setColor(new Color(100, 240, 170));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		ArrayList<Actor> world = new ArrayList<>();

		world.addAll(walls);
		world.addAll(areas);
		world.addAll(baggs);
		world.add(soko);

		for (int i = 0; i < world.size(); i++) {

			Actor item = world.get(i);

			if (item instanceof Area ) {

				g.drawImage(item.getImage(), item.x() + 16, item.y() + 16, this);
			}
			else if(item instanceof Player) {
				if(selectCharacter.equals("Mario")) {
					g.drawImage(item.getImage(), item.x(), item.y() , this);
					
				}else {
					g.drawImage(item.getImage(), item.x() + 13, item.y() , this);
					
				}
			}
			else {

				g.drawImage(item.getImage(), item.x(), item.y(), this);
			}
			
			if(!isReplay) {
				
				g.setColor(new Color(0, 0, 0));
				g.drawString("ExtraUndo : " + Integer.toString(undoCount), w-70, 18);
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

	private class TAdapter extends KeyAdapter { // Ű���� Ŭ������ �Ѱ���

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
					soko.changePlayerVector(LEFT_COLLISION);
					
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

					if (failed.isFailedDetected(bags)) {
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
					soko.changePlayerVector(RIGHT_COLLISION);

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

					if (failed.isFailedDetected(bags)) {
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
					soko.changePlayerVector(TOP_COLLISION);

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

					if (failed.isFailedDetected(bags)) {
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
					soko.changePlayerVector(BOTTOM_COLLISION);

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

					if (failed.isFailedDetected(bags)) {
						isFailed();
					}

					break;

				case KeyEvent.VK_R:

					restartLevel();

					break;
					
				case KeyEvent.VK_BACK_SPACE :
					if(!replay_Deque.isEmpty() ) {
						if(undoCount>0) {
							undo();
							undoCount--;
						}
						
					}
					
					break;

				default:
					break;
				}

				isCompleted();
				repaint();
			} else {

				int key1 = e.getKeyCode();

				switch (key1) { // ���� ���� �Ǻ��ؾߵʤ�!@#!@$!@$%#!#$%

				case KeyEvent.VK_LEFT:
					replay.goBack();
					break;

				case KeyEvent.VK_RIGHT:
					replay.goAhead();
					break;

				default:
					break;
				}
				isCompleted();
				repaint();
			}
		}
	}

	private boolean checkWallCollision(Actor actor, int type) { //actor = bag

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
	
	private void undo() {
		if(!replay_Deque.isEmpty()) {
			int key = replay_Deque.pollLast();
			replay = new Replay(this);
			replay.offerReplay_Deque(key);
			if(key == 5 || key == 6) {
				key = replay_Deque.pollLast();
				replay.offerReplay_Deque(key);
			}
			replay.goBack();
		}
	}
	
	public boolean getCheckWallCollision(Actor actor, int type) {
		if(checkWallCollision(actor, type))
			return true;
		return false;
	}
	
	public boolean getCheckBagCollision(int type) {
		if(checkBagCollision(type))
			return true;
		return false;
	}
	
	public boolean getIsCollision() {
		return isCollision;
	}
	
	public void setIsCollision(boolean TorF) {
		isCollision = TorF;
	}

	public boolean getFlag() {
		return flag;
	}
	
	public Baggage getBaggs(int i) {
		return baggs.get(i);
	}
	
	public Wall getWalls(int i) {
		return walls.get(i);
	}
	
	public Baggage getBags() {
		return bags;
	}
	
	public void setBags(Baggage collisionBag) {
		if(collisionBag!=null) {
			bags = collisionBag;
		}
	}
	
	public Player getSoko() {
		return soko;
	}
	
	public void callIsEntered(Baggage bags) {
		isEntered(bags);
	}
	
	public void callIsFailed() {
		isFailed();
	}
	
	public boolean callIsFailedDetected(Baggage bags) {
		return failed.isFailedDetected(bags);
	}
	
	public int getBaggsSize() {
		return baggs.size();
	}

	public int getWallsSize() {

		return walls.size();
	}
	
	class MyActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();

			if (b.equals(backSpaceButton)) {
				frame.changePanel(previousPanel);
			}
		}
	}
}

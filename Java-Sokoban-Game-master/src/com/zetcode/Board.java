package com.zetcode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel {

	private Deque<Integer> replay_Deque = new LinkedList<>();

	private int undoCount = 3;
	private int moveCount;
	private int limitturn = 1;
	private int timerCount;
	private UIManager frame;
	private LevelSelectPanel previousPanel;
	private LevelPanel levelpanel;
	private Baggage bags = null;
	private File file;
	private Replay replay;
	private FailedDetected failed;
	private MyTimer time;
	private Timer timer;
	
	private ImageIcon backSpaceIcon = new ImageIcon("src/resources/BackSpace/BackSpace.png");
	private JLabel backSpaceLabel = new JLabel(backSpaceIcon);
	private ImageIcon[] boomImage = new ImageIcon[3];
	private JLabel[] boomLabel = new JLabel[3];
	
	private Score score;
	
	private int size;
	private boolean isCollision = false;
	private int levelSelected;
	private int mode;
	private boolean flag = false; // 밀면서 갔는지 확인하는 함수
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
	private boolean isReplay = false;
	
	private String level[] ={
			"    ######\n"
		  + "    ##   #\n"
		  + "    ##$  #\n"
		  + "  ####  $##\n"
		  + "  ##  $ $ #\n"
		  + "#### ! !! #   ######\n"
		  + "##   ! !! #####  ..#\n"
		  + "## $  $          ..#\n"
		  + "###### !!! #@##  ..#\n"
		  + "    ##     #########\n"
		  + "    ########\n",
          
      		"    ######\n"
          + "############\n"
          + "#..  #     ###\n"
          + "#..  # $  $  #\n"
          + "#..  #$!!!!  #\n"
          + "#..    @ !!  #\n"
          + "#..  # !  $ ##\n"
          + "###### !!$ $ #\n"
          + "  # $  $ $ $ #\n"
          + "  #    #     #\n"
          + "  ############\n",
          
      	    "        ######## \n"
          + "        #     @# \n"
          + "        # $!$ ## \n"
          + "        # $  $# \n"
          + "        ##$ $ # \n"
          + "######### $ ! ###\n"
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
          + "#   ! #########       \n"
          + "#    $  ##            \n"
          + "# $$#$$ @#            \n"
          + "#   #   ##            \n"
          + "#########             \n",
          
            "        #####    \n"
          + "        #   #####\n"
          + "        # !$##  #\n"
          + "        #     $ #\n"
          + "######### !!!   #\n"
          + "#....  ## $  $###\n"
          + "#....    $ $$ ## \n"
          + "#....  ##$  $ @# \n"
          + "#########  $  ## \n"
          + "        # $ $  # \n"
          + "        ### !! # \n"
          + "          #    # \n"
          + "          ###### \n"
 
          };
	
	public Board(int levelSelected, LevelSelectPanel previousPanel, UIManager frame, String selectCharacter, int mode) {

		setLayout(null);

		this.previousPanel = previousPanel;
		this.levelSelected = levelSelected;
		this.frame = frame;
		this.selectCharacter = selectCharacter;
		this.moveCount = 0;
		this.timerCount = 0;
		
	
		
		add(backSpaceLabel);
		
		for(int i = 0; i < boomImage.length; i++) {
			boomImage[i] = new ImageIcon("src/resources/Boom/boom"+i+".png");
			boomLabel[i] = new JLabel(boomImage[i]);
			boomLabel[i].setVisible(true);
			add(boomLabel[i]);
		}
		
		backSpaceLabel.addMouseListener(new MyMouseListener()); 
		backSpaceLabel.setBounds(25, 20, 128, 128);
		failed = new FailedDetected(this);
		time = new MyTimer();
		
		this.mode = mode;
		
		limitturn = 500;
	
		initBoard();
	}

	public Board(int levelSelected, LevelSelectPanel previousPanel, UIManager frame, File file, Replay replay, String selectCharacter) {

		setLayout(null);
		
		this.previousPanel = previousPanel;
		this.levelSelected = levelSelected;
		this.frame = frame;
		this.file = file;
		this.selectCharacter = selectCharacter;

		add(backSpaceLabel);
		backSpaceLabel.addMouseListener(new MyMouseListener()); 
		backSpaceLabel.setBounds(25, 20, 128, 128);

		try {
			FileReader fr = new FileReader(file);
			int c;
			while ((c = fr.read()) != -1) {
				replay_Deque.offer(c - 48);
			}
			fr.close();
		} catch (IOException e) {
			System.out.println("오류");
		}
		
		this.replay = replay;
		failed = new FailedDetected(this);
		time = new MyTimer();
		
		isReplay = true;
		
		initBoard();
		
	}

	private void initBoard() {
		
		timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
			
		});
		timer.start();
		
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

	private void initWorld() { // 초기화

		walls = new ArrayList<>();
		baggs = new ArrayList<>();
		areas = new ArrayList<>();

		int x = OFFSET;
		int y = OFFSET;

		Wall wall; // 벽
		Baggage b; // 미는거
		Area a; // 끝나는거
		Llm llm;// 가운데 안보이는 벽
		
		for (int i = 0; i < level[levelSelected].length(); i++) {

			char item = level[levelSelected].charAt(i);

			switch (item) {

			case '\n':
				y += SPACE;

				if (this.w < x) { // width의 크기를 x가 가장 큰걸로 함
					this.w = x;
				}

				x = OFFSET; // 한번 줄바꿈이 되면 x=OFF으로 다시 초기화
				break;

			case '#':
				wall = new Wall(x, y, mode); // x,y 지점에 Wall 객체 생성
				walls.add(wall); // Wall 객체를 Wall 어레이 리스트에 넣음
				x += SPACE; // x에 한칸(SPACE)를 더함
				break;
				
			case '!':
				llm = new Llm(x, y);
				walls.add(llm);
				x += SPACE;
				break;

			case '$':
				b = new Baggage(x, y);
				baggs.add(b); // b == Baggage 객체
				x += SPACE;
				break;

			case '.':
				a = new Area(x, y); // 끝나는 지점
				areas.add(a); // a == Area 객체
				x += SPACE;
				break;

			case '@':
				soko = new Player(x, y, selectCharacter); // 플레이어임
				x += SPACE;
				break;

			case ' ':
				x += SPACE;
				break;

			default:
				break;
			}

			h = y; // 높이를 정함.
		}
		
	}

	private void buildWorld(Graphics g) {
		
		if (mode == 1) {
			g.setColor(new Color(0, 0, 0));
		} else {
			g.setColor(new Color(250, 240, 170));
		}
		
		
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
				if(selectCharacter.equals("Mario")) { // 마리오 일때
					g.drawImage(item.getImage(), item.x(), item.y() , this);
					
				}else { // 노란모자 일때
					g.drawImage(item.getImage(), item.x() + 13, item.y() , this);
				}
			}
			else {
				Actor.setMode(mode); // 없어지는 모드인지 그냥인지 판별하기 위해
				g.drawImage(item.getImage(), item.x(), item.y(), this);
			}
			
			if(!isReplay) {
				
				g.setColor(new Color(0, 0, 0));
				g.drawString("ExtraUndo : " + Integer.toString(undoCount), w-90, 18);
				if(mode==3) {
				g.drawString("MoveLimited : " + limitturn , w-90, 80);
				}
			}

			
		}
		
		if (isFailed) {
			if(!isReplay) {
				time.setIsFinished(true);
				if(time.notMoveTime==6) {
					boomLabel[2].setBounds(w/2+200, 50, 64, 64);
					boomLabel[2].setVisible(true);
				}
			}
		}
	    
	    if(time.notMoveTime==6) {
	    	isFailed();
	    }
		
		if(mode==3) {
			if(limitturn <= moveCount) {
				System.out.println(limitturn);
				System.out.println(moveCount);
				isFailed();
	    	}
        }
		
		if(!isReplay) {
			if(time.notMoveTime==4) {
				boomLabel[0].setBounds(w/2-200, 50, 64, 64);
				boomLabel[0].setVisible(true);
			}
			if(time.notMoveTime==5) {
				boomLabel[1].setBounds(w/2, 50, 64, 64);
				boomLabel[1].setVisible(true);
			}
		}
		
		if(time.time >= 1) {
			
			g.setColor(Color.BLUE);	
			String nowTime = Integer.toString(time.time);
			g.drawString("Time : "+nowTime,  w-90, 40);
		}
		
		if(!(mode==4)) {
		g.setColor(Color.BLUE);
		String nowTurn = Integer.toString(moveCount);
		g.drawString("MoveCount : "+nowTurn,  w-90, 60);
		}
	}
	
	
	
	@Override
	public void paintComponent(Graphics g) { // 컴포넌트를 그림
		super.paintComponent(g);

		buildWorld(g);
	}

	private class TAdapter extends KeyAdapter { // 키값을 클래스에 넘겨줌
		
		@Override
		public void keyPressed(KeyEvent e) {
			if(!isReplay) {
				for(int i=0; i<3; i++) {
					boomLabel[i].setVisible(false);
				}
			}
			if (isCompleted) { // 게임이 끝남.
				
				return;
			}

			if (isFailed) {
				timer.stop();
				return;
			}

			if (!isReplay) {
				int key = e.getKeyCode();
				flag=false;

				switch (key) {

				case KeyEvent.VK_LEFT:
					
					moveCount++;
					
					if (checkWallCollision(soko, LEFT_COLLISION)) { // soko객체 왼쪽에 벽이 있다면 움직이지 않고 키 이벤트를 끝냄
						return;
					}

					if (checkBagCollision(LEFT_COLLISION)) {
						// 왼쪽으로 움직였을때, soko객체 왼쪽에 bag객체가 존재하고 또 왼쪽에 또다른 Bag 객체가 존재하거나,
						// soko 객체 왼쪽 Bag 객체의 왼쪽에 Wall 객체가 존재하면 움직이지 않고 이벤트 종료
						return;
					}

					soko.move(-SPACE, 0); // 만약 위 상황을 만족하지 않는다면 왼쪽으로 한칸 움직임.
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

					moveCount++;
					
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

					moveCount++;
					
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

					moveCount++;

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
					time.time = 0;
					moveCount = 0;
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
				Iterator<Wall> iter = walls.iterator();// 이 부분부터
				
				if (mode == 2) {

					if (moveCount > 0)

						for (int i = 0; i < walls.size(); i++) {
							Wall next = iter.next();

							if (next instanceof Llm && moveCount >= 1) {
								((Llm) next).rellm();
							}
						}
				} // 이부분까지 설정

				isCompleted();
				repaint();
			} else {

				int key1 = e.getKeyCode();

				switch (key1) { // 꼬임 내일 판별해야됨ㄴ!@#!@$!@$%#!#$%

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

		switch (type) { // type == 왼쪽 오른쪽 위 아래인지 숫자 1,2,3,4

		case LEFT_COLLISION: // 왼쪽 키가 눌렸을때

			for (int i = 0; i < walls.size(); i++) {

				Wall wall = walls.get(i); // walls 어레이 리스트에서 하나씩 wall에 Wall객체저장

				if (actor.isLeftCollision(wall)) { // actor 왼쪽에 벽이 있다면 true 리턴

					return true;
				}
			}

			return false; // actor 왼쪽에 벽이 없다면 false 리턴

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

	private boolean checkBagCollision(int type) { // Bag 객체의 충돌

		switch (type) {

		case LEFT_COLLISION: // 왼쪽 키가 눌렸을때,

			for (int i = 0; i < baggs.size(); i++) {

				Baggage bag = baggs.get(i);

				if (soko.isLeftCollision(bag)) { // soko 왼쪽에 bag이 존재하면

					for (int j = 0; j < baggs.size(); j++) {

						Baggage item = baggs.get(j); // bag 어레이 리스트를 하나씩 검사하여 item에 넣음

						if (!bag.equals(item)) { // item과 bag이 다른 객체라면

							if (bag.isLeftCollision(item)) {// bag 왼쪽에 item(또 다른 Baggage)이 존재하면 안움직임
								return true;
							}
						}

						if (checkWallCollision(bag, LEFT_COLLISION)) {// bag객체 왼쪽에 wall 객체가 존재하면 안움직임.
							return true;
						}
					}
					// 위의 if문을 다 만족하지 않으면 그제서야 bag객체가 움직임.
					bag.move(-SPACE, 0);
					flag = true;
					this.bags = bag;
					// bag객체가 움직인 후 게임이 끝났는지 검사함.

				}

			}

			return false;

		case RIGHT_COLLISION: // 오른쪽 키가 눌렸을때

			for (int i = 0; i < baggs.size(); i++) {

				Baggage bag = baggs.get(i); // 변수 bag에 Baggage 어레이 리스트에서 객체를 하나씩 검사함 검사를 하다가

				if (soko.isRightCollision(bag)) { // soko의 오른쪽에 bag객체가 존재하면

					for (int j = 0; j < baggs.size(); j++) {

						Baggage item = baggs.get(j); // Baggage 어레이 리스트에서 객체를 하나씩 검사하여 item에 넣음

						if (!bag.equals(item)) { // item(또 다른 Baggage)객체와 bag객체가 같지 않을때

							if (bag.isRightCollision(item)) { // bag 객체 오른쪽에 item 객체가 존재하면 안움직임
								return true;
							}
						}

						if (checkWallCollision(bag, RIGHT_COLLISION)) { // bag객체 오른쪽에 wall 객체가 존재하면 안움직임
							return true;
						}
					}
					// 위의 if문을 다 만족하지 않으면 그제서야 bag 객체가 움직임.
					bag.move(SPACE, 0);
					flag = true;
					this.bags = bag;
					// bag객체가 움직인 후 게임이 끝났는지 검사함.
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

	public void isCompleted() { // 다 최종지점에 넣었을경우 isCompleted=true
		
		int nOfBags = baggs.size(); // Bag 객체의 숫자
		int finishedBags = 0; // Bag객체의 숫자와 finishedBags가 isCompleted=ture == 게임 종료

		for (int i = 0; i < nOfBags; i++) {

			Baggage bag = baggs.get(i);

			for (int j = 0; j < nOfBags; j++) {

				Area area = areas.get(j); // 끝나는 지점

				if (bag.x() == area.x() && bag.y() == area.y()) { // bag x,y와 area x,y가 같으면 finishedBags +1증가

					finishedBags += 1;
				}
			}
		}

		if (finishedBags == nOfBags) { // finishedBag과 nOfbags가 같으면 모두 최종지점에 넣었다는 뜻
			moveCount = 0;
			timer.stop();
			String s = "Completed";

			FileIO replayFileIo = new FileIO();
			int size = replay_Deque.size();
			
			for (int i = 0; i < size; i++) {
				replayFileIo.enqueue(replay_Deque.poll());
			}
			
			replayFileIo.replayFileInput(levelSelected, s);
			
			 
			if(!isReplay) { // replay가 아닐떄만 스코어 계산
				this.timerCount = time.getTime();
				time.setIsFinished(true);
				File scoreFileFolder = new File("src/score");
				if(!scoreFileFolder.exists())
					scoreFileFolder.mkdir();
				File scoreFile = new File("src/score/score_"+levelSelected+".txt");
				score = new Score(levelSelected, moveCount, timerCount, scoreFile);
			}
			
			isCompleted = true; // 따라서 끝남
			
			ImageIcon completeImage = new ImageIcon("src/resources/Complete & Failed/Complete.png");
			JLabel completeLabel = new JLabel(completeImage);
			
			add(completeLabel);
			completeLabel.setBounds(0, 0, w, h);
			
			repaint(); // 컴포넌트의 모양 색상등이 바뀌었을때 사용
		}
	}

	public void isFailed() {
		
		isFailed = true;
		
		if (isFailed) {
			moveCount= 0;
			String s = "Failed";

			FileIO fileio = new FileIO();
			int size = replay_Deque.size();

			for (int i = 0; i < size; i++) {
				fileio.enqueue(replay_Deque.poll());
			}

			fileio.replayFileInput(levelSelected, s);
			
			ImageIcon failedImage = new ImageIcon("src/resources/Complete & Failed/Failed.png");
			JLabel failedLabel = new JLabel(failedImage);
			
			add(failedLabel);
			failedLabel.setBounds(0, 0, w, h);
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
	
	class MyMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			JLabel la = (JLabel)e.getSource();
			if(la.equals(backSpaceLabel)) {
				time.setIsFinished(true);
				frame.changePanel(previousPanel);
				isFailed = false;
				time.notMoveTime = 0;
				moveCount=0;
				
			}
		}
	}
}

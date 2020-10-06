package com.zetcode;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class Board extends JPanel {

    private final int OFFSET = 30;
    private final int SPACE = 20;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;
    private int levelSelected;
    
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
            + "#..  #     ###\n"
            + "#..  # $  $  #\n"
            + "#..  #$####  #\n"
            + "#..    @ ##  #\n"
            + "#..  # #  $ ##\n"
            + "###### ##$ $ #\n"
            + "  # $  $ $ $ #\n"
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
            + "#       $   $@  #\n"
            + "#      #$       #\n"
            + "#               #\n"
            + "#               #\n"
            + "#               #\n"
            + "#################\n"
   
            };
    
    
	private JButton backSpaceButton = new JButton("<-");
	private GameStart frame;
	private LevelPanel previousPanel;
	private Baggage bags=null;

	public Board(int levelSelected, LevelPanel previousPanel, GameStart frame) {

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
		}); // 뒤로가기 버튼에 액션리스너 등록
		backSpaceButton.setBounds(25, 20, 45, 20);
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

	private void initWorld() { // 초기화

		walls = new ArrayList<>();
		baggs = new ArrayList<>();
		areas = new ArrayList<>();

		int x = OFFSET;
		int y = OFFSET;

		Wall wall; // 벽
		Baggage b; // 미는거
		Area a; // 끝나는거

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
				wall = new Wall(x, y); // x,y 지점에 Wall 객체 생성
				walls.add(wall); // Wall 객체를 Wall 어레이 리스트에 넣음
				x += SPACE; // x에 한칸(SPACE)를 더함
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
				soko = new Player(x, y); // 플레이어임
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

			if (isCompleted) { // isCompleted가 true면 화면에 completed를 띄움

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
	public void paintComponent(Graphics g) { // 컴포넌트를 그림
		super.paintComponent(g);

		buildWorld(g);
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			if (isCompleted) { // 게임이 끝남.
				return;
			}

			if (isFailed) {
				return;
			}

			int key = e.getKeyCode();

			switch (key) {

			case KeyEvent.VK_LEFT:

				if (checkWallCollision(soko, LEFT_COLLISION)) { // soko객체 왼쪽에 벽이 있다면 움직이지 않고 키 이벤트를 끝냄
					return;
				}

				if (checkBagCollision(LEFT_COLLISION)) {
					// 왼쪽으로 움직였을때, soko객체 왼쪽에 bag객체가 존재하고 또 왼쪽에 또다른 Bag 객체가 존재하거나,
					// soko 객체 왼쪽 Bag 객체의 왼쪽에 Wall 객체가 존재하면 움직이지 않고 이벤트 종료
					return;
				}

				soko.move(-SPACE, 0); // 만약 위 상황을 만족하지 않는다면 왼쪽으로 한칸 움직임.
				
				if(bags!=null) {
					isEntered(bags);
					if(bags.getIsEntered()) {
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
				
				if(bags!=null) {
					isEntered(bags);
					if(bags.getIsEntered()) {
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
				
				if(bags!=null) {
					isEntered(bags);
					if(bags.getIsEntered()) {
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
				
				if(bags!=null) {
					isEntered(bags);
					if(bags.getIsEntered()) {
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

			repaint();
		}
	}

	private boolean checkWallCollision(Actor actor, int type) {

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
					this.bags=bag;
					isCompleted(); // bag객체가 움직인 후 게임이 끝났는지 검사함.

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
					this.bags=bag;
					isCompleted(); // bag객체가 움직인 후 게임이 끝났는지 검사함.
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
					this.bags=bag;
					isCompleted();
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
					this.bags=bag;
					isCompleted();
				}
			}

			break;

		default:
			break;
		}

		return false;
	}

	private boolean isFailedDetected(Baggage bag) { 
		
		if(bag!=null) {
			if(checkWallCollision(bag, TOP_COLLISION)) { // 위에 벽 두개 아래 bag 2개
				System.out.println(1);
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isTopCollision(item1)) {
						System.out.println(2);
						for(int j=0; j<walls.size(); j++) {
							Wall item2 = walls.get(j);
							if(!item2.equals(item1) && item1.isLeftCollision(item2) || item1.isRightCollision(item2)) {
								System.out.println(3);
								for(int k=0; k<baggs.size(); k++) {
									Baggage item3=baggs.get(k);
									if(!item3.equals(bag) && item2.isBottomCollision(item3)) {
										System.out.println(4);
										return true;
									}
								}
							}
						}
					}
				}
				if(checkWallCollision(bag, LEFT_COLLISION) || checkWallCollision(bag, RIGHT_COLLISION)) {
					
					return true;
				}
			}
			
			if(checkWallCollision(bag, LEFT_COLLISION)) { // 왼쪽 벽 두개 아래 bag 2개
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isLeftCollision(item1)) {
						for(int j=0; j<walls.size(); j++) {
							Wall item2 = walls.get(j);
							if(!item2.equals(item1) && item1.isTopCollision(item2) || item1.isBottomCollision(item2)) {
								for(int k=0; k<baggs.size(); k++) {
									Baggage item3=baggs.get(k);
									if(!item3.equals(bag) && item2.isRightCollision(item3)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
			
			if(checkWallCollision(bag, BOTTOM_COLLISION)) { // 아래에 벽 두개 위에 bag 2개
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isBottomCollision(item1)) {
						for(int j=0; j<walls.size(); j++) {
							Wall item2 = walls.get(j);
							if(!item2.equals(item1) && item1.isLeftCollision(item2) || item1.isRightCollision(item2)) {
								for(int k=0; k<baggs.size(); k++) {
									Baggage item3=baggs.get(k);
									if(!item3.equals(bag) && item2.isTopCollision(item3)) {
										return true;
									}
								}
							}
						}
					}
				}
				
				if(checkWallCollision(bag, LEFT_COLLISION) || checkWallCollision(bag, RIGHT_COLLISION)) {
					
					return true;
				}
			}
			
			if(checkWallCollision(bag, RIGHT_COLLISION)) {// 오른쪽 벽 2개 왼쪽 bag 2개
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isRightCollision(item1)) {
						for(int j=0; j<walls.size(); j++) {
							Wall item2 = walls.get(j);
							if(!item2.equals(item1) && item1.isTopCollision(item2) || item1.isBottomCollision(item2)) {
								for(int k=0; k<baggs.size(); k++) {
									Baggage item3=baggs.get(k);
									if(!item3.equals(bag) && item2.isLeftCollision(item3)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
			
			if(checkWallCollision(bag, TOP_COLLISION)) { // 위에 벽 1개 
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isTopCollision(item1)) {
						for(int j=0; j<baggs.size(); j++) {
							Baggage item2 = baggs.get(j);
							
							if(!item2.equals(bag) && bag.isLeftCollision(item2) || bag.isRightCollision(item2)) {
								for(int k=0; k<baggs.size(); k++) {
									Baggage item3= baggs.get(k);
									
									if(!item3.equals(item2) && !item3.equals(bag)) {
										if(item2.isTopCollision(item3))
											return true;
										
										if(item2.isBottomCollision(item3)) {
											for(int g=0; g<walls.size(); g++) {
												Wall item4 = walls.get(g);
												if(!item4.equals(item1) && item3.isLeftCollision(item4) || item3.isRightCollision(item4))
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
			
			if(checkWallCollision(bag, LEFT_COLLISION)) { // 왼쪽 벽 하나
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isLeftCollision(item1)) {
						for(int j=0; j<baggs.size(); j++) {
							Baggage item2 = baggs.get(j);
							if(!item2.equals(bag) && item1.isTopCollision(item2) || item1.isBottomCollision(item2)) {
								for(int k=0; k<baggs.size(); k++) {
									Baggage item3 = baggs.get(k);
									if(!item3.equals(item2) && !item3.equals(bag) && item2.isRightCollision(item3)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
			
			if(checkWallCollision(bag, BOTTOM_COLLISION)) {
													// 아래 벽 1개
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isBottomCollision(item1)) { // 벽이 bag 아래에 있는지 판별
						
						for(int j=0; j<baggs.size(); j++) {
							Baggage item2 = baggs.get(j);
							
							if(!item2.equals(bag) && bag.isLeftCollision(item2) || bag.isRightCollision(item2)) {
								for(int k=0; k<baggs.size(); k++) {
									Baggage item3= baggs.get(k);
									
									if(!item3.equals(item2) && !item3.equals(bag)) {
										if(item2.isBottomCollision(item3))
											return true;
										
										if(item2.isTopCollision(item3)) {
											for(int g=0; g<walls.size(); g++) {
												Wall item4 = walls.get(g);
												if(!item4.equals(item1) && item3.isLeftCollision(item4) || item3.isRightCollision(item4))
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
			
			if(checkWallCollision(bag, RIGHT_COLLISION)) { //  오른쪽 벽 하나
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isRightCollision(item1)) {
						for(int j=0; j<baggs.size(); j++) {
							Baggage item2 = baggs.get(j);
							if(!item2.equals(bag) && item1.isTopCollision(item2) || item1.isBottomCollision(item2)) {
								for(int k=0; k<baggs.size(); k++) {
									Baggage item3 = baggs.get(k);
									if(!item3.equals(item2) && !item3.equals(bag) && item2.isLeftCollision(item3)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
			
			
			for(int i=0; i<baggs.size(); i++) { // 4개다 bag일때
				Baggage item1 = baggs.get(i) ;
				if(!item1.equals(bag) && bag.isTopCollision(item1)) {
					
					for(int j=0; j<baggs.size(); j++) {
						
						Baggage item2 = baggs.get(j);
						
						if(!item2.equals(item1) && !item2.equals(bag)) {
							
							if(item1.isLeftCollision(item2) || item1.isRightCollision(item2) ) {
								
								for(int k=0; k<baggs.size(); k++) {
									
									Baggage item3 = baggs.get(k);
									
									if(item2.isBottomCollision(item3)) {
										
										return true;
									}
								}
							}
						}
					}
				}
			}
			
			
			for(int i=0; i<baggs.size(); i++) { // 4개 다 bag 일때
				Baggage item1 = baggs.get(i) ;
				if(!item1.equals(bag) && bag.isBottomCollision(item1)) {
					
					for(int j=0; j<baggs.size(); j++) {
						
						Baggage item2 = baggs.get(j);
						
						if(!item2.equals(item1) && !item2.equals(bag)) {
							
							if(item1.isLeftCollision(item2) || item1.isRightCollision(item2) ) {
								
								for(int k=0; k<baggs.size(); k++) {
									
									Baggage item3 = baggs.get(k);
									
									if(item2.isTopCollision(item3)) {
										
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

			isCompleted = true; // 따라서 끝남
			repaint(); // 컴포넌트의 모양 색상등이 바뀌었을때 사용
		}
	}

	public void isFailed() {
		isFailed = true;
		repaint();
	}
	
	public void isEntered(Baggage bag) {
		for(int i=0; i<areas.size(); i++) {
			Area area = areas.get(i);
			if(bag.x() == area.x() && bag.y() == area.y()) {
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

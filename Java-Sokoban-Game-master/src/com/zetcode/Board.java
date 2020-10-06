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
		}); // �ڷΰ��� ��ư�� �׼Ǹ����� ���
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

			int key = e.getKeyCode();

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
					this.bags=bag;
					isCompleted(); // bag��ü�� ������ �� ������ �������� �˻���.

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
					this.bags=bag;
					isCompleted(); // bag��ü�� ������ �� ������ �������� �˻���.
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
			if(checkWallCollision(bag, TOP_COLLISION)) { // ���� �� �ΰ� �Ʒ� bag 2��
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
			
			if(checkWallCollision(bag, LEFT_COLLISION)) { // ���� �� �ΰ� �Ʒ� bag 2��
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
			
			if(checkWallCollision(bag, BOTTOM_COLLISION)) { // �Ʒ��� �� �ΰ� ���� bag 2��
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
			
			if(checkWallCollision(bag, RIGHT_COLLISION)) {// ������ �� 2�� ���� bag 2��
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
			
			if(checkWallCollision(bag, TOP_COLLISION)) { // ���� �� 1�� 
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
			
			if(checkWallCollision(bag, LEFT_COLLISION)) { // ���� �� �ϳ�
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
													// �Ʒ� �� 1��
				for(int i=0; i<walls.size(); i++) {
					Wall item1 = walls.get(i);
					if(bag.isBottomCollision(item1)) { // ���� bag �Ʒ��� �ִ��� �Ǻ�
						
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
			
			if(checkWallCollision(bag, RIGHT_COLLISION)) { //  ������ �� �ϳ�
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
			
			
			for(int i=0; i<baggs.size(); i++) { // 4���� bag�϶�
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
			
			
			for(int i=0; i<baggs.size(); i++) { // 4�� �� bag �϶�
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

			isCompleted = true; // ���� ����
			repaint(); // ������Ʈ�� ��� ������� �ٲ������ ���
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

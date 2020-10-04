package com.zetcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Board extends JPanel {

    private final int OFFSET = 30; // 공백을 줘서 알아보게 쉽게함
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
          + "          ###### \n"
          };

    public Board(int levelSelected) {
    	
    	this.levelSelected=levelSelected;
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

        int x = OFFSET; //30
        int y = OFFSET; //30 위에 한칸을 띄어주기 위해 y 초기값에 30설정

        Wall wall; // 벽
        Baggage b; //미는거
        Area a; //끝나는거

        for (int i = 0; i < level[levelSelected].length(); i++) {

            char item = level[levelSelected].charAt(i);

            switch (item) {

                case '\n':
                    y += SPACE;

                    if (this.w < x) { //한번 줄 바꿈이 될 떄 마다 x의 크기를 검사, width의 크기를 x가 가장 큰걸로 함
                        this.w = x;
                    }

                    x = OFFSET; //한번 줄바꿈이 되면 x=OFFSET으로 다시 초기화 왼쪽에 한칸 띄어주기 위해서
                    break;

                case '#':
                    wall = new Wall(x, y); //x,y 지점에 Wall 객체 생성
                    walls.add(wall); // Wall 객체를 Wall 어레이 리스트에 넣음
                    x += SPACE; // x에 한칸(SPACE(20))를 더함
                    break;

                case '$':
                    b = new Baggage(x, y);
                    baggs.add(b); //b == Baggage 객체
                    x += SPACE;
                    break;

                case '.':
                    a = new Area(x, y); // x,y 지점에 Area 객체 생성
                    areas.add(a); //a == Area 객체
                    x += SPACE;
                    break;

                case '@':
                    soko = new Player(x, y); //플레이어임
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
            	
                g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this); // 16픽셀이여서 가운데 맞춰줄라고 +2 함
            } 
            else {
                
                g.drawImage(item.getImage(), item.x(), item.y(), this); // 이것도
            }

            if (isCompleted) { //isCompleted가 true면 화면에 completed를 띄움
                
                g.setColor(new Color(0, 0, 0));
                g.drawString("Completed", 25, 20);
            }

        }
    }

    @Override
    public void paintComponent(Graphics g) { //컴포넌트를 그림
        super.paintComponent(g);

        buildWorld(g);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (isCompleted) { // 게임이 끝남.
                return;
            }

            int key = e.getKeyCode();

            switch (key) {
                
                case KeyEvent.VK_LEFT:
                    
                    if (checkWallCollision(soko, LEFT_COLLISION)) { //soko객체 왼쪽에 벽이 있다면 움직이지 않고 키 이벤트를 끝냄
                        return;
                    }
                    
                    if (checkBagCollision(LEFT_COLLISION)) { 
                    // 왼쪽으로 움직였을때, soko객체 왼쪽에 bag객체가 존재하고 또 왼쪽에 또다른 Bag 객체가 존재하거나,
                    // soko 객체 왼쪽 Bag 객체의 왼쪽에 Wall 객체가 존재하면 움직이지 않고 이벤트 종료
                        return;
                    }
                    
                    soko.move(-SPACE, 0); // 만약 위 상황을 만족하지 않는다면 왼쪽으로 한칸 움직임.
                    
                    break;
                    
                case KeyEvent.VK_RIGHT:
                    
                    if (checkWallCollision(soko, RIGHT_COLLISION)) {
                        return;
                    }
                    
                    if (checkBagCollision(RIGHT_COLLISION)) {
                        return;
                    }
                    
                    soko.move(SPACE, 0);
                    
                    break;
                    
                case KeyEvent.VK_UP:
                    
                    if (checkWallCollision(soko, TOP_COLLISION)) {
                        return;
                    }
                    
                    if (checkBagCollision(TOP_COLLISION)) {
                        return;
                    }
                    
                    soko.move(0, -SPACE);
                    
                    break;
                    
                case KeyEvent.VK_DOWN:
                    
                    if (checkWallCollision(soko, BOTTOM_COLLISION)) {
                        return;
                    }
                    
                    if (checkBagCollision(BOTTOM_COLLISION)) {
                        return;
                    }
                    
                    soko.move(0, SPACE);
                    
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

        switch (type) { //type == 왼쪽 오른쪽 위 아래인지 숫자 1,2,3,4
            
            case LEFT_COLLISION: //왼쪽 키가 눌렸을때
                
                for (int i = 0; i < walls.size(); i++) {
                    
                    Wall wall = walls.get(i); //walls 어레이 리스트에서 하나씩 wall에 Wall객체저장
                    
                    if (actor.isLeftCollision(wall)) { //actor 왼쪽에 벽이 있다면  true 리턴
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
        
        return false; //false 를 리턴하므로써 reprint()가 가능하게함
    }

    private boolean checkBagCollision(int type) { //Bag 객체의 충돌

        switch (type) {
            
            case LEFT_COLLISION: //왼쪽 키가 눌렸을때,
                
                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isLeftCollision(bag)) { //soko 왼쪽에 bag이 존재하면

                        for (int j = 0; j < baggs.size(); j++) {
                            
                            Baggage item = baggs.get(j); //bag 어레이 리스트를 하나씩 검사하여 item에 넣음
                            
                            if (!bag.equals(item)) { //item과 bag이 다른 객체라면
                                
                                if (bag.isLeftCollision(item)) {// bag 왼쪽에 item(또 다른 Baggage)이 존재하면 안움직임
                                    return true;
                                }
                            }
                            
                            if (checkWallCollision(bag, LEFT_COLLISION)) {// bag객체 왼쪽에 wall 객체가 존재하면 안움직임.
                                return true;
                            }
                        }
                        //위의 if문을 다 만족하지 않으면 그제서야 bag객체가 움직임.
                        bag.move(-SPACE, 0);
                        isCompleted(); //bag객체가 움직인 후 게임이 끝났는지 검사함.
                    }
                }
                
                return false; //false를 리턴하여 reprint()를 하게함
                
            case RIGHT_COLLISION: // 오른쪽 키가 눌렸을때
                
                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i); // 변수 bag에 Baggage 어레이 리스트에서 객체를 하나씩 검사함 검사를 하다가
                    
                    if (soko.isRightCollision(bag)) { //soko의 오른쪽에 bag객체가 존재하면
                        
                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j); //Baggage 어레이 리스트에서 객체를 하나씩 검사하여 item에 넣음
                            
                            if (!bag.equals(item)) { //item(또 다른 Baggage)객체와 bag객체가 같지 않을때
                                
                                if (bag.isRightCollision(item)) { //bag 객체 오른쪽에 item 객체가 존재하면 안움직임
                                    return true;
                                }
                            }
                            
                            if (checkWallCollision(bag, RIGHT_COLLISION)) { //bag객체 오른쪽에 wall 객체가 존재하면 안움직임
                                return true;
                            }
                        }
                        //위의 if문을 다 만족하지 않으면 그제서야 bag 객체가 움직임.
                        bag.move(SPACE, 0);
                        isCompleted(); //bag객체가 움직인 후 게임이 끝났는지 검사함.
                    }
                }
                return false; //false를 리턴하여 reprint()를 하게함
                
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
                        isCompleted();
                    }
                }

                return false; //false를 리턴하여 reprint()를 하게함
                
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
                            
                            if (checkWallCollision(bag,BOTTOM_COLLISION)) {
                                
                                return true;
                            }
                        }
                        
                        bag.move(0, SPACE);
                        isCompleted();
                    }
                }
                
                break;
                
            default:
                break;
        }

        return false; //false를 리턴하여 reprint()를 하게함
    }

    public void isCompleted() { //다 최종지점에 넣었을경우 isCompleted=true

        int nOfBags = baggs.size(); //Bag 객체의 숫자
        int finishedBags = 0; // Bag객체의 숫자와 finishedBags가 isCompleted=true == 게임 종료

        for (int i = 0; i < nOfBags; i++) {
            
            Baggage bag = baggs.get(i);
            
            for (int j = 0; j < nOfBags; j++) {
                
                Area area =  areas.get(j); // 끝나는 지점
                
                if (bag.x() == area.x() && bag.y() == area.y()) { //bag 객체의 x,y와 area 객체의 x,y가 같으면 finishedBags +1증가
                    
                    finishedBags += 1;
                }
            }
        }

        if (finishedBags == nOfBags) { //finishedBag과 nOfbags가 같으면 모두 최종지점에 넣었다는 뜻
            
            isCompleted = true; //따라서 끝남
            repaint(); //컴포넌트의 모양 색상등이 바뀌었을때 사용
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
    }
}

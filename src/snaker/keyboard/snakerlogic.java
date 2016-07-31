package snaker.keyboard;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

enum DIRECTION {
	Up,Down,Left,Right
};

//双向链表
class SnakeBody{
	Point section;
	SnakeBody next;
	SnakeBody before;
}


/*用于描述蛇本身*/
class Snake {
	/*蛇分为蛇头和身体*/
	private static int snakeLength=1; //蛇有一个头
	private SnakeBody snaketail,snakehead;
	private DIRECTION direction_current=DIRECTION.Right;
	private static Point head_position_current;
	public Snake(int hlength,int vlength) {
		/*
		 * 现在我们改用链表
		snakebody = new Point[hlength*vlength];
		snakebody[0].setLocation(0, 0); //起始位置为(0,0)
		*/
		snakehead = new SnakeBody(); 
		snakehead.section.setLocation(0, 0); //起始位置为(0,0)
		snakehead.next=null;
		snakehead.before=null;
		snaketail=snakehead; //初始 头和尾在一起
		
		direction_current=DIRECTION.Right; //那么必须向右走
	}
	
	/*蛇头位于最后的位置*/
	public Point get_head_position() {
		return snakehead.section;
	}
	
	public DIRECTION getDirection() {
		return direction_current;
	}

	public void setDirection(DIRECTION direction) {
		direction_current = direction;
	}

	/*在Timer Action中调用*/
	/*蛇吃食物的过程*/
	/*
	 * 蛇头在不停的闪动，闪动的蛇头覆盖食物，旧的蛇头变成身体，新的蛇头长出
	 * 我觉得应该有一个消化的过程，即在time n吃入，在time n+1时变成新的身体
	 */
	public void eatFood(Point foodPosition) {
		//这里不能直接赋值的哦！并且执行不能被打断
		/*这里我们改用双向链表了
		 * snakebody[snakeLength].setLocation(foodPosition);
		snakeLength++;*/
		
	}

	/*由于蛇头在最后的位置，那么不应该将蛇头与自己比较*/
	private boolean isBitedBySelf() {
		Point head = get_head_position();
		/*我们改用链表了
		 * for(int i=0;i<snakeLength-1;i++) {
			if(head.equals(snakebody[i])) {
				return true;
			}
		}*/
		SnakeBody nextloop=snakehead.next;
		while(nextloop!=null) {
			if(head.equals(nextloop.section)) {
				return true;
			}
			nextloop=nextloop.next;
		}
		return false;
	}
	
	public boolean amIDead() {
		//检查自己是不是死了
		Point checkPoint = this.get_head_position();
		if(checkPoint.x<0 || checkPoint.x>=InitialCondition.count_horizontal
				|| checkPoint.y<0 || checkPoint.y>=InitialCondition.count_vertical) {
			//蛇越界了
			return true;
		}
		else if(isBitedBySelf()){ //蛇咬到自己的身体了
			return true;
		}
		else {
			return false;
		}
	}
	
	/*位置变化的只有头和尾*/
	public void moveToNext(DIRECTION direction) {
		this.setDirection(direction);
		SnakeBody newHead = new SnakeBody();
		newHead.section.setLocation(snakehead.section);
		switch (direction) {
		case Up:
			newHead.section.y--;
			break;
		case Down:
			newHead.section.y++;
			break;
		case Left:
			newHead.section.x--;
			break;
		case Right:
			newHead.section.x++;
			break;
		default:
			System.out.println("something error");
			//throw(new Exception());
			break;
		}
		//更改蛇体数据结构
		newHead.next=snakehead;
		snakehead.before=newHead;
		snakehead=newHead;
		//新的蛇尾，留下旧的蛇尾等待垃圾收集
		snaketail = snaketail.before;
		snaketail.next.before=null;
		snaketail.next=null;
	}
}

class SnakerGame extends JPanel {
	//private int count_horizontal,count_vertical;
	//private static Point head_position_current;
	private static Point foodPosition;
	private static Snake snake;
	private Timer gametimer;
	private Random hrand,vrand;
	private BufferedImage foodImage;
	private Graphics2D g2d_current,g2d_background;
	private BufferedImage image_current=
			new BufferedImage(InitialCondition.borderWidth,
					InitialCondition.borderHeight,
					BufferedImage.TYPE_INT_ARGB);
	private BufferedImage image_background=
			new BufferedImage(InitialCondition.borderWidth,
					InitialCondition.borderHeight,
					BufferedImage.TYPE_INT_ARGB);
	
	public SnakerGame() {
		try{
			foodImage=ImageIO.read(new File("img/apple.png"));
		}catch(IOException ex) {
			//handle exception
			System.out.println("No such file!");
		}
		
		/*需要一个定时器*/
		gametimer = new Timer(InitialCondition.timer_inter_sec,
				new GameTimerActionListener());
		
		/*水平和垂直方向的随机数发生其*/
		hrand = new Random();
		vrand = new Random();
		/*初始化snake*/
		/*
		count_horizontal=InitialCondition.borderWidth/
                InitialCondition.foodWidth;
		count_vertical=InitialCondition.borderHeight/
                InitialCondition.foodHeight;
                */
		snake = new Snake(InitialCondition.count_horizontal,
				InitialCondition.count_vertical);
		
		/*初始化食物*/
		foodPosition = new Point(InitialCondition.count_horizontal/2,
				InitialCondition.count_vertical/2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image_current,0,0,500,500,null);
	}
	
	/*先实现单一图片的上下左右移动和自动沿着一个方向移动*/
	public boolean startgame() {
		System.out.println("Start game!");
		g2d_current=image_current.createGraphics();
		g2d_background=image_background.createGraphics();
		
		//画出初始的食物
		g2d_current.drawImage(foodImage, foodPosition.x*InitialCondition.foodWidth, 
				foodPosition.y*InitialCondition.foodHeight, 
				InitialCondition.foodWidth, 
				InitialCondition.foodHeight, null);
		
		//画出初始的Snake
		
		//开启计时器
		gametimer.start();
		return true;
	}
	
	public boolean pausegame() {
		gametimer.stop();
		return true;
	}
	
	public boolean resumegame() {
		gametimer.start();
		return true;
	}
	
	public boolean exitgame() {
		System.out.println("Exit game!");
		gametimer.stop();
		return true;
	}
	
	/*移动程序*/
	/*在下一个timer到期的时候改变方向*/
	public static void moveUp() {
		snake.setDirection(DIRECTION.Up);
	}
	
	public static void moveDown() {
		snake.setDirection(DIRECTION.Down);
	}
	
	public static void moveLeft() {
		snake.setDirection(DIRECTION.Left);
	}
	
	public static void moveRight() {
		snake.setDirection(DIRECTION.Right);
	}
	
	/*需要被timer调用*/
	public void generateFood() {
		/*
		 * x [0,InitialCondition.borderWidth)
		 * y [0,InitialCondition.borderHeight)
		 */
		foodPosition.setLocation(hrand.nextInt(InitialCondition.count_horizontal)*
				InitialCondition.foodWidth
				, vrand.nextInt(InitialCondition.count_vertical)*
				InitialCondition.foodHeight);
		
	}
	
	/*just for test*/
	public void showFood() {
		g2d_current.drawImage(foodImage, 
				foodPosition.x, 
				foodPosition.y, 
				InitialCondition.foodWidth, 
				InitialCondition.foodHeight, null);
	}
	
	public void eatFood() {
		;
	}
	
	public void gameover() {
		;
	}
	class GameTimerActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 执行定时器动作
			if(snake.get_head_position().equals(foodPosition)) {
				//吃到食物，在当前方向上变长了。
				snake.eatFood(foodPosition);
				generateFood();
				showFood();
			}
			else {
				//没有食物，蛇就移动到下一个位置，哈哈！
				snake.moveToNext(snake.getDirection());
			}
			repaint();
		}
		
	}
}
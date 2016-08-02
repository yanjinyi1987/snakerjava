package snaker.keyboard;

import java.awt.Color;
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
	
	public SnakeBody() {
		section = new Point();
	}
}


/*用于描述蛇本身*/
class Snake {
	/*蛇分为蛇头和身体*/
	//private static int snakeLength=1; //蛇有一个头
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
	
	public SnakeBody getSnakeHead() {
		return snakehead;
	}
	
	public void setSnakeHead(SnakeBody snakehead) {
		this.snakehead=snakehead;
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

	public SnakeBody getSnaketail() {
		return snaketail;
	}

	public void setSnaketail(SnakeBody snaketail) {
		this.snaketail = snaketail;
	}
}

class SnakerGame extends JPanel {
	//private int count_horizontal,count_vertical;
	//private static Point head_position_current;
	private int score;
	private static Point foodPosition;
	private static Snake snake;
	private Timer gametimer;
	private boolean ateFoodFlag;
	private Random hrand,vrand;
	private BufferedImage foodImage,snakeBodyImage;
	private static Graphics2D g2d_current,g2d_background;
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

		try{
			snakeBodyImage=ImageIO.read(new File("img/brick.png"));
		}catch(IOException ex) {
			//handle exception
			System.out.println("No such file!");
		}
		initialGame();
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
	}

	public void initialGame() {
		/*得分*/
		score=0;
		snakerframe.scoreLabel.setText("0");
		/*有食物吃*/
		ateFoodFlag=false; //类变量初始化的方式？
		/*初始化食物*/
		foodPosition = new Point(InitialCondition.count_horizontal/2,
				InitialCondition.count_vertical/2);
		
		snake = new Snake(InitialCondition.count_horizontal,
				InitialCondition.count_vertical);
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
		
		//初始化
		initialGame();
		g2d_current.setBackground(Color.WHITE);
		g2d_current.fillRect(0, 0
				, InitialCondition.borderWidth
				, InitialCondition.borderHeight);
		//画出初始的食物
		g2d_current.drawImage(foodImage, foodPosition.x*InitialCondition.foodWidth, 
				foodPosition.y*InitialCondition.foodHeight, 
				InitialCondition.foodWidth, 
				InitialCondition.foodHeight, null);
		
		//画出初始的Snake
		g2d_current.drawImage(snakeBodyImage, snake.get_head_position().x*InitialCondition.foodWidth, 
				snake.get_head_position().y*InitialCondition.foodHeight, 
				InitialCondition.foodWidth, 
				InitialCondition.foodHeight, null);	
		
		repaint();
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
	private boolean isFoodinSnake(Point foodPosition) {
		SnakeBody nextloop=snake.getSnakeHead();
		while(nextloop!=null) {
			if(foodPosition.equals(nextloop.section)) {
				return true;
			}
			nextloop=nextloop.next;
		}
		return false;
	}
	public void generateFood() {
		/*
		 * x [0,InitialCondition.borderWidth)
		 * y [0,InitialCondition.borderHeight)
		 * 新产生的食物不能位于蛇的身体里面额
		 */
		do {
			int x=hrand.nextInt(InitialCondition.count_horizontal);
			int y=vrand.nextInt(InitialCondition.count_vertical);
			foodPosition.setLocation(x,y);
		}while(isFoodinSnake(foodPosition));
		
	}
	
	/*just for test*/
	public void drawFood() {
		g2d_current.drawImage(foodImage, 
				foodPosition.x*InitialCondition.foodWidth, 
				foodPosition.y*InitialCondition.foodHeight, 
				InitialCondition.foodWidth, 
				InitialCondition.foodHeight, null);
	}
	
	public void drawSnake() {
		SnakeBody snake = this.snake.getSnakeHead();
		while(snake!=null) {
			g2d_current.drawImage(snakeBodyImage
					, snake.section.x*InitialCondition.foodWidth
					, snake.section.y*InitialCondition.foodHeight
					, InitialCondition.foodWidth
					, InitialCondition.foodHeight, null);
			
			snake=snake.next;
		}
	}
	
	public void clearImageBuffer() {
		g2d_current.setBackground(Color.WHITE); //setcolor 都无效的哦
		g2d_current.clearRect(0, 0
				, InitialCondition.borderWidth
				, InitialCondition.borderHeight);
	}
	
	public void gameover() {
		gametimer.stop();
		snakerframe.pauseButton.setEnabled(false);
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
		SnakeBody newHead = new SnakeBody();
		SnakeBody snakehead=snake.getSnakeHead();
		newHead.next=snakehead;
		snakehead.before=newHead;
		newHead.section.setLocation(snakehead.section);
		//snakehead=newHead; 
		snake.setSnakeHead(newHead);//在下一次timer中将蛇头的坐标更新。
		

		g2d_current.setBackground(Color.WHITE); //setcolor 都无效的哦
		g2d_current.clearRect(foodPosition.x*InitialCondition.foodWidth
				, foodPosition.y*InitialCondition.foodHeight
				, InitialCondition.foodWidth
				, InitialCondition.foodHeight);
		
		g2d_current.drawImage(snakeBodyImage
				, newHead.section.x*InitialCondition.foodWidth
				, newHead.section.y*InitialCondition.foodHeight
				, InitialCondition.foodWidth
				, InitialCondition.foodHeight, null);
	}

	/*由于蛇头在最后的位置，那么不应该将蛇头与自己比较*/
	private boolean isBitedBySelf(Point headLocation) {
		Point head = snake.get_head_position();
		SnakeBody snakehead = snake.getSnakeHead();
		/*我们改用链表了
		 * for(int i=0;i<snakeLength-1;i++) {
			if(head.equals(snakebody[i])) {
				return true;
			}
		}*/
		SnakeBody nextloop=snakehead.next;
		while(nextloop!=null) {
			if(headLocation.equals(nextloop.section)) {
				return true;
			}
			nextloop=nextloop.next;
		}
		return false;
	}
	
	public boolean amIDead(Point headLocation) {
		//检查自己是不是死了
		if(headLocation.x<0 || headLocation.x>=InitialCondition.count_horizontal
				|| headLocation.y<0 || headLocation.y>=InitialCondition.count_vertical) {
			//蛇越界了
			System.out.println("Hit Wall"+" x is "+headLocation.x+"y is "+headLocation.y);
			return true;
		}
		else if(isBitedBySelf(headLocation)){ //蛇咬到自己的身体了
			System.out.println("Eat self"+" x is "+headLocation.x+"y is "+headLocation.y);
			return true;
		}
		else {
			return false;
		}
	}

	/*位置变化的只有头和尾*/
	/*return successGrowth*/
	public boolean growth(DIRECTION direction) {
		SnakeBody snakehead = snake.getSnakeHead();
		//this.setDirection(direction);
		switch (direction) {
		case Up:
			snakehead.section.y--;
			break;
		case Down:
			snakehead.section.y++;
			break;
		case Left:
			snakehead.section.x--;
			break;
		case Right:
			snakehead.section.x++;
			break;
		default:
			System.out.println("something error");
			//throw(new Exception());
			break;
		}
		if(amIDead(snakehead.section)==true) {
			return false;
		}
		else {
			System.out.println("growth "+"x is "+snake.get_head_position().x
					+" y is "+
					snake.get_head_position().y);
			g2d_current.drawImage(snakeBodyImage
					, snake.get_head_position().x*InitialCondition.foodWidth
					, snake.get_head_position().y*InitialCondition.foodHeight
					, InitialCondition.foodWidth
					, InitialCondition.foodHeight, null);
			return true;
		}
	}
	
	/*位置变化的只有头和尾*/
	/*return successMoveToNext*/
	public boolean moveToNext(DIRECTION direction) {
		//this.setDirection(direction);
		SnakeBody newHead = new SnakeBody();
		SnakeBody snakehead = snake.getSnakeHead();
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
		if(amIDead(newHead.section)==true) {
			return false;
		}
		else {
			//
			g2d_current.drawImage(snakeBodyImage
					, newHead.section.x*InitialCondition.foodWidth
					, newHead.section.y*InitialCondition.foodHeight
					, InitialCondition.foodWidth
					, InitialCondition.foodHeight, null);
			
			g2d_current.setBackground(Color.WHITE); //setcolor 都无效的哦
			g2d_current.clearRect(snake.getSnaketail().section.x*InitialCondition.foodWidth
					, snake.getSnaketail().section.y*InitialCondition.foodHeight
					, InitialCondition.foodWidth
					, InitialCondition.foodHeight);
			//更改蛇体数据结构
			newHead.next=snakehead;
			snakehead.before=newHead;
			//snakehead=newHead;
			snake.setSnakeHead(newHead);
			//新的蛇尾，留下旧的蛇尾等待垃圾收集
			snake.setSnaketail(snake.getSnaketail().before);
			//snaketail = snaketail.before;
			snake.getSnaketail().next.before=null;
			snake.getSnaketail().next=null;
			return true;
		}
	}
	
	class GameTimerActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 执行定时器动作
			boolean noNeedMoveToNext=false;
			boolean successMoveToNext=false;
			boolean successGrowth=false;
			//clearImageBuffer(); //但是没有必要完全的清除额
			if(ateFoodFlag==true) {
				successGrowth=growth(snake.getDirection());
				ateFoodFlag=false;
				noNeedMoveToNext=true;
			}
			if(snake.get_head_position().equals(foodPosition)) {
				//吃到食物，待消化。
				score++;
				snakerframe.scoreLabel.setText(Integer.toString(score));
				eatFood(foodPosition);
				ateFoodFlag=true;
				generateFood();
			}
			else {
				//没有食物，也不是在消化，蛇就移动到下一个位置，哈哈！
				if(noNeedMoveToNext==false) {
					successMoveToNext=moveToNext(snake.getDirection());
				}
			}
			if(ateFoodFlag ==false && successMoveToNext==false && successGrowth==false) {
				snakerframe.scoreLabel.setText("GG");
				gameover();
			}
			else {
				drawFood();
				//drawSnake();
				repaint();
			}
		}
		
	}
}
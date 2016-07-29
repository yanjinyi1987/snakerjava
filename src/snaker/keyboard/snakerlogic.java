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

/*用于描述蛇本身*/
class Snake {
	private static Point[] snake;
	private static int snakeLength=0;
	private static Point head_position_current;
	public Snake(int hlength,int vlength) {
		snake = new Point[hlength*vlength];
	}
	
	public Point get_head_position() {
		return snake[snakeLength-1];
	}
	
	/*在Timer Action中调用*/
	public void eatFood(Point foodPosition) {
		//这里不能直接赋值的哦！并且执行不能被打断
		snake[snakeLength].setLocation(foodPosition);
		snakeLength++;
		
		//判断是不是死了
		amIDead();
	}
	
	private boolean amIDead() {
		//检查自己是不是死了
		return false;
	}
	
	public void moveToNext(int direction) {
		;
	}
}
class SnakerGame extends JPanel {
	/*
	 * 0 - 上
	 * 1 - 下
	 * 2 - 左
	 * 3 - 右
	 */
	private int count_horizontal,count_vertical;
	private static int direction_current=0;
	private static Point head_position_current;
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
		gametimer = new Timer(100,new GameTimerActionListener());
		
		/*水平和垂直方向的随机数发生其*/
		hrand = new Random();
		vrand = new Random();
		/*初始化snake*/
		count_horizontal=InitialCondition.borderWidth/
                InitialCondition.foodWidth;
		count_vertical=InitialCondition.borderHeight/
                InitialCondition.foodHeight;
		snake = new Snake(count_horizontal,count_vertical);
		
		/**/
		foodPosition = new Point(count_horizontal/2,
				count_vertical/2);
		
		head_position_current = new Point();
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
		direction_current=0;
	}
	
	public static void moveDown() {
		direction_current=1;
	}
	
	public static void moveLeft() {
		direction_current=2;
	}
	
	public static void moveRight() {
		direction_current=3;
	}
	
	/*需要被timer调用*/
	public void generateFood() {
		/*
		 * x [0,InitialCondition.borderWidth)
		 * y [0,InitialCondition.borderHeight)
		 */
		foodPosition.setLocation(hrand.nextInt(count_horizontal)*
				InitialCondition.foodWidth
				, vrand.nextInt(count_vertical)*
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
				snake.moveToNext(direction_current);
			}
			repaint();
		}
		
	}
}
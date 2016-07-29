package snaker.keyboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class testkeyboard {
	public static void main(String[] argv) {
		snakerframe mainframe=new snakerframe();
		while(true) {
			mainframe.run();
			break;
		}
	}
}
class InitialCondition {
	public static int startx=5;
	public static int starty=5;
	public static int borderWidth=500;
	public static int borderHeight=500;
	public static int foodWidth=25;
	public static int foodHeight=25;
}

class FoodPosition {
	public FoodPosition() {
		x=0;y=0;
	}
	public int x;
	public int y;
}

class snaker_keylistener extends KeyAdapter{ 
	public void keyPressed(KeyEvent e) {
		char charA=e.getKeyChar();
		int keycode=e.getKeyCode();
		System.out.println(charA+" code is "+String.valueOf(keycode));
		//我们在这里需要做点什么事情
		//上38 下40 左37 右39
		//dosomething()
		switch(keycode) {
		case KeyEvent.VK_UP:
			break;
		case KeyEvent.VK_DOWN:
			break;
		case KeyEvent.VK_LEFT:
			break;
		case KeyEvent.VK_RIGHT:
			break;
		default:
			break;
		}
	}
}

class startButton_ActionListener implements ActionListener {
	public SnakerGame f;
	public JButton pause;
	public startButton_ActionListener(SnakerGame f,JButton pause) {
		this.f=f;
		this.pause=pause;
	}
	public void actionPerformed(ActionEvent e) {
		//code that reacts to the action...
		f.requestFocus();
		pause.setEnabled(true);
		//重头开始游戏
	}
}

class pauseButton_ActionListener implements ActionListener {
	private static boolean flag=true;
	public JButton b;
	public SnakerGame f;
	public pauseButton_ActionListener(SnakerGame f,JButton b) {
		this.b=b;
		this.f=f;
	}
	public void actionPerformed(ActionEvent e) {
		//暂停游戏
		if(flag==true) {
			//暂停游戏
			b.setText("Resume");
		}
		else {
			//恢复游戏进度
			b.setText("Pause");
			f.requestFocus();
		}
		flag=!flag;
	}
}

class exitButton_ActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}

class GameZonePanel extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//画网格
		for(int i=0;i<=InitialCondition.borderWidth/InitialCondition.foodWidth;i++) {
			g.drawLine(InitialCondition.startx,
					InitialCondition.starty+i*InitialCondition.foodHeight,
					InitialCondition.startx+InitialCondition.borderWidth,
					InitialCondition.starty+i*InitialCondition.foodHeight);
		}
		
		for(int i=0;i<=InitialCondition.borderHeight/InitialCondition.foodHeight;i++) {
			g.drawLine(InitialCondition.startx+i*InitialCondition.foodHeight,
					InitialCondition.starty,
					InitialCondition.startx+i*InitialCondition.foodHeight,
					InitialCondition.starty+InitialCondition.borderHeight);
		}
	}
}

class SnakerGame extends JPanel {
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
			image=ImageIO.read(new File("img/apple.png"));
		}catch(IOException ex) {
			//handle exception
			System.out.println("No such file!");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage
	}
	/*需要一个定时器*/
	/*先实现单一图片的上下左右移动和自动沿着一个方向移动*/
	public boolean startgame() {
		return true;
	}
	
	public boolean pausegame() {
		return true;
	}
	
	public boolean resumegame() {
		return true;
	}
	
	public boolean exitgame() {
		return true;
	}
	
}
class snakerframe {
	private static void constructGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame();
		frame.setTitle("Snaker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.addKeyListener(new snaker_keylistener());
		frame.setLayout(null);
		frame.setSize(630,600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	
		JPanel globalPanel = new JPanel();
		globalPanel.setSize(630,
				InitialCondition.borderHeight+2*InitialCondition.starty);
		globalPanel.setLayout(null);
		//globalPanel.addKeyListener(new snaker_keylistener());
		
		GameZonePanel gPanel = new GameZonePanel();
		gPanel.setSize(InitialCondition.borderWidth+2*InitialCondition.startx
				,InitialCondition.borderHeight+2*InitialCondition.starty);
		gPanel.setBorder(BorderFactory.createMatteBorder(InitialCondition.startx
				,InitialCondition.startx
				,3//InitialCondition.startx
				,3//InitialCondition.startx
				,Color.green));
		gPanel.setOpaque(false); //将背景网格设置为透明
		
		SnakerGame gGamePanel = new SnakerGame();
		gGamePanel.setLocation(InitialCondition.startx,
				InitialCondition.starty);	
		gGamePanel.setSize(InitialCondition.borderWidth,
				InitialCondition.borderHeight);
		gGamePanel.setDoubleBuffered(true);
		gGamePanel.addKeyListener(new snaker_keylistener());
		
		JButton startButton = new JButton();
		JButton pauseButton = new JButton();
		JButton exitButton = new JButton();
		
		startButton.setText("Start");
		startButton.setSize(100, 100);
		startButton.setLocation(InitialCondition.borderWidth+InitialCondition.startx+10, 0);
		startButton.addActionListener(new startButton_ActionListener(gGamePanel,pauseButton));

		pauseButton.setText("Pause");
		pauseButton.setSize(100, 100);
		pauseButton.setLocation(InitialCondition.borderWidth+InitialCondition.startx+10, 110);
		pauseButton.addActionListener(new pauseButton_ActionListener(gGamePanel,pauseButton));
		pauseButton.setEnabled(false);
		
		exitButton.setText("Exit");
		exitButton.setSize(100, 100);
		exitButton.setLocation(InitialCondition.borderWidth+InitialCondition.startx+10, 220);
		exitButton.addActionListener(new exitButton_ActionListener());
		
		globalPanel.add(gPanel);
		globalPanel.add(gGamePanel);
		globalPanel.add(startButton);
		globalPanel.add(pauseButton);
		globalPanel.add(exitButton);
		frame.getContentPane().add(globalPanel);
		//如果不加这一行，焦点会在button上，
		//并且鼠标无法修改焦点到frame之上的。
		frame.setVisible(true);
		gGamePanel.requestFocus();
		//开始游戏
		gGamePanel.startgame();
	}
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				constructGUI();
			}
		});
	}
}

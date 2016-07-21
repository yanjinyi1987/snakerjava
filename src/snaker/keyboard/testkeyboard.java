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
	public JFrame f;
	public JButton pause;
	public startButton_ActionListener(JFrame f,JButton pause) {
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
	public JFrame f;
	public pauseButton_ActionListener(JFrame f,JButton b) {
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
	private BufferedImage image;
	
	public GameZonePanel() {
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
		g.drawImage(image,5,5,25,25, null);
		g.drawImage(image,470,470,25,25, null);
	}
}
class snakerframe {
	private static void constructGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame();
		frame.setTitle("Snaker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new snaker_keylistener());
		frame.setLayout(null);
		frame.setSize(630,600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	
		JPanel globalPanel = new JPanel();
		globalPanel.setSize(630,500);
		globalPanel.setLayout(null);
		//globalPanel.addKeyListener(new snaker_keylistener());
		
		GameZonePanel gPanel = new GameZonePanel();
		gPanel.setSize(500,500);
		gPanel.setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.green));
		gPanel.setDoubleBuffered(true);
		
		JButton startButton = new JButton();
		JButton pauseButton = new JButton();
		JButton exitButton = new JButton();
		
		startButton.setText("Start");
		startButton.setSize(100, 100);
		startButton.setLocation(510, 0);
		startButton.addActionListener(new startButton_ActionListener(frame,pauseButton));

		pauseButton.setText("Pause");
		pauseButton.setSize(100, 100);
		pauseButton.setLocation(510, 110);
		pauseButton.addActionListener(new pauseButton_ActionListener(frame,pauseButton));
		pauseButton.setEnabled(false);
		
		exitButton.setText("Exit");
		exitButton.setSize(100, 100);
		exitButton.setLocation(510, 220);
		exitButton.addActionListener(new exitButton_ActionListener());
		
		globalPanel.add(gPanel);
		globalPanel.add(startButton);
		globalPanel.add(pauseButton);
		globalPanel.add(exitButton);
		frame.getContentPane().add(globalPanel);
		//如果不加这一行，焦点会在button上，
		//并且鼠标无法修改焦点到frame之上的。
		frame.requestFocus();
		
	}
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				constructGUI();
			}
		});
	}
}

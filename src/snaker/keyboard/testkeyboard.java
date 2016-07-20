package snaker.keyboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
		frame.setSize(800,600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		JPanel globalPanel = new JPanel();
		globalPanel.setSize(700,500);
		globalPanel.setLayout(null);
		
		GameZonePanel gPanel = new GameZonePanel();
		gPanel.setSize(500,500);
		gPanel.setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.green));
		gPanel.setDoubleBuffered(true);
		
		JButton startButton = new JButton();
		startButton.setText("Start");
		startButton.setSize(100, 100);
		startButton.setLocation(510, 0);
		
		globalPanel.add(gPanel);
		globalPanel.add(startButton);
		frame.getContentPane().add(globalPanel);
	}
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				constructGUI();
			}
		});
	}
}

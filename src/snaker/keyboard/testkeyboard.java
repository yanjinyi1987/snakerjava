package snaker.keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
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
		//dosomething()
	}
}

class snakerframe {
	private static void constructGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame();
		frame.setTitle("Snaker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new snaker_keylistener());
		frame.pack();
		frame.setVisible(true);
	}
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				constructGUI();
			}
		});
	}
}

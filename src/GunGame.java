import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Notice that we are not extending the panel here
final public class GunGame {
	// We define the frame and panel as class level
	JFrame frame;
	DrawPanel drawPanel;

	boolean random = false;
	boolean clearScreen = false;

	int bulletCount = 0;
	int clickCount = 0;
	int totalBullets = 0;
	int score = 0;

	boolean mutex_acquired = false;

	ArrayList<Bullet> bulletArray = new ArrayList<Bullet>();
	Target theTarget = new Target();
	Cannon cannon = new Cannon();
	Set<Integer> keyPresses = new HashSet<>();

	private JButton fire;
	private JButton changePicture;
	private JButton scramble;
	private JButton randomize;
	private JButton clear;

	public static void main(String[] args) {
		new GunGame().go();
	}

	private void go() {
		Color backgroundColor = new Color(100, 206, 255);

		frame = new JFrame("Gun Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new DrawPanel());
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(1200, 700);
		frame.setLocation(100, 0);
		frame.setBackground(backgroundColor);

		moveIt();
	}

	private synchronized boolean acquireMutex() {
		if (mutex_acquired) {
			return false;
		} else {
			mutex_acquired = true;
			return true;
		}
	}

	private void releaseMutex() {
		mutex_acquired = false;
	}

//We now create a class that is the panel and has the buttons...for our
	class DrawPanel extends JPanel implements ActionListener, KeyListener {

		//paintComponent rather than paint...for the redraw
		public void paintComponent(Graphics g) {
			drawPicture(g);
		}

		private void drawPicture(Graphics g) {
			Color grassColor = new Color(34, 139, 34);

			Dimension d = getSize();

			g.drawString("Clicks: " + clickCount, 10, 20);
			g.drawString("Bullets: " + totalBullets, 10, 35);

			mutex();
			
			if (!bulletArray.isEmpty()) {
				g.drawString("X: " + bulletArray.get(bulletCount).getXValue(), 10, 50);
				g.drawString("Y: " + bulletArray.get(bulletCount).getYValue(), 10, 65);
				g.drawString("X v: " + bulletArray.get(bulletCount).getXChanger(), 10, 80);
				g.drawString("Y v: " + bulletArray.get(bulletCount).getYChanger(), 10, 95);
				g.drawString("Score: " + score, 10, 110);
			}
			
			releaseMutex();

			//Import sun photo
			Image sun;
			ImageIcon s = new ImageIcon("C:\\Users\\Kevin Crossgrove\\Documents\\GitHub\\GunGame\\GunGame\\src\\sun.png");
			sun = s.getImage();
			int sunWidth = sun.getWidth(null)/2;
			int sunHeight = sun.getHeight(null)/2;
			g.drawImage(sun, 80, 10, sunWidth/3, sunHeight/3, this);

			//Set up grass photo
			Image grass;
			ImageIcon gg = new ImageIcon("C:\\Users\\Kevin Crossgrove\\Documents\\GitHub\\GunGame\\GunGame\\src\\Grass.png");
			grass = gg.getImage();
			int grassWidth = grass.getWidth(null)/2;
			int grassHeight = grass.getHeight(null)/2;
			g.drawImage(grass, 0, frame.getHeight() - grassHeight + 50, grassWidth, grassHeight - 50, this);

			theTarget.paintTarget(g);
			cannon.paintCannon(g);

			if (totalBullets == 0)
				changePicture.setVisible(false);
			else
				changePicture.setVisible(true);

			mutex();
			
			if (!bulletArray.isEmpty()) {
				for (Bullet newBullet : bulletArray) {
					newBullet.paint(g);
				}
			}

			releaseMutex();
		}

		// Function that setups buttons
		public void setupButton(JButton button) {
			button.addActionListener(this);
			button.setBackground(Color.blue);
			button.setForeground(Color.white);
			button.setFocusable(false);
			add(button);
		}

		//Constructor for creating Buttons
		public DrawPanel() {
			fire = new JButton("Fire Gun");
			changePicture = new JButton("Change Red Bullet");
			scramble = new JButton("Scramble");
			randomize = new JButton("Randomize");
			clear = new JButton("Clear Screen");

			setupButton(fire);
			setupButton(changePicture);
			setupButton(scramble);
			setupButton(randomize);
			setupButton(clear);
			
			addKeyListener(this);
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);
		}

		// Responds to button clicks the user makes
		public void actionPerformed(ActionEvent ae) {
			mutex();

			if (ae.getSource() == fire) {
				addNewBullet();
				random = false;
			}
			else if (ae.getSource() == changePicture) {
				bulletArray.get(bulletCount).setRandom();
				if (bulletArray.get(bulletCount).getCount() == 0) {
					bulletArray.get(bulletCount).startY();
				}
				else {
					bulletArray.get(bulletCount).changeXDirection();
					bulletArray.get(bulletCount).changeYDirection();
				}

				bulletArray.get(bulletCount).updateCount();
			} 
			else if (ae.getSource() == scramble) {
				for (Bullet everyBullet : bulletArray) {
					everyBullet.setRandom();
					if (everyBullet.getCount() == 0) {
						everyBullet.startY();
					}
					else {
						everyBullet.changeXDirection();
						everyBullet.changeYDirection();
					}

					everyBullet.updateCount();
				}
			} 
			else if (ae.getSource() == randomize) {
				for (Bullet everyBullet : bulletArray) {
					everyBullet.setRandom();

					if (bulletArray.get(bulletCount).getCount() == 0) {
						bulletArray.get(bulletCount).startY();
					}

					everyBullet.generateRandomPath();
				}
			} else if (ae.getSource() == clear) {
				clearScreen = true;
			}

			clickCount++;
			releaseMutex();
			repaint();
		}

		// Respond to Keyboard events
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			keyPresses.add(code);
			
			mutex();

			if (keyPresses.contains(KeyEvent.VK_W))
				cannon.up();
			if (keyPresses.contains(KeyEvent.VK_S))
				cannon.down();
			if (keyPresses.contains(KeyEvent.VK_D))
				cannon.right();
			if (keyPresses.contains(KeyEvent.VK_A))
				cannon.left();
			if (keyPresses.contains(KeyEvent.VK_F))
				addNewBullet();
			
			releaseMutex();
			
		}

		public void keyTyped(KeyEvent e) { }

		public void keyReleased(KeyEvent e) {
			int code = e.getKeyCode();
			if (keyPresses.contains(code))
				keyPresses.remove(code);
		}
	}

	//my function to control the animation
	private void moveIt() {
		while (true) {
			theTarget.moveTarget();

			if (theTarget.getTargetY() > frame.getSize().height - 100 || theTarget.getTargetY() < 0) {
				theTarget.changeDirection();
			}
			
			mutex();
			
			for (Bullet thisBullet : bulletArray) {
				thisBullet.moveX();
				thisBullet.moveY();
				
				if((thisBullet.getXValue() > theTarget.getTargetX() && thisBullet.getXValue() < theTarget.getTargetX() + theTarget.getWidth())
						&& (thisBullet.getYValue() > theTarget.getTargetY() && thisBullet.getYValue() < theTarget.getTargetY() + theTarget.getHeight()) )
				{
					score++;
					thisBullet.setCollided();
					thisBullet.changeXDirection();
					thisBullet.changeYDirection();
				}

				if (!clearScreen) {
					if (thisBullet.getXValue() > frame.getSize().width - thisBullet.width() || thisBullet.getXValue() < 0) {
						thisBullet.changeXDirection();
					}
					if (thisBullet.getYValue() > frame.getSize().height - 35 || thisBullet.getYValue() < 0) {
						thisBullet.changeYDirection();
					}
				}
			}
		
			//Iterator used for deleting items out of the list
			Iterator<Bullet> iter = bulletArray.iterator();
			while (iter.hasNext() && clearScreen) {
				Bullet thisBullet = (Bullet) iter.next();
				if (thisBullet.getXValue() > frame.getWidth() || thisBullet.getYValue() > frame.getHeight()
						|| thisBullet.getXValue() < 0 || thisBullet.getYValue() < 0) {
					iter.remove();
					totalBullets -= 1;
					if (bulletCount != 0) {
						bulletCount -= 1;
					}
				}
			}

			// Reset clear screen flag when all bullets are gone.
			if (bulletArray.isEmpty()) clearScreen = false;

			try {
				//Changes the speed of moving objects
				Thread.sleep(4);
			} catch (Exception e) {}

			releaseMutex();
			frame.repaint();
		}
	}
	
	// Mutual exclusion function - Runs while mutex has not been acquired
	public void mutex() {
		while (!acquireMutex()) { }
	}
	
	public void addNewBullet(){

		if (bulletArray.isEmpty()) {
			bulletArray.add(new Bullet());
			bulletArray.get(bulletCount).setY(cannon.getY() + cannon.getHeight()/2 - 5);
			bulletArray.get(bulletCount).setX(cannon.getX() + cannon.getWidth() - 10);
			bulletArray.get(bulletCount).startX();
		} else {
			bulletArray.get(bulletCount).makeBlack();
			bulletArray.add(new Bullet());
			bulletArray.get(bulletCount + 1).setY(cannon.getY() + cannon.getHeight()/2 - 5);
			bulletArray.get(bulletCount + 1).setX(cannon.getX() + cannon.getWidth() - 10);
			bulletArray.get(bulletCount + 1).startX();
			bulletCount++;
		}
		totalBullets++;
	}
}

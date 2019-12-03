import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Notice that we are not extending the panel here
//Replace array list with a set
//Replace bullet count with current bullet
//Research mutex's in java
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

	boolean mutex_acquired = false;

	ArrayList<Bullet> bulletArray = new ArrayList<Bullet>();
	Set<Bullet> bulletSet = new HashSet<Bullet>();
	Target theTarget = new Target();

	private JButton fire;
	private JButton changePicture;
	private JButton scramble;
	private JButton randomize;
	private JButton clear;

	public static void main(String[] args) {
		new GunGame().go();
	}

	private void go() {
		Color backgroundColor = new Color(135, 206, 235);

		frame = new JFrame("Test");
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

	class DrawPanel extends JPanel implements ActionListener {

		private static final long serialVersionUID = 1L;

//paintComponent rather than paint...for the redraw
		public void paintComponent(Graphics g) {
			drawPicture(g);
		}

		private void drawPicture(Graphics g) {
			Color grassColor = new Color(34, 139, 34);

			Dimension d = getSize();

			g.drawString("Clicks: " + clickCount, 10, 20);
			g.drawString("Bullets: " + totalBullets, 10, 35);

			while (!acquireMutex()) {
			}
			if (!bulletArray.isEmpty()) {
				g.drawString("X: " + bulletArray.get(bulletCount).getXValue(), 10, 50);
				g.drawString("Y: " + bulletArray.get(bulletCount).getYValue(), 10, 65);
				g.drawString("X v: " + bulletArray.get(bulletCount).getXChanger(), 10, 80);
				g.drawString("Y v: " + bulletArray.get(bulletCount).getYChanger(), 10, 95);
			}
			releaseMutex();

			g.setColor(Color.yellow);
			g.fillOval(d.width / 16, d.height / 16, 100, 100);
			g.setColor(grassColor);
			g.fillRect(0, d.height - d.height / 4, d.width, d.height / 4);
			theTarget.paintTarget(g);

			int x1 = 50;
			int y1 = d.height - d.height / 4;
			g.setColor(Color.black);
			// Create gun
			Polygon myPolygon;
			myPolygon = new Polygon();
			myPolygon.addPoint(x1, y1);
			myPolygon.addPoint(x1 + 20, y1 - 100);
			myPolygon.addPoint(x1 + 100, y1 - 100);
			myPolygon.addPoint(x1 + 100, y1 - 80);
			myPolygon.addPoint(x1 + 40, y1 - 80);
			myPolygon.addPoint(x1 + 20, y1);
			g.fillPolygon(myPolygon);

			if (totalBullets == 0)
				changePicture.setVisible(false);
			else
				changePicture.setVisible(true);

			while (!acquireMutex()) {
			}
			;
			if (!bulletArray.isEmpty()) {
				for (Bullet newBullet : bulletArray) {
					newBullet.paint(g);
				}
			}
			releaseMutex();
		}

//constructor to add buttons
		public DrawPanel() {

			fire = new JButton("Fire Gun");
			changePicture = new JButton("Change Red Bullet");
			scramble = new JButton("Scramble");
			randomize = new JButton("Randomize");
			clear = new JButton("Clear Screen");

			fire.addActionListener(this);
			changePicture.addActionListener(this);
			scramble.addActionListener(this);
			randomize.addActionListener(this);
			clear.addActionListener(this);

			fire.setBackground(Color.blue);
			changePicture.setBackground(Color.blue);
			scramble.setBackground(Color.blue);
			randomize.setBackground(Color.blue);
			clear.setBackground(Color.blue);

			fire.setForeground(Color.white);
			changePicture.setForeground(Color.white);
			scramble.setForeground(Color.white);
			randomize.setForeground(Color.white);
			clear.setForeground(Color.white);

			add(fire);
			add(changePicture);
			add(scramble);
			add(randomize);
			add(clear);

		}

		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == fire) {
				clickCount++;
				totalBullets++;

				while (true) {
					if (acquireMutex()) {
						break;
					}
				}
				// If else statement for adding bullets. Doesn't run until mutex is completed.
				if (bulletArray.isEmpty()) {
					bulletArray.add(new Bullet());
					bulletArray.get(bulletCount).startX();
				} else {
					bulletArray.get(bulletCount).makeBlack();
					bulletArray.add(new Bullet());
					bulletArray.get(bulletCount + 1).startX();
					bulletCount++;
				}
				releaseMutex();
				random = false;
			}

			else if (ae.getSource() == changePicture) {
				clickCount++;

				while (!acquireMutex()) {
				}
				;
				bulletArray.get(bulletCount).setRandom();
				if (bulletArray.get(bulletCount).getCount() == 0) {
					bulletArray.get(bulletCount).startY();
				}

				else {
					bulletArray.get(bulletCount).changeXDirection();
					bulletArray.get(bulletCount).changeYDirection();
				}

				bulletArray.get(bulletCount).updateCount();
				releaseMutex();
			}

			else if (ae.getSource() == scramble) {
				clickCount++;

				while (!acquireMutex()) {}
				
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
				releaseMutex();
			} else if (ae.getSource() == randomize) {
				clickCount++;

				while (!acquireMutex()) {}

				for (Bullet everyBullet : bulletArray) {
					if (bulletArray.get(bulletCount).getCount() == 0) {
						bulletArray.get(bulletCount).startY();
					}

					everyBullet.generateRandomPath();
				}
				releaseMutex();
			} else if (ae.getSource() == clear) {
				clickCount++;

				clearScreen = true;

			}
			repaint();
		}
	}

//end of class DrawPanel
//my function to control the animation
	private void moveIt() {
		while (true) {
			theTarget.moveTarget();

			if (theTarget.getTargetY() > frame.getSize().height - 100 || theTarget.getTargetY() < 0) {
				theTarget.changeDirection();
			}

			while (!acquireMutex()) {
			}
			for (Bullet thisBullet : bulletArray) {
				thisBullet.moveX();
				thisBullet.moveY();

				if (thisBullet.isRandom() && !clearScreen) {
					if (thisBullet.getXValue() > frame.getSize().width - 30 || thisBullet.getXValue() < 10) {
						thisBullet.changeXDirection();
					}
					if (thisBullet.getYValue() > frame.getSize().height - 35 || thisBullet.getYValue() < 10) {
						thisBullet.changeYDirection();
					}
				} else if (!clearScreen) {
					if (!bulletArray.isEmpty()) {

						if (thisBullet.getXValue() > frame.getSize().width - ((frame.getSize().width) / 10)) {
							thisBullet.changeXDirection();
						}

						if (thisBullet.getXValue() < 130) {
							thisBullet.changeXDirection();
						}
					}
				}
			}

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

			if (bulletArray.isEmpty()) {
				clearScreen = false;
			}
			releaseMutex();

			try {
				Thread.sleep(1);
			} catch (Exception e) {
			}
			frame.repaint();
		}
	}
	
	//Mutual exclusion function - runs while mutex 
	public void mutex()
	{
		while (!acquireMutex()) {
		}
	}
}

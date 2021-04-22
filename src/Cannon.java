import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Cannon extends JPanel {

	private int x, y, height, width, velocity;
	private Image image;
	
	
	public Cannon() {
		x = 100;
		y = 325;
		ImageIcon ii = new ImageIcon("C:\\Users\\Kevin Crossgrove\\Documents\\GitHub\\GunGame\\GunGame\\src\\Cannon.png");
		image = ii.getImage();
		height = image.getHeight(null)/2;
		width = image.getWidth(null)/2;
		velocity = 4;
	}
	
	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}

	public void up() {
		y-= velocity;
	}
	
	public void down() {
		y+= velocity;	
	}

	public void right() {
		x += velocity;
	}

	public void left() {
		x -= velocity;
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public void paintCannon(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, width, height, this);
		repaint();
	}

	
}

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Cannon extends JPanel {

	private int x;
	private int y;
	private int height;
	private int width;
	private int velocity;
	
	
	public Cannon() {
		x = 100;
		y = 400;
		height = 25;
		width = 100;
		velocity = 4;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public void up() {
		y-= velocity;;
	}
	
	public void down() {
		y+= velocity;
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public void paintCannon(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(x, y, width, height);
		repaint();
	}

	
}

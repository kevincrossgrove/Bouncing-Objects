
import javax.swing.*;
import java.util.Random;
import java.awt.*;

public class Bullet extends JPanel
{
	private int x;
	private int y;
	private int xchanger;
	private int ychanger;
	private int bulletWidth;
	private int bulletHeight;
	private int count;
	private int distanceToGround;
	private boolean random;
	private boolean isRed;
	Random rand = new Random();
	
	public Bullet()
	{
		xchanger = 0;
		ychanger = 0;
		count = 0;
		bulletWidth = 20;
		bulletHeight = 10;
		random = false;
		isRed = true;
		x = 145;
		y = 410;
		distanceToGround = (y - 700) * -1;
	}
	
	public void changeXDirection()
	{
		xchanger = -xchanger;
	}
	
	public void changeYDirection()
	{
		ychanger = -ychanger;
	}
	
	public void moveX()
	{
		x += xchanger;
		
	}
	
	public void moveY()
	{
		y += ychanger;
	}

	
	public void startX()
	{
		xchanger = 1;
	}
	public void startY()
	{
		ychanger = 1;
	}
	
	public void startGravity()
	{
		int gravity = (1)/distanceToGround;
		
		y += gravity;
	}
	
	public int getXValue()
	{
		return x;
	}
	
	public int getYValue()
	{
		return y;
	}
	
	public int getXChanger()
	{
		return xchanger;
	}
	
	public int getYChanger()
	{
		return ychanger;
	}
	
	public void updateCount()
	{
		count++;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void setRandom()
	{
		random = true;
	}
	
	public boolean isRandom()
	{
		return random;
	}
	
	public void makeBlack()
	{
		isRed = false;
	}
	
	public void generateRandomPath()
	{
		xchanger = rand.ints(1, 1, 6).findFirst().getAsInt();
		ychanger = rand.ints(1, 1, 6).findFirst().getAsInt();
	}
	
	public void paint(Graphics g)
	{
		if (isRed)
		{
			g.setColor(Color.red);
		}
		else
		{
			g.setColor(Color.black);
		}
		
		g.fillOval(x, y, bulletWidth, bulletHeight);
		
		repaint();
	}
	
	public int getDistance()
	{
		int distance = (700 - y);
		return distance;
	}

}

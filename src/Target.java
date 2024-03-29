import javax.swing.*;
import java.awt.*;

public class Target extends JPanel
{

	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int width;
	private int height;
	private int velocity;
	
	public Target()
	{
		x = 900;
		y = 360;
		width = 100;
		height = 100;
		velocity = 1;
	}
	
	public int getTargetX()
	{
		return x;
	}
	
	public int getTargetY()
	{
		return y;
	}
	
	
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}



	public void paintTarget(Graphics g)
	{
		g.setColor(Color.darkGray);
		g.fillRect(x, y, width, height);
		g.setColor(Color.MAGENTA);
		g.fillOval(x + width/4, y + height/4, width/2, height/2);
	}

	public void moveTarget()
	{
		y += velocity;
	}
	
	public void changeDirection()
	{
		velocity = -velocity;
	}


}


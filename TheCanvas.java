
/**
 * TheCanvas
 *
 * main component of vector visualizer gui
 *
 */
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.LinkedList;
import java.util.HashMap;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;





public class TheCanvas extends JPanel implements MouseListener, KeyListener
{
	//keyboard controls
	public boolean w_pressed;
	public boolean s_pressed;
	public boolean a_pressed;
	public boolean d_pressed;
	public boolean up_pressed;
	public boolean dwn_pressed;
	public boolean right_pressed;
	public boolean left_pressed;
	
	ArrayList<Drawable> drawQueue;

    public TheCanvas (int sizeX, int sizeY ) {
		//init key listener
		w_pressed = false;
		s_pressed = false;
		a_pressed = false;
		d_pressed = false;
		up_pressed = false;
		dwn_pressed = false;
		right_pressed = false;
		left_pressed = false;
		
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		addKeyListener(this);
		
		
		//The following is another way to guarantee correct size.	 
		setPreferredSize(new Dimension(sizeX,sizeY));
		setBackground(Color.lightGray);
		
		//this component is a mouse listener
		addMouseListener(this);
		
		drawQueue = new ArrayList< Drawable >();

		
		
	}
	


	
    public void paintComponent(Graphics g)
    {
		super.paintComponent(g);  //without this no background color set.
		
		
	
		Graphics2D g2d = (Graphics2D)g; //cast so we can use JAVA2D.
		
		g2d.translate(this.getWidth()/2, this.getHeight()/2);
		
		//for (Drawable a : drawQueue){
		//for (int i = 0; i < drawQueue.size() ; i ++ ){
		Drawable a;
		while (!drawQueue.isEmpty()	) {
			a = drawQueue.remove(0);
			//if ( a != null ) {
            	a.draw(g2d, true);
            //}
        }

	
	
	}

	/* MOUSE LISTENER */
	 
	//generate random colored boxes
	public void mouseClicked(MouseEvent e)
	{
		
	}
	 
	public void mouseEntered(MouseEvent e)
	{
	}
 
	public void mouseExited(MouseEvent e)
	{
	} 
	public void mousePressed(MouseEvent e)
	{
	}	
	public void mouseReleased(MouseEvent e)
	{

	}	
	public void keyPressed(KeyEvent e)
	{
		if ( e.getKeyCode() == KeyEvent.VK_W ) {
			w_pressed = true;
		} else if ( e.getKeyCode() == KeyEvent.VK_S ) {
			s_pressed = true;
		} else if ( e.getKeyCode() == KeyEvent.VK_D ) {
			d_pressed = true;
		} else if ( e.getKeyCode() == KeyEvent.VK_A ) {
			a_pressed = true;
			
		}else if (e.getKeyCode() == KeyEvent.VK_E) {
		}else if (e.getKeyCode() == KeyEvent.VK_Q) {
			
		}else if (e.getKeyCode() == KeyEvent.VK_UP) {
			up_pressed = true;
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right_pressed = true;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left_pressed = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			dwn_pressed = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
		}
	}
	public void keyReleased( KeyEvent e )
	{
		if ( e.getKeyCode() == KeyEvent.VK_W) {
			w_pressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			s_pressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			d_pressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			a_pressed = false;
		}else if (e.getKeyCode() == KeyEvent.VK_UP) {
			up_pressed = false;
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			dwn_pressed = false;
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right_pressed = false;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left_pressed = false;
		}
	}
	
	public void keyTyped(KeyEvent e)
	{

	}

}// SimpleCanvas

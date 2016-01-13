
/**
 * BasicDraw.java
 *
 *
 * Template for beginning graphics programs.
 *
 */

import javax.swing.*;
import java.util.ArrayList;

public class BasicDraw
{
   
    //For this type of program, event handling determines the path of 
    //execution.  This main method "looks" like it sets up the frame
    //and then stops.
    
    public static TheCanvas myCanvas;
    

    public BasicDraw(int sizeX, int sizeY ) {
    
		myCanvas = new TheCanvas(sizeX, sizeY );
		JFrame myFrame = new JFrame();
		myFrame.setTitle("Basic Draw");
		myFrame.setSize(sizeX,sizeY);

		//Sets the window to close when upper right corner clicked.  
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Must use getContentPane() with JFrame.
		myFrame.add(myCanvas);
		//myFrame.pack(); //resizes to preferred size for components.
		myFrame.setResizable(true);
		myFrame.setVisible(true);
		
		

		
    }
    

} // BasicDraw



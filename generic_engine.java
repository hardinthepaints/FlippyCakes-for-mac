
import java.util.ArrayList;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

/**
 * 
 * the main engine of the game - compile and run this class to start
 * 
 *
 */


public class generic_engine
{
	//size of the window
	private static int sizeX = 1200;
	private static int sizeY = 800;
	
	/* the viewer */
	private static BasicDraw viewer;

	/* frames per second */
	private static final long CLOCKTICK = 1000 / 60;
	
	static GameEnv gameEnv;
	

	//Check key presses and do physics accordingly
    public static void thrust( FryingPan fryingPan ) {

		float[] forceCell= new float[] {0, 0, 0, 1};
		float[] forcePan = new float[] {0, 0, 0, 1};
		float panIcr = .01f;

		
		TheCanvas myCanvas = viewer.myCanvas;
			
			if (myCanvas.up_pressed) {
				forcePan[0] += panIcr;
			}
			if (myCanvas.dwn_pressed) {
				forcePan[0] -= panIcr;

			}
			if (myCanvas.left_pressed) {
				forcePan[1] -= panIcr;

			}
			if (myCanvas.right_pressed) {
				forcePan[1] += panIcr;
				//testCellCollide();

			}
		fryingPan.physicsPresence.applyRotationForce( forcePan );

		
    }    
	
	//main string, mostly inits the game
    public static void main(String[] args){

		viewer = new BasicDraw(sizeX, sizeY );
		gameEnv = new GameEnv( viewer.myCanvas.drawQueue );

		//game loop at 60 fps
		Timer game_loop = new Timer();
		game_loop.scheduleAtFixedRate(new game_loop(), 0, CLOCKTICK);
		
    }

    
    //game loop thread
    static class game_loop extends TimerTask {
    
		public void run() {
			thrust( gameEnv.fryingPan );
			gameEnv.update();
			viewer.myCanvas.repaint();

		}	
	}  
} 





import java.util.ArrayList;
import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ListIterator;



public class TextBox implements Drawable{

	ArrayList<String> lines;
	Color textColor;
	GraphicsEnvironment gEnv;
    Font [] allSystemFonts;
    Font curr_font;
    int x;
    int y;
	
	public TextBox(int _x, int _y){
		lines = new ArrayList<>();
		textColor = Color.WHITE;
	    gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		allSystemFonts = gEnv.getAllFonts();
		x = _x;
		y = _y;
		curr_font = new Font( getRandomFont().getFontName(), Font.PLAIN, 30 );
	}
	
	/* add a line of text to draw */
	public void addLine( String input ) {
		lines.add( input );
	}
	
	public void draw( Object obj, boolean outline){
		Graphics2D g2d = (Graphics2D) obj;
		addLine( "font name: " + curr_font.getFontName() );
		g2d.setPaint( textColor );
		g2d.setFont( curr_font );
		int X = x;
		int Y = y;
		//for ( String line : lines ){
		ListIterator<String> li = lines.listIterator();
		while( li.hasNext() ){
			g2d.drawString( li.next(), X, Y );
			Y += 20;
		}
		lines.clear();
	
	}
	
     /* returns a random font */
     public Font getRandomFont() {
          int numFonts = allSystemFonts.length;
          int random = (int)(Math.random()*numFonts); // look also at java.util.Random
          return allSystemFonts[random];
     }
     
    public void updateSpec( float[] normal, float[] light_src, float[] pov, float[] equationOfPlane ){}
    
    public float[] getBoundsInfo(){
    	return new float[]{ x, y };
    }



}
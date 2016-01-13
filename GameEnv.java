
import java.util.Collections;
import java.util.ArrayList;
//import java.awt.Color;

class GameEnv{

	boolean gravity_on = true;

	/* represent where the Eye is in relation to the center of cube w/ a vector */
	final float[] E = new float[]{0,0,700,1};

	/* light source */
	final float[] light_source = new float[]{0, -700, 200, 1};


	/* GameThread
	* has an arraylist of 'items', which are the items in the environment
	* must keep track of physics + graphics
	 */
	private ArrayList<Item> env = new ArrayList<Item>();

	/* store all the faces of the items in the environment in
	* 1 place so they can be sorted and drawn in order;
	 */
	private ArrayList<Region3D> faces = new ArrayList<>();

	/* visible items in the env */
	public FryingPan fryingPan;
	public CellularItem pancake;
	public TextBox textBox;
	Hologram hologram;
	
	/* ref to drawables which repaint() will use */
	ArrayList<Drawable> drawables;
	
	
	private void newGame(){
		/* pan */
		fryingPan = new FryingPan(50);
		env.add( fryingPan );
		
		/* pancake */
		pancake = new CellularItem();
		env.add(pancake);
		
		/* textBox */
		textBox = new TextBox( -300, -300 );
		
		/* hologram */
		hologram = new Hologram();
		env.add( hologram );
		hologram.setTarget( fryingPan.physicsPresence );
	
	}
	
	private void checkForGameOver(){
		Region3D face = pancake.faces.get(0);
		float[] point = face.curr_points.get(0);
		if ( VectorOps.distSquared( point, E ) > 10000000 ) {
			env.clear();
			newGame();
		}
		
	}
	

	public void update(){
		faces.clear();
		checkForGameOver();
		
	
			    //Cell cellA = pancake.cells.get(0);
            //thrust( cellA );
		/* make a comparator to sort faces of each object in environment */
		FaceComparator faceComparator = new FaceComparator(E);

		//FryingPan fryingPan = (FryingPan)env.get(0);
		hologram.updateScore( pancake.score );


		for (Item A : env){
			if (A instanceof CellularItem){
				CellularItem pancake = (CellularItem) A;

				//apply gravity
				if (gravity_on) {
					pancake.applyTranslationalForce( new float[] { 0, 0, -.5f, 1});
				}
				//cell.draw(c, true);
				pancake.isCollided(fryingPan , E);
			}
		}
		
		/* iterate thru env and draw items */
		for (Item a : env) {
			a.updateSelf(E, light_source);
			if ( a instanceof CellularItem ){
				for ( Region3D face : a.faces ){
					Colorizer.castShadow( face, fryingPan.bottom, E, light_source);
				}
			}
			faces.addAll( a.faces );
		}
		
		
		/* draw faces in order */
		/* NOTE this is the best so far we can do in ordering the faces
		 * there are better ways like gridding out the environment etc, but for
		 * now its the best and hopefully will look realistic
		  * */
		Collections.sort(faces, faceComparator);
		
		for ( Region3D face : faces ){
			drawables.add( face.region2D );
		}
		//viewer.myCanvas.drawQueue.addAll( drawables );
		
		//textBox.addLine( "Score: " + pancake.score );
		//drawables.add( textBox );
		
		//zero.updateSelf( ThreeDTransform.get_identity(), E, light_source );
		//Color temp =  Color.WHITE ;
		//zero.region2D.color = temp.getRGB() ;
		//drawables.add( zero.region2D );
		
	} 

	public GameEnv( ArrayList<Drawable> _drawables){
		drawables = _drawables;
		
		newGame();

		
		/* aggregate faces */
		//faces.addAll(env.get(0).getFaces());
		//faces.addAll(pancake.getFaces() );
		//faces.addAll( hologram.getFaces() );
		
		
		//System.out.println( "faces in pan: " + fryingPan.faces.size() );

		

	
	}

}
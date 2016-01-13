import java.util.ArrayList;

class Hologram extends Item{
	
	/* describe how the glyphs are sized and spaced */
	private static final float SPACESIZE = NumberGenerator.WIDTH/2;
	private static final float scale = 1;
	private static float r = 200 + NumberGenerator.HEIGHT + SPACESIZE;
	
	private boolean enforceSpacing = false;
	private int score = 0;
	
	private PhysicsPresence target = null;

	public Hologram(){
		super();
		initFaces();
		concatTransform( ThreeDTransform.get_translate(-r, "y") );
		rotationCenter = new float[]{0, 0, 0, 1};
	}
	
	public void updateScore( int _score ){
		if (_score == score){
		
		} else {
			enforceSpacing = true;
			score = _score;
		}
	
	}
	
	private void addFace( Region3D face ){
		if ( !( face instanceof Glyph ) ) throw new IllegalArgumentException( "Faces of hologram must be of type Glyph!" );
		face.parent = this;
		faces.add( face );
	
	
	}
	
	/* set the target physicspresence */
	public void setTarget( PhysicsPresence _target ){ target = _target; };
	
	@Override
	public void updateSelf(float[] pov, float[] light_src) {
		rotationCenter = new float[]{0, 0, 0, 1};

		if ( target != null ){
			this.physicsPresence.multiplyBy( 0 );
			this.physicsPresence.r_x = target.r_x;
			this.physicsPresence.r_y = target.r_y;
			this.physicsPresence.r_z = target.r_z;
	
		}
	
		super.updateSelf( pov, light_src );
		
		if (enforceSpacing) enforceSpacing();
    }
	
	private void enforceSpacing(){
		/* display glyphs and center justify */
		/* method: manipulate the original points so they are spaced,
		so the rest of the transformation can be applied */	
		
		faces.clear();
		
		/* go thru the digits in the score and add the correct glyph for each digit */
		String scoreString = score + "";
		char c;
		int digit;
		for ( int i = 0; i < scoreString.length(); i ++ ) {
			c = scoreString.charAt( i );
			digit = Integer.parseInt( c + "" );
			addFace( NumberGenerator.generateNumber( digit ) );
		
		}		
		
		float[][] translateUp = ThreeDTransform.get_translate(-r, "y") ;
		float[][] translateDown = ThreeDTransform.get_translate(r, "y") ;
		float[][] transformation;
		float[][] rotation;
		int count = 0;
		/* amount to change angle between letters */
		float angleSpace = ((NumberGenerator.WIDTH) + 2 * (SPACESIZE / 2) ) / r;
				
		for ( Region3D face : faces ){
			
			transformation = ThreeDTransform.get_identity();
			transformation = ThreeDTransform.multiply( transformation, translateDown );

			rotation = ThreeDTransform.get_rotate( angleSpace * count, "z" );
			transformation = ThreeDTransform.multiply( transformation, rotation );	
			transformation = ThreeDTransform.multiply( transformation, translateUp );
		

			
			for (float[] one_pt : face.orig_points){
				//float[] new_pt = one_pt;
				ThreeDTransform.transform(transformation, one_pt);
				//curr_points.add(new_pt);
			}
			
			count++;
		
		
		}
		
		enforceSpacing = false;
		

		
		
		

	}
	
	private void initFaces(){
		faces = new ArrayList< Region3D > ();
		Glyph curr = null;
		for (int i = 0; i < 10; i ++ ) {
			curr = NumberGenerator.generateNumber( i );
			if ( curr != null ) {
				addFace( curr );
			}
		
		}

		enforceSpacing();
		

	}

}
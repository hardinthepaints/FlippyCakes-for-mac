import java.util.ArrayList;

/**
* Generate glyphs which represent the numerals 0 thru 9
*/

class NumberGenerator{
	private static final float[] startPoint = new float[]{0,0,0,1};
	public static final float WIDTH = 20;
	public static final float HEIGHT = WIDTH * 1.618f;
	


	private static Glyph toGlyph( ArrayList< float[] > points ){
		return new Glyph( points, null, false );	
	}
	
	private static Glyph genZero(){
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		
		/* outer "circle" */
		points.add( start.clone() );
		start[0] += WIDTH;
		points.add( start.clone() );
		start[1] += HEIGHT;
		points.add( start.clone() );
		start[0] -= WIDTH;
		points.add( start.clone() );
		
		/* back to start */
		start[1] -= HEIGHT;
		points.add( start.clone() );
		
		start[0] += WIDTH / 3;
		start[1] += HEIGHT / 3;
		points.add( start.clone() );
		
		/* backwards around again */
		start[1] += HEIGHT / 3;
		points.add( start.clone() );
		start[0] += WIDTH / 3;
		points.add( start.clone() );
		start[1] -= HEIGHT / 3;
		points.add( start.clone() );
		start[0] -= WIDTH / 3;
		points.add( start.clone() );
		
		return toGlyph(points);
	}


	private static Glyph genOne(){
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		start[0] += WIDTH / 3;
		points.add( start.clone() );
		start[0] += WIDTH * .33;
		points.add( start.clone() );
		start[1] += HEIGHT * ( .66 );
		points.add( start.clone() );
		start[0] += WIDTH * .333;
		points.add( start.clone() );
		start[1] += HEIGHT / 3f;
		points.add( start.clone() );
		start[0] -= WIDTH;
		points.add( start.clone() );
		start[1] -= HEIGHT/3f;
		points.add( start.clone() );
		start[0] += WIDTH/3f;
		points.add( start.clone() );

		start[1] -= HEIGHT/3f;
		points.add( start.clone() );

		start[0] -= WIDTH/3f;


		
		//start[0] += WIDTH/3;
		points.add( start.clone() );
		
		return toGlyph(points);


	}
	
	private static Glyph genTwo(){
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		
		points.add( start.clone() );
		start[0] += WIDTH;
		start[1] += HEIGHT / 4f;
		points.add( start.clone() );
		start[1] += HEIGHT / 4f;
		points.add( start.clone() );
		start[0] -= WIDTH * ( .666 );
		start[1] += HEIGHT / 4f;
		points.add( start.clone() );
		start[0] += WIDTH * ( .666 );
		points.add( start.clone() );
		start[1] += HEIGHT / 4f;
		points.add( start.clone() );
		start[0] -= WIDTH;
		points.add( start.clone() );
		start[1] -= HEIGHT / 2f;
		points.add( start.clone() );
		start[0] += WIDTH * .666;
		points.add( start.clone() );
		
		return toGlyph(points);


	}

	
	private static Glyph genThree(){
	
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		points.add( start.clone() );
		start[0] += WIDTH;
		points.add( start.clone() );
		start[1] += HEIGHT;
		points.add( start.clone() );
		start[0] -= WIDTH;
		points.add( start.clone() );
		
		for ( int i = 0; i< 2; i ++ ) {
			start[1] -= HEIGHT * .25;
			start[0] += (WIDTH * (.666)) ;
			points.add( start.clone() );
		
			start[1] -= HEIGHT * .25;
			start[0] -= (WIDTH * (.666)) ;
			points.add( start.clone() );
		
		}

		return toGlyph(points);

	
	}
	
	private static Glyph genFour(){
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		points.add( start.clone() );
		start[0] += WIDTH / 3f;
		points.add( start.clone() );
		start[1] += HEIGHT / 3f;
		points.add( start.clone() );
		start[0] += WIDTH / 3f;
		points.add( start.clone() );
		start[1] -= HEIGHT / 3f;
		points.add( start.clone() );
		start[0] += WIDTH / 3f;
		points.add( start.clone() );
		start[1] += HEIGHT;
		points.add( start.clone() );
		start[0] -= WIDTH / 3f;
		points.add( start.clone() );
		start[1] -= HEIGHT / 3f;
		points.add( start.clone() );
		start[0] -= WIDTH * (2f/ 3f);
		points.add( start.clone() );
		
		return toGlyph(points);


	}
	
	private static Glyph genFive(){
	
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		points.add( start.clone() );
		start[0] += WIDTH * ( 2f / 3f );
		points.add( start.clone() );
		start[1] += HEIGHT / 5f;
		points.add( start.clone() );
		start[0] -= WIDTH / 3f ;
		points.add( start.clone() );
		start[1] += HEIGHT / 5f ;
		points.add( start.clone() );
		start[0] += WIDTH * ( 2f / 3f );
		points.add( start.clone() );
		start[1] += (3f/5f) * HEIGHT;
		points.add( start.clone() );
		start[0] -= WIDTH;
		points.add( start.clone() );
		start[1] -= HEIGHT / 5f;
		points.add( start.clone() );
		start[0] += WIDTH / 2;
		//start[1] -= HEIGHT / 5f;
		points.add( start.clone() );
		start[0] -= WIDTH / 2;
		start[1] -= HEIGHT * (2f/ 5f );
		points.add( start.clone() );

		
		return toGlyph(points);


	}
	
	private static Glyph genSix(){
	
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		//points.add( start.clone() );
		start[0] += WIDTH ;
		start[1] += HEIGHT / 3f ;
		points.add( start.clone() );
		start[1] += HEIGHT * (2f/3f) ;
		points.add( start.clone() );
		start[0] -= WIDTH ;
		points.add( start.clone() );
		start[1] -= HEIGHT ;
		points.add( start.clone() );
		start[0] += WIDTH/3f ;
		points.add( start.clone() );
		start[1] += HEIGHT/3f ;
		points.add( start.clone() );
		start[0] += WIDTH* (2f / 3f );
		points.add( start.clone() );
		
		start[0] -= WIDTH/3f ;
		start[1] += HEIGHT * (2f / 9f) ;
		points.add( start.clone() );
		start[0] -= WIDTH/3f ;
		points.add( start.clone() );
		start[1] += HEIGHT * (2f / 9f) ;
		points.add( start.clone() );
		start[0] += WIDTH/3f ;
		points.add( start.clone() );		
		start[1] -= HEIGHT * (2f / 9f) ;
		points.add( start.clone() );				
		
		return toGlyph(points);


	}
	private static Glyph genSeven(){
	
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		points.add( start.clone() );
		start[0] += WIDTH;
		points.add( start.clone() );
		start[1] += HEIGHT / 3f;
		points.add( start.clone() );
		start[1] += HEIGHT * (2f/ 3f);
		start[0] -= WIDTH * ( 1f / 3f );
		points.add( start.clone() );
		start[0] -= WIDTH * (2f / 3f );
		points.add( start.clone() );
		//start[1] -= HEIGHT / 3f;
		points.add( start.clone() );
		start[0] += WIDTH * ( 2f / 3f );
		start[1] -= HEIGHT * (2f/ 3f);
		points.add( start.clone() );
		start[0] -= WIDTH * ( 2f / 3f );
		points.add( start.clone() );
		return toGlyph(points);
	}
	private static Glyph genEight(){
	
		ArrayList<float[]> points = new ArrayList<>();
		float[] start = startPoint.clone();
		points.add( start.clone() );
		
		start[0] += WIDTH;
		points.add( start.clone() );
		start[1] += ( 3f / 7f ) * HEIGHT;
		points.add( start.clone() );
		start[0] -= WIDTH/5f;
		start[1] += ( 1f / 14f ) * HEIGHT;
		points.add( start.clone() );
		start[0] += WIDTH/5f;
		start[1] += ( 1f / 14f ) * HEIGHT;
		points.add( start.clone() );
		start[1] += ( 3f / 7f ) * HEIGHT;
		points.add( start.clone() );
		
		/* make inner 'circle ' */
		start[0] -= WIDTH/3f;
		start[1] -= HEIGHT/5f;
		points.add( start.clone() );
		start[1] -= HEIGHT /5f;
		points.add( start.clone() );
		start[0] -= WIDTH/3f;
		points.add( start.clone() );
		start[1] += HEIGHT / 5f;
		points.add( start.clone() );
		start[0] += WIDTH/3f;
		points.add( start.clone() );
		start[0] += WIDTH/3f;
		start[1] += HEIGHT/5f;
		points.add( start.clone() );
		
		/* back up other side */
		start[0] -= WIDTH;
		points.add( start.clone() );
		start[1] -= ( 3f / 7f ) * HEIGHT;
		points.add( start.clone() );
		start[0] += WIDTH/5f;
		start[1] -= ( 1f / 14f ) * HEIGHT;
		points.add( start.clone() );
		start[0] -= WIDTH/5f;
		start[1] -= ( 1f / 14f ) * HEIGHT;
		points.add( start.clone() );
		start[1] -= ( 3f / 7f ) * HEIGHT;
		points.add( start.clone() );
		
		/* make inner 'circle ' */
		start[0] += WIDTH/3f;
		start[1] += HEIGHT/5f;
		points.add( start.clone() );
		start[1] += HEIGHT /5f;
		points.add( start.clone() );
		start[0] += WIDTH/3f;
		points.add( start.clone() );
		start[1] -= HEIGHT / 5f;
		points.add( start.clone() );
		start[0] -= WIDTH/3f;
		points.add( start.clone() );
		start[0] -= WIDTH/3f;
		start[1] -= HEIGHT/5f;
		points.add( start.clone() );

		return toGlyph(points);


	}
	
	private static Glyph genNine(){
		
		Glyph upsideDownNine = genSix();
		float[][] transformation = ThreeDTransform.get_identity();
		float[][] rotate = ThreeDTransform.get_rotate( (float)Math.PI, "z" ) ;
		transformation = ThreeDTransform.multiply( transformation, rotate );
		float[][] translate = ThreeDTransform.get_translate(-WIDTH, -HEIGHT, 0 );
		transformation = ThreeDTransform.multiply( transformation, translate );
		
		for ( float[] point : upsideDownNine.orig_points ){
			ThreeDTransform.transform(transformation, point);

		}
		
		return upsideDownNine;

	}


	public static Glyph generateNumber( int x ){
		if ( (x > 9) || (x < 0) ) throw new IllegalArgumentException( "input must be int 0 - 9" );
		if ( x == 0 ){
			return genZero();
		} else if ( x == 1 ) {
			return genOne();
		} else if ( x == 2 ) {
			return genTwo();
			
		} else if ( x == 3 ) {
			return genThree();
		} else if ( x == 4 ) {
			return genFour();
		} else if ( x == 5 ) {
			return genFive();
		} else if ( x == 6 ) {
			return genSix();
		} else if ( x == 7 ) {
			return genSeven();
		} else if ( x == 8 ) {
			return genEight();
		} else if ( x == 9 ) {
			return genNine();
		} else {
			return genThree();
		}
		
// 		}else if ( x == 1 ){
// 			return genFour();
// 		
// 		}else if ( x == 1 ){
// 			return genOne();
// 		
// 		}else if ( x == 1 ){
// 			return genOne();
// 		
// 		}else if ( x == 1 ){
// 			return genOne();
// 		
// 		}else if ( x == 1 ){
// 			return genOne();
// 		
// 		}else if ( x == 1 ){
// 			return genOne();
// 		
// 		}
		
	}


}
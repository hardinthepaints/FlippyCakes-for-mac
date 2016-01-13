//package com.xanderfehsenfeld.flippycakes;

// import android.graphics.Bitmap;
// import android.graphics.Canvas;
// import android.graphics.Color;
// import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.RectF;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Color;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Xander on 11/10/15.
 *
 * Used to store an Android Path and a color
 */
public class Region2D implements Drawable{

	private final boolean specularOn;
    private final int SPECULAR_DENSITY =5;

    Path2D path;
    int color;
    int outlineColor;
    
    //Bitmap bitmap;
    public BufferedImage image;

    /* an arraylist to store 2d points representing intersections */
    ArrayList<float[]> intersections = new ArrayList<>();
    
    private ArrayList<Path2D> shadows = new ArrayList<Path2D>();

    //float[] intersectionPt = new float[]{1000, 0, 1};

    /* Constructor */
    public Region2D( boolean _specularOn ){
        path = new Path2D.Float();
        Color c = Color.WHITE;
        color = c.getRGB();
        c = Color.BLACK;
        outlineColor = c.getRGB();
        specularOn = _specularOn;
        //bitmap = Bitmap.createBitmap(5,5, Bitmap.Config.ARGB_8888);
        init_image();
        
    }
    
    private void init_image(){
		Rectangle2D bounds = path.getBounds2D();
		//top_left = new Point2D.Double(bounds.getX(), bounds.getY());
		image = new BufferedImage(Math.max(1, (int)bounds.getWidth()), Math.max(1, (int)bounds.getHeight()), BufferedImage.TYPE_INT_ARGB);
// 		for (int i = 0; i < image.getWidth(); i++ ) {
// 			for (int j = 0; j < image.getHeight(); j ++ ) {
// 				image.setRGB(i, j, clr);	
// 			}
// 		}

	}

    /* set the intersection point */
    public void addIntersection( float[] input ) {
        intersections.add( input );
    }

    /** setPath
     *       set the path and resize the bitmap
     * @param input the new path
     */
    public void setPath( Path2D input ){
        path = input;
        
        if (specularOn) {
			float[] info = getBoundsInfo();

			int width = (int)info[2];
			int height = (int)info[3];

			/* resize/color the bitmap */
			init_image();
			if ( width > 0 && height > 0 ) {
				int[] colors = new int[width * height];
				Arrays.fill(colors, color);
				//bitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.RGB_565);
				image.setRGB(0,0, width, height, colors, 0, width);
			} else {
				image = null;
			}
        }

    }
    
    public void addShadow( Path2D input ){
    	shadows.add( input );
    
    }

    
    private void drawShadows( Graphics2D g2d ) {
    	/* draw shadows, if any */
		if (!shadows.isEmpty()){
			g2d.setPaint( new Color( 0, 0, 0, 100 ) );
			for ( Path2D shadow : shadows ){
				g2d.fill( (Shape) shadow );
			}
			shadows.clear();
		}
    
    }
    /* draw itself */
    public void  draw(Object obj, boolean draw_outline){
    	Graphics2D g2d = (Graphics2D) obj;

		g2d.clip( path );

		if ( !specularOn ) {
			g2d.setPaint( new Color( color ) );
			g2d.fill( path );
		} else {
			float[] info = getBoundsInfo();
			g2d.drawImage( image, null, (int)info[0], (int)info[1]  );			
			
		}

				
		drawShadows( g2d );

		g2d.setClip(null);

		/* draw the outline */
		if ( draw_outline ){
			g2d.setPaint( new Color( outlineColor ) );
			g2d.draw( path );
		}
		



    }

    /** getBoundsInfo
     *
     * @return array float[] {x, y, width, height}
     */
    public float[] getBoundsInfo(){
        Rectangle2D bounds = (Rectangle2D.Float)path.getBounds2D();
		float height = (float)bounds.getHeight();
		float width = (float)bounds.getWidth();
		int topRightX = (int) (bounds.getX());
		int topRightY = (int) (bounds.getY());
		int deltaX = (int) (width + 10);
		int deltaY = (int) (height + 10);
        return new float[]  {topRightX, topRightY, width, height};
    }

    public void updateSpec( float[] normal, float[] light_src, float[] pov, float[] equationOfPlane ){
		if ( specularOn ) {
			 /* tools for drawing */
			//Paint paint = new Paint();

			/* get bounds */
			//float[] bounds = getBoundsInfo();
			int height = image.getHeight();
			int width = image.getWidth();
			int topRightX = image.getMinX();
			int topRightY = image.getMinY();
			//int deltaX = (int) (width + 10);
			//int deltaY = (int) (height + 10);

			/* reusable pointers */
			float[] pointA;
			float[] pointB;
			float[] intersection;
			int currColor;
			int drawWidth;
			int drawHeight;

			/* store pixel values */
			int[] pixels = new int[ SPECULAR_DENSITY * SPECULAR_DENSITY ];
			//int[] pixels = new int[ width * height ];
			int index = 0;


			/* calculate specular */
				for (int x = topRightX; x < topRightX + width; x += SPECULAR_DENSITY) {
					for (int y = topRightY; y < topRightY + height; y += SPECULAR_DENSITY) {
						//if (!c.quickReject(x, y, x + 1, y + 1, null)) {						

							/* go back to 3d */
							pointA = pov;
							pointB = new float[]{x, y, 0, 1};
							intersection = VectorOps.getIntersectionPoint(pointA, pointB, equationOfPlane, false);
							if (intersection != null) {
								//paint.setColor(Colorizer.calculateSpecular(normal, intersection, light_src, pov));
									currColor = Colorizer.calculateSpecular(normal, intersection, light_src, pov);

									//image.setRGB( x, y, );
									Arrays.fill( pixels, currColor ) ;
									//pixels[index] = Colorizer.calculateSpecular(normal, intersection, light_src, pov);
									//bitmap.setPixel(x - deltaX, y - deltaY, Colorizer.calculateSpecular(normal, intersection, light_src, pov))
									drawWidth = Math.min( SPECULAR_DENSITY, (topRightX + width) - x );
									drawHeight = Math.min( SPECULAR_DENSITY, (topRightY + height) - y );

									//if ( x + SPECULAR_DENSITY < topRightX + width && y + SPECULAR_DENSITY < topRightY + height ) {
									image.setRGB( x, y, drawWidth, drawHeight, pixels, 0, drawWidth );
									index ++;
									//}
							}
							//index ++;
						//}
					}
					//bitmap.setPixels(pixels, 0, 0, 0, 0, bitmap.getWidth(), bitmap.getHeight() );

				}
				//image.setRGB( 0, 0, width, height, pixels, 0, width );
				
				//System.out.println( index );




			//c.restore();
		}
    
    }

}

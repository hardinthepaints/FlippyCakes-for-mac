//package com.xanderfehsenfeld.flippycakes;

//import android.graphics.Color;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.geom.Path2D;

/**
 * Created by Xander on 11/18/15.
 *
 * Help to define the colors of things based on various shading techniques
 */
public class Colorizer {

    /** getAmbient
     *
     * @param normal vector representing the normal
     * @param lightSource vector of light source
     * @return int representing resultant grayscale
     */
    public static int getAmbient(float[] normal, float[] lightSource){
        //get a value between 0 and 255 to do shading
        int comp = (int)(255 * .5 * (VectorOps.cosAngBtwn(lightSource, normal) + 1));
        return new Color(comp, comp, comp).getRGB();
    }

    /** calculateSpecular
     *
     * @param normal of face
     * @param point 3d point in question
     * @return value representing a color
     */
    public static int calculateSpecular( float[] normal, float[] point, float[] light_source, float[] E) {

        //make normal vector a unit vector
        float normal_magnitude = VectorOps.get_magnitude(normal);
        for (int i = 0; i < 3; i ++ ) {
            normal[i] /= normal_magnitude;
        }

		/* Brightness from specular*/
		/* PERTINENT VECTORS*/
        //the vector from the light source to the pt
        float[] light_to_pt = {point[0] - light_source[0], point[1] - light_source[1],
                point[2] - light_source[2], 1 };
        //vector from current point to observer E
        float[] pt_to_eye = { E[0] - point[0], E[1] - point[1], E[2] - point[2], 1};


        float angle_light_to_normal = (float) (Math.acos(VectorOps.cosAngBtwn(light_to_pt, normal)) - Math.PI);

        for (int i = 0; i < 3; i ++ ) {
            normal[i] *=  2 * VectorOps.get_magnitude(light_to_pt) * Math.cos( angle_light_to_normal );
        }

        float[] reflected = new float[]{ light_to_pt[0] + normal[0],
                light_to_pt[1] + normal[1],
                light_to_pt[2] + normal[2],
                1 };

		/* PERTINENT ANGLES*/
        float cos_eye_reflected =
                VectorOps.cosAngBtwn(reflected, pt_to_eye);
        int comp = (int)(255 * (cos_eye_reflected + 1) / 2 );
        return new Color(comp, comp, comp).getRGB();
    }
    
    /* cast shadow of a onto b */
    public static void castShadow(  Region3D a, Region3D b, float[] E , float[] light_src ){
    	float[] intersection;
    	float[] plane;
    	Path2D shadow; 
    	ArrayList<float[]> shadowPoints = new ArrayList<>();
    	for ( float[] point: a.curr_points ) {
    		
    		intersection = VectorOps.getIntersectionPoint( light_src, point, b.calcEquationOfPlane(), false);
    		if (intersection != null && VectorOps.getIntersectionPoint( light_src, point, b.calcEquationOfPlane(), true) == null) {
    			//shadow = = new Path2D.Float();
    			shadowPoints.add( intersection );	
    		} 
    		
    	}
    	
    	if (!shadowPoints.isEmpty()){
    		shadow = Projector.projectFace( shadowPoints, E );
    		b.region2D.addShadow(shadow);
    	}
    
    }
}

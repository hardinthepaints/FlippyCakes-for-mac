//package com.xanderfehsenfeld.flippycakes;

import java.util.Comparator;

/**
 * Created by Xander on 11/18/15.
 *
 *	Descrip: A comparator to compare 3D objects so that objects that are farther away are 
 * 	considered Greater. That way when they are drawn in order they overlap correctly.
 */
class FaceComparator implements Comparator {

    float[] E;

    public FaceComparator(float[] _E){
        E = _E;
    }

    /**compare
     *  compare 2 Region3D objects based on their distance from the pov
     * @param lhs
     * @param rhs
     * @return
     */

    @Override
    public int compare(Object lhs, Object rhs) {
		if ( (lhs instanceof Triangle) && (rhs instanceof Circle)){
			return compare( (Triangle) lhs, (Circle) rhs );
		} else if ( (lhs instanceof Circle) && (rhs instanceof Triangle)){
			return -compare( (Triangle) rhs, (Circle) lhs );
		} else {
			return compare( (Region3D) lhs, (Region3D) rhs );
		}
    }
    
    /* for comparing a triangle to the circle that is the pan */
    private int compare( Triangle lhs, Circle rhs ){
    	float[] plane = rhs.calcEquationOfPlane(); 
    	//for ( float[] point : lhs.curr_points ){
    	Cell curr;
    	for (int i = 0; i < 3; i ++ ){
    		curr = lhs.tri[i];
			if (curr.contact) return 1;
    		float[] intersection = VectorOps.getIntersectionPoint( curr.rotationCenter, E, plane, true );
    		if (intersection == null) return 1;
    	}
    	return -1;
    }
    
    /* for comparing 2 triangles */
    private int compare( Region3D lhs, Region3D rhs ){
		float dist_lhs = getAvgDistance( lhs );
		float dist_rhs = getAvgDistance( rhs );


		if (dist_lhs > dist_rhs) {
			return 1;
		} else if (dist_lhs < dist_rhs) {
			return -1;
		} else {
			return 0;
		}
    }

    /** getAvgDistance
     *
     * @param obj the obj to calculate average distance
     * @return the average distance squared from E
     */
    private float getAvgDistance (Object obj){

		Region3D face = (Region3D) obj;

		int count = 0;
		float sumDistSq = 0;
		for (float[] point : face.getCurrPoints()) {
			count++;
			sumDistSq += Math.pow(point[0] - E[0], 2)
					+ Math.pow(point[1] - E[1], 2)
					+ Math.pow(point[2] - E[2], 2);

     	}
        return sumDistSq / count;
    }

}

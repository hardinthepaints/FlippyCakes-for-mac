//package com.xanderfehsenfeld.flippycakes;
// 
// import android.graphics.Color;
// import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by Xander on 11/19/15.
 */
public class Circle extends Region3D {


    /* things that make it a circle */
    public float[] center;
    public float r_squared;


    /** Constructor
     *
     * @param _orig_points points to describe region
     */
    public Circle(ArrayList<float[]> _orig_points, float[] _center, Item _parent){
        super(_orig_points, _parent);
        center = _center;
        r_squared = calculateR_squared( center );

    }
    
    public Circle(ArrayList<float[]> _orig_points, float[] _center, Item _parent, boolean specularOn){
        super(_orig_points, _parent, specularOn);
        center = _center;
        r_squared = calculateR_squared( center );

    }

    /* calculate the value of r_squared method only called once at construction */
    private float calculateR_squared(float[] center){
        float[] a = curr_points.get(0);
        float sum = 0;
        for ( int i = 0; i < 3; i ++){
            sum += Math.pow(  center[i] - a[i], 2  );
        }
        return sum;

    }

    /** isCollided
     *
     * @param other the other region to check if collided with
     * @return whether or not the regions are collided (intersecting)
     */
    @Override
    public boolean isCollided(Region3D other){
        /* STEPS
        * 1 -- determine if other object has a line segment passing thru the PLANE of this region
        *           (not the same as intersecting, the other object could be adjacent)
        *  2 -- if 1, determine if the other object is within the bounds of this region
        *
        *  if both are true then the other region is intersecting this one! (and vice versa)
        *
        *  IDEAS
        *  so we must break b down into reasonable line segments and then determine a way to 1 and 2 on line segments
        *  maybe this would be better implemented with a separate thread? there would be a concurrency issue
         */

        boolean isCollided = false;

        /* Step 1: convert this region to a plane */
            /* obtain 3 points in the plane */
        float[] normal = getNormal();
        float a = normal[0];
        float b = normal[1];
        float c = normal[2];
        float[] pointQ = curr_points.get(0);

        ArrayList<float[]> otherPoints = other.getCurrPoints();

        for (int i = 0; i < otherPoints.size(); i ++){
            float[] pointA = otherPoints.get(i);
            float[] pointB;

            /* wraparound */
            if ( i == (otherPoints.size() - 1) ) {
                pointB = otherPoints.get(0);
            } else {
                pointB = otherPoints.get(i + 1);
            }

            if(VectorOps.lineIntersectsPlane(pointA, pointB, normal, curr_points.get(0))) {
                /* store the intersection point */
                float[] intersect = getIntersectionPoint(pointA, pointB, calcEquationOfPlane());

                    if (intersect != null) {

                        /* check if intersect is too far from center of circle to be intercepting */
                        float dist = (float) (Math.pow( intersect[0] - center[0], 2 ) + Math.pow( intersect[1] - center[1], 2 ) + Math.pow( intersect[2] - center[2], 2 ));
                        if (!(dist > r_squared)) {
                            isCollided = true;
                            addIntersection(intersect);
                        }
                    }

            } else {
            }
        }
        return isCollided;
    }


    /** updateSelf
     *
     * @param pov vector representing the point of view
     */
    @Override
    public void updateSelf(  float[][] transformation, float[] pov, float[] light_src ) {

        /* transform the center */
        ThreeDTransform.transform(transformation, center);

        /* update the super */
        super.updateSelf(transformation, pov, light_src);


    }

}

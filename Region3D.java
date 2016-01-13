//package com.xanderfehsenfeld.flippycakes;

// import android.graphics.Canvas;
// import android.graphics.Color;
// import android.graphics.Path;

import java.util.ArrayList;
//import java.awt.Color;
//import java.awt.geom.Path2D;
//import java.awt.Graphics2D;


/**
 * Created by Xander on 11/19/15.
 */
public class Region3D {

    /** points to describe region3d
     * orig_points X transformation = curr_points if transformation has been applied
     */
    public final ArrayList<float[]> orig_points;
    public ArrayList<float[]> curr_points;

    /* keep track of location of last intersection */
    //private float[] intersectionPt3D = new float[] {1000, 0, 0, 1};
    //private float[] intersectionPt2D = new float[] {1000, 0, 1};

    /* Store 3d intersections */
    private ArrayList<float[]> intersections = new ArrayList<>();

    public Item parent;

    /** 2d representation of Region (cannot be derived without a POV */
    public Region2D region2D;

    /** Constructor
     *
     * @param _orig_points points to describe region
     */
    public Region3D(ArrayList<float[]> _orig_points, Item _parent, boolean _specularOn){
        orig_points = _orig_points;
        curr_points = (ArrayList<float[]>)orig_points.clone();
        region2D = new Region2D(_specularOn);
        parent = _parent;
        
    }
    public Region3D(ArrayList<float[]> _orig_points, Item _parent){
        orig_points = _orig_points;
        curr_points = (ArrayList<float[]>)orig_points.clone();
        region2D = new Region2D( false);
        parent = _parent;

    }


    public void updateSelf(float[] pov, float[] light_src ) {
        updateSelf( parent.getTransformation(), pov, light_src);
    }

    /** updateSelf
     *
     * @param pov vector representing the point of view
     */
    public void updateSelf(  float[][] transformation, float[] pov, float[] light_src ){

        //intersectionPt2D = Projector.projectPoint( intersectionPt3D, pov);
        //System.out.println(intersectionPt2D[0] + ", " + intersectionPt2D[1]);

        //System.out.println(intersectionPt2D[0]);
        //if (region2D != null) {

        for (float[] intersection : intersections){
            region2D.addIntersection( Projector.projectPoint( intersection, pov) );
        }
        intersections.clear();
        //region2D.setIntersectionPt(intersectionPt2D);
        //}

        /* step 1 : update 3D self */
        curr_points = new ArrayList<float[]>(orig_points.size());
        for (float[] one_pt : orig_points){
            float[] new_pt = one_pt.clone();
            ThreeDTransform.transform(transformation, new_pt);
            curr_points.add(new_pt);
        }

        /* step 2 : update 2D self */
        region2D.color = Colorizer.getAmbient(getNormal(), light_src);
        region2D.setPath( Projector.projectFace(curr_points, pov) );
        
        if (parent instanceof FryingPan) {
        	updateColor( light_src, pov );
        }

    }

    /* update the region2d bitmap to have correct specular */
    public void updateColor(float[] light_src, float[] pov){
        region2D.updateSpec(getNormal(), light_src, pov, calcEquationOfPlane());
    }


    /** isCollided
     *
     * @param other the other region to check if collided with
     * @return whether or not the regions are collided (intersecting)
     */
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

        /* the vector representing the normal */
        float[] normal = getNormal();

        ArrayList<float[]> otherPoints = other.getCurrPoints();

        for (int i = 0; i < otherPoints.size(); i ++) {
            float[] pointA = otherPoints.get(i);
            float[] pointB;

            /* wraparound */
            if (i == (otherPoints.size() - 1)) {
                pointB = otherPoints.get(0);
            } else {
                pointB = otherPoints.get(i + 1);
            }

            if (VectorOps.lineIntersectsPlane(pointA, pointB, normal, curr_points.get(0))) {
                /* store the intersection point */
                float[] intersect = getIntersectionPoint(pointA, pointB, calcEquationOfPlane());
                if (intersect != null) {
                    isCollided = true;
                    intersections.add(intersect);
                }
            }
        }
        return isCollided;
    }

    /* add an intersection point */
    public void addIntersection( float[] point ){
        if ( point != null ) intersections.add( point );
    }

    public ArrayList<float[]> getCurrPoints() {
        return curr_points;
    }

    public Region2D getRegion2D() {
        return region2D;
    }



    /** calcEquationOfPlane
     *
     * @return the equation of the plane in Ax + By + Cz = D form
     */
    public float[] calcEquationOfPlane(){
        /* convert this region to a plane */
        float[] normal = getNormal();
        float a = normal[0];
        float b = normal[1];
        float c = normal[2];
        float[] pointQ = curr_points.get(0);

        /* equation for plane is A(x - Qx) + B(y - Qy) + C(z - Qz) = 0
        * in this case A, B, C, and D are the indexes of equationOfPlane
        * */
        float[] equationOfPlane = new float[] { a, b, c, 0 };

        /* shift appropriately */
        equationOfPlane[3] += (equationOfPlane[0] * pointQ[0])
                + equationOfPlane[1] * pointQ[1]
                + equationOfPlane[2] * pointQ[2];
        return equationOfPlane;
    }
    /** lineIntersectsPlane
     *
     * @param pointA starting point of line
     * @param pointB ending point of line
     * @param equationOfPlane equation of place Ax + By + Cz = D where equationOfPlane = {A, B, C, D}
     * @return indicator of whether or not the line intersects the plane
     */
    public float[] getIntersectionPoint(float[] pointA, float[] pointB, float[] equationOfPlane){
        return VectorOps.getIntersectionPoint(pointA, pointB, equationOfPlane, true);
    }


    /** getNormal
     *
     * @return - the vector normal to the face
     */
    public float[] getNormal() {
        int x = 0;
        int y = 1;
        int z = 2;
        float[] point0 = curr_points.get(0);
        float[] point1 = curr_points.get(1);
        float[] point2 = curr_points.get(2);

        float[] vec_a = { point0[x] - point1[x], point0[y] - point1[y], point0[z] - point1[z], 1 };
        float[] vec_b = { point2[x] - point1[x], point2[y] - point1[y], point2[z] - point1[z], 1 };

        //the direction of the normal vector of the face
        float[] normal = VectorOps.crossProduct(vec_b, vec_a);

        if (normal[0] == 0 && normal[1] == 0 && normal[2] == 0){
            throw new IllegalArgumentException("Points must be noncollinear to calculate normal!");
        }

        return normal;

    }

    /* return the list of intersections */
    public ArrayList<float[]> getIntersections(){
        return intersections;
    }


    public void draw(Object obj, boolean outline){
        region2D.draw(obj,outline);

    }


}

//package com.xanderfehsenfeld.flippycakes;

import java.util.ArrayList;

/**
 * Created by Xander on 11/18/15.
 *
 * Helpful notes:
 *      A three-dimensional point is represented by float of size 4 - {x, y, z, 1}
 */
public class FryingPan extends Item {

	boolean hasHandle = true;
	boolean hasSides = false;

    public Region3D bottom;



    /** Constructor
     *@param _sides number of sides for the frying pan
     *@param _rotationCenter point to rotate pan around
     *               will lie in the center of the bottom of the pan
     */
    public FryingPan(int _sides, float[] _rotationCenter){
        initFaces(getPoints(_sides));
        rotationCenter = _rotationCenter;
        physicsPresence = new PhysicsPresence(1, this);
    }
    public FryingPan(int _sides){
        initFaces(getPoints(_sides));
        rotationCenter = new float[]{0,0,0,1};
        physicsPresence = new PhysicsPresence(1, this);
        //rotationCenter[1] += 300;
    }

    /** initFaces
     *
     * @param points the list of points in the pan
     *                       each element of the array is a face,
     *                       each float[] is point in the face
     */
    private void initFaces(ArrayList<ArrayList<float[]>> points){
        faces = new ArrayList<>();
        for (ArrayList<float[]> face_pts : points){
            if ( face_pts.size() > 4 ) {
                faces.add( new Circle( face_pts , DEFAULT_CENTER, this, true ) );

            } else if ( face_pts.size() == 4 ){
                faces.add( new Quadrilateral( face_pts, this ) );

            }

        }
        bottom = faces.get(0);
    }
    
    private void handleDown(){
    	
    	float[] ideal = new float[]{ 1, 0, 0, 1 };
    	float[] curr = VectorOps.midPoint( faces.get(1).curr_points.get(0),faces.get(1).curr_points.get(1) ) ;
    	curr[2] = 0;
    	
    	float cosAngBtwn = VectorOps.cosAngBtwn( ideal, curr );
    	if ( Math.abs( cosAngBtwn ) < .5f ){
    		physicsPresence.r_z += cosAngBtwn / 1000;
    	}
    }

    /** updateSelf
     *      applies transformation to all faces and the rotationCenter
     * @param pov point of view
     * @param light_src light source
     */
	@Override
	public void updateSelf(float[] pov, float[] light_src) {
		if (hasHandle) handleDown();
	   super.updateSelf( pov, light_src );
	   
	   

	}



    /** getBottom
     *
     * @param sides number of sides of bottom
     * @param center location of the center
     * @return Arraylist of ordered 3d points which make the bottom of the pan
     */
    private ArrayList<float[]> getBottom (int sides, float[] center, float r){
        final float deltaAngle = (float)(2 * Math.PI) / sides;
        float angle = 0;
        ArrayList<float[]> bottom = new ArrayList<float[]>();

        for (int i = 0; i < sides; i ++){
            float[] a_point = new float[]{(float)Math.cos(angle) * r + center[0],
                    (float)Math.sin(angle) * r + center[1],
                    center[2],
                    center[3] };
            bottom.add(a_point);
            angle += deltaAngle;
        }
        return bottom;
    }

    /** getSide
     *      get points representing one side
     * @param sides number of sides intended
     * @param center location of center
     * @param r radius of bottom
     * @param startingAngle the angle to start the side at
     * @return ArrayList of points representing the first side
     */
    private ArrayList<float[]> generateSide(int sides, float[] center, float r, float startingAngle) {
        final float h = 50;
        final float extension = 25;
        final float deltaAngle = (float)(2 * Math.PI) / sides;
        ArrayList<float[]> first_side = new ArrayList<float[]>();

        float[][] temp = new float[4][4];

        int index = 0;

        for (float i = 0; i < deltaAngle*2; i += deltaAngle){
            temp[index] = new float[]{
                    (float) (center[0] + Math.cos(i + startingAngle) * r),
                    (float) (center[1] + Math.sin(i + startingAngle) * r),
                    center[2],
                    center[3]};
            index ++;
            temp[index] = new float[]{
                    (float) (center[0] + Math.cos(i + startingAngle) * (r + extension)),
                    (float) (center[1] + Math.sin(i + startingAngle) * (r + extension)),
                    center[2] + h,
                    center[3]};
            index++;
        }
        /* ensure points are in clockwise order */
        first_side.add(temp[0]);
        first_side.add(temp[1]);
        first_side.add(temp[3]);
        first_side.add(temp[2]);
        return first_side;
    }

    /** getPoints
     *
     * @param sides the number of sides in the pan
     * @return all the faces which make up the pan
     */
    private ArrayList<ArrayList<float[]>> getPoints(int sides){

        /* vars to describe pan */
        final float deltaAngle = (float)(2 * Math.PI) / sides;
        final float r = 200;
        final float[] center = DEFAULT_CENTER;

        ArrayList<ArrayList<float[]>> points = new ArrayList<>();

        /* the bottom of the pan */
        points.add(getBottom(sides, center, r));

        /* add the sides here */
        if ( hasSides ) {
			for (int i = 0; i < sides; i ++){
				points.add(generateSide(sides, center, r, deltaAngle*i));
			}
        }
        if (hasHandle) points.add( generateHandle(r, center ) );
        return points;
    }
    
    private ArrayList<float[]> generateHandle( float r, float[] center ){
		final float handleWidth = 50;
		final float handleHeight = 400;    
    	
    	ArrayList<float[]> output = new ArrayList<float[]>();
    	
    	float[] edgePoint = center.clone();
    	edgePoint[1] += r;
    	edgePoint[0] -= handleWidth/2;
    	output.add( edgePoint.clone() );
    	edgePoint[0] += handleWidth;
    	output.add( edgePoint.clone() );
    	edgePoint[1] += handleHeight;
    	edgePoint[0] += handleWidth/2;
    	output.add( edgePoint.clone() );
    	edgePoint[0] -= 2 * handleWidth;
    	output.add( edgePoint.clone() );

    	
    	return output;


    	
    	
    	
    }
    
    




}

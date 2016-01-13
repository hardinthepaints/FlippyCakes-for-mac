//package com.xanderfehsenfeld.flippycakes;

//import android.graphics.Canvas;


import java.util.ArrayList;

/**
 * Created by Xander on 11/18/15.
 *
 * IDEAS
 *      I want to make the pancake a honeycomb pattern of spheres which exert forces of attraction
 *      on each other so they say close together, but each have individual collisions with with pan, so it can
 *      bend and flex in any way
 *
 */
public class Pancake extends Item {


    /** Constructor
     *@param _sides number of sides for the frying pan
     *@param _rotationCenter point to rotate pan around
     *               will lie in the center of the bottom of the pan
     *@param _at where the pancake will be made
     */
    public Pancake(int _sides, float[] _rotationCenter, float[] _at){
        initFaces(getPoints(_sides));
        rotationCenter = _rotationCenter;
        physicsPresence = new PhysicsPresence(1, this);

        /* translate the pancake to where it needs to go */
        concatTransform( ThreeDTransform.get_translate(_at[0], _at[1], _at[2]) );
    }
    public Pancake(int _sides){
        initFaces(getPoints(_sides));
        rotationCenter = new float[]{0,0,0,1};
        physicsPresence = new PhysicsPresence(1, this);
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
                faces.add( new Circle( face_pts , DEFAULT_CENTER, this) );

            } else if ( face_pts.size() == 4 ){
                faces.add( new Quadrilateral( face_pts , this) );

            }        }

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
        ArrayList<float[]> bottom = new ArrayList<>();

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



    /** getPoints
     *
     * @param sides the number of sides in the pan
     * @return all the faces which make up the pan
     */
    private ArrayList<ArrayList<float[]>> getPoints(int sides){

        /* vars to describe pan */
        final float deltaAngle = (float)(2 * Math.PI) / sides;
        final float r = 50;
        final float[] center = DEFAULT_CENTER;

        ArrayList<ArrayList<float[]>> points = new ArrayList<ArrayList<float[]>>();

        /* the bottom of the pan */
        points.add(getBottom(sides, center, r));

        return points;
    }

    /** Apply Transformations to faces */
    /** rotate
     *      rotates the item around its rotationCenter
     * @param theta amount in radians to rotate
     * @param axis axis to rotate about
     */
//    @Override
//    public void rotate(float theta, String axis) {
//        concatTransform(ThreeDTransform.get_translate(-rotationCenter[0], -rotationCenter[1], -rotationCenter[2]));
//        float[][] rotate = ThreeDTransform.get_rotate(theta, axis);
//        concatTransform(rotate);
//        concatTransform(ThreeDTransform.get_translate(rotationCenter[0], rotationCenter[1], rotationCenter[2]));
//
//    }
//
//    @Override
//    public void translate(float distance, String axis) {
//        float[][] translate = ThreeDTransform.get_translate(distance, axis);
//        concatTransform(translate);
//
//    }

    /** draw
     *
     * @param c the canvas to draw on
     * @param outline whether or not to draw an outline
     */
//    @Override
//    public void draw(Canvas c, boolean outline) {
//        for ( Region3D face : faces ){
//
//            Region2D region2D = face.getRegion2D();
//            region2D.draw( c, outline );
//        }
//
//    }

//    /** updateSelf
//     *      applies transformation to all faces and the rotationCenter
//     * @param pov point of view
//     * @param light_src light source
//     */
//    @Override
//    public void updateSelf(float[] pov, float[] light_src) {
//
//        physicsPresence.update();
//        for ( Region3D face : faces ){
//            face.updateSelf(transformation, pov, light_src);
//        }
//        ThreeDTransform.transform(transformation, rotationCenter);
//    }
}

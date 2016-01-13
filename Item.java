//package com.xanderfehsenfeld.flippycakes;

//import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Timer;
//import com.xanderfehsenfeld.flippycakes.*;
//import Region3D;
//import java.awt.Graphics2D;

/**
 * Represent an object in a game environment in terms of graphics and physics
 */
public class Item{



    /* the real default center of the bottom of pan */
    public final float[] DEFAULT_CENTER = new float[]{ 0, 0, 0, 1 };

    public ArrayList<Region3D> faces;
    //private ArrayList<float[]> aggregatePoints = new ArrayList<float[]>();
    private float[][] transformation = ThreeDTransform.get_identity();
    public float[] rotationCenter;
    public PhysicsPresence physicsPresence;

    //public Region3D bottom;


    /** Constructor
     *
     */
    public Item(){
        physicsPresence = new PhysicsPresence(1, this);
    }

    /** concatTransform
     *      multiplies the current transformation by a new transformation and the
     *      result is reassigned
     *
     *      NOTE: not sure at this point if the order is correct but if it looks
     *          wacky I should try switching the order
     * @param a
     */
    public void concatTransform(float[][] a){
        transformation = ThreeDTransform.multiply(a, transformation);
    }

    public ArrayList<Region3D> getFaces() {
        return faces;
    }

    public PhysicsPresence getPhysicsPresence() {
        return physicsPresence;
    }

    /** Apply Transformations to faces */
    /** rotate
     *      rotates the item around its rotationCenter
     * @param theta amount in radians to rotate
     * @param axis axis to rotate about
     */
    public void rotate(float theta, String axis) {
        concatTransform(ThreeDTransform.get_translate(-rotationCenter[0], -rotationCenter[1], -rotationCenter[2]));
        float[][] rotate = ThreeDTransform.get_rotate(theta, axis);
        concatTransform(rotate);
        concatTransform(ThreeDTransform.get_translate(rotationCenter[0], rotationCenter[1], rotationCenter[2]));

    }

    public void translate(float distance, String axis) {
        float[][] translate = ThreeDTransform.get_translate(distance, axis);
        concatTransform(translate);

    }


    /** draw
     *
     * @param c the canvas to draw on
     * @param outline whether or not to draw an outline
     *
     *   this is never used because the environment would rather sort all the
     *                3d regions nearest first and draw them, instead of
     *                drawing each item
     */
    public void draw(Object obj, boolean outline) {
        for ( Region3D face : faces ){

                face.draw(obj, outline);

        }

    }

    /** updateSelf
     *      applies transformation to all faces and the rotationCenter
     * @param pov point of view
     * @param light_src light source
     */
    public void updateSelf(float[] pov, float[] light_src) {

        physicsPresence.update();
        for ( Region3D face : faces ){
            face.updateSelf(transformation, pov, light_src);
        }
        ThreeDTransform.transform(transformation, rotationCenter);
    }

    /** isCollided
     *      decided if two items are collided
     * @param otherItem the other item in question
     * @return whether or not they are collided
     */
//     public boolean isCollided( Item otherItem ){
//         /* check all face pairings for a collision
//          */
//         for (Region3D face : faces){
//             for (Region3D otherFace : otherItem.faces){
//                 if ( face.isCollided( otherFace ) ) return true;
//             }
//         }
//         return false;
//     }

    public float[][] getTransformation(){return transformation;}


}

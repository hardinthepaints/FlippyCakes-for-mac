//package com.xanderfehsenfeld.flippycakes;

import java.util.ArrayList;

/**
 * Created by Xander on 11/18/15.
 *
 * store physics and easily apply forces
 */
public class PhysicsPresence {


	/* NEWTONIAN COLLISION EQUATIONS */
	/* get a final velocity after an inelastic collision */
	public static float inelasticCollision( float v1i, float v2i, float m1, float m2 ){
		/* equation for final velocity in an inelastic collision ( from wikipedia )
		*	v1f = ( m2*( v2i - v1i ) + m1*v1 + m2 * v2 ) / ( m1 + m2 )
		*/ 
		return ( m2*( v2i - v1i ) + m1*v1i + m2 * v2i ) / ( m1 + m2 );
	}

	/* get the final velocities in an inelastic collision */
	public static float[] inelasticVelocities( float v1i, float v2i, float m1, float m2 ) {
		
		/* store the velocities { v1f, v2f } */
		float[] velocities = new float[2];
		velocities[0] = inelasticCollision( v1i, v2i, m1, m2 );
		velocities[1] = inelasticCollision( v2i, v1i, m2, m1 );
		
		return velocities;
	
	}
	
	/* get a final velocity after an elastic collision */
	public static float elasticCollision( float v1i, float v2i, float m1, float m2 ){
		/* equation for final velocity in an elastic collision ( from wikipedia )
		*	v1f = (v1i * ( m1 - m2 ) + 2 * m2 * v2i) / (m1 + m2)
		*/ 
		return (v1i * ( m1 - m2 ) + 2 * m2 * v2i) / (m1 + m2);
	}
	
	/* get the final velocities in an inelastic collision */
	public static float[] elasticVelocities( float v1i, float v2i, float m1, float m2 ) {
		
		/* store the velocities { v1f, v2f } */
		float[] velocities = new float[2];
		velocities[0] = elasticCollision( v1i, v2i, m1, m2 );
		velocities[1] = elasticCollision( v2i, v1i, m2, m1 );
		
		return velocities;
	
	}

    /* constrain how much the frying pan can rotate */
    private final float maxAngX = (float) (Math.PI / 8);
    private final float maxAngY = (float) (Math.PI / 8);

    public ArrayList<float[]> translationVectors;

    /* angles */
    public float angX = 0;
    public float angY = 0;
    public float angZ = 0;

    /* translational velocities */
    public float v_x = 0f;
    public float v_y = 0f;
    public float v_z = 0f;

    /* rotational velocities */
    public float r_x = 0f;
    public float r_y = 0f;
    public float r_z = 0f;

    public float m = 0;
    

    /* store all vars in an array */
    private Item target;


    /** Constructor
     *
     * @param _mass mass of the object
     * @param _target the item to which this PhysicsPresence belongs
     */
    public PhysicsPresence(float _mass, Item _target){
        m = _mass;
        target = _target;
        translationVectors = new ArrayList<>();
    }
    
    public float[] getVelocityVector(){
    	return new float[] {v_x, v_y, v_z, 1};
    }

    /** multiplyBy
     *
     * @param val the value to multiply all velocities by;
     */
    public void multiplyBy(float val){
        v_x *= val;
        v_y *= val;
        v_z *= val;
        r_x *= val;
        r_y *= val;
        r_z *= val;
    }

    /** apply a force
     *
     * @param vector the amount of force to apply
     */
    public void applyTranslationForce( float[] vector ){
        if (!VectorOps.containsNaN(vector)) {
            //translationVectors.add(vector);
           v_x += vector[0] / m;
           v_y += vector[1] / m;
           v_z += vector[2] / m;
        }
    }
    public void applyRotationForce( float[] vector ){
        r_x += vector[0] / m;
        r_y += vector[1] / m;
        r_z += vector[2] / m;
    }



    private void constrainRotation( ){
            if (angX + r_x > maxAngX) {
                r_x = maxAngX - angX;
            } else if (angX + r_x < -maxAngX) {
                r_x = -maxAngX - angX;
            }

            if (angY + r_y > maxAngY) {
                r_y = maxAngY - angY;
            } else if (angY + r_y < -maxAngY) {
                r_y = -maxAngY - angY;
            }

    }
    /** up
     * advances the physics of the item
     */
    public void update(){
        for (float[] vector : translationVectors){
            v_x += vector[0] / m;
            v_y += vector[1] / m;
            v_z += vector[2] / m;
        }
        translationVectors.clear();
        target.concatTransform(ThreeDTransform.get_translate( v_x, v_y, v_z ));

        /* constrain and apply rotation */
        constrainRotation();
        float[] rotationCenter = target.rotationCenter;
        target.concatTransform( ThreeDTransform.get_translate( -rotationCenter[0], -rotationCenter[1], -rotationCenter[2] ) );
        target.concatTransform(ThreeDTransform.get_rotate(r_x, r_y, r_z));
        target.concatTransform( ThreeDTransform.get_translate( rotationCenter[0], rotationCenter[1], rotationCenter[2] ) );
        angX += r_x;
        angY += r_y;
        angZ += r_z;

        multiplyBy( .95f );

    }

    /** toString
     *
     */
    public String toString(){
        return "" + v_x + ", " + v_y + ", " + v_z + ", " + r_x + ", " + r_y + ", " + r_z + ", " + m;
    }




}

//package com.xanderfehsenfeld.flippycakes;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.Shape;
//import java.awt.geom.Ellipse2D;
/**
 * Created by Xander on 12/1/15.
 */
public class Cell extends Item {

    //int color = Color.BLUE.getRGB();

    /* whether or not to print comments */
    public boolean verbose = false;

    /* spring constant */
    public static final float k = .3f;

    /* torsion spring constant */
    public static final float tk = .2f;
    
    /* message to be printed near circle */
    public String s = "";

    /* list of neighboring cells EXTERNALLY ENFORCED */
    ArrayList<Cell> neighbors;
    ArrayList<float[]> vectorsToDraw;

    /* list of cell a where there is exactly one cell between this cell and a
    *       NOTE this is externally enforced (no init )
    * */
    //ArrayList<Cell> neighborsOnceRemoved;
    HashMap<Cell, ArrayList<Cell>> neighborsOnceRemoved;


    /* the diameter of the cell for drawing */
    float r;


    /* whether or not the cell is in contact with a surface */
    boolean contact = false;

    /* store the most recent x and y  and r vals representing the cell's 2d presence */
    float[] circle = new float[]{ 0, 0, 0};

    /* location of circle original center */
    float[] orig = { 0, 0, 0, 1 };


    public Cell (float _r, float[] center){
        rotationCenter = center.clone();
        orig = center.clone();
        r = _r;

    }
    
    public float[] getRotationCenter(){
    	float[] output = orig.clone();
    	ThreeDTransform.transform(getTransformation(), output);
    	return output;
    	
    }

    /** cosAngleBtwn
     *      calculate the cos of angle made by 3 cells
     * @param a
     * @param b
     * @param c
     * @return the cos of angle made by vectors ba and bc
     */
    public static float cosAngleBtwn(Cell a, Cell b, Cell c){
        float[] vectorBA = VectorOps.getVector( b.rotationCenter, a.rotationCenter );
        float[] vectorBC = VectorOps.getVector( b.rotationCenter, c.rotationCenter );
        return VectorOps.cosAngBtwn( vectorBA, vectorBC);
    }

    public void addNeighbor( Cell neighbor ){
        if ( neighbors == null) {
            neighbors = new ArrayList<Cell>();
        }
        neighbors.add( neighbor );
    }

    /* apply force as if neighbors are connected by springs
    * PROBABLY WONT USE BUT GOOD TO HAVE
    * */
    public void applySpringForce( Cell neighbor ){
        float dist = VectorOps.dist(rotationCenter, neighbor.rotationCenter);
        //System.out.println("dist: " + dist);
        float equilibriumDist = 2 * r;
        float x = dist - equilibriumDist;

        /* get the unit vector which points from this cell to the neighbor cell */
        float[] forceVector = VectorOps.getVector(rotationCenter, neighbor.rotationCenter);
        forceVector = VectorOps.getUnitVector( forceVector );

        /* force of a spring = -kx */
        for (int i = 0; i < 3; i ++){
            forceVector[i] *= (1)* x * k;
            //forceVector[i] = Math.round(forceVector[i]);
            //forceVector[i] = 0;
        }

        //System.out.println( "force vector: " + forceVector[0] + ", " + forceVector[1] + ", " + forceVector[2]);

        /* apply the force */
        physicsPresence.applyTranslationForce(forceVector);
        //System.out.println( physicsPresence.toString() );

    }
    

    private void applyTorsion( Cell b ){
        Cell a = this;
        /* let this cell represent A, the neighbor represent B, and the neighbor once removed C */

        for (Cell c : neighborsOnceRemoved.get(b)){
            float dist = VectorOps.dist(rotationCenter, c.rotationCenter);
            //System.out.println("dist: " + dist);
            float equilibriumDist = 4 * r;
            float x = dist - equilibriumDist;

        /* get the unit vector which points from this cell to the neighbor cell */
            float[] forceVector = VectorOps.getVector(rotationCenter, c.rotationCenter);
            forceVector = VectorOps.getUnitVector( forceVector );

        /* force of a spring = -kx */
            for (int i = 0; i < 3; i ++){
                forceVector[i] *= (1)* x * tk;
                //forceVector[i] = Math.round(forceVector[i]);
                //forceVector[i] = 0;
            }

            //System.out.println( "force vector: " + forceVector[0] + ", " + forceVector[1] + ", " + forceVector[2]);

        /* apply the force */
            physicsPresence.applyTranslationForce(forceVector);


        }
    }


    /**applyTether
     *      exert a force on this cell, as if it were colliding inelastic with the other cell
     *      however, it is as if the two cells are connected by a static bar
     * @param neighbor
     */
    public void applyTether( Cell neighbor ) {
		float[] targetDestination = this.rotationCenter.clone();
        float[] vector = VectorOps.getVector( this.rotationCenter, neighbor.rotationCenter );
		vector = VectorOps.getUnitVector( vector );
 		for (int i = 0;  i < 3; i ++ ){
 			vector[i] *= 2*r ;
 			targetDestination[i] += vector[i];
 		}
 		float[] otherVector = VectorOps.getVector( neighbor.rotationCenter, targetDestination );
 		float[][] translate = ThreeDTransform.get_translate( otherVector[0], otherVector[1], otherVector[2] );
 		ThreeDTransform.transform( translate, neighbor.rotationCenter ); 	
 		neighbor.concatTransform( translate );	
 		 		
 	} 
 		
 		


	/**sameBond
	*	decide if two bonds are the same
	*/
// 	public boolean sameBond(Cell[] a, Cell[] b){
// 		if ( a[0] == b[0] && a[1] == b[1] ) {
// 			return true;
// 		} else if ( a[0] == b[1] && a[1] == b[0] ) {
// 			return true;
// 		} else {
// 			return false;
// 		}
// 	
// 	}

    /** applyNeighbors
     * Apply the spring force of this cell's neighbors
     */
//     public void applyNeighbors( ArrayList<Cell[]> alreadySeen ){
//         if ( neighbors != null) {
//             for (int i = 0; i < neighbors.size(); i ++){
//             	//applyTether( neighbors.get(i) );
//             	Cell[] orderA = new Cell[]{ this, neighbors.get(i) };
//             	Cell[] orderB = new Cell[]{ neighbors.get(i), this };
//             	boolean same = false;
//             	for ( Cell[] bond : alreadySeen ){
//             		if (sameBond(bond, orderA)) {
//             			same = true;
//             		}
//             	}
//             	
// 
//             	
//             	//if ( !( alreadySeen.contains( orderA ) ) && !( alreadySeen.contains( orderB ) ) ) {
//             	if (!same) {
//                 	applyTether( neighbors.get(i) );
//                 	alreadySeen.add( orderA );
//                 	//alreadySeen.add( orderB );
//                 	System.out.println( alreadySeen.size() );
// 
//                 	neighbors.get(i).applyNeighbors( alreadySeen );
//                 } else {
//                 	//System.out.println(" caught! ");
//                 }
//             }
//         }
//     }

    /** distTo
     *      get the distance between this cell and another
     * @param otherCell the other cell to find the distance to
     * @return the distance
     */
    public float distTo( Cell otherCell ){
        return VectorOps.dist( this.rotationCenter, otherCell.rotationCenter );
    }
    
    public void addVector( float[] vector ){
    	if ( vectorsToDraw == null ){
    		vectorsToDraw = new ArrayList<float[]>();
    	}
    	vectorsToDraw.add( vector );
    
    }

    public void setString( String input ){ s = input; };

    /* need to override draw */
    @Override
    public void draw(Object obj, boolean outline) {
//     	throw new IllegalStateException( "Cells cannot draw ");
// //         Paint paint = new Paint();
//            g2d.setPaint(new Color( color ) );
//            Ellipse2D.Float circ = new Ellipse2D.Float(circle[0], circle[1], circle[2] * 2, circle[2] * 2);
//            g2d.fill( (Shape) circ ) ;
// //         paint.setStyle(Paint.Style.FILL_AND_STROKE);
// //         c.drawCircle(circle[0], circle[1], circle[2], paint);
//         //if (verbose) {
//             System.out.println(rotationCenter[0] + ", " + rotationCenter[1] + ", " + rotationCenter[2]);
// //             if (new Float(rotationCenter[0]).isNaN()){
// //                 System.out.println("Caught NaN!");
// //             }
        //}
        
        /* draw vectors */
//         g2d.setPaint( Color.MAGENTA );
//         float[] twoDendpoint;
//         //float[] twoDstart;
//         if (vectorsToDraw != null ) {
// 			for ( float[] vector : vectorsToDraw ){
// 				twoDendpoint = new float[]{ circle[0] + vector[0], circle[1] + vector[1] };
// 				g2d.setPaint( Color.MAGENTA );
// 				//twoDstart = projector.projectPoint( rotationCenter );
// 				g2d.drawLine( (int) circle[0], (int) circle[1], (int) twoDendpoint[0], (int) twoDendpoint[1] );
// 				g2d.setPaint( Color.RED );
// 				g2d.drawOval( (int) circle[0] - 3, (int) circle[1] - 3, 5, 5 );
// 				g2d.setPaint( Color.GREEN );
// 				g2d.drawOval( (int) twoDendpoint[0] -3 , (int) twoDendpoint[1] - 3, 5, 5 );
// 				//g2d.setPaint( Color.ORANGE);
// 
// 				//g2d.drawString( VectorOps.arrayToString( rotationCenter), (int) circle[0] - 3, (int) circle[1] - 3 ) ;
// 
// 
// 			}
// 			vectorsToDraw.clear();
//         }
//         g2d.setPaint( Color.ORANGE);
//         /* draw String */
// 		g2d.drawString( s, (int) circle[0] + r, (int) circle[1] + r  );

    }

    /** updateSelf
     *      applies transformation to the rotationCenter
     * @param pov point of view
     * @param light_src light source
     */
    @Override
    public void updateSelf(float[] pov, float[] light_src) {

        physicsPresence.update();

        rotationCenter = orig.clone();
        ThreeDTransform.transform(getTransformation(), rotationCenter);


        /* get projected location */
        float[] twoD = Projector.projectPoint(rotationCenter, pov);

        /* get the 2d r value */
        float[] projectedR = Projector.projectPoint(new float[]{r, 0, rotationCenter[2], 1}, pov);

        /* store the values */
        circle = new float[]{ twoD[0], twoD[1],   projectedR[0]};
        
        if (vectorsToDraw != null) {
			for ( float[] vector : vectorsToDraw ){
				vector = Projector.projectPoint( vector, pov );   
			}
        }


    }

    /** doCollision
     *
     * @param otherFace the face the cell is collided with
     * @param otherNormal the normal of the face
     *
     *  NOTE should only be called if there is a collision
     *
     */
    private void doCollision( Region3D otherFace, float[] otherNormal ){
        /* freeze physics presence */
        if (!contact) {
            //physicsPresence = this.getPhysicsPresence();
            //physicsPresence.multiplyBy(0);
            contact = true;
        }

        float[] pointB = new float[] {
                rotationCenter[0] + otherNormal[0],
                rotationCenter[1] + otherNormal[1],
                rotationCenter[2] + otherNormal[2],
                1};
        float[] destination = VectorOps.getIntersectionPoint(rotationCenter, pointB, otherFace.calcEquationOfPlane(), false);

        if ( destination != null ) {

            float[] vector = new float[] {destination[0] - rotationCenter[0], destination[1] - rotationCenter[1], destination[2] - rotationCenter[2], 1};
                /* correct the collision */
            float[][] translation = ThreeDTransform.get_translate(
                    vector[0], vector[1], vector[2]);
            //concatTransform(translation);
             /* tone it */
//             for (int i = 0; i < 3; i ++){
//                 vector[i] *= .5;
//             }
            physicsPresence.applyTranslationForce( vector );

        }

        float[] gravity = new float[]{0, 0, -1, 1};
        float normalMag = VectorOps.get_magnitude(otherNormal);
        for (int i = 0; i < otherNormal.length; i++){
            otherNormal[i] /= normalMag;
            gravity[i] += otherNormal[i];
            gravity[i] *= 5;
        }
        //physicsPresence.applyTranslationForce(gravity);




    }

    /** isCollided
     *      decided if two items are collided
     * @param otherItem the other item in question
     * @return whether or not they are collided
     */
    public boolean isCollided( Item otherItem, float[] pov ){
        /* check all face pairings for a collision
         */
        boolean isCollided = false;
        for (Region3D otherFace : otherItem.faces){
            if ( !isCollided ) {
                isCollided = isCollided(otherFace, pov);
            } else {
                isCollided(otherFace, pov);
            }
        }
        contact = isCollided;
        return isCollided;
    }

    public boolean isCollided( Region3D otherFace, float[] pov ){
        //float[] plane = otherFace.calcEquationOfPlane();
        float[] pointQ = otherFace.curr_points.get(0);
        float[] otherNormal = otherFace.getNormal();
        boolean inBounds = false;

        boolean onFarSide = VectorOps.lineIntersectsPlane(pov, rotationCenter, otherNormal, pointQ);

        FryingPan fryPan = (FryingPan) otherFace.parent;
        float[] otherCenter = ((Circle)fryPan.bottom).center;
        float[] outerPoint = otherFace.curr_points.get(1);
        float maxDistSquared = VectorOps.distSquared(outerPoint, otherCenter);
        float distSquared = VectorOps.distSquared(rotationCenter, otherCenter);
        if (distSquared < maxDistSquared) {



            /* if cell is on far side of pan */
            if (onFarSide) {
                if (otherFace instanceof Circle) {
                    //otherCenter = ((Circle) otherFace).center;
                    //float distSquared = VectorOps.distSquared(otherCenter, rotationCenter);
                    if (distSquared < ((Circle) otherFace).r_squared + 20) {
                        inBounds = true;
                    }
                } else if (otherFace instanceof Quadrilateral) {
                    inBounds = true;
                }
                if (inBounds) {
                    doCollision(otherFace, otherNormal);
                    return true;
                }
            }
        }
        return false;
    }
}

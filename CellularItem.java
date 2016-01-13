//package com.xanderfehsenfeld.flippycakes;

//import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
//import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.Collections;
//import java.awt.Color;
import java.util.Arrays;
import java.util.ListIterator;


/**
 * Created by Xander on 12/2/15.
 *      A class to represent an object made up of individual 'cell' objects, which all react to their own physics but
 *      may in some way be connected
 * 		The connection part is executed in 'applyforce' in class cell
 */
public class CellularItem extends Item {

	/* keep track of how much the item has turned */
	int score = 0;
	float angX = 0;
	float angY = 0;
	float[] lastNormal;
	float[] scoreVector = new float[] { 0, 0, -1, 0 };

    /* store all the cells in the cellular item */
    public ArrayList<Cell> cells;
    //private ArrayList<Cell> top;
    
    private ArrayList<Cell[]> triangles;
    private HashMap<Cell, Cell> torsionMapLong;
    private HashMap<Cell, Cell> torsionMapShort;
    private HashMap<Cell, ArrayList<CellDist>> springMap;
    
    private final boolean hasThickness = false;
    
    private final int sideLength = 3;
    private final float r = 20;
    
    
    public Level[] levels;
    public Level levelZero;
    //public Level levelOne;

    public CellularItem(){
    
        levelZero = new Level();
        //levelOne = new Level();
    	levels = new Level[] { levelZero };
    	cells = levelZero.mCells;
    	triangles = levelZero.mTriangles;
        initCells();
        physicsPresence = null;

    }
    
    private void initFaces( Level level ) {
    	faces = new ArrayList<Region3D>();
    	for ( Cell[] tri : level.mTriangles ){
    	
    		faces.add( new Triangle( tri, this, 1 ) );
    		if (hasThickness) {
    			faces.add( new Triangle( tri, this, -1 ) );
    		}
    	
    	}
    
    }

    private Cell addCell(Cell cell){
        for ( Cell a : cells ) {
        	if (a.distTo(cell) < 10 ){
        		System.out.println( "found repeat cell " );
        		return a;
        	}
        }
        
        cells.add( cell );
        return cell;
    }
    
    private Cell[] addTriangle(Cell[] tri, Level level){

        for ( Cell[] a : level.mTriangles ) {
			//Cell[] aClone = a.clone();
			int score = 0;
			for (int i = 0; i < 3; i ++ ){
				for ( int j = 0; j < 3; j ++ ){
					if ( (tri[i].s) .equals ( a[j].s  ) ) {
						score++;
						//aClone[j] = null;
					} else if ( tri[i] == tri[j] && i != j ){
						throw new IllegalArgumentException( "triangle must contain 3 different cells! input: " 
										+ tri[0].s + ", " + tri[1].s + ", " + tri[2].s);
					}
				}	
			}
			if (score >= 3){
				throw new IllegalArgumentException( "triangle must be unique! input: " 
										+ tri[0].s + ", " + tri[1].s + ", " + tri[2].s + " is same as  " 
										+ a[0].s + ", " + a[1].s + ", " + a[2].s + ". Score: " + score);
			} 
		}
		level.addTriangle( tri );
        return tri;
        

    }

	private void centerItem(  ){
		float[][] translation = ThreeDTransform.get_translate( 2*r, (float)-Math.sqrt( 3 ) * r, 0 );
		for ( Cell a : levelZero.mCells ){
			a.concatTransform( translation );
		
		}
	}

	private Cell calculateRoot(){
		
		Cell minCell = null;
		Cell curCell = null;
		float minDist = 0;
		float currDist = 0;
		//for (Cell c : cells){
		for (int i = 0; i < cells.size(); i ++ ){
			currDist = 0;
			curCell = cells.get(i);

			for (Cell b : cells){
				currDist += curCell.distTo( b );
			
			}
			if (i == 0){
				minDist = currDist;
				minCell = curCell;
			} else if (currDist < minDist) {
				minDist = currDist;
				minCell = curCell;
			
			}
		}
		return minCell;
	}
	
	/** resolveVector
	* 		resolve the given velocity vector onto the given direction vector
	*/
	private float[] resolveVector( float[] velocityVector, float[] dirToResolve ){
		
		float mag = resolveMagnitude( velocityVector, dirToResolve );
		float[] output = VectorOps.getUnitVector( dirToResolve.clone() );
		if (mag != 0 ) {
			float cosAngBtwn = VectorOps.cosAngBtwn( velocityVector, dirToResolve );
			for ( int i = 0; i < 3; i ++){
				output[i] *= mag * cosAngBtwn;
			}
		} else {
			return output;
		}
		
		return output;
	}
	
	private float resolveMagnitude( float[] velocityVector, float[] dirToResolve ){
		float mag = VectorOps.get_magnitude( velocityVector );
		if ( mag != 0 ) {
			float cosAngBtwn = VectorOps.cosAngBtwn( velocityVector, dirToResolve );
			return mag * cosAngBtwn ;
		} else {
			return mag;
		}
		
	}
	
	
		
		
	public void addLineBetween(	Cell a, Cell b ){
		float[] vectorAB = VectorOps.getVector( a.rotationCenter, b.rotationCenter );
		a.addVector( vectorAB );
	}
	/** collideCells
	*		simulate the transfer of energy between cells
	*		its as if cell a is collided with cell b, the the velocitys are first projected onto the line
	* 		between them
	*/
	public void collideCells( Cell a, Cell b ) {
	
		if ( a != b ) {
		
			/* the line to resolve the collision on  */
			float[] vectorAB = VectorOps.getVector(a.rotationCenter, b.rotationCenter  );
			float[] unitvectorAB = VectorOps.getUnitVector( vectorAB );


			/* a's velocity vector split into the velocity upon the line and everything else */
			float[] velA = a.physicsPresence.getVelocityVector();
			float[] resolvedVelA = new float[]{ 0, 0, 0, 1 };
			float mag = VectorOps.get_magnitude( velA );
			float cosAngBtwn;
			if ( mag != 0 ) {
				cosAngBtwn = VectorOps.cosAngBtwn( velA, vectorAB );
				for (int i = 0; i < 3; i ++ ) {
					resolvedVelA[i] = unitvectorAB[i] * mag * cosAngBtwn ;
					velA[i] -= resolvedVelA[i];
				}
			}
			a.physicsPresence.multiplyBy( 0 );
			a.physicsPresence.applyTranslationForce( velA );
		
		
			float[] velB = b.physicsPresence.getVelocityVector();
			float[] resolvedVelB = new float[]{ 0, 0, 0, 1 };
			mag = VectorOps.get_magnitude( velB );
			if ( mag != 0 ) {
				cosAngBtwn = VectorOps.cosAngBtwn( velB, vectorAB );
				for (int i = 0; i < 3; i ++ ) {
					resolvedVelB[i] = unitvectorAB[i] * mag * cosAngBtwn ;
					velB[i] -= resolvedVelB[i];
				}
			}
			b.physicsPresence.multiplyBy( 0 );
			b.physicsPresence.applyTranslationForce( velB);
		
			for (int i = 0; i < 3; i ++){
				resolvedVelA[i] = (resolvedVelA[i] + resolvedVelB[i]) / 2;
			}
		
			a.physicsPresence.applyTranslationForce( resolvedVelA );
			b.physicsPresence.applyTranslationForce( resolvedVelA );
		
		}
	
	}
	

   	
    private void initHoneyComb( int sideLength, float r, Level level, float[] startPoint ){
    
    	level.mCells.clear();
    	level.mTriangles.clear();
    	ArrayList <Cell> rowOne = new ArrayList<>();
    	ArrayList <Cell> rowTwo = new ArrayList<>();

    	/* using a hexagon shape, make the pancake */
        float[] currPoint = startPoint.clone();
        int rowLength = sideLength - 1;
        Cell currentCell;

        float deltaX = -r;
        float deltaRow = 1;
        int count = 0;
        
        //if (level == levelOne ) count += 300;
        

        /* create all the cells and put them in the right spots */

       	
			for (int i = 0; i < (sideLength * 2) -1; i ++) {
		
				if (i == 1){
					rowOne.addAll( level.mCells );
				} 

				//only do first row
				//if ( i == 1 ) break;

				startPoint[0] += deltaX;
				startPoint[1] += Math.sqrt(3) * r;
				currPoint = startPoint.clone();
				rowLength += deltaRow;
				for (int j = 0; j < rowLength; j ++) {
					currentCell = new Cell(r, currPoint.clone());
					currentCell.setString( count + "" );
					count++;
					if (i > 0 ) rowTwo.add( currentCell );
					level.addCell(currentCell);
					currPoint[0] += 2*r;
				}
			
				if (i > 0 ) {
				
					System.out.println( "rowOne size: " + rowOne.size() );
					System.out.println( "rowTwo size: " + rowTwo.size() );
					/* assign triangles */
					if (rowOne.size() < rowTwo.size()){
				
						/* point up */
						for ( int k = 0; k < (rowTwo.size() - 1); k ++){
							Cell a = rowTwo.get(k + 1);
							Cell b = rowTwo.get(k);
							Cell c = rowOne.get(k);
							level.addTriangle(new Cell[] { a, b, c });

						}
						/* point down */
						for ( int k = 0; k < rowOne.size() - 1; k ++){
							Cell a = rowOne.get(k);
							Cell b = rowOne.get(k + 1);
							Cell c = rowTwo.get(k + 1);
							level.addTriangle(new Cell[] { a, b, c });

						}
				
					/* rowOne is longer than row 2 */
					} else {
						/* point up */
						for ( int k = 0; k < (rowTwo.size() - 1); k ++){
							Cell a = rowTwo.get(k + 1);
							Cell b = rowTwo.get(k);
							Cell c = rowOne.get(k + 1);
							level.addTriangle(new Cell[] { a, b, c });

						}
						/* point down */
						for ( int k = 0; k < rowOne.size() - 1; k ++){
							Cell a = rowOne.get(k );
							Cell b = rowOne.get(k + 1);
							Cell c = rowTwo.get(k );
							level.addTriangle(new Cell[] { a, b, c } );

						}
				
				
					}
				
					/* reset rows, should happen every time */
					//if (rowOne.size() < rowTwo.size() ) {
					rowOne = (ArrayList<Cell>)rowTwo.clone();
					rowTwo.clear();

				}
				if ( triangles != null ) {
					System.out.println( level.mTriangles.size() );
				}
			
			

				if (i == (sideLength - 1)){
					deltaRow *= -1;
					deltaX *= -1;
				}
			}
		
        
        System.out.println ( "cells: " + level.mCells.size() );
    }
    
    private void assignNeighbors(){
    	float r;
    	/* match up neighbors  */
        for ( Cell a : cells){
        	r = a.r;
            //a.neighborsOnceRemoved = new HashMap<Cell, ArrayList<Cell>>();
            for (Cell b: cells){
                float dist = a.distTo(b);
                
                if ( dist < (2*r + 5) && !(dist < 5) && dist > r) {
                    a.addNeighbor(b);
                    //a.neighborsOnceRemoved.put(b, new ArrayList<Cell>());
                }
            }
            System.out.println("neighbors : " +  a.neighbors.size() );

        }
    }
    
	private void putToSpringMap( Cell a, CellDist b){
		if ( springMap.containsKey(a) ){
			springMap.get(a).add(b);
		} else {
			ArrayList<CellDist> newMapping = new ArrayList<>();
			newMapping.add( b );
			springMap.put(a, newMapping );
		}
		
	}
    
    private void mapSprings( Level level, HashMap< Cell, ArrayList<CellDist> > springMap ){
    
    	/* 		If cells were points on a graph with the center cell the origin,
    	*	then the following block maps each cell to its reflection thru the origin
    	*/
    
    	int centerIndex = level.mCells.size() / 2;
    	Cell center = level.mCells.get( centerIndex );
    	Cell a;
    	Cell b;
    	int start;
    	int end;
    	int maps = 0;
    	
    	for (int i = 0 ; i < level.mCells.size(); i++ ){
    	    start = i;
    		end = start + 2*(centerIndex - start);
    	
    		a = level.mCells.get( start );
    		b = level.mCells.get( end );
    		
    		if ( a != b) { putToSpringMap( 
    			a, new CellDist( b, a.distTo(b) ) );
    			maps++;
    		}
    	
    	}
    	
    	

    	
    	System.out.println( "Springmap size: " + maps );
    	
    	
    }
    
    public void initCells(){

    	
    	/* get the honeycomb shape in hexagon of given sidelength */
    	float[] startPoint = new float[] { -sideLength * (r), -sideLength * (r), 200, 1};
    	//levelOne = new Level();
		initHoneyComb( sideLength, r, levelZero, startPoint );
		
		/* make the faces to draw */

		initFaces( levelZero );

		
		levelZero.sortTriangles();
		

		
		mapSprings( levelZero, springMap = new HashMap<>() );
		
		centerItem();
		
		
		

    }
    
		
    	
    	
    	
    	

    
    /** put force on one cell as if it were attached by spring to another */
    private void applySpringForce( float equilibrium, Cell a, Cell b){
    	float springContant = .5f;
    	float dist = VectorOps.dist(a.rotationCenter, b.rotationCenter);
    	
    	if (dist == 0) {
    		throw new IllegalArgumentException( "a and b have distance of 0!" );
		} else {
			float x = dist - equilibrium;

			/* get the unit vector which points from this cell to the neighbor cell */
			float[] forceVector = VectorOps.getVector(a.rotationCenter, b.rotationCenter);
			forceVector = VectorOps.getUnitVector( forceVector );

			/* force of a spring = -kx */
			for (int i = 0; i < 3; i ++){
				forceVector[i] *= (10)* (x/equilibrium) * springContant;
				//forceVector[i] = Math.round(forceVector[i]);
				//forceVector[i] = 0;
			}


			/* apply the force */
			a.physicsPresence.applyTranslationForce(forceVector);
        
        }
        //System.out.println( VectorOps.arrayToString( forceVector ) );
    
    }
    
    
    /* enforce a triangle so it is equilateral */
    private void enforceTriangle( Cell[] triangle ){
    	Cell cellA = triangle[0];
    		

		//triangle[1].applySpringForce( triangle[2] );
		
		/* midpoint between a and b */
		float[] midpoint = VectorOps.midPoint( cellA.rotationCenter, triangle[1].rotationCenter );
		
		/* calculate where cell c should be */
		float[] lineAB = VectorOps.getVector( cellA.rotationCenter, triangle[1].rotationCenter );
		//try {
			float[] lineAC = VectorOps.getVector( cellA.rotationCenter, triangle[2].rotationCenter );
			float[] orthogABAC = VectorOps.crossProduct( lineAB , lineAC ) ;
			float[] towardsC = VectorOps.crossProduct( orthogABAC, lineAB );
			float[] moveCvector = new float[] { 0, 0, 0, 1};
			towardsC = VectorOps.getUnitVector( towardsC );
			for( int i = 0 ; i < 3; i ++){
				towardsC[i] *= Math.sqrt(3) * cellA.r;
				midpoint[i] += towardsC[i];
				moveCvector[i] = midpoint[i] - triangle[2].rotationCenter[i] ;
			}
		
			float[][] translate = ThreeDTransform.get_translate(moveCvector[0], moveCvector[1], moveCvector[2] );
			ThreeDTransform.transform( translate, triangle[2].rotationCenter ); 	
			triangle[2].concatTransform( translate );	
		
			/* cells a and b have already been collided with eachother */
			//collideCells(triangle[1], triangle[2] );
			//collideCells(triangle[2], triangle[0] );
			
// 		} catch ( IllegalArgumentException e ) {
// 		
// 		}
			
    }
    
//     public float[] getNormal(){
//     	
//     }

	/* returns the the angle in the xy plane and yz plane 
	* 		returns: float[] { xy, yz }
	*/
	
	private double[] getAngles( float[] vector ){
		float[] xAxis = new float[]{1, 0, 0, 1};
		float[] yAxis = new float[]{0, 1, 0, 1};
		double[] output = new double[2];
		
		/* to measure the vector's angle in the xy plane, find the angle
		* btwn the x axis and the component of the vector which lies in the xy plane */
		
		float[] vectorXY = vector.clone();
		vectorXY[2] = 0;
		if ( VectorOps.get_magnitude( vectorXY ) != 0 ) { 
			//output[0] = Math.acos( VectorOps.cosAngBtwn( xAxis, vectorXY ) );
			output[0] = Math.atan2( vectorXY[1], vectorXY[0] )  + Math.PI;
		
		} else {
			output[0] = 0;
		}
		
		float[] vectorYZ = vector.clone();
		vectorYZ[0] = 0;
		if ( VectorOps.get_magnitude( vectorYZ ) != 0 ) { 
			//output[1] = Math.acos( VectorOps.cosAngBtwn( yAxis, vectorYZ ) );
			output[1] =  Math.atan2( vectorYZ[1], vectorYZ[2] ) + Math.PI;

		} else {
			output[1] = 0;
		}
		return output;
	}
	
	/* get the avg of the normal vectors of all the faces */
	private float[] getAvgNormal(){
		float[] n = null;
		for (Region3D face : faces){
			if (n == null) {
				n = VectorOps.getUnitVector( face.getNormal() );
			} else {
			
				n = VectorOps.midPoint( n, face.getNormal() );
			}
			
		}
		return n;
	}
	
	/* check for scores */
	private void updateScore(){

		//Region3D a = faces.get(0);
		float[] normal = getAvgNormal();
		if ( VectorOps.cosAngBtwn( normal, scoreVector ) > .1f ){
			score++;
			for (int i = 0; i < 3 ; i ++ ) {
				scoreVector[i] *= -1;
			
			}
		}
		
		//System.out.println( "Score: " + score );

	}
	

    /** updateSelf
     *      applies transformation to all faces and the rotationCenter
     * @param pov point of view
     * @param light_src light source
     */
    @Override
    public void updateSelf(float[] pov, float[] light_src) {
    
		ArrayList<Cell[]> currTris;
		for (int x = 0; x < levels.length; x ++ ) {
			currTris = levels[x].mTriangles;
			/* iterate through triangles and enforce them */
			for (int y = 0; y < currTris.size(); y++){
				Cell[] triangle = currTris.get(y);
			
			
				if( y == 0) {
					Cell cellA = triangle[0];
					cellA.applyTether( triangle[1] );
					cellA.applyTether( triangle[2] );
				}
			
				enforceTriangle( triangle );
			
				/* transfer energy between cells */
				collideCells( triangle[0], triangle[1] );
				collideCells( triangle[1], triangle[2] );
				collideCells( triangle[2], triangle[0] );
	// 			
	//     		
	//      		//triangle[1].applyTether( triangle[2] );
	//     		//System.out.println( "got here " );
			}
		}
		
		/* update faces */
		if (hasThickness) {
			float[] norm = VectorOps.getUnitVector( faces.get(0).getNormal() );
			float thickness = 5;
			for (int i = 0; i < 3; i ++){
				norm[i] *= thickness;
			}
			for ( Region3D face : faces ){
				Triangle tri = (Triangle) face;
				tri.normal = norm;
			}
		}
	
    	
  		if ( springMap != null ) {
  			ArrayList<CellDist> mappings;
			//CellDist value;
			for ( Cell key : springMap.keySet() ){
				mappings = springMap.get(key);
				for (CellDist value : mappings){
					applySpringForce( value.d, key, value.c  );
				}
				
			}
    	}

        /* update all the cells */
        for (Cell c : levelZero.mCells){
            c.updateSelf(pov, light_src);
        }
        
        for ( Region3D face : faces ){
        	face.updateSelf( ThreeDTransform.get_identity(), pov, light_src );
        	
        }
                	
		updateScore();
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
    @Override
    public void draw(Object obj, boolean outline) {
    
    	boolean drawTriangles = true;
    	
//         for ( Cell cell : cells ){
//             cell.draw(g2d, outline);
//         }
        
//         if (drawTriangles){
//         	Cell a;
//         	Cell b;
//         	float[] vector;
//         	g2d.setPaint( Color.GREEN );
//         	for (Cell[] tri : triangles){
//         		for (int i = 0; i < 3; i ++ ){
//         			a = tri[i];
//         			b = tri[ ( i + 1 ) % 3 ];
//         			vector = VectorOps.getVector( a.rotationCenter, b.rotationCenter );
//         			a.addVector (vector );
//         		
//         		}
//         	
//         	}
//         
//         }

    }

    /** isCollided
     *      decided if two items are collided
     * @param otherItem the other item in question
     * @return whether or not they are collided
     */
    public void isCollided( Item otherItem, float[] pov ){
        for (Cell c : cells){
            c.isCollided(otherItem, pov);
        }
    }

    public void applyTranslationalForce( float[] vector ){
        for (Cell c : cells){
            c.physicsPresence.applyTranslationForce(vector);

        }
    }
}


class Level{
	public ArrayList<Cell> mCells;
	public ArrayList<Cell[]> mTriangles;
	
	public Level( ){
		mCells = new ArrayList<>();
		mTriangles = new ArrayList<>();
	}
	
	public void addCell( Cell a ) { 
		if (!mCells.contains( a ) ) {
			mCells.add( a ); 
		} else {
			throw new IllegalArgumentException( "cell must be unique!" ); 
		}
	}
	
	public void addTriangle(Cell[] tri){
		for ( Cell[] a : mTriangles ) {
			//Cell[] aClone = a.clone();
			int score = 0;
			for (int i = 0; i < 3; i ++ ){
				for ( int j = 0; j < 3; j ++ ){
					if ( (tri[i].s) .equals ( a[j].s  ) ) {
						score++;
						//aClone[j] = null;
					} else if ( tri[i] == tri[j] && i != j ){
						throw new IllegalArgumentException( "triangle must contain 3 different cells! input: " 
										+ tri[0].s + ", " + tri[1].s + ", " + tri[2].s);
					}
				}	
			}
			if (score >= 3){
				throw new IllegalArgumentException( "triangle must be unique! input: " 
										+ tri[0].s + ", " + tri[1].s + ", " + tri[2].s + " is same as  " 
										+ a[0].s + ", " + a[1].s + ", " + a[2].s + ". Score: " + score);
			} 
		}
		mTriangles.add( tri );	

	}
	public void sortTriangles(){
	
		/* 'triangles' needs to be sorted so that each subsequent
			triangle in the list shares 2 cells with the last
		*/
		//Cell [] triAclone;
		//Cell [] triBclone;
		
		ArrayList<Cell[]> trianglesClone = (ArrayList<Cell[]>)mTriangles.clone();
		ArrayList<Cell[]> output = new ArrayList<Cell[]>();
		ArrayList<Cell> enforcedCells = new ArrayList<Cell>();

		Cell[] curr = trianglesClone.remove(0);
		enforcedCells.addAll(Arrays.asList( curr ));
		output. add (curr );
		
		while( !trianglesClone.isEmpty() ) {

			ListIterator<Cell[]> li = trianglesClone.listIterator();
			while (li.hasNext()) {
				Cell[] tri = li.next();

				if (tri != null) {
					Cell[] sortedTri = new Cell[3];
					int score = 0;
					for (int i = 0; i < 3; i ++ ) {
						if ( enforcedCells.contains( tri[i] ) ) {
							sortedTri[score] = tri[i];
							score++;
						} 	
					}
				
					/* if 2 of 3 cells are already enforced, enforce the third and sort so the
					already enforced cell are first */
					if (score == 2){
						for (int i = 0; i < 3; i ++){
							if (!enforcedCells.contains(tri[i])) {
								enforcedCells.add( tri[i] );
								sortedTri[2] = tri[i];

							}

						}
						output.add( sortedTri );
						li.remove();

						//trianglesClone.set( l, null );

					} else if (score == 3) {
						enforcedCells.addAll( Arrays.asList( tri ) );
						//output.add( tri );
						li.remove();
					}
				}
			}		
		}	

		mTriangles = output;
	
	}
	

}

class CellDist{
	public float d;
	public Cell c;
	/* store a cell and a distance */
	public CellDist( Cell _c, float _d ){
		c = _c;
		d = _d;
	}

}



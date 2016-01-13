//package com.xanderfehsenfeld.flippycakes;


/**
 * Created by Xander on 10/27/15 to store functions which perform common vector operations
 */
public class VectorOps {

    /* cross_product : returns the vector orthogonal to a and b */
    public static float[] crossProduct(float[] a, float[] b) {
        int x = 0;
        int y = 1;
        int z = 2;
        float[] result = new float[4];
        result[3] = 1;
        if (a.length != 4 || b.length != 4) {
            throw new IllegalArgumentException("Lengths of input arrays must be exactly 4!");
        } else {
            result[x] = a[y] * b[z] - a[z] * b[y];
            result[y] = -( a[x] * b[z] - a[z] * b[x] );
            result[z] = a[x] * b[y] - a[y] * b[x];
        }
        if (get_magnitude(result) == 0){
            throw new IllegalArgumentException( "Magnitude of cross product is 0 aka input vectors are parallel! inputs:"
                    + arrayToString(a) + " and " + arrayToString(b)  );
        } else {
            return result;
        }

    }

    /* returns the dot product divided by the magnitudes *
    * AKA the cos of the angle between the two vectors
     */
    public static float cosAngBtwn( float[] a, float[] b ){
        if (get_magnitude(a) == 0 || get_magnitude(b) == 0){
            throw new IllegalArgumentException( "magnitude of one or more of inputs equal to 0!" );
        }
        return dotProduct(a, b) / (get_magnitude(a) * get_magnitude(b));
    }

    /* dot_product : returns |a||b|cos(<angle btween a and b>) */
    public static float dotProduct( float[] a, float[] b ){
        int x = 0;
        int y = 1;
        int z = 2;
        if (a.length != 4 || b.length != 4) {
            System.out.println("error in dotproduct length of inputs != 4");
            return 666;
        } else {
            return a[x] * b[x] + a[y] * b[y] + a[z] * b[z];
        }
    }

    /** get the magnitude (length) of a vector */
    public static float get_magnitude(float[] a){
        return (float)Math.sqrt(a[0]*a[0] + a[1]*a[1] + a[2]*a[2] );
    }

    /** getIntersectionPoint
     *      get the point, if any, where line AB intersects a plane
     * @param pointA start point of line
     * @param pointB    endpoint of line
     * @param equationOfPlane equation representing plane in form Ax + By + Cz = D
     * @param lineSegment whether or not the line is infinite or a linesegment
     * @return the point of intersection if any
     */
    public static float[] getIntersectionPoint(float[] pointA, float[] pointB, float[] equationOfPlane, boolean lineSegment){
        /* the following are equations to represent the line
        * they are all of size 2, A + (B - A) *t
        *       index 0 is A
        *       index 1 is (B-A)
         */
        float[] a_x = new float[]{ pointA[0], pointB[0] - pointA[0] };
        float[] a_y = new float[]{ pointA[1], pointB[1] - pointA[1] };
        float[] a_z = new float[]{ pointA[2], pointB[2] - pointA[2] };

        /* clone all three equations for later use */
        float[] a_xClone = a_x.clone();
        float[] a_yClone = a_y.clone();
        float[] a_zClone = a_z.clone();



        /* "now plug those values into x, y, and z of the plane equation" */
        float A = equationOfPlane[0];
        float B = equationOfPlane[1];
        float C = equationOfPlane[2];
        float D = equationOfPlane[3];

        /* multiply out coefficients */
        a_x[0] *= A;
        a_x[1] *= A;
        a_y[0] *= B;
        a_y[1] *= B;
        a_z[0] *= C;
        a_z[1] *= C;

        /* subtract constants */
        D -= (a_x[0] + a_y[0] + a_z[0]);

        /* add up side with t's */
        float sum =  a_x[1] + a_y[1] + a_z[1];


        if ( sum == 0 && D != 0) {
            /* line does not intersect plane */
            return null;
        } else if ( D == 0){
            return null;
        } else {
            D /= sum;
            if ( ( D < 0 || D > 1 ) && lineSegment) {
                return null;

            }

            return new float[]{
                    a_xClone[0] + a_xClone[1]*D,
                    a_yClone[0] + a_yClone[1]*D,
                    a_zClone[0] + a_zClone[1]*D,
                    1 };
        }
    }

    /** lineIntersectsPlane
     *
     * @param pointA startPt of line segment
     * @param pointB endPt of line segment
     * @param normal normal of plane
     * @param pointQ point not in normal in plane
     * @return whether or not line AB intersects the plane
     */
    public static boolean lineIntersectsPlane(float[] pointA, float[] pointB, float[] normal, float[] pointQ){
        /* algorithm
        *       pointA and pointB are the endpoints of the line
        *       normal is the normal vector
        *       point q is a point not in normal vector but in the plane
        *
        *       find lines qA and qB
        *       take dot product(qA, normal), and dot product(qB, normal)
        *       if both results are <0 or >0 the line does not intersects, otherwise it does!
        *
         */
        float[] qA = new float[]{ pointA[0] - pointQ[0], pointA[1] - pointQ[1], pointA[2] - pointQ[2], 1};
        float[] qB = new float[]{ pointB[0] - pointQ[0], pointB[1] - pointQ[1], pointB[2] - pointQ[2], 1};
        float dotProductA = VectorOps.dotProduct(qA, normal);
        float dotProductB = VectorOps.dotProduct(qB, normal);
        if ((dotProductA < 0 && dotProductB < 0) || (dotProductA > 0 && dotProductB > 0)){
            return false;
        } else {
            return true;
        }

    }

    /* get distance btwn 2 points */
    public static float dist(float[] pointA, float[] pointB){
        return (float) Math.sqrt( distSquared(pointA, pointB) );
    }

    /* get distance squared between two points */
    public static float distSquared(float[] pointA, float[] pointB){
        float sum = 0;
        for (int i = 0; i < pointA.length; i ++){
            sum += Math.pow(pointA[i] - pointB[i], 2);
        }
        return sum;
    }

    /** getVector
     *
     * @param pointA the origin point
     * @param pointB the terminal
     * @return the 3d vector from A to B
     */
    public static float[] getVector( float[] pointA, float[] pointB ){
        float[] vector = new float[4];
        for (int i = 0; i < 3; i ++){
            vector[i] = pointB[i] - pointA[i];
        }
        vector[3] = 1;
        //checkForNaN(vector);
        return vector;
    }

    /** getUnitVector
     *
     * @param vector the vector to calculate the unit vector of
     * @return the unit vector
     */
    public static float[] getUnitVector( float[] vector ) {
        float[] unit = new float[4];
        float mag = get_magnitude(vector);
//        if (mag == 0) {
//            throw new IllegalArgumentException( "Magnitude of input cannot be 0! input: " + arrayToString(vector));
//        }
        for (int i = 0; i < 3; i++) {
            unit[i] = vector[i] / mag;
        }
        unit[3] = 1;
        //checkForNaN(unit);
        return unit;
    }

    public static void printArray( float[] input ){

        System.out.println(arrayToString(input));
    }
    public static String arrayToString( float[] input ){
        String toPrint = "";
        for (int i = 0; i < input.length; i ++){
            if (i == input.length -1 ){
                toPrint += input[i];
            } else {
                toPrint += input[i] + ", ";
            }
        }
        return toPrint;
    }

    private static boolean isNaN(float input){
        return new Float( input ).isNaN();
    }
    private static void checkForNaN(float[] input){
        for (int i = 0; i < input.length; i ++){
            if (isNaN(input[i])){
                throw new NumberFormatException("NaN occured! input: " + arrayToString(input));
            }
        }
    }
    public static boolean containsNaN(float[] input){
        for (int i = 0; i < input.length; i ++){
            if (isNaN(input[i])) return true;
        }
        return false;
    }
    
    public static float[] midPoint( float[] pointA, float[] pointB ){
    	float[] output = new float[]{ 0, 0, 0, 1};
    	for (int i = 0; i < 3; i ++){
    		output[i] = (pointA[i] + pointB[i]) / 2;
    	}
    	return output;
    }


}

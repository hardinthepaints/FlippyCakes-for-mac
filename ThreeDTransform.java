//package com.xanderfehsenfeld.flippycakes;


public class ThreeDTransform{


    /** get_identity
     *
     * @return - the identity matrix, which doesn't do anything
     */
    public static float[][] get_identity(){
        return new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
        };
    }

    /* rotate a cube around an axis */
    public static float[][] get_rotate( float theta, String axis ) {
        if ( axis.equals("x") ) {
            return get_rotate_around_x(theta);
        } else if ( axis.equals("y") ){
            return get_rotate_around_y(theta);
        }	else if ( axis.equals("z") ) {
            return get_rotate_around_z(theta);
        }


        return null;
    }

    /* scale evenly */
    public static float[][] get_scale(float factor){
        float[][] scale = {{factor, 0, 0, 0}, {0, factor, 0, 0}, {0, 0, factor, 0}, {0, 0, 0, 1}};
        return scale;

    }

	/* 3D transformations */

    private static float[][] get_rotate_around_z( float theta) {
        float[][] z_rot =
                {
                        {(float)Math.cos(theta), (float)-Math.sin(theta), 0, 0},
                        {(float)Math.sin(theta), (float)Math.cos(theta), 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                };
        return z_rot;
    }

    private static float[][] get_rotate_around_y( float theta) {
        float[][] y_rot =
                {
                        {(float)Math.cos(theta), 0, (float)Math.sin(theta), 0},
                        {0, 1, 0, 0},
                        {(float)-Math.sin(theta), 0, (float)Math.cos(theta), 0},
                        {0, 0, 0, 1}
                };
        return y_rot;
    }

    private static float[][] get_rotate_around_x( float theta) {
        float[][] x_rot =
                {
                        {1, 0, 0, 0},
                        {0, (float)Math.cos(theta), (float)-Math.sin(theta), 0},
                        {0, (float)Math.sin(theta), (float)Math.cos(theta), 0},
                        {0, 0, 0, 1}
                };
        return x_rot;

    }

    /* 3d TRANSLATION */
    public static float[][] get_translate( float val, String axis ) {
        if ( axis.equals("x") ) {
            return get_translate_x(val);
        } else if ( axis.equals("y") ){
            return get_translate_y(val);
        }	else if ( axis.equals("z") ) {
            return get_translate_z(val);
        }


        return null;
    }

    /** get_translate
     *
     * @param x
     * @param y
     * @param z
     * @return the translate which translates by x y and z in respective directions
     */
    public static float[][] get_translate(float x, float y, float z){
        float[][] translate = get_identity();
        if (x != 0){
            translate = multiply(get_translate_x(x), translate);
        }
        if (y != 0){
            translate = multiply(get_translate_y(y), translate);
        }
        if (z != 0){
            translate = multiply(get_translate_z(z), translate);
        }
        return translate;
    }

    /** get_rotate
     *
     * @param x
     * @param y
     * @param z
     * @return rotate transform by x y and z in respective directions
     */
    public static float[][] get_rotate(float x, float y, float z){
        float[][] rotate = get_identity();
        if (x != 0) rotate = multiply(get_rotate_around_x(x), rotate);
        if (y != 0) rotate = multiply(get_rotate_around_y(y), rotate);
        if (z != 0) rotate = multiply(get_rotate_around_z(z), rotate);
        return rotate;
    }
    private static float[][] get_translate_x(float val){
        return new float[][]{
                {1, 0, 0, val},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }
    private static float[][] get_translate_y(float val){
        return new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, val},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }
    private static float[][] get_translate_z(float val){
        return new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, val},
                {0, 0, 0, 1}
        };
    }

    /** MATRIX OPS - common ops in relation to matrices */

    /** apply a 3d transform to the cube */
    public static float[][] multiply(float[][] a , float[][] b){

        if ( a != null && b != null) {
            float[][] c = new float[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};


            int rowa = a.length;
            int colb = b[0].length;
            int cola = a[0].length;

            //multiply a and b

            for (int i = 0; i < rowa; ++i) {

                for (int j = 0; j < colb; ++j) {
                    c[i][j] = 0;
                    for (int k = 0; k < cola; ++k) {
                        c[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return c;

        } else {
            //return identity matrix, which does nothing
            return new float[][]{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
        }


    }



    /* TRANSFORM : apply a transformation to one point using array multiplication */
    public static void transform( float[][] matrix, float[] one_pt ) {
        //store the output values
        float [] output_point  = new float[one_pt.length];

        float sum;

        //rows of matrix
        for (int j = 0; j < matrix.length; j++) {
            //cols of matrix
            sum = 0;
            for (int k = 0; k < matrix[0].length; k ++ ) {
                sum += one_pt[k] * matrix[j][k];
            }
            output_point[j] = sum;
        }
        for ( int row = 0; row < output_point.length; row ++){
            one_pt[row] = output_point[row];
        }
    }


}
//package com.xanderfehsenfeld.flippycakes;

//import android.graphics.Path;
//import java.awt.geom.Path2D;

import java.awt.geom.Path2D;


import java.util.ArrayList;

/**
 * Created by Xander on 10/27/15.
 */
public class Projector {

    /** projectPoint
     *
     * @param three_d_point a 3d point to be projected
     * @param pov the point of view
     * @return an array of size 2 representing the projected point
     */
    public static float[] projectPoint(float[] three_d_point, float[] pov){
        float E = pov[2];

        float x_3d = three_d_point[0];
        float y_3d = three_d_point[1];
        float z_3d = three_d_point[2];
        //translate
        float x = x_3d / ( 1 - ( z_3d/E ) );
        float y = y_3d / ( 1 - ( z_3d/E ) );

        return new float[]{x,y, 1};

    }

    /** projectFace
     *
     * @param curr_points the points representing the face
     * @param pov the vector representing the pov
     * @return a Path2D representing the projected face
     */
    public static Path2D projectFace( ArrayList<float[]> curr_points, float[] pov){
        Path2D new_path = new Path2D.Float();
        for (int i = 0; i < curr_points.size(); i++) {
            float[] projected_pt = Projector.projectPoint(curr_points.get(i), pov);

            if (i == 0) {
                new_path.moveTo((float) projected_pt[0], (float) projected_pt[1]);
            } else {
                new_path.lineTo((float) projected_pt[0], (float) projected_pt[1]);
            }
        }
        new_path.closePath();
        return new_path;
    }
}

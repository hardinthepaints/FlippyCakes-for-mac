//package com.xanderfehsenfeld.flippycakes;

//import android.graphics.Canvas;
//import java.awt.Graphics2D;

/**
 * Created by Xander on 12/2/15.
 */
public interface Drawable {
    public void draw (Object obj, boolean outline);
    
    public float[] getBoundsInfo();
    
    public void updateSpec( float[] normal, float[] light_src, float[] pov, float[] equationOfPlane );
}

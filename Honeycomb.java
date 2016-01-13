//package com.xanderfehsenfeld.flippycakes;

import java.util.ArrayList;

/**
 * Created by Xander on 12/3/15.
 */
public class Honeycomb extends CellularItem {
    public Honeycomb(){}

    @Override
    public void initCells(){

        cells = new ArrayList<Cell>();

        /* using a hexagon shape, make the pancake */
        int sideLength = 3;
        float[] startPoint = new float[] {50, 0, 200, 1};
        float[] currPoint = startPoint.clone();
        int rowLength = sideLength - 1;
        float r = 10;
        Cell currentCell;

        float deltaX = -r;
        float deltaRow = 1;

        /* create all the cells and put them in the right spots */
        for (int i = 0; i < (sideLength * 2) -1; i ++) {

            //if ( i == 1 ) break;


            startPoint[0] += deltaX;
            startPoint[1] += Math.sqrt(3) * r;
            currPoint = startPoint.clone();
            rowLength += deltaRow;
            for (int j = 0; j < rowLength; j ++) {
                currentCell = new Cell(r, currPoint.clone());
                cells.add(currentCell);
                currPoint[0] += 2*r;
            }

            if (i == (sideLength - 1)){
                deltaRow *= -1;
                deltaX *= -1;
            }
        }
        /* match up neighbors */
        for ( Cell a : cells){
            for (Cell b: cells){
                float dist = a.distTo(b);
                if ( dist < (2*r + 5) && !(dist < 5)){
                    a.addNeighbor(b);
                }
            }
        }



    }
}

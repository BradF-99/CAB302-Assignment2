package main.java.components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class PolygonComponent{

    //list of polygons
    public final LinkedList<Polygon> polygon = new LinkedList<>();
    //points for polygon visual
    private Point startPoint = null;
    private Point lastPoint = null;
    //LinkedList for visual of polygon
    public final LinkedList<LineComponent.Line> drawnLines = new LinkedList<>();

    /**
     * Adds new polygon to the List of polygons
     *
     * @param pointArray array of points in the polygon
     */
    public void addNewObject(Object[] pointArray){
        Polygon newPolygon = new Polygon();
        for (int i = 0; i < pointArray.length; i++) {
            Point point = ((Point) pointArray[i]);
            newPolygon.addPoint(point.x, point.y);
        }
        polygon.add(newPolygon);
    }

    /**
     * Clears the list of polygons
     */
    public void clearObjects(){
        this.polygon.clear();
    }

    /**
     * Sets start point
     *
     * @param point sets startPoint
     */
    public void getStart(Point point){
        startPoint = point;
    }

    /**
     * Adds a line to the drawnLines linked list based on the position of the last point or start point
     * to the location of the point given.
     *
     * @param x1 x position of the point
     * @param y1 y position of the point
     */
    public void addDrawObject(int x1, int y1){
        //if it last point is null there has not been a previous line so use start point
        if(lastPoint == null){
            this.drawnLines.add(new LineComponent.Line(startPoint.x,startPoint.y, x1, y1));
        }else {
            this.drawnLines.add(new LineComponent.Line(lastPoint.x, lastPoint.y, x1, y1));
        }
        lastPoint = new Point(x1,y1);
    }

    /**
    *clears the visually drawn polygon and sets last point to null
     */
    public void clearDrawObject(){
        this.drawnLines.clear();
        this.lastPoint = null;
    }

    /**
     * Determines if the point specified in x1,y1 is close enough to the start position to end the polygon.
     *
     * @param startx x co-ordinate of the start Point
     * @param starty y-co-ordinate of the start Point
     * @param x1 x- co-ordinate of the point that is to be checked against
     * @param y1 y- co-ordinate of the point that is to be checked against
     * @return
     */
    public boolean checkPoly(int startx, int starty, int x1, int y1){
        if((startx - x1 <= 5 && startx - x1 >= -5) && (starty - y1 <= 5 && starty - y1 >= -5)){
            return true;
        }else{
            return false;
        }
    }
}

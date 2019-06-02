package main.java.components;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class PolygonComponent{

    //inner polygon class
    public static class Polygon{
        public Object[] pointArray;
        public Color borderColor;
        public boolean filled;
        public Color fillColor;
        /**
         * Constructor for the Helper class
         * @param borderColor Color of the object border
         * @param filled true if object is filled
         * @param fillColor Color the object will be filled in
         */
        public Polygon(Object[] pointArray, Color borderColor, boolean filled, Color fillColor){
            this.pointArray = pointArray;
            this.borderColor = borderColor;
            this.filled = filled;
            this.fillColor = fillColor;
        }
    }

    //list of polygons
    public final LinkedList<PolygonComponent.Polygon> polygon = new LinkedList<>();
    //points for polygon visual
    private Point2D.Float startPoint = null;
    private Point2D.Float lastPoint = null;
    //LinkedList for visual of polygon
    public final LinkedList<LineComponent.Line> drawnLines = new LinkedList<>();

    /**
     * Adds new polygon to the List of polygons
     *
     * @param pointArray array of points in the polygon
     * @param borderColor Color of the object border
     * @param filled true if object is filled
     * @param fillColor Color the object will be filled in
     */
    public void addNewObject(Object[] pointArray,Color borderColor,boolean filled, Color fillColor){
        polygon.add(new Polygon(pointArray,borderColor,filled,fillColor));
    }


    /**
     * Creates a Path2D.Float (polygon) based on the point array and the screen size.
     *
     * @param pointArray array of points making up the polygon
     * @param screenSize size of the area being drawn to
     * @return
     */
    public static Path2D.Float createScaledPolygon(Object[] pointArray, Dimension screenSize){
        Path2D.Float newPolygon = new Path2D.Float();
        for (int i = 0; i < pointArray.length; i++) {
            Point2D.Float point = ((Point2D.Float) pointArray[i]);
            //first point
            if(i == 0){
                newPolygon.moveTo(point.x * screenSize.width, point.y * screenSize.height);
            }else{ // all other points
                newPolygon.lineTo(point.x * screenSize.width, point.y * screenSize.height);
            }
        }
        //close path
        newPolygon.closePath();
        return newPolygon;
    }
    /**
     * Clears the list of polygons
     */
    public void clearObjects(){
        this.polygon.clear();
    }

    /**
     * Clears a single entry in the list
     * @param index index of the list to be removed
     */
    public void clearObject(int index){
        this.polygon.remove(index);
    }


    /**
     * Sets start point
     *
     * @param point sets startPoint
     */
    public void getStart(Point2D.Float point){
        startPoint = point;
    }

    /**
     * Adds a line to the drawnLines linked list based on the position of the last point or start point
     * to the location of the point given.
     *
     * @param x1 x position of the point
     * @param y1 y position of the point
     * @param borderColor Color of the object border
     */
    public void addDrawObject(Float x1, Float y1,Color borderColor){
        //if it last point is null there has not been a previous line so use start point
        if(lastPoint == null){
            this.drawnLines.add(new LineComponent.Line((float) startPoint.x,(float) startPoint.y, x1, y1,borderColor));
        }else {
            this.drawnLines.add(new LineComponent.Line(lastPoint.x, lastPoint.y, (float) x1, (float) y1,borderColor));
        }
        lastPoint = new Point.Float(x1,y1);
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
     * @return true if the point is close enough, false if it is not.
     */
    public boolean checkPoly(int startx, int starty, int x1, int y1){
        if((startx - x1 <= 5 && startx - x1 >= -5) && (starty - y1 <= 5 && starty - y1 >= -5)){
            return true;
        }else{
            return false;
        }
    }
}

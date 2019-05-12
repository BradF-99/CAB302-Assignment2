package main.java.components;

import java.awt.*;
import java.util.LinkedList;

public class PolygonComponent{

    /**
     * Helper class for the polygon list.
     */
    public static class Polygon{
        public java.awt.Polygon polygon;
        public Color borderColor;
        public boolean filled;
        public Color fillColor;

        /**
         * Constructor for the Helper class
         * @param borderColor Color of the object border
         * @param filled true if object is filled
         * @param fillColor Color the object will be filled in
         */
        public Polygon(java.awt.Polygon polygon, Color borderColor, boolean filled, Color fillColor){
            this.polygon = polygon;
            this.borderColor = borderColor;
            this.filled = filled;
            this.fillColor = fillColor;
        }
    }

    //list of polygons
    public final LinkedList<PolygonComponent.Polygon> polygon = new LinkedList<>();
    //points for polygon visual
    private Point startPoint = null;
    private Point lastPoint = null;
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
        java.awt.Polygon newPolygon = new java.awt.Polygon();
        for (int i = 0; i < pointArray.length; i++) {
            Point point = ((Point) pointArray[i]);
            newPolygon.addPoint(point.x, point.y);
        }
        polygon.add(new Polygon(newPolygon, borderColor, filled, fillColor));
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
    public void getStart(Point point){
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
    public void addDrawObject(int x1, int y1,Color borderColor){
        //if it last point is null there has not been a previous line so use start point
        if(lastPoint == null){
            this.drawnLines.add(new LineComponent.Line(startPoint.x,startPoint.y, x1, y1,borderColor));
        }else {
            this.drawnLines.add(new LineComponent.Line(lastPoint.x, lastPoint.y, x1, y1,borderColor));
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

package main.java.components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class PolygonComponent extends JComponent{

    //list of polygons
    private final LinkedList<Polygon> polygon = new LinkedList<>();
    //points for polygon visual
    private Point startPoint = null;
    private Point lastPoint = null;
    //LinkedList for visual of polygon
    private final LinkedList<LineComponent.Line> drawnLines = new LinkedList<>();

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
        repaint();
    }

    /**
     * Clears the list of polygons
     */
    public void clearObjects(){
        this.polygon.clear();
        repaint();
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
        repaint();
    }

    /**
    *clears the visually drawn polygon and sets last point to null
     */
    public void clearDrawObject(){
        this.drawnLines.clear();
        this.lastPoint = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //loop through all the polygons in the LinkedList
        for (Polygon poly : this.polygon) {
            g.drawPolygon(poly);
        }
        //loop through the lines to create a visual polygon
        for(LineComponent.Line line : this.drawnLines) {
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
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

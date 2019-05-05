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

    //adds new polygon to the linked list
    public void addNewObject(Object[] pointArray){
        Polygon newPolygon = new Polygon();
        for (int i = 0; i < pointArray.length; i++) {
            Point point = ((Point) pointArray[i]);
            newPolygon.addPoint(point.x, point.y);
        }
        polygon.add(newPolygon);
        repaint();
    }

    //clear all polygons
    public void clearObjects(){
        this.polygon.clear();
        repaint();
    }

    //gets the start point
    public void getStart(Point point){
        startPoint = point;
    }

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

    //clears the visually drawn polygon and sets last point to null
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

    //checks if the point is within 5 pixels of the start point
    public boolean checkPoly(int startx, int starty, int x1, int y1){
        if((startx - x1 <= 5 && startx - x1 >= -5) && (starty - y1 <= 5 && starty - y1 >= -5)){
            return true;
        }else{
            return false;
        }
    }
}

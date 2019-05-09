package test.java;

import main.java.components.EllipseComponent;
import main.java.components.LineComponent;
import main.java.components.PolygonComponent;
import main.java.components.RectangleComponent;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    Random rand = new Random();
    @Test
    public void testExample() {
        int testSum = 3+6+9;
        assertEquals(18,testSum);
    }
    @Test
    public void testMainExampleTrue (){
        assertEquals(main.java.Main.exampleFunc(true),"Hi!");
    }
    @Test
    public void testMainExampleFalse (){
        assertEquals(main.java.Main.exampleFunc(false),"Hello!");
    }
    @Test
    public void testListSize() {
        LineComponent line = new LineComponent();
        line.addNewObject(1,2,3,4);
            assertEquals(1, line.lines.size());
    }
    @Test
    public void testListClear() {
        LineComponent line = new LineComponent();
        line.addNewObject(1,2,3,4);
        line.clearObjects();
        assertEquals(0,line.lines.size());
    }
    @Test
    public void testPolygon() {
        PolygonComponent polyComp = new PolygonComponent();
        Polygon poly = new Polygon();
        LinkedList<Point> polyPoints = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Point point = new Point(rand.nextInt(1000),rand.nextInt(1000));
            polyPoints.add(point);
            poly.addPoint(point.x,point.y);
        }
        Object[] array = polyPoints.toArray();
        polyComp.addNewObject(array);
        Polygon testPoly = ((Polygon) polyComp.polygon.getLast());
        //test that the number of the points is the same and the array of x and y points are the same
        assertEquals(testPoly.npoints, poly.npoints);
        assertArrayEquals(poly.xpoints, testPoly.xpoints);
        assertArrayEquals(poly.ypoints, testPoly.ypoints);
    }
    @Test
    public void testLine() {
        LineComponent line = new LineComponent();
        line.addNewObject(1,2,3,4);
        assertEquals(1,line.lines.getLast().x1);
        assertEquals(2,line.lines.getLast().y1);
    }
    @Test
    public void testEllipse() {
        EllipseComponent ellipse = new EllipseComponent();
        ellipse.addNewObject(1,2,3,4);
        assertEquals(1,ellipse.ellipses.getLast().x);
        assertEquals(2,ellipse.ellipses.getLast().y);
    }
    @Test
    public void testRectangle() {
        RectangleComponent rect = new RectangleComponent();
        rect.addNewObject(1,2,3,4);
        assertEquals(1,rect.rectangles.getLast().x);
        assertEquals(2,rect.rectangles.getLast().y);
    }


}
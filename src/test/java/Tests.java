package test.java;

import main.java.*;
import main.java.components.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    Random rand = new Random();

    /*
     * File Handling Tests
     *
     * Things we need to test for during read:
     * - Testing read of a valid VEC file
     * - Testing read of an invalid VEC file
     * - Testing read of a file that is partially valid (has some incorrect arguments)
     * - Testing read of a non-VEC file
     * - Testing read of a non-existent file
     * - Testing read of a file that has been deleted or has become inaccessible during processing
     * - Testing read of a file that we do not have permissions to open
     * - Testing read of a file that is wrongly encoded (eg. binary encoded instead of text based)
     *
     * Things we need to test for during write:
     * - Testing write of a VEC file
     * - Verifying a successful write of a VEC file (using read)
     * - Testing write of a VEC file to a read only folder
     * - Testing write of a VEC file to a place where we do not have access (OS permissions)
     * - Testing write of a VEC file where the media becomes inaccessible during write
     * - Testing write of a very detailed and/or large file
     * - Testing overwriting an existing file
     * - Testing overwriting a file that we do not have permission to overwrite
     */

    @Test
    public void testListSize() {
        LineComponent line = new LineComponent();
        line.addNewObject(1,2,3,4,Color.BLACK);
            assertEquals(1, line.lines.size());
    }
    @Test
    public void testListClear() {
        LineComponent line = new LineComponent();
        line.addNewObject(1,2,3,4,Color.BLACK);
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
        polyComp.addNewObject(array, Color.BLACK);
        Polygon testPoly = ((Polygon) polyComp.polygon.getLast());
        //test that the number of the points is the same and the array of x and y points are the same
        assertEquals(testPoly.npoints, poly.npoints);
        assertArrayEquals(poly.xpoints, testPoly.xpoints);
        assertArrayEquals(poly.ypoints, testPoly.ypoints);
    }
    @Test
    public void testLine() {
        LineComponent line = new LineComponent();
        line.addNewObject(1,2,3,4,Color.BLACK);
        assertEquals(1,line.lines.getLast().x1);
        assertEquals(2,line.lines.getLast().y1);
    }
    @Test
    public void testEllipse() {
        EllipseComponent ellipse = new EllipseComponent();
        ellipse.addNewObject(1,2,3,4,Color.BLACK);
        assertEquals(1,ellipse.ellipses.getLast().x);
        assertEquals(2,ellipse.ellipses.getLast().y);
    }
    @Test
    public void testRectangle() {
        RectangleComponent rect = new RectangleComponent();
        rect.addNewObject(1,2,3,4,Color.BLACK);
        assertEquals(1,rect.rectangles.getLast().x);
        assertEquals(2,rect.rectangles.getLast().y);
    }
    @Test
    public void testLineColour() {
        LineComponent line = new LineComponent();
        line.addNewObject(1,2,3,4,Color.RED);
        assertEquals(Color.RED, line.lines.getLast().color);
    }
    @Test
    public void testPolygonColour() {
        PolygonComponent polyComp = new PolygonComponent();
        LinkedList<Point> polyPoints = new LinkedList<>();
        polyPoints.add(new Point(100,100));
        Object[] array = polyPoints.toArray();
        polyComp.addNewObject(array, Color.YELLOW);
        assertEquals(Color.YELLOW, polyComp.polyColour.getLast());
    }
}
package test.java.components;

import main.java.components.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComponentTests {
    Random rand = new Random();
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
        polyComp.addNewObject(array, Color.BLACK,true,Color.BLACK);
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
        ellipse.addNewObject(1,2,3,4,Color.BLACK,true,Color.BLACK);
        assertEquals(1,ellipse.ellipses.getLast().x);
        assertEquals(2,ellipse.ellipses.getLast().y);
    }
    @Test
    public void testRectangle() {
        RectangleComponent rect = new RectangleComponent();
        rect.addNewObject(1,2,3,4,Color.BLACK,true,Color.BLACK);
        assertEquals(1,rect.rectangles.getLast().x);
        assertEquals(2,rect.rectangles.getLast().y);
    }
    @Test
    public void testPlot() {
        PlotComponent plot = new PlotComponent();
        plot.addNewObject(1,2,Color.BLACK);
        assertEquals(1,plot.plots.getLast().x);
        assertEquals(2,plot.plots.getLast().y);
    }
    @Test
    public void testLineColour() {
        LineComponent line = new LineComponent();
        line.addNewObject(1,2,3,4,Color.RED);
        assertEquals(Color.RED, line.lines.getLast().color);
    }
    @Test
    public void testFilledRect() {
        RectangleComponent rect = new RectangleComponent();
        rect.addNewObject(1,2,3,4,Color.BLACK,false,Color.BLACK);
        assertEquals(Color.BLACK, rect.rectangles.getLast().fillColor);
        assertEquals(false, rect.rectangles.getLast().filled);
    }
    @Test
    public void testFilledPolyComp() {
        PolygonComponent polyComp = new PolygonComponent();
        LinkedList<Point> polyPoints = new LinkedList<>();
        polyPoints.add(new Point(100,100));
        Object[] array = polyPoints.toArray();
        polyComp.addNewObject(array, Color.YELLOW,true,Color.RED);
        assertEquals(Color.YELLOW, polyComp.polyHelpers.getLast().borderColor);
        assertEquals(Color.RED, polyComp.polyHelpers.getLast().fillColor);
        assertEquals(true, polyComp.polyHelpers.getLast().filled);

    }
}

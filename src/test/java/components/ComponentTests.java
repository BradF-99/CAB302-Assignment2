package test.java.components;

import main.java.components.*;

import java.awt.*;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComponentTests {
    @Test
    public void testListSize() {
        LineComponent line = new LineComponent();
        line.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK);
        assertEquals(1, line.lines.size());
    }
    @Test
    public void testListClear() {
        LineComponent line = new LineComponent();
        line.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK);
        line.clearObjects();
        assertEquals(0,line.lines.size());
    }
    @Test
    public void testLine() {
        LineComponent line = new LineComponent();
        line.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK);
        assertEquals(1,line.lines.getLast().x1);
        assertEquals(2,line.lines.getLast().y1);
    }
    @Test
    public void testEllipse() {
        EllipseComponent ellipse = new EllipseComponent();
        ellipse.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK,true,Color.BLACK);
        assertEquals(1,ellipse.ellipses.getLast().x);
        assertEquals(2,ellipse.ellipses.getLast().y);
    }
    @Test
    public void testRectangle() {
        RectangleComponent rect = new RectangleComponent();
        rect.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK,false,Color.BLACK);
        assertEquals(1,rect.rectangles.getLast().x);
        assertEquals(2,rect.rectangles.getLast().y);
    }
    @Test
    public void testPlot() {
        PlotComponent plot = new PlotComponent();
        plot.addNewObject((float) 1,(float) 2,Color.BLACK);
        assertEquals(1,plot.plots.getLast().x);
        assertEquals(2,plot.plots.getLast().y);
    }
    @Test
    public void testLineColour() {
        LineComponent line = new LineComponent();
        line.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.RED);
        assertEquals(Color.RED, line.lines.getLast().color);
    }
    @Test
    public void testFilledRect() {
        RectangleComponent rect = new RectangleComponent();
        rect.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK,false,Color.BLACK);
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
        assertEquals(Color.YELLOW, polyComp.polygon.getLast().borderColor);
        assertEquals(Color.RED, polyComp.polygon.getLast().fillColor);
        assertEquals(true, polyComp.polygon.getLast().filled);
    }

    @Test
    public void testAddUndo() {
        ComponentsClass comp = new ComponentsClass(new Dimension(250,250));
        comp.rectComp.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK,false,Color.BLACK);
        comp.addUndo(comp.rectComp.rectangles.size() -1 , ShapesEnum.Shapes.RECTANGLE);
        assertEquals(1,comp.undoList.size());
    }

    @Test
    public void testUndo() {
        ComponentsClass comp = new ComponentsClass(new Dimension(250,250));
        comp.rectComp.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK,false,Color.BLACK);
        comp.addUndo(comp.rectComp.rectangles.size() -1 , ShapesEnum.Shapes.RECTANGLE);
        comp.rectComp.addNewObject((float) 1.0,(float) 2.0,(float) 3.0,(float) 4.0,Color.BLACK,false,Color.BLACK);
        comp.addUndo(comp.rectComp.rectangles.size() -1 , ShapesEnum.Shapes.RECTANGLE);
        comp.Undo();
        assertEquals(1, comp.undoList.size());
        assertEquals(1,comp.rectComp.rectangles.size());
    }

    @Test
    public void testFloatToPoint() {
        ComponentsClass comp = new ComponentsClass(new Dimension(250,250));
        Float floatValue = comp.pointToFloat(100,1000);
        assertEquals((float) 0.1, floatValue);
    }

    @Test
    public void testPointToFloat() {
        ComponentsClass comp = new ComponentsClass(new Dimension(250, 250));
        comp.setFrameSize(new Dimension(1000,1000));
        int[] array = comp.floatToPoint((float) 0.1, (float) 0.2, (float) 0.3, (float) 0.4);
        assertEquals(100,array[0]);
        assertEquals(200, array[1]);
    }
}

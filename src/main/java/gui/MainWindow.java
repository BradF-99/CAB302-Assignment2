package main.java.gui;

import main.java.components.EllipseComponent;
import main.java.components.LineComponent;
import main.java.components.PolygonComponent;
import main.java.components.RectangleComponent;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.Border;


public class MainWindow {
    private JFrame frame;
    private LineComponent lineComp = new LineComponent();
    private EllipseComponent ellComp = new EllipseComponent();
    private RectangleComponent rectComp = new RectangleComponent();
    private PolygonComponent polyComp = new PolygonComponent();
    private java.awt.Point startPoint;
    /**
     * This is just an example thread-safe GUI based off the example from the lecture.
     */
    private void buildGUI() {
        frame = new JFrame("Hello World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(polyComp);
        frame.getContentPane().addMouseListener(new MyMouseAdapter());
        frame.getContentPane().addMouseMotionListener(new MyMouseAdapter());
        frame.setPreferredSize(new Dimension(500, 250));
        frame.setLocation(new Point(200, 200));
        frame.pack();
        frame.setVisible(true);
    }

    //inner class to handle mouse events
    class MyMouseAdapter extends MouseAdapter {
        private boolean started = false;
        private LinkedList<Point> polyPoints = new LinkedList<>();
        @Override
        public void mousePressed(MouseEvent e) {
            if(!started) {
                startPoint = e.getPoint();
                started = true;
                polyPoints.add(e.getPoint());
                polyComp.getStart(e.getPoint());
            }else if(polyComp.checkPoly(startPoint.x,startPoint.y,e.getPoint().x,e.getPoint().y)){
                polyPoints.add(e.getPoint());
                Object[] array = polyPoints.toArray();
                polyComp.addNewObject(array);
                polyPoints.clear();
                polyComp.clearDrawObject();
                started = false;
            }else{
                polyComp.addDrawObject(e.getPoint().x,e.getPoint().y);
                polyPoints.add(e.getPoint());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }
    }

    public void showGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            buildGUI();
        });
    }
}



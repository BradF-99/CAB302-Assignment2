package main.java.gui;

import main.java.components.*;
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
    ComponentsClass comp = new ComponentsClass();
    private java.awt.Point startPoint;
    /**
     * This is just an example thread-safe GUI based off the example from the lecture.
     */
    private void buildGUI() {
        frame = new JFrame("Hello World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(comp);
        frame.getContentPane().addMouseListener(new MyMouseAdapter());
        frame.getContentPane().addMouseMotionListener(new MyMouseAdapter());
        frame.setPreferredSize(new Dimension(500, 250));
        frame.setLocation(new Point(200, 200));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Inner class for handling mouse events,
     * must call comp.repaint() to ensure that all the components are drawn correctly.
     */
    class MyMouseAdapter extends MouseAdapter {
        private boolean started = false;
        private LinkedList<Point> polyPoints = new LinkedList<>();
        @Override
        public void mousePressed(MouseEvent e) {
            if(!started) {
                startPoint = e.getPoint();
                started = true;
                polyPoints.add(e.getPoint());
                comp.polyComp.getStart(e.getPoint());
            }else if(comp.polyComp.checkPoly(startPoint.x,startPoint.y,e.getPoint().x,e.getPoint().y)){
                polyPoints.add(e.getPoint());
                Object[] array = polyPoints.toArray();
                comp.polyComp.addNewObject(array);
                polyPoints.clear();
                comp.polyComp.clearDrawObject();
                started = false;
                comp.repaint();
            }else{
                comp.polyComp.addDrawObject(e.getPoint().x,e.getPoint().y);
                polyPoints.add(e.getPoint());
                comp.repaint();
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



package main.java.gui;

import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import javax.swing.*;


public class MainWindow {
    private JFrame frame;
    ComponentsClass comp = new ComponentsClass(new Dimension(0,0));
    private java.awt.Point startPoint;
    private ShapesEnum.Shapes currentShape = ShapesEnum.Shapes.ELLIPSE;
    private Color selectedBorderColor = Color.RED;
    private Color selectedFillColor = Color.BLACK;
    private boolean filled = true;
    /**
     * This is just an example thread-safe GUI based off the example from the lecture.
     */
    private void buildGUI() {
        frame = new JFrame("Hello World");
        frame.getContentPane().add(comp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().addMouseListener(new MyMouseAdapter());
        frame.getContentPane().addMouseMotionListener(new MyMouseAdapter());
        frame.getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
                public void componentResized(ComponentEvent e) {
                    comp.setFrameSize(frame.getContentPane().getSize());
            }
        });
        frame.addKeyListener(new MyKeyAdapter());
        frame.setPreferredSize(new Dimension(500, 250));
        frame.setLocation(new Point(200, 200));
        frame.pack();
        frame.setVisible(true);
        comp.setFrameSize(frame.getSize());
    }

    /**
     * Inner class for handling mouse events,
     * must call comp.repaint() to ensure that all the components are drawn correctly.
     */
    class MyMouseAdapter extends MouseAdapter {
        private boolean started = false;
        private LinkedList<Point2D.Float> polyPoints = new LinkedList<>();
        @Override
        public void mousePressed(MouseEvent e) {
            Point2D.Float point = new Point2D.Float((float) e.getPoint().x/frame.getSize().width, (float) e.getPoint().y / frame.getSize().height);
            if(currentShape == ShapesEnum.Shapes.POLYGON) {
                if (!started) {
                    startPoint = e.getPoint();
                    started = true;
                    polyPoints.add(point);
                    comp.polyComp.getStart(point);
                } else if (comp.polyComp.checkPoly(startPoint.x, startPoint.y, e.getPoint().x, e.getPoint().y)) {
                    polyPoints.add(point);
                    Object[] array = polyPoints.toArray();
                    comp.polyComp.addNewObject(array,selectedBorderColor,filled,selectedFillColor);
                    polyPoints.clear();
                    comp.polyComp.clearDrawObject();
                    comp.addUndo(comp.polyComp.polygon.size() - 1, ShapesEnum.Shapes.POLYGON);
                    started = false;
                } else {
                    comp.polyComp.addDrawObject(comp.pointToFloat(e.getPoint().x,frame.getSize().width), comp.pointToFloat(e.getPoint().y,frame.getSize().height),selectedBorderColor);
                    polyPoints.add(point);
                }
            }else{
                //everything besides the polygon will only have two points so the start point doesnt change.
                startPoint = e.getPoint();
            }
            if(currentShape == ShapesEnum.Shapes.PLOT) {
                comp.plotComp.addNewObject(comp.pointToFloat(e.getPoint().x,frame.getSize().width), comp.pointToFloat(e.getPoint().y,frame.getSize().height), selectedBorderColor);
                comp.addUndo(comp.plotComp.plots.size() - 1, ShapesEnum.Shapes.PLOT);
            }
            comp.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(currentShape == ShapesEnum.Shapes.LINE){
                comp.lineComp.addNewObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height),
                        comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y,frame.getSize().height),selectedBorderColor);
                comp.lineComp.clearDrawObject();
                comp.addUndo(comp.lineComp.lines.size() - 1, ShapesEnum.Shapes.LINE);
            }else if(currentShape == ShapesEnum.Shapes.RECTANGLE){
                comp.rectComp.addNewObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height)
                        ,comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y, frame.getSize().height),selectedBorderColor,filled,selectedFillColor);
                comp.rectComp.clearDrawObject();
                comp.addUndo(comp.rectComp.shapes.size() - 1,ShapesEnum.Shapes.RECTANGLE);
            }else if(currentShape == ShapesEnum.Shapes.ELLIPSE){
                comp.ellComp.addNewObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height)
                        ,comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y, frame.getSize().height),selectedBorderColor,filled,selectedFillColor);
                comp.ellComp.clearDrawObject();
                comp.addUndo(comp.ellComp.shapes.size() - 1, ShapesEnum.Shapes.ELLIPSE);
            }
            comp.repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(currentShape == ShapesEnum.Shapes.LINE){
                comp.lineComp.clearDrawObject();
                comp.lineComp.addDrawObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height),
                        comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y,frame.getSize().height),selectedBorderColor);
            } else if(currentShape == ShapesEnum.Shapes.RECTANGLE){
                comp.rectComp.clearDrawObject();
                comp.rectComp.addDrawObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height)
                        ,comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y, frame.getSize().height),selectedBorderColor,filled,selectedFillColor);
            } else if (currentShape == ShapesEnum.Shapes.ELLIPSE){
                comp.ellComp.clearDrawObject();
                comp.ellComp.addDrawObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height)
                        ,comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y, frame.getSize().height),selectedBorderColor,filled,selectedFillColor);
            }
            comp.repaint();
        }
    }

    class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
                comp.Undo();
            }
            comp.repaint();
        }
    }

    public void showGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            buildGUI();
        });
    }
}



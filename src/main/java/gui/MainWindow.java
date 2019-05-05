package main.java.gui;

import main.java.components.LineComponent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


public class MainWindow {
    JFrame frame;
    private java.awt.Point startPoint;
    /**
     * This is just an example thread-safe GUI based off the example from the lecture.
     */
    private void buildGUI() {
        frame = new JFrame("Hello World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LineComponent lineComp = new LineComponent();
        frame.getContentPane().add(lineComp);
        frame.getContentPane().addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
               lineComp.addNewObject(startPoint.x,startPoint.y,e.getPoint().x,e.getPoint().y);
            }
        });

        frame.getContentPane().addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                lineComp.clearDrawObject();
                lineComp.addDrawObject(startPoint.x,startPoint.y,e.getPoint().x,e.getPoint().y);
            }
        });

        frame.setPreferredSize(new Dimension(500, 250));
        frame.setLocation(new Point(200, 200));
        frame.pack();
        frame.setVisible(true);
    }


    public void showGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            buildGUI();
        });
    }
}



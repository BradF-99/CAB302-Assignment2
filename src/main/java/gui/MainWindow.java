package main.java.gui;

import org.w3c.dom.css.ElementCSSInlineStyle;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


public class MainWindow {
    JFrame frame;
    /**
     * This is just an example thread-safe GUI based off the example from the lecture.
     */
    private void buildGUI() {
        frame = new JFrame("Hello World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel(main.java.Main.exampleFunc(true),SwingConstants.LEFT);
        frame.getContentPane().add(label);
        LineComponent lineComp = new LineComponent();
        frame.getContentPane().add(lineComp, BorderLayout.CENTER);
        frame.addMouseListener(new MouseAdapter(){
            private java.awt.Point startPoint;
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getPoint());
                startPoint = e.getPoint();
            }
            public void mouseReleased(MouseEvent e) {
                System.out.println(e.getPoint());
                lineComp.addNewLine(startPoint.x,startPoint.y,e.getPoint().x,e.getPoint().y);
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



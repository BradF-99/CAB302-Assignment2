package main.java.gui;

import java.awt.*;
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
        label.addMouseListener((new MyMouseAdapter()));
        frame.getContentPane().add(label);

        frame.setPreferredSize(new Dimension(500, 250));
        frame.setLocation(new Point(200, 200));
        frame.pack();
        frame.setVisible(true);
    }

    //inner MouseAdapter class for handling mouse inputs, removes the need for having every event implemented
    class MyMouseAdapter extends MouseAdapter {
        private java.awt.Point startPoint;
        private java.awt.Point endPoint;

        public void mousePressed(MouseEvent e) {
            System.out.println(e.getPoint());
            startPoint = e.getPoint();
        }
        public void mouseReleased(MouseEvent e) {
            System.out.println(e.getPoint());
            endPoint = e.getPoint();
            JLabel mousePoints = new JLabel(endPoint.toString(),SwingConstants.CENTER);
            frame.getContentPane().add(mousePoints);
            frame.getContentPane().revalidate();
        }


    }

    public void showGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            buildGUI();
        });
    }
}

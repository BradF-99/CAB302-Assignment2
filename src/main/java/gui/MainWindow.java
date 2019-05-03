package main.java.gui;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.*;


public class MainWindow {
    /**
     * This is just an example thread-safe GUI based off the example from the lecture.
     */
    private static void buildGUI() {
        JFrame frame = new JFrame("Hello World");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel(main.java.Main.exampleFunc(true),SwingConstants.CENTER);
        frame.getContentPane().add(label);

        frame.setPreferredSize(new Dimension(500, 250));
        frame.setLocation(new Point(200, 200));
        frame.pack();
        frame.setVisible(true);
    }

    public static void showGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            buildGUI();
        });
    }
}

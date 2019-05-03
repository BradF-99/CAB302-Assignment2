package main.java.gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

//create a Jcomponent line class this will be expanded later to be for other types
public class LineComponent extends JComponent {

    //Line class
    private static class Line{
        private int x1;
        private int y1;
        private int x2;
        private int y2;

        //constructor for the line class
        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
    //create a LinkedList of Lines to store all the lines
    private LinkedList<Line> lines = new LinkedList<Line>();

    public void addNewLine(int x1, int y1, int x2, int y2){
        lines.add(new Line(x1,y1,x2,y2));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //loop through all the lines in the LinkedList
        for (Line line : lines) {
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
    }
}

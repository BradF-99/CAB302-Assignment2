package main.java.components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class LineComponent extends JComponent implements ComponentInterface {

    //Line class
    public static class Line{
        public int x1;
        public int y1;
        public int x2;
        public int y2;

        //constructor for the line class
        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    //create a LinkedList of Lines to store all the lines
    private final LinkedList<Line> lines = new LinkedList<>();
    //LinkedList for the visual line whilst mouse is pressed
    private final LinkedList<Line> drawnLines = new LinkedList<>();

    public void addNewObject(int x1, int y1, int x2, int y2){
        this.lines.add(new Line(x1,y1,x2,y2));
        repaint();
    }

    public void clearObjects(){
        this.lines.clear();
        repaint();
    }


    public void addDrawObject(int x1, int y1, int x2, int y2){
        this.drawnLines.add(new Line(x1,y1,x2,y2));
        repaint();
    }

    public void clearDrawObject(){
        this.drawnLines.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //loop through all the lines in the LinkedList
        for (Line line : this.lines) {
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        for (Line line : this.drawnLines) {
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
    }
}

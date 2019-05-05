package main.java.components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class EllipseComponent extends JComponent implements ComponentInterface {

    private static class Ellipse{
        private int x;
        private int y;
        private int width;
        private int height;

        //constructor for the Ellipse class
        public Ellipse(int x1, int y1, int x2, int y2) {
            /*
            If the mouse is moved above or to the left of the starting position nothing would be drawn,
            if the mouse is above or left the Ellipse is now drawn from the current mouse position to the start position,
            if the mouse is below or right the Ellipse is drawn from the start position to the mouse.
             */
            this.x = x2 < x1 ? x2 : x1;
            this.y = y2 < y1 ? y2 : y1;
            this.width = x1-x2 < 0 ? x2-x1 : x1-x2;
            this.height = y1-y2 < 0 ? y2-y1 : y1-y2;
        }
    }

    //create a LinkedList of ellipses
    private final LinkedList<Ellipse> ellipses = new LinkedList<>();
    //LinkedList for the visual ellipses whilst mouse is pressed
    private final LinkedList<Ellipse> drawnEllipses = new LinkedList<>();

    public void addNewObject(int x1, int y1, int x2, int y2){
        this.ellipses.add(new Ellipse(x1,y1,x2,y2));
        repaint();
    }

    public void clearObjects(){
        this.ellipses.clear();
        repaint();
    }


    public void addDrawObject(int x1, int y1, int x2, int y2){
        this.drawnEllipses.add(new Ellipse(x1,y1,x2,y2));
        repaint();
    }

    public void clearDrawObject(){
        this.drawnEllipses.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //loop through all the rectangles in the LinkedList
        for (Ellipse ellipse : this.ellipses) {
            g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
        for (Ellipse ellipse : this.drawnEllipses) {
            g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
    }
}

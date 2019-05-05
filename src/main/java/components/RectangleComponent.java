package main.java.components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class RectangleComponent extends JComponent implements ComponentInterface {

    private static class Rectangle{
        private int x;
        private int y;
        private int width;
        private int height;

        //constructor for the rectangle class
        public Rectangle(int x1, int y1, int x2, int y2) {
            /*
            If the mouse is moved above or to the left of the starting position nothing would be drawn,
            if the mouse is above or left the rectangle is now drawn from the current mouse position to the start position,
            if the mouse is below or right the rectangle is drawn from the start position to the mouse.
             */
            this.x = x2 < x1 ? x2 : x1;
            this.y = y2 < y1 ? y2 : y1;
            this.width = x1-x2 < 0 ? x2-x1 : x1-x2;
            this.height = y1-y2 < 0 ? y2-y1 : y1-y2;
        }
    }

    //create a LinkedList of Rectangles
    private final LinkedList<Rectangle> rectangles = new LinkedList<>();
    //LinkedList for the visual rectangle whilst mouse is pressed
    private final LinkedList<Rectangle> drawnRectangles = new LinkedList<>();

    public void addNewObject(int x1, int y1, int x2, int y2){
        this.rectangles.add(new Rectangle(x1,y1,x2,y2));
        repaint();
    }

    public void clearObjects(){
        this.rectangles.clear();
        repaint();
    }


    public void addDrawObject(int x1, int y1, int x2, int y2){
        this.drawnRectangles.add(new Rectangle(x1,y1,x2,y2));
        repaint();
    }

    public void clearDrawObject(){
        this.drawnRectangles.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //loop through all the rectangles in the LinkedList
        for (Rectangle rect : this.rectangles) {
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
        for (Rectangle rect : this.drawnRectangles) {
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
    }
}

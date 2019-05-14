package main.java.components;

import java.awt.*;
import java.util.LinkedList;

public class EllipseComponent implements ComponentInterface {

    public static class Ellipse{
        public Float x;
        public Float y;
        public Float width;
        public Float height;
        public Color borderColor;
        public boolean filled;
        public Color fillColor;

        /**
         * Constructor for the Ellipse class
         *
         * If the mouse is moved above or to the left of the starting position nothing would be drawn,
         * if the mouse is above or left the Ellipse is now drawn from the current mouse position to the start position,
         * if the mouse is below or right the Ellipse is drawn from the start position to the mouse.
         *
         * @param x1 x-coordinate of first point
         * @param y1 y-coordinate of first point
         * @param x2 x-coordinate of second point
         * @param y2 y-coordinate of second point
         * @param borderColor Color of the object border
         * @param filled true if object is filled
         * @param fillColor Color the object will be filled in
         */
        public Ellipse(Float x1, Float y1, Float x2, Float y2, Color borderColor, boolean filled, Color fillColor) {
            /*
            If the mouse is moved above or to the left of the starting position nothing would be drawn,
            if the mouse is above or left the Ellipse is now drawn from the current mouse position to the start position,
            if the mouse is below or right the Ellipse is drawn from the start position to the mouse.
             */
            this.x = x2 < x1 ? x2 : x1;
            this.y = y2 < y1 ? y2 : y1;
            this.width = x1 - x2 < 0 ? x2 - x1 : x1 - x2;
            this.height = y1 - y2 < 0 ? y2 - y1 : y1 - y2;
            this.borderColor = borderColor;
            this.filled = filled;
            this.fillColor = fillColor;
        }
    }

    //create a LinkedList of ellipses
    public final LinkedList<Ellipse> ellipses = new LinkedList<>();
    //LinkedList for the visual ellipses whilst mouse is pressed
    public final LinkedList<Ellipse> drawnEllipses = new LinkedList<>();

    /**
     * Adds new ellipse to the list of ellipses
     *
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @param borderColor Color of the object border
     * @param filled true if object is filled
     * @param fillColor Color the object will be filled in
     */
    public void addNewObject(Float x1, Float y1, Float x2, Float y2, Color borderColor, boolean filled, Color fillColor){
        this.ellipses.add(new Ellipse(x1,y1,x2,y2,borderColor, filled, fillColor));
    }


    /**
     * clears the list of ellipses
     */
    public void clearObjects(){
        this.ellipses.clear();
    }

    /**
     * Clears a single entry in the list
     * @param index index of the list to be removed
     */
    public void clearObject(int index){
        this.ellipses.remove(index);
    }

    /**
     * Adds new ellipse to the drawnlist of ellipses
     *
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @param borderColor Color of the object border
     * @param filled true if object is filled
     * @param fillColor Color the object will be filled in
     */
    public void addDrawObject(Float x1, Float y1, Float x2, Float y2, Color borderColor, boolean filled, Color fillColor){
        this.drawnEllipses.add(new Ellipse(x1,y1,x2,y2,borderColor,filled,fillColor));
    }

    /**
     * clears the list of drawn ellipses
     */
    public void clearDrawObject(){
        this.drawnEllipses.clear();
    }
}

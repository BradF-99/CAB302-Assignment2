package main.java.components;

import java.awt.*;
import java.util.LinkedList;

public class LineComponent {

    //inner line class
    public static class Line{
        public Float x1;
        public Float y1;
        public Float x2;
        public Float y2;
        public Color color;

        /**
         * Constructor for the Line class
         * @param x1 x-coordinate of first point
         * @param y1 y-coordinate of first point
         * @param x2 x-coordinate of second point
         * @param y2 y-coordinate of second point
         * @param color Color of the object
         */
        public Line(Float x1, Float y1, Float x2, Float y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }

    //create a LinkedList of Lines to store all the lines
    public final LinkedList<Line> lines = new LinkedList<>();
    //LinkedList for the visual line whilst mouse is pressed
    public final LinkedList<Line> drawnLines = new LinkedList<>();

    /**
     * Adds new line to the list of lines
     *
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @param color Color of the object
     */
    public void addNewObject(Float x1, Float y1, Float x2, Float y2, Color color){
        this.lines.add(new Line(x1,y1,x2,y2,color));
    }

    /**
     * clears the list of lines
     */
    public void clearObjects(){
        this.lines.clear();
    }

    /**
     * Clears a single entry in the list
     * @param index index of the list to be removed
     */
    public void clearObject(int index){
        this.lines.remove(index);
    }

    /**
     * Adds new line to the drawnlist of lines
     *
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @param color Color of the object
     */
    public void addDrawObject(Float x1, Float y1, Float x2, Float y2,Color color){
        this.drawnLines.add(new Line(x1,y1,x2,y2,color));
    }

    /**
     * clears the list of drawn lines
     */
    public void clearDrawObject(){
        this.drawnLines.clear();
    }
}

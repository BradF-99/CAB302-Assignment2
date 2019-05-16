package main.java.components;

import java.awt.*;
import java.util.LinkedList;

public class ShapeComponent implements ComponentInterface {

    public static class Shape{
        public float x;
        public float y;
        public float width;
        public float height;
        public Color borderColor;
        public boolean filled;
        public Color fillColor;

        /**
         * Constructor for the Rectangle class
         *
         * If the mouse is moved above or to the left of the starting position nothing would be drawn,
         * if the mouse is above or left the Rectangle is now drawn from the current mouse position to the start position,
         * if the mouse is below or right the Rectangle is drawn from the start position to the mouse.
         *
         * @param x1 x-coordinate of first point
         * @param y1 y-coordinate of first point
         * @param x2 x-coordinate of second point
         * @param y2 y-coordinate of second point
         * @param borderColor Color of the object border
         * @param filled true if object is filled
         * @param fillColor Color the object will be filled in
         */
        //constructor for the rectangle class
        public Shape(Float x1, Float y1, Float x2, Float y2,Color borderColor, boolean filled, Color fillColor) {
            this.x = x2 < x1 ? x2 : x1;
            this.y = y2 < y1 ? y2 : y1;
            this.width = x1-x2 < 0 ? x2-x1 : x1-x2;
            this.height = y1-y2 < 0 ? y2-y1 : y1-y2;
            this.borderColor = borderColor;
            this.filled = filled;
            this.fillColor = fillColor;
        }
    }

    //create a LinkedList of Rectangles
    public final LinkedList<Shape> shapes = new LinkedList<>();
    //LinkedList for the visual rectangle whilst mouse is pressed
    public final LinkedList<Shape> drawnShapes = new LinkedList<>();

    /**
     * Adds new ellipse to the list of rectangles
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
        this.shapes.add(new Shape(x1,y1,x2,y2,borderColor,filled,fillColor));
    }

    /**
     * clears the list of rectangles
     */
    public void clearObjects(){
        this.shapes.clear();
    }

    /**
     * Clears a single entry in the list
     * @param index index of the list to be removed
     */
    public void clearObject(int index){
        this.shapes.remove(index);
    }
    /**
     * Adds new ellipse to the drawnlist of rectangles
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
        this.drawnShapes.add(new Shape(x1,y1,x2,y2,borderColor,filled,fillColor));
    }

    /**
     * clears the list of drawn rectangles
     */
    public void clearDrawObject(){
        this.drawnShapes.clear();
    }

}

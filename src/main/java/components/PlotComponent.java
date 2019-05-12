package main.java.components;

import java.awt.*;
import java.util.LinkedList;

public class PlotComponent {

    //Line class
    public static class Plot{
        public int x;
        public int y;
        public Color color;

        /**
         * Constructor for the Line class
         * @param x x-coordinate of first point
         * @param y y-coordinate of first point
         * @param color Color of the object
         */
        public Plot(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    //create a LinkedList of Plots to store all the lines
    public final LinkedList<Plot> plots = new LinkedList<>();
    /**
     * Adds new plot to the list of plots
     *
     * @param x x-coordinate of first point
     * @param y y-coordinate of first point
     * @param color Color of the object
     */
    public void addNewObject(int x, int y, Color color){
        this.plots.add(new Plot(x,y,color));
    }

    /**
     * clears the list of plots
     */
    public void clearObjects(){
        this.plots.clear();
    }

    /**
     * Clears a single entry in the list
     * @param index index of the list to be removed
     */
    public void clearObject(int index){
        this.plots.remove(index);
    }
}

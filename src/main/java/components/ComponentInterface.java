package main.java.components;

import java.awt.*;

public interface ComponentInterface {

    /**
     * Adds new object to the list of objects
     *
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @param filled true if object is filled
     * @param borderColor Color of the object border
     * @param fillColor Color the object will be filled in
     */
    void addNewObject(int x1, int y1, int x2, int y2, Color borderColor, boolean filled, Color fillColor);

    /**
     * clears the list of objects
     */
    void clearObjects();

    /**
     * Adds new object to the temporary draw list of objects
     *
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @param filled true if object is filled
     * @param borderColor Color of the object border
     * @param fillColor Color the object will be filled in
     */
    void addDrawObject(int x1, int y1, int x2, int y2, Color borderColor, boolean filled, Color fillColor);

    /**
     * clears the list of temporary drawn objects
     */
    void clearDrawObject();
}

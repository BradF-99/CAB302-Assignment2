package main.java.components;
public interface ComponentInterface {

    /**
     * Adds new object to the list of objects
     *
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     */
    void addNewObject(int x1, int y1, int x2, int y2);

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
     */
    void addDrawObject(int x1, int y1, int x2, int y2);

    /**
     * clears the list of temporary drawn objects
     */
    void clearDrawObject();
}

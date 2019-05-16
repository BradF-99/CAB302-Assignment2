package main.java.components;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.LinkedList;

public class ComponentsClass extends JComponent {
    public LineComponent lineComp = new LineComponent();
    public ShapeComponent ellComp = new ShapeComponent();
    public ShapeComponent rectComp = new ShapeComponent();
    public PolygonComponent polyComp = new PolygonComponent();
    public PlotComponent plotComp = new PlotComponent();
    private Dimension frameSize;

    /**
     * Constructor for ComponentClass;
     * @param frameSize size of the frame
     */
    public ComponentsClass(Dimension frameSize){
        this.frameSize = frameSize;
    }

    /**
     * inner class for the undoList
     */
    public class undoListHelper {
        public int index;
        public ShapesEnum.Shapes component;

        /**
         * Constructor for undoListHelper
         * @param index index
         * @param shape shape type
         */
        public undoListHelper(int index, ShapesEnum.Shapes shape){
            this.index = index;
            this.component = shape;
        }
    }

    /**
     * Adds a listing to the undoList
     *
     * @param index index of the list added
     * @param shape shape type
     */
    public void addUndo (int index, ShapesEnum.Shapes shape){
        undoList.add(new undoListHelper(index, shape));
    }

    /**
     * Gets and sets frameSize (called by the frameresize event)
     * @param frameSize Dimension of the frame
     */
    public void setFrameSize(Dimension frameSize){
        this.frameSize = frameSize;
    }

    /**
     * Removes the last image drawn
     */
    public void Undo(){
        if(undoList.size() != 0){
            switch (undoList.getLast().component){
                case PLOT:
                    plotComp.clearObject(undoList.getLast().index);
                    break;
                case LINE:
                    lineComp.clearObject(undoList.getLast().index);
                    break;
                case RECTANGLE:
                    rectComp.clearObject(undoList.getLast().index);
                    break;
                case ELLIPSE:
                    ellComp.clearObject(undoList.getLast().index);
                    break;
                case POLYGON:
                    polyComp.clearObject(undoList.getLast().index);
                    break;
            }
            undoList.removeLast();
        }
    }

    //List of drawn images
    public LinkedList<undoListHelper> undoList = new LinkedList<>();

    /**
     * Loops through all the paintable components and draws them.
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        //polygons
        for(PolygonComponent.Polygon poly : polyComp.polygon){
            Path2D.Float polygon = polyComp.createScaledPolygon(poly.pointArray, frameSize);
            if(poly.filled){
                g2d.setColor(poly.fillColor);
                g2d.fill(polygon);
            }
            g2d.setColor(poly.borderColor);
            g2d.draw(polygon);
        }
        for(LineComponent.Line line : polyComp.drawnLines) {
            int[] points = floatToPoint(line.x1, line.y1, line.x2, line.y2);
            g2d.setColor(line.color);
            g2d.drawLine(points[0],points[1],points[2],points[3]);
        }
        //rectangles
        for (ShapeComponent.Shape rect : rectComp.drawnShapes) {
            int[] points = floatToPoint(rect.x, rect.y, rect.width, rect.height);
            if(rect.filled) {
                g2d.setColor(rect.fillColor);
                g2d.fillRect(points[0],points[1],points[2],points[3]);
            }
            g2d.setColor(rect.borderColor);
            g2d.drawRect(points[0],points[1],points[2],points[3]);
        }
        for (ShapeComponent.Shape rect : rectComp.shapes) {
            int[] points = floatToPoint(rect.x, rect.y, rect.width, rect.height);
            if(rect.filled) {
                g2d.setColor(rect.fillColor);
                g2d.fillRect(points[0],points[1],points[2],points[3]);
            }
            g2d.setColor(rect.borderColor);
            g2d.drawRect(points[0],points[1],points[2],points[3]);
        }
        //lines
        for (LineComponent.Line line : lineComp.lines) {
            int[] points = floatToPoint(line.x1, line.y1, line.x2, line.y2);
            g2d.setColor(line.color);
            g2d.drawLine(points[0],points[1],points[2],points[3]);
        }

        for (LineComponent.Line line : lineComp.drawnLines) {
            int[] points = floatToPoint(line.x1, line.y1, line.x2, line.y2);
            g2d.setColor(line.color);
            g2d.drawLine(points[0],points[1],points[2],points[3]);
        }
        //ellipse
        for (ShapeComponent.Shape ellipse : ellComp.drawnShapes){
            int[] points = floatToPoint(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
            if(ellipse.filled) {
                g2d.setColor(ellipse.fillColor);
                g2d.fillOval(points[0],points[1],points[2],points[3]);
            }
            g2d.setColor(ellipse.borderColor);
            g2d.drawOval(points[0],points[1],points[2],points[3]);
        }
        for (ShapeComponent.Shape ellipse : ellComp.shapes) {
            int[] points = floatToPoint(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
            if(ellipse.filled) {
                g2d.setColor(ellipse.fillColor);
                g2d.fillOval(points[0],points[1],points[2],points[3]);
            }
            g2d.setColor(ellipse.borderColor);
            g2d.drawOval(points[0],points[1],points[2],points[3]);
        }
        //plot
        for (PlotComponent.Plot plot : plotComp.plots){
            g2d.setColor(plot.color);
            g2d.drawLine((int) (plot.x * frameSize.width), (int) (plot.y * frameSize.height), (int) (plot.x * frameSize.width), (int) (plot.y * frameSize.height));
        }
    }

    /**
     * converts a point to the float value based on the screen size
     * @param point point to be converted
     * @param screenSize size of the screen
     * @return returns a float value
     */
    public float pointToFloat(int point, int screenSize){
        return (float) point/ screenSize;
    }

    /**
     * converts floats to Point to be drawn by 2DGraphics
     * @param x1 x-coordinate of first point
     * @param y1 y-coordinate of first point
     * @param x2 x-coordinate of second point
     * @param y2 y-coordinate of second point
     * @return returns an array of points
     */
    public int[] floatToPoint(Float x1, Float y1, Float x2, Float y2){
        int[] points = {(int) (x1 * frameSize.width) ,(int) (y1 * frameSize.height), (int) (x2 * frameSize.width), (int) (y2 * frameSize.height)};
        return points;
    }
}

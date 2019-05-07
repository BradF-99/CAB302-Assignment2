package main.java.components;

import javax.swing.*;
import java.awt.*;

public class ComponentsClass extends JComponent {
    public LineComponent lineComp = new LineComponent();
    public EllipseComponent ellComp = new EllipseComponent();
    public RectangleComponent rectComp = new RectangleComponent();
    public PolygonComponent polyComp = new PolygonComponent();

    /**
     * Loops through all the paintable components and draws them.
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Polygon poly : polyComp.polygon) {
            g.drawPolygon(poly);
        }
        for(LineComponent.Line line : polyComp.drawnLines) {
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        for (RectangleComponent.Rectangle rect : rectComp.rectangles) {
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
        for (RectangleComponent.Rectangle rect : rectComp.drawnRectangles) {
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
        for (LineComponent.Line line : lineComp.lines) {
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        for (LineComponent.Line line : lineComp.drawnLines) {
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        for (EllipseComponent.Ellipse ellipse : ellComp.ellipses) {
            g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
        for (EllipseComponent.Ellipse ellipse : ellComp.drawnEllipses) {
            g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
    }
}

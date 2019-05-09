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
        for (int i = 0; i < polyComp.polygon.size(); i++) {
            g.setColor(polyComp.polyColour.get(i));
            g.drawPolygon(polyComp.polygon.get(i));
        }

        for(LineComponent.Line line : polyComp.drawnLines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        for (RectangleComponent.Rectangle rect : rectComp.rectangles) {
            g.setColor(rect.color);
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
        for (RectangleComponent.Rectangle rect : rectComp.drawnRectangles) {
            g.setColor(rect.color);
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
        for (LineComponent.Line line : lineComp.lines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        for (LineComponent.Line line : lineComp.drawnLines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        for (EllipseComponent.Ellipse ellipse : ellComp.ellipses) {
            g.setColor(ellipse.color);
            g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
        for (EllipseComponent.Ellipse ellipse : ellComp.drawnEllipses) {
            g.setColor(ellipse.color);
            g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
    }
}

package main.java.components;

import javax.swing.*;
import java.awt.*;

public class ComponentsClass extends JComponent {
    public LineComponent lineComp = new LineComponent();
    public EllipseComponent ellComp = new EllipseComponent();
    public RectangleComponent rectComp = new RectangleComponent();
    public PolygonComponent polyComp = new PolygonComponent();
    public PlotComponent plotComp = new PlotComponent();

    /**
     * Loops through all the paintable components and draws them.
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //polygons
        for (int i = 0; i < polyComp.polygon.size(); i++) {
            if(polyComp.polygon.get(i).filled){
                g.setColor(polyComp.polygon.get(i).fillColor);
                g.fillPolygon(polyComp.polygon.get(i).polygon);
            }
            g.setColor(polyComp.polygon.get(i).borderColor);
            g.drawPolygon(polyComp.polygon.get(i).polygon);
        }
        for(LineComponent.Line line : polyComp.drawnLines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        //rectangles
        for (RectangleComponent.Rectangle rect : rectComp.rectangles) {
            if(rect.filled) {
                g.setColor(rect.fillColor);
                g.fillRect(rect.x,rect.y,rect.width, rect.height);
            }
            g.setColor(rect.borderColor);
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
        for (RectangleComponent.Rectangle rect : rectComp.drawnRectangles) {
            if(rect.filled) {
                g.setColor(rect.fillColor);
                g.fillRect(rect.x,rect.y,rect.width, rect.height);
            }
            g.setColor(rect.borderColor);
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
        //lines
        for (LineComponent.Line line : lineComp.lines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        for (LineComponent.Line line : lineComp.drawnLines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
        //ellipse
        for (EllipseComponent.Ellipse ellipse : ellComp.ellipses) {
            if(ellipse.filled) {
                g.setColor(ellipse.fillColor);
                g.fillOval(ellipse.x,ellipse.y,ellipse.width, ellipse.height);
            }
            g.setColor(ellipse.borderColor);
            g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
        for (EllipseComponent.Ellipse ellipse : ellComp.drawnEllipses) {
            if(ellipse.filled) {
                g.setColor(ellipse.fillColor);
                g.fillOval(ellipse.x,ellipse.y,ellipse.width, ellipse.height);
            }
            g.setColor(ellipse.borderColor);
            g.drawOval(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
        //plot
        for (PlotComponent.Plot plot : plotComp.plots){
            g.setColor(plot.color);
            g.drawLine(plot.x,plot.y,plot.x,plot.y);
        }
    }
}

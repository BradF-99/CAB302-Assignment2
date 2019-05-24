package main.java.gui;

import javafx.embed.swing.JFXPanel;
import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.*;


public class MainWindow {
    private JFrame frame;
    private JMenuBar mainMenu;
    private JSplitPane mainDisplay;
    private JPanel sideBar;
    private JPanel drawingBoard;
    ComponentsClass comp;
    private java.awt.Point startPoint;
    private ShapesEnum.Shapes currentShape = ShapesEnum.Shapes.ELLIPSE;
    private Color selectedBorderColor = Color.RED;
    private Color selectedFillColor = Color.BLACK;
    private boolean filled = true;


    /**
     * This is just an example thread-safe GUI based off the example from the lecture.
     */
    private void buildGUI() {
        //Create Core GUI Components
        frame = new JFrame("Hello World");
        mainMenu = new JMenuBar();
        mainDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        sideBar = new JPanel();
        drawingBoard = new JPanel();
        comp = new ComponentsClass(new Dimension(0,0));

        //Create menu components
        JMenu fileDropdown = new JMenu("File Options"); //dropdown title
        JMenu additionalDropdown = new JMenu("Additional Commands");
        String[] fileCmds = {"Save file", "Open file"}; //List of options in dropdown
        String[] additionalCmds = {"Undo", "Export BMP"};
        for (String cmd : fileCmds){
            fileDropdown.add(cmd);
        }
        for(String cmd : additionalCmds){
            additionalDropdown.add(cmd);
        }
        mainMenu.add(fileDropdown);
        mainMenu.add(additionalDropdown);

        //Create sideBar components
        mainDisplay.setLeftComponent(sideBar);
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.PAGE_AXIS));
        String[] sideBarBtns = {"Plot", "Line", "Rectangle", "Ellipse", "Polygon", "Fill Colour", "Pen Colour"};
        for (String btn : sideBarBtns){
            sideBar.add(new JButton(btn));
        }

        //Create drawingBoard components
        mainDisplay.setRightComponent(drawingBoard);
        drawingBoard.setLayout(new BoxLayout(drawingBoard, BoxLayout.PAGE_AXIS));
        drawingBoard.add(comp);
        comp.setFrameSize(drawingBoard.getSize());

        //Add event handlers
        fileDropdown.getMenuComponent(0).addMouseListener(new MyMenuMouseAdapter());
        fileDropdown.getMenuComponent(1).addMouseListener(new MyMenuMouseAdapter());
        additionalDropdown.getMenuComponent(0).addMouseListener(new MyMenuMouseAdapter());
        sideBar.getComponents()[0].addMouseListener(new MyMenuMouseAdapter());
        sideBar.getComponents()[1].addMouseListener(new MyMenuMouseAdapter());
        sideBar.getComponents()[2].addMouseListener(new MyMenuMouseAdapter());
        sideBar.getComponents()[3].addMouseListener(new MyMenuMouseAdapter());
        sideBar.getComponents()[4].addMouseListener(new MyMenuMouseAdapter());
        frame.addComponentListener(new MyWindowListener()); //needed to set the divider by % of screen
        sideBar.addComponentListener(new MySideBarListener());
        drawingBoard.addMouseListener(new MyMouseAdapter());
        frame.addKeyListener(new MyKeyAdapter());

        //add frame settings
        frame.setJMenuBar(mainMenu);
        frame.add(mainDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setVisible(true);
    }

    /**
     * Inner class for handling mouse events,
     * must call comp.repaint() to ensure that all the components are drawn correctly.
     */
    class MyMouseAdapter extends MouseAdapter {
        private boolean started = false;
        private LinkedList<Point2D.Float> polyPoints = new LinkedList<>();
        @Override
        public void mousePressed(MouseEvent e) {
            Point2D.Float point = new Point2D.Float((float) e.getPoint().x/frame.getSize().width, (float) e.getPoint().y / frame.getSize().height);
            if(currentShape == ShapesEnum.Shapes.POLYGON) {
                if (!started) {
                    startPoint = e.getPoint();
                    started = true;
                    polyPoints.add(point);
                    comp.polyComp.getStart(point);
                } else if (comp.polyComp.checkPoly(startPoint.x, startPoint.y, e.getPoint().x, e.getPoint().y)) {
                    polyPoints.add(point);
                    Object[] array = polyPoints.toArray();
                    comp.polyComp.addNewObject(array,selectedBorderColor,filled,selectedFillColor);
                    polyPoints.clear();
                    comp.polyComp.clearDrawObject();
                    comp.addUndo(comp.polyComp.polygon.size() - 1, ShapesEnum.Shapes.POLYGON);
                    started = false;
                } else {
                    comp.polyComp.addDrawObject(comp.pointToFloat(e.getPoint().x,frame.getSize().width), comp.pointToFloat(e.getPoint().y,frame.getSize().height),selectedBorderColor);
                    polyPoints.add(point);
                }
            }else{
                //everything besides the polygon will only have two points so the start point doesnt change.
                startPoint = e.getPoint();
            }
            if(currentShape == ShapesEnum.Shapes.PLOT) {
                comp.plotComp.addNewObject(comp.pointToFloat(e.getPoint().x,frame.getSize().width), comp.pointToFloat(e.getPoint().y,frame.getSize().height), selectedBorderColor);
                comp.addUndo(comp.plotComp.plots.size() - 1, ShapesEnum.Shapes.PLOT);
            }
            comp.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(currentShape == ShapesEnum.Shapes.LINE){
                comp.lineComp.addNewObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height),
                        comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y,frame.getSize().height),selectedBorderColor);
                comp.lineComp.clearDrawObject();
                comp.addUndo(comp.lineComp.lines.size() - 1, ShapesEnum.Shapes.LINE);
            }else if(currentShape == ShapesEnum.Shapes.RECTANGLE){
                comp.rectComp.addNewObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height)
                        ,comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y, frame.getSize().height),selectedBorderColor,filled,selectedFillColor);
                comp.rectComp.clearDrawObject();
                comp.addUndo(comp.rectComp.shapes.size() - 1,ShapesEnum.Shapes.RECTANGLE);
            }else if(currentShape == ShapesEnum.Shapes.ELLIPSE){
                comp.ellComp.addNewObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height)
                        ,comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y, frame.getSize().height),selectedBorderColor,filled,selectedFillColor);
                comp.ellComp.clearDrawObject();
                comp.addUndo(comp.ellComp.shapes.size() - 1, ShapesEnum.Shapes.ELLIPSE);
            }
            comp.repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(currentShape == ShapesEnum.Shapes.LINE){
                comp.lineComp.clearDrawObject();
                comp.lineComp.addDrawObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height),
                        comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y,frame.getSize().height),selectedBorderColor);
            } else if(currentShape == ShapesEnum.Shapes.RECTANGLE){
                comp.rectComp.clearDrawObject();
                comp.rectComp.addDrawObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height)
                        ,comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y, frame.getSize().height),selectedBorderColor,filled,selectedFillColor);
            } else if (currentShape == ShapesEnum.Shapes.ELLIPSE){
                comp.ellComp.clearDrawObject();
                comp.ellComp.addDrawObject(comp.pointToFloat(startPoint.x,frame.getSize().width),comp.pointToFloat(startPoint.y,frame.getSize().height)
                        ,comp.pointToFloat(e.getPoint().x,frame.getSize().width),comp.pointToFloat(e.getPoint().y, frame.getSize().height),selectedBorderColor,filled,selectedFillColor);
            }
            comp.repaint();
        }
    }

    /**
     * MyMenuMouseAdapter handles all mouse presses corresponding to the menu & options commands
     */

    class MyMenuMouseAdapter extends MouseAdapter{
        /**
         * This override of mousePressed checks
         * the menu pressed, and then takes appropriate action.
         * @param e the MouseEvent, used to check what component was pressed
         */
        @Override
        public void mousePressed(MouseEvent e){
            Component pressedComp = e.getComponent();
            JMenu fileOpt = mainMenu.getMenu(0);
            JMenu additionalOpt = mainMenu.getMenu(1);
            Component[] sideBarButtons = sideBar.getComponents();
            if (pressedComp == additionalOpt.getMenuComponent(0)){
                comp.Undo();
                comp.repaint();
            }
            else if (pressedComp == fileOpt.getMenuComponent(0)){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(frame);
            }
            else if (pressedComp == fileOpt.getMenuComponent(1)){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(frame);
            }
            else if (pressedComp == sideBarButtons[0]){
                currentShape = ShapesEnum.Shapes.PLOT;
            }
            else if (pressedComp == sideBarButtons[1]){
                currentShape = ShapesEnum.Shapes.LINE;
            }
            else if (pressedComp == sideBarButtons[2]){
                currentShape = ShapesEnum.Shapes.RECTANGLE;
            }
            else if (pressedComp == sideBarButtons[3]){
                currentShape = ShapesEnum.Shapes.ELLIPSE;
            }
            else if (pressedComp == sideBarButtons[4]){
                currentShape = ShapesEnum.Shapes.POLYGON;
            }


        }
    }

    class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
                comp.Undo();
            }
            comp.repaint();
            System.out.println(111);
        }
    }

    /**
     * MyWindowListener sets the position of the divider dynamically based on % of current
     * screen size. ComponentAdapter is extended as it already provides the componentResized method to be overloaded
     */
    class MyWindowListener extends ComponentAdapter{
        @Override
        public void componentResized(ComponentEvent e){
            windowChangeActions();
        }

        @Override
        public void componentShown(ComponentEvent e){
            windowChangeActions();
        }

        public void windowChangeActions(){
            mainDisplay.setDividerLocation(0.14);
            comp.setFrameSize(drawingBoard.getSize());
        }
    }

    /**
     * MySideBarListener checks the width of the sidebar, and sets the buttons to that width.
     * ComponentAdapter is extended as it already provides the componentResized method to be overloaded
     */
    class MySideBarListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            windowChangeActions();
        }

        @Override
        public void componentShown(ComponentEvent e) {
            windowChangeActions();
        }

        public void windowChangeActions() {
            int newWidth = sideBar.getWidth();
            for (Component button : sideBar.getComponents()) {
                int currentHeight = button.getHeight();
                button.setSize(newWidth, currentHeight);
            }
            comp.setFrameSize(drawingBoard.getSize());
        }
    }

    public void showGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            buildGUI();
        });
    }
}



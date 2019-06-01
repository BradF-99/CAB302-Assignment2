package main.java.gui;

//import javafx.scene.input.KeyCode; // seems to be breaking travis ci
import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.*;

/**
 * MainWindow constitues the GUI, and as such contains all of the graphical and event handler configurations and
 * declarations.
 */
public class MainWindow {
    private JFrame frame;
    private JMenuBar mainMenu;
    private JSplitPane mainDisplay;
    private JPanel sideBar;
    private JScrollPane sideBarScroll;
    private ButtonGroup btnGroup;
    Component[] sideBarComps;
    private JPanel drawingBoard;
    ComponentsClass comp;
    private JColorChooser colorChooser;
    private java.awt.Point startPoint;
    private ShapesEnum.Shapes currentShape = ShapesEnum.Shapes.ELLIPSE;
    private Color selectedBorderColor = Color.RED;
    private Color selectedFillColor = Color.BLACK;
    private boolean filled = true;
    private boolean undoHistoryActive = false;
    private LinkedList<ComponentsClass.undoListHelper> undoHistoryStore;
    private int undoHistoryNum;

    /**
     * buildGUI instantiates all of the components of the GUI, configures their graphical settings and adds event
     * listeners.
     */
    private void buildGUI() {
        //Create Core GUI Components
        comp = new ComponentsClass(new Dimension(0,0));
        frame = new JFrame("Drawing Program");
        mainMenu = new JMenuBar();
        mainDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        sideBar = new JPanel();
        sideBarScroll = new JScrollPane(sideBar);
        btnGroup = new ButtonGroup();
        sideBarComps = new Component[]{sideBar, sideBarScroll};
        drawingBoard = new JPanel();
        colorChooser = new JColorChooser();
        undoHistoryStore = comp.undoList;
        undoHistoryNum = 0;

        //Create menu components
        String[] dropdownTitle = {"File Options", "Picture Commands", "Drawing Tools", "Colour Tools"};
        String[] fileCmds = {"Save File (ctrl+s)", "Open File (ctrl+o)", "Export BMP (ctrl+b)"}; //List of options in dropdown
        String[] additionalCmds = {"Undo (ctrl+z)", "Show Undo History (ctrl+h)", "Confirm Selected History (ctrl+r)"};
        String[] drawingCmds = {"Plot", "Line", "Rectangle", "Ellipse", "Polygon"};
        String[] colorCmds = {"Fill Colour", "Pen Colour"};
        for (String title : dropdownTitle){
            mainMenu.add(new JMenu(title));
        }
        for (String cmd : fileCmds){
            mainMenu.getMenu(0).add(new JMenuItem(cmd));
        }
        for(String cmd : additionalCmds){
            mainMenu.getMenu(1).add(new JMenuItem(cmd));
        }
        for (String cmd : drawingCmds){
            mainMenu.getMenu(2).add(new JMenuItem(cmd));
        }
        for (String cmd : colorCmds){
            mainMenu.getMenu(3).add(new JMenuItem(cmd));
        }
        //Create sideBar components
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.PAGE_AXIS));
        sideBarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainDisplay.setLeftComponent(sideBarScroll);


        //Create drawingBoard components
        mainDisplay.setRightComponent(drawingBoard);
        drawingBoard.setLayout(new BoxLayout(drawingBoard, BoxLayout.PAGE_AXIS));
        drawingBoard.add(comp);
        comp.setFrameSize(drawingBoard.getSize());

        //Add event handlers
        for (int index = 0; index < mainMenu.getMenuCount(); index++){
            JMenu dropdown = mainMenu.getMenu(index);
            for (Component dropdownCmd : dropdown.getMenuComponents()){
                dropdownCmd.addMouseListener(new MyMenuMouseAdapter());
                dropdownCmd.addKeyListener(new MyKeyAdapter());
            }
        }
        drawingBoard.addMouseListener(new MyMouseAdapter());
        drawingBoard.addMouseMotionListener(new MyMouseAdapter());
        frame.addKeyListener(new MyKeyAdapter());
        frame.addComponentListener(new MyWindowListener());

        //add frame and mainDisplay settings
        frame.setJMenuBar(mainMenu);
        frame.add(mainDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setFocusable(true);
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
            Dimension windowSize = new Dimension(drawingBoard.getSize());
            Point2D.Float point = new Point2D.Float((float) e.getPoint().x/windowSize.width, (float) e.getPoint().y / windowSize.height);
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
                    comp.polyComp.addDrawObject(comp.pointToFloat(e.getPoint().x,windowSize.width), comp.pointToFloat(e.getPoint().y,windowSize.height),selectedBorderColor);
                    polyPoints.add(point);
                }
            }else{
                //everything besides the polygon will only have two points so the start point doesnt change.
                startPoint = e.getPoint();
            }
            if(currentShape == ShapesEnum.Shapes.PLOT) {
                comp.plotComp.addNewObject(comp.pointToFloat(e.getPoint().x,windowSize.width), comp.pointToFloat(e.getPoint().y,windowSize.height), selectedBorderColor);
                comp.addUndo(comp.plotComp.plots.size() - 1, ShapesEnum.Shapes.PLOT);
            }
            comp.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Dimension windowSize = new Dimension(drawingBoard.getSize());
            if(currentShape == ShapesEnum.Shapes.LINE){
                comp.lineComp.addNewObject(comp.pointToFloat(startPoint.x,windowSize.width),comp.pointToFloat(startPoint.y,windowSize.height),
                        comp.pointToFloat(e.getPoint().x,windowSize.width),comp.pointToFloat(e.getPoint().y,windowSize.height),selectedBorderColor);
                comp.lineComp.clearDrawObject();
                comp.addUndo(comp.lineComp.lines.size() - 1, ShapesEnum.Shapes.LINE);
            }else if(currentShape == ShapesEnum.Shapes.RECTANGLE){
                comp.rectComp.addNewObject(comp.pointToFloat(startPoint.x,windowSize.width),comp.pointToFloat(startPoint.y,windowSize.height)
                        ,comp.pointToFloat(e.getPoint().x,windowSize.width),comp.pointToFloat(e.getPoint().y, windowSize.height),selectedBorderColor,filled,selectedFillColor);
                comp.rectComp.clearDrawObject();
                comp.addUndo(comp.rectComp.shapes.size() - 1,ShapesEnum.Shapes.RECTANGLE);
            }else if(currentShape == ShapesEnum.Shapes.ELLIPSE){
                comp.ellComp.addNewObject(comp.pointToFloat(startPoint.x,windowSize.width),comp.pointToFloat(startPoint.y,windowSize.height)
                        ,comp.pointToFloat(e.getPoint().x,windowSize.width),comp.pointToFloat(e.getPoint().y, windowSize.height),selectedBorderColor,filled,selectedFillColor);
                comp.ellComp.clearDrawObject();
                comp.addUndo(comp.ellComp.shapes.size() - 1, ShapesEnum.Shapes.ELLIPSE);
            }
            comp.repaint();
            MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
            MenuCommands.refreshComps(sideBarComps);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Dimension windowSize = new Dimension(drawingBoard.getSize());
            if(currentShape == ShapesEnum.Shapes.LINE){
                comp.lineComp.clearDrawObject();
                comp.lineComp.addDrawObject(comp.pointToFloat(startPoint.x,windowSize.width),comp.pointToFloat(startPoint.y,windowSize.height),
                        comp.pointToFloat(e.getPoint().x,windowSize.width),comp.pointToFloat(e.getPoint().y,windowSize.height),selectedBorderColor);
            } else if(currentShape == ShapesEnum.Shapes.RECTANGLE){
                comp.rectComp.clearDrawObject();
                comp.rectComp.addDrawObject(comp.pointToFloat(startPoint.x,windowSize.width),comp.pointToFloat(startPoint.y,windowSize.height)
                        ,comp.pointToFloat(e.getPoint().x,windowSize.width),comp.pointToFloat(e.getPoint().y, windowSize.height),selectedBorderColor,filled,selectedFillColor);
            } else if (currentShape == ShapesEnum.Shapes.ELLIPSE){
                comp.ellComp.clearDrawObject();
                comp.ellComp.addDrawObject(comp.pointToFloat(startPoint.x,windowSize.width),comp.pointToFloat(startPoint.y,windowSize.height)
                        ,comp.pointToFloat(e.getPoint().x,windowSize.width),comp.pointToFloat(e.getPoint().y, windowSize.height),selectedBorderColor,filled,selectedFillColor);
            }
            comp.repaint();
        }
    }

    /**
     * MyMenuMouseAdapter handles all mouse presses corresponding to the menu & options commands
     */
    class MyMenuMouseAdapter extends MouseAdapter{
        /**
         * This eventListener checks for what button is pressed, and then calls
         * the appropriate static method from MenuCommands (e.g. Undo will call MenuCommands.undo()
         *
         * @param e the MouseEvent, used to check what component was pressed
         */
        @Override
        public void mousePressed(MouseEvent e){
            Component pressedComp = e.getComponent();
            JMenu fileOpt = mainMenu.getMenu(0);
            JMenu additionalOpt = mainMenu.getMenu(1);
            JMenu drawingOpt = mainMenu.getMenu(2);
            JMenu colorOpt = mainMenu.getMenu(3);
            if (pressedComp == additionalOpt.getMenuComponent(0)){
                MenuCommands.undo(comp, frame, sideBar, undoHistoryActive);
                MenuCommands.refreshComps(sideBarComps);
            }
            else if (pressedComp == additionalOpt.getMenuComponent(1)){
                undoHistoryActive = MenuCommands.showUndoHistory(frame, sideBar, drawingBoard,
                        new UndoHistorySelectingShapes(), undoHistoryActive);
                undoHistoryStore = MenuCommands.saveUndoList(comp);

            }
            else if (pressedComp == additionalOpt.getMenuComponent(2)){
                undoHistoryActive = MenuCommands.editUndoHistory(frame, sideBar, drawingBoard, btnGroup, comp,
                        new MyMouseAdapter(), new MyMouseAdapter(), undoHistoryStore, undoHistoryNum, undoHistoryActive);
                MenuCommands.refreshComps(sideBarComps);
            }
            else if (pressedComp == fileOpt.getMenuComponent(0)){
                MenuCommands.saveFile(frame);
            }
            else if (pressedComp == fileOpt.getMenuComponent(1)){
                MenuCommands.openFile(frame);
            }
            else if (pressedComp == fileOpt.getMenuComponent(2)){
                MenuCommands.exportBMP(drawingBoard);
            }
            else if (pressedComp == drawingOpt.getMenuComponent(0)){
                currentShape = MenuCommands.changeShape(ShapesEnum.Shapes.PLOT);
            }
            else if (pressedComp == drawingOpt.getMenuComponent(1)){
                currentShape = MenuCommands.changeShape(ShapesEnum.Shapes.LINE);
            }
            else if (pressedComp == drawingOpt.getMenuComponent(2)){
                currentShape = MenuCommands.changeShape(ShapesEnum.Shapes.RECTANGLE);
            }
            else if (pressedComp == drawingOpt.getMenuComponent(3)){
                currentShape = MenuCommands.changeShape(ShapesEnum.Shapes.ELLIPSE);
            }
            else if (pressedComp == drawingOpt.getMenuComponent(4)){
                currentShape = MenuCommands.changeShape(ShapesEnum.Shapes.POLYGON);
            }
            else if (pressedComp == colorOpt.getMenuComponent(0)){
                MenuCommands.changeColor(frame, colorChooser, new ConfirmListenerFill(), new CancelListener(),
                        selectedFillColor);
            }
            else if (pressedComp == colorOpt.getMenuComponent(1)){
                MenuCommands.changeColor(frame, colorChooser, new ConfirmListenerPen(), new CancelListener(),
                        selectedBorderColor);
            }
        }
    }

    /**
     * MyKeyAdapter handles all shortcuts relating to the menu bar and side bar controls
     */
    class MyKeyAdapter extends KeyAdapter {
        /**
         * This eventListener checks key strokes, and takes action by calling the relevant
         * method from MenuCommands.
         * @param e the KeyEvent variable, used to check the keycode from pressed key
         */
        @Override
        public void keyPressed(KeyEvent e) {
           int pressedKey = e.getKeyCode();
           System.out.println(e.getKeyChar());
           if (e.isControlDown()){
               if (pressedKey == KeyEvent.VK_S){
                   MenuCommands.saveFile(frame);
               }
               else if (pressedKey == KeyEvent.VK_O){
                   MenuCommands.openFile(frame);
               }
               else if (pressedKey == KeyEvent.VK_B){
                   MenuCommands.exportBMP(drawingBoard);
               }
               else if (pressedKey == KeyEvent.VK_Z){
                   MenuCommands.undo(comp, frame, sideBar, undoHistoryActive);
                   MenuCommands.refreshComps(sideBarComps);
               }
               else if (pressedKey == KeyEvent.VK_H){
                   undoHistoryActive = MenuCommands.showUndoHistory(frame, sideBar, drawingBoard,
                           new UndoHistorySelectingShapes(), undoHistoryActive);
                   undoHistoryStore = MenuCommands.saveUndoList(comp);
               }
               else if (pressedKey == KeyEvent.VK_R){
                   undoHistoryActive = MenuCommands.editUndoHistory(frame, sideBar, drawingBoard, btnGroup, comp,
                           new MyMouseAdapter(), new MyMouseAdapter(), undoHistoryStore, undoHistoryNum, undoHistoryActive);
                   MenuCommands.refreshComps(sideBarComps);
               }
           }
        }
    }

    /**
     * MyWindowListener sets the position of the divider dynamically based on % of current
     * screen size. ComponentAdapter is extended as it already provides the componentResized method to be overloaded
     */
    class MyWindowListener extends ComponentAdapter{
        /**
         * componentResized calls windowChangeActions upon resize
         * @param e Placeholder
         */
        @Override
        public void componentResized(ComponentEvent e){
            windowChangeActions();
        }
        /**
         * componentShown calls windowChangeActions upon being shown
         * @param e Placeholder
         */
        @Override
        public void componentShown(ComponentEvent e){
            windowChangeActions();
        }

        /**
         * windowChangeActions sets the divider's location on the screen, using a series of widths to
         * provide a dynamic and smooth appearance as the width shrinks or widens. It also ensures that the
         * ComponentsClass object contained by the drawingBoard has the same dimensions as the drawingBoard
         */
        public void windowChangeActions(){
            if (mainDisplay.getWidth() > 1500){
                mainDisplay.setDividerLocation(0.09);
            }
            else if (mainDisplay.getWidth() > 900){
                mainDisplay.setDividerLocation(0.12);
            }
            else{
                mainDisplay.setDividerLocation(0.19);
            }

            comp.setFrameSize(drawingBoard.getSize());
        }
    }

    /**
     * ConfirmListenerFill handles the processng if the user accepts a change of fill colour.
     */
    class ConfirmListenerFill implements ActionListener{
        /**
         * actionPerformed sets the private variable selectedFillColor to the colorChooser's current colour,
         * as such setting the fill colour.
         * @param actionEvent A placeholder
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            selectedFillColor = colorChooser.getColor();
        }
    }

    /**
     * ConfirmListenerPen handles the processng if the user accepts a change of pen colour.
     */
    class ConfirmListenerPen implements ActionListener{
        /**
         * actionPerformed sets the private variable selectedBorderColor to the colorChooser's current colour,
         * as such setting the pen colour.
         * @param actionEvent A placeholder
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            selectedBorderColor = colorChooser.getColor();
        }
    }

    /**
     * CancelListener is a placeholder class used for MenuCommands.changeColor in the event that cancel logic is required.
     */
    class CancelListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        }
    }

    /**
     * UndoHistorySelectingShapes is an ItemListener applied to every JRadioButton in the sideBar, and is responsible
     * for allowing users to 'step back' through states of the program.
     */
    class UndoHistorySelectingShapes implements ItemListener{
        /**
         * itemStateChanged finds the index of the selected radio button within the list of all radio buttons contained
         * by the sideBar. It then creates a new LinkedList of undoListHelpers which will only contain those shapes at or
         * below the selected index. The ComponentsClass object then has its undoList set to this list so as to only
         * show the requested shapes. The object's undoList is reset to full on each call of this method by using the
         * undoHistoryStore.
         * @param itemEvent Used to check if the radio button is selected or not.
         */
        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            comp.undoList = undoHistoryStore;
            JRadioButton btn = (JRadioButton) itemEvent.getItem();
            Component[] btns = sideBar.getComponents();
            undoHistoryNum = undoHistoryStore.size() - 1;
            frame.requestFocus(); //used to ensure that frame's keyboard listener still works
            if (btn.isSelected()){
                for (int index = 0; index < btns.length; index++) {
                    JRadioButton currentBtn = (JRadioButton) sideBar.getComponent(index);
                    if (currentBtn == btn) {
                        undoHistoryNum = index;
                        break;
                    }
                }
                LinkedList<ComponentsClass.undoListHelper> displayShapes = new LinkedList<>();
                int count = 0;
                for (ComponentsClass.undoListHelper helper : comp.undoList){
                    if (count <= undoHistoryNum){
                        displayShapes.add(helper);
                        count += 1;
                    }
                    else{
                        break;
                    }
                }
                comp.undoList = displayShapes;
                comp.repaint();
            }

        }
    }

    /**
     * showGUI runs the GUI until such time that the user closes the program.
     */
    public void showGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            buildGUI();
        });
    }
}



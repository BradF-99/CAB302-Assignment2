package main.java.gui;

//import javafx.scene.input.KeyCode; // seems to be breaking travis ci
import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

import main.java.exceptions.FileInvalidArgumentException;
import main.java.filehandler.*;


public class MainWindow {
    private JFrame frame;
    private JMenuBar mainMenu;
    private JSplitPane mainDisplay;
    private JPanel sideBar;
    private JPanel drawingBoard;
    ComponentsClass comp;
    HashMap<Integer, Integer> undoHistoryMapping;
    LinkedList<ComponentsClass.undoListHelper> undoHistoryStore;
    private JColorChooser colorChooser;
    private java.awt.Point startPoint;
    private ShapesEnum.Shapes currentShape = ShapesEnum.Shapes.ELLIPSE;
    private Color selectedBorderColor = Color.BLACK;
    private Color selectedFillColor = Color.BLACK;
    private boolean filled = true;
    private boolean undoHistoryActive = false;

    private List<String[]> argsList = new ArrayList<>();
    private FileRead fileReader = new FileRead();
    private FileWrite fileWriter = new FileWrite();

    /**
     * buildGUI()
     * Does what it says on the tin!
     */

    private void buildGUI() {
        //Create Core GUI Components
        comp = new ComponentsClass(new Dimension(0,0));
        frame = new JFrame("Vector Design Tool");
        mainMenu = new JMenuBar();
        mainDisplay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        sideBar = new JPanel();
        drawingBoard = new JPanel();
        colorChooser = new JColorChooser();
        undoHistoryMapping = new HashMap<>();
        undoHistoryStore = comp.undoList;

        //Create menu components
        String[] dropdownTitle = {"File Options", "Picture Commands", "Drawing Tools", "Colour Tools"};
        String[] fileCmds = {"Save file (ctrl+s)", "Open file (ctrl+o)"}; //List of options in dropdown
        String[] additionalCmds = {"Undo (ctrl+z)", "Show Undo History (ctrl+h)", "Confirm Selected History (ctrl+r)",
                "Export BMP"};
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
        mainDisplay.setLeftComponent(sideBar);
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.PAGE_AXIS));

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
        sideBar.addComponentListener(new MySideBarListener());
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
            MenuCommands.addUndoHistory(comp, sideBar);
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
                MenuCommands.undo(comp, sideBar);
            }
            else if (pressedComp == additionalOpt.getMenuComponent(1)){
                undoHistoryActive = MenuCommands.showUndoHistory(frame, sideBar, drawingBoard, comp.undoList,
                        undoHistoryStore, undoHistoryMapping, new UndoHistorySelectingShapes(), undoHistoryActive);
            }
            else if (pressedComp == additionalOpt.getMenuComponent(2)){
                undoHistoryActive = MenuCommands.editUndoHistory(frame, sideBar, drawingBoard, comp, new
                                MyMouseAdapter(), new MyMouseAdapter(), undoHistoryStore, undoHistoryActive);
            }
            else if (pressedComp == fileOpt.getMenuComponent(0)){
                MenuCommands.saveFile(frame);
            }
            else if (pressedComp == fileOpt.getMenuComponent(1)){
                try {
                    fileRead(MenuCommands.openFile(frame));
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (FileInvalidArgumentException ex) {
                    ex.printStackTrace();
                }
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
               if (pressedKey == KeyEvent.VK_Z){
                   MenuCommands.undo(comp, sideBar);
               }
               else if (pressedKey == KeyEvent.VK_S){
                   MenuCommands.saveFile(frame);
               }
               else if (pressedKey == KeyEvent.VK_O){
                   MenuCommands.openFile(frame);
               }
               else if (pressedKey == KeyEvent.VK_H){
                   undoHistoryActive = MenuCommands.showUndoHistory(frame, sideBar, drawingBoard, comp.undoList,
                           undoHistoryStore, undoHistoryMapping, new UndoHistorySelectingShapes(), undoHistoryActive);
               }
           }
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
            mainDisplay.setDividerLocation(0.11);
            comp.setFrameSize(drawingBoard.getSize());
        }
    }

    /**
     * MySideBarListener checks the width of the sidebar, and sets the buttons to that width.
     * ComponentAdapter is extended as it already provides the componentResized method to be overloaded
     */
    class MySideBarListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) { windowChangeActions(); }

        @Override
        public void componentShown(ComponentEvent e) { windowChangeActions(); }

        public void windowChangeActions() {
            comp.setFrameSize(drawingBoard.getSize());
            int newWidth = sideBar.getWidth();
            for (Component button : sideBar.getComponents()) {
                int currentHeight = button.getHeight();
                button.setSize(newWidth, currentHeight);
            }
        }
    }

    class ConfirmListenerFill implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            selectedFillColor = colorChooser.getColor();
        }
    }

    class ConfirmListenerPen implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            selectedBorderColor = colorChooser.getColor();
        }
    }

    class CancelListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        }
    }

    class UndoHistorySelectingShapes implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            JCheckBox chkbx = (JCheckBox) itemEvent.getItem();
            int chkbxIndex = 0;
            for (int index = 0; index < sideBar.getComponents().length; index++) {
                JCheckBox currentChkbx = (JCheckBox) sideBar.getComponent(index);
                if (currentChkbx == chkbx) {
                    chkbxIndex = index;
                    break;
                }
            }
            if (chkbx.isSelected()){
                undoHistoryMapping.put(chkbxIndex, 1);
            }
            else{
                undoHistoryMapping.put(chkbxIndex, 0);
            }
            ArrayList<ComponentsClass.undoListHelper> currentShapesList = new ArrayList<>();
            LinkedList<ComponentsClass.undoListHelper> currentShapesLinkedList = new LinkedList<>();
            for (int key : undoHistoryMapping.keySet()){
                if (undoHistoryMapping.get(key) == 1){
                    currentShapesList.add(undoHistoryStore.get(key));
                }
            }
            for (ComponentsClass.undoListHelper helper : currentShapesList){
                currentShapesLinkedList.add(helper);
            }
            comp.undoList = currentShapesLinkedList;
            comp.repaint();
            System.out.println(undoHistoryMapping);
            System.out.println(undoHistoryStore.size());
        }
    }

    private void fileRead(String path) throws IOException, FileInvalidArgumentException {

        if(path.isBlank() || path.isEmpty()){
            return; // user didn't select a file
        }

        argsList.clear();
        argsList = fileReader.readFile(path);

        boolean fill = false;

        for (String[] argument : argsList){
            // have to initialise these or java has a massive cry
            float x1 = 0.0f;
            float y1 = 0.0f;
            float x2 = 0.0f;
            float y2 = 0.0f;

            try {
                x1 = Float.parseFloat(argument[1]);
                y1 = Float.parseFloat(argument[2]);
                x2 = Float.parseFloat(argument[3]);
                y2 = Float.parseFloat(argument[4]);
            } catch (NumberFormatException err) { // this will fail if the co-ord is not castable to float
                // fail silently because it's going to be a colour command
            } catch (IndexOutOfBoundsException err){
                // fail silently - probably a plot
            } // catch other exceptions?
            switch(argument[0].toUpperCase()){
                case "LINE":
                    currentShape = ShapesEnum.Shapes.LINE;
                    comp.lineComp.addNewObject(x1, y1, x2, y2, selectedBorderColor );
                    comp.addUndo(comp.lineComp.lines.size() - 1, ShapesEnum.Shapes.LINE);
                    break;
                case "RECTANGLE":
                    currentShape = ShapesEnum.Shapes.RECTANGLE;
                    comp.rectComp.addNewObject(x1, y1, x2, y2, selectedBorderColor,fill,selectedFillColor);
                    comp.addUndo(comp.rectComp.shapes.size() - 1,ShapesEnum.Shapes.RECTANGLE);
                    break;
                case "ELLIPSE":
                    currentShape = ShapesEnum.Shapes.ELLIPSE;
                    comp.ellComp.addNewObject(x1, y1, x2, y2, selectedBorderColor,fill,selectedFillColor);
                    comp.addUndo(comp.ellComp.shapes.size() - 1, ShapesEnum.Shapes.ELLIPSE);
                    break;
                case "PLOT":
                    currentShape = ShapesEnum.Shapes.PLOT;
                    comp.plotComp.addNewObject(x1, y1, selectedBorderColor);
                    comp.addUndo(comp.plotComp.plots.size() - 1, ShapesEnum.Shapes.PLOT);
                    break;
                case "POLYGON":
                    currentShape = ShapesEnum.Shapes.POLYGON;
                    List polyPoints = new ArrayList();
                    // we cant use a foreach loop because our co-ordinates are in pairs.
                    for(int i = 1; i < argument.length; i+= 2){ // we start at index 1 and increment in 2s
                        float x = Float.parseFloat(argument[i]);
                        float y = Float.parseFloat(argument[i+1]);
                        Point2D.Float point = new Point2D.Float(x,y);
                        polyPoints.add(point);
                    }

                    Object[] pointArray = polyPoints.toArray();
                    comp.polyComp.addNewObject(pointArray,selectedBorderColor,fill,selectedFillColor);

                    polyPoints.clear();
                    comp.addUndo(comp.polyComp.polygon.size() - 1, ShapesEnum.Shapes.POLYGON);
                    break;
                case "PEN":
                    selectedBorderColor = Color.decode(argument[1]);
                    break;
                case "FILL":
                    if(argument[1].equals("OFF")){
                        fill = false;
                        break;
                    } else {
                        fill = true;
                        selectedFillColor = Color.decode(argument[1]);
                        break;
                    }
                default:
                    throw new FileInvalidArgumentException("Invalid argument in file.");
            }

        }
        // reset our stuff
        selectedFillColor = Color.BLACK;
        selectedBorderColor = Color.BLACK;
        currentShape = ShapesEnum.Shapes.LINE;

        comp.repaint(); // its too fast for repaints during file load which makes me sad :(

    }

    public void showGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            buildGUI();
        });
    }
}



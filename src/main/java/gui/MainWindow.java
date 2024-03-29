package main.java.gui;

import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.*;

import java.util.List;

import main.java.exceptions.FileInvalidArgumentException;
import main.java.filehandler.*;


/**
 * MainWindow constitutes the GUI, and as such contains all of the graphical and event handler configurations and
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
    private ComponentsClass comp;
    private LinkedList<ComponentsClass.undoListHelper> undoHistoryStore;
    private JColorChooser colorChooser;
    private java.awt.Point startPoint;
    private ShapesEnum.Shapes currentShape = ShapesEnum.Shapes.ELLIPSE;
    private Color selectedBorderColor = Color.BLACK;
    private Color selectedFillColor = Color.BLACK;
    private boolean filled = false;
    private int undoHistoryNum;
    private boolean undoHistoryActive = false;
    private boolean undoPolygon = false;
    private List<String[]> readArgsList = new ArrayList<>();
    private List<String> writeArgsList = new ArrayList<>();
    private FileRead fileReader = new FileRead();
    private FileWrite fileWriter = new FileWrite();
    private boolean started = false;
    private LinkedList<Point2D.Float> polyPoints = new LinkedList<>();


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
        String[] dropdownTitle = {"File", "Picture Commands", "Drawing Tools", "Colour Tools"};
        String[] fileCmds = {"New File","Open (Ctrl + O)", "Save (Ctrl + S)", "Export BMP (ctrl+b)"}; //List of options in dropdown
        String[] additionalCmds = {"Undo (ctrl+z)", "Show Undo History (ctrl+h)", "Confirm Selected History (ctrl+r)"};
        String[] drawingCmds = {"Plot", "Line", "Rectangle", "Ellipse", "Polygon", "Clear Polygon"};
        String[] colorCmds = {"Fill Colour", "Pen Colour", "Toggle Fill"};
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
                    MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
                    MenuCommands.refreshComps(sideBarComps);
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
                MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
                MenuCommands.refreshComps(sideBarComps);
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
                MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
                MenuCommands.refreshComps(sideBarComps);
            }else if(currentShape == ShapesEnum.Shapes.RECTANGLE){
                comp.rectComp.addNewObject(comp.pointToFloat(startPoint.x,windowSize.width),comp.pointToFloat(startPoint.y,windowSize.height)
                        ,comp.pointToFloat(e.getPoint().x,windowSize.width),comp.pointToFloat(e.getPoint().y, windowSize.height),selectedBorderColor,filled,selectedFillColor);
                comp.rectComp.clearDrawObject();
                comp.addUndo(comp.rectComp.shapes.size() - 1,ShapesEnum.Shapes.RECTANGLE);
                MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
                MenuCommands.refreshComps(sideBarComps);
            }else if(currentShape == ShapesEnum.Shapes.ELLIPSE){
                comp.ellComp.addNewObject(comp.pointToFloat(startPoint.x,windowSize.width),comp.pointToFloat(startPoint.y,windowSize.height)
                        ,comp.pointToFloat(e.getPoint().x,windowSize.width),comp.pointToFloat(e.getPoint().y, windowSize.height),selectedBorderColor,filled,selectedFillColor);
                comp.ellComp.clearDrawObject();
                comp.addUndo(comp.ellComp.shapes.size() - 1, ShapesEnum.Shapes.ELLIPSE);
                MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
                MenuCommands.refreshComps(sideBarComps);
            }
            comp.repaint();
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
                MenuCommands.newFile(sideBar, comp, readArgsList);
                undoHistoryActive = MenuCommands.refreshEventListeners(drawingBoard, new MyMouseAdapter(),
                        new MyMouseAdapter());
                MenuCommands.refreshComps(sideBarComps);
            }
            else if (pressedComp == fileOpt.getMenuComponent(1)){
                try {
                    fileRead(MenuCommands.openFile(frame));
                    undoHistoryActive = MenuCommands.refreshEventListeners(drawingBoard, new MyMouseAdapter(),
                            new MyMouseAdapter()
                    );
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (FileInvalidArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
            else if (pressedComp == fileOpt.getMenuComponent(2)){
                try {
                    fileWrite(MenuCommands.saveFile(frame, undoHistoryActive));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if (pressedComp == fileOpt.getMenuComponent(3)){
                MenuCommands.exportBMP(drawingBoard, undoHistoryActive);
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
            else if (pressedComp == drawingOpt.getMenuComponent(5)){
                started = MenuCommands.clearPolygon(started, polyPoints, comp, frame);
            }
            else if (pressedComp == colorOpt.getMenuComponent(0)){
                MenuCommands.changeColor(frame, colorChooser, new ConfirmListenerFill(), new CancelListener(),
                        selectedFillColor);
            }
            else if (pressedComp == colorOpt.getMenuComponent(1)){
                MenuCommands.changeColor(frame, colorChooser, new ConfirmListenerPen(), new CancelListener(),
                        selectedBorderColor);
            }
            else if (pressedComp == colorOpt.getMenuComponent(2)){
                filled = MenuCommands.enableFill(filled);
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
                   try {
                       fileWrite(MenuCommands.saveFile(frame, undoHistoryActive));
                   } catch (IOException ex) {
                       ex.printStackTrace();
                   }
               }
               else if (pressedKey == KeyEvent.VK_O){
                   try {
                       fileRead(MenuCommands.openFile(frame));
                       undoHistoryActive = MenuCommands.refreshEventListeners(drawingBoard, new MyMouseAdapter(),
                               new MyMouseAdapter()
                       );
                   } catch (IOException ex) {
                       JOptionPane.showMessageDialog(frame, ex.getMessage());
                   } catch (FileInvalidArgumentException ex) {
                       JOptionPane.showMessageDialog(frame, ex.getMessage());
                   }
               }
               else if (pressedKey == KeyEvent.VK_B){
                   MenuCommands.exportBMP(drawingBoard, undoHistoryActive);
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
     * ConfirmListenerFill handles the processing if the user accepts a change of fill colour.
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
     * ConfirmListenerPen handles the processing if the user accepts a change of pen colour.
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

    private void fileRead(String path) throws IOException, FileInvalidArgumentException {
        if(path.isBlank() || path.isEmpty()) return; // user didn't select a file

        sideBar.removeAll();
        comp.clearAllObjects();
        readArgsList.clear();
        readArgsList = fileReader.readFile(path);

        boolean fill = false;

        for (String[] argument : readArgsList){
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
                    MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
                    break;
                case "RECTANGLE":
                    currentShape = ShapesEnum.Shapes.RECTANGLE;
                    comp.rectComp.addNewObject(x1, y1, x2, y2, selectedBorderColor,fill,selectedFillColor);
                    comp.addUndo(comp.rectComp.shapes.size() - 1,ShapesEnum.Shapes.RECTANGLE);
                    MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
                    break;
                case "ELLIPSE":
                    currentShape = ShapesEnum.Shapes.ELLIPSE;
                    comp.ellComp.addNewObject(x1, y1, x2, y2, selectedBorderColor,fill,selectedFillColor);
                    comp.addUndo(comp.ellComp.shapes.size() - 1, ShapesEnum.Shapes.ELLIPSE);
                    MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
                    break;
                case "PLOT":
                    currentShape = ShapesEnum.Shapes.PLOT;
                    comp.plotComp.addNewObject(x1, y1, selectedBorderColor);
                    comp.addUndo(comp.plotComp.plots.size() - 1, ShapesEnum.Shapes.PLOT);
                    MenuCommands.addUndoHistory(comp, sideBar, btnGroup);
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
                    MenuCommands.addUndoHistory(comp, sideBar, btnGroup);

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
        filled = false;
        currentShape = ShapesEnum.Shapes.LINE;
        MenuCommands.refreshComps(sideBarComps);
        MenuCommands.refreshEventListeners(drawingBoard, new MyMouseAdapter(), new MyMouseAdapter());
        comp.repaint(); // its too fast for repaints during file load which makes me sad :(
        readArgsList.clear();
    }

    private void fileWrite(String path) throws IOException {
        String arg = "";
        String colour = "";
        String penColour = "#000000";
        String fillColour = "OFF";
        boolean fillOn = false; // false by default
        readArgsList.clear();

        for (int i = 0; i < comp.undoList.size(); i++) {
            arg = ""; // initialise arg every loop
            switch (comp.undoList.get(i).component) {
                case PLOT:
                    PlotComponent.Plot plot = comp.plotComp.plots.get(comp.undoList.get(i).index);
                    colour = rgbIntToHex(plot.color.getRGB());
                    if (!penColour.equalsIgnoreCase(colour)) {
                        penColour = colour;
                        arg = "PEN " + penColour + "\n";
                    }
                    arg = arg + "PLOT " + plot.x.toString() + " " + plot.y.toString();
                    break;
                case LINE:
                    LineComponent.Line line = comp.lineComp.lines.get(comp.undoList.get(i).index);
                    colour = rgbIntToHex(line.color.getRGB());
                    if (!penColour.equalsIgnoreCase(colour)) {
                        penColour = colour;
                        arg = "PEN " + penColour + "\n";
                    }
                    arg = arg + "LINE " +
                                line.x1.toString() + " " +
                                line.y1.toString() + " " +
                                line.x2.toString() + " " +
                                line.y2.toString();
                    break;
                case RECTANGLE:
                    ShapeComponent.Shape rect = comp.rectComp.shapes.get(comp.undoList.get(i).index);
                    if(rect.filled){
                        fillOn = true;
                        colour = rgbIntToHex(rect.fillColor.getRGB());
                        if (!fillColour.equalsIgnoreCase(colour)) { // dont need to make unnecessary fill commands
                            fillColour = colour;
                            arg = arg + "FILL " + fillColour + "\n";
                        }
                    } else {
                        if(fillOn){
                            fillOn = false;
                            arg = arg + "FILL OFF\n";
                        }
                    }

                    colour = rgbIntToHex(rect.borderColor.getRGB());
                    if (!penColour.equalsIgnoreCase(colour)) {
                        penColour = colour;
                        arg = arg + "PEN " + penColour + "\n";
                    }

                    arg = arg + "RECTANGLE "    + rect.x + " "
                                                + rect.y + " "
                                                + (rect.x+rect.width) + " "
                                                + (rect.y+rect.height);
                    break;
                case ELLIPSE:
                    ShapeComponent.Shape ellipse = comp.ellComp.shapes.get(comp.undoList.get(i).index);
                    if(ellipse.filled){
                        fillOn = true;
                        colour = rgbIntToHex(ellipse.fillColor.getRGB());
                        if (!fillColour.equalsIgnoreCase(colour)) { // dont need to make unnecessary fill commands
                            fillColour = colour;
                            arg = arg + "FILL " + fillColour + "\n";
                        }
                    } else {
                        if(fillOn){
                            fillOn = false;
                            arg = arg + "FILL OFF\n";
                        }
                    }

                    colour = rgbIntToHex(ellipse.borderColor.getRGB());
                    if (!penColour.equalsIgnoreCase(colour)) {
                        penColour = colour;
                        arg = arg + "PEN " + penColour + "\n";
                    }

                    arg = arg + "ELLIPSE "
                            + ellipse.x + " "
                            + ellipse.y + " "
                            + (ellipse.x+ellipse.width) + " "
                            + (ellipse.y+ellipse.height);
                    break;
                case POLYGON:

                    PolygonComponent.Polygon poly = comp.polyComp.polygon.get(comp.undoList.get(i).index);
                    if(poly.filled){
                        fillOn = true;
                        colour = rgbIntToHex(poly.fillColor.getRGB());
                        if (!fillColour.equalsIgnoreCase(colour)) {
                            fillColour = colour;
                            arg = arg + "FILL " + fillColour + "\n";
                        }
                    } else {
                        if(fillOn){
                            fillOn = false;
                            arg = arg + "FILL OFF\n";
                        }
                    }

                    colour = rgbIntToHex(poly.borderColor.getRGB());
                    if (!penColour.equalsIgnoreCase(colour)) {
                        penColour = colour;
                        arg = arg + "PEN " + penColour + "\n";
                    }

                    arg = arg + "POLYGON";

                    for (int j = 0; j < poly.pointArray.length; j++) { // nice and easy
                        Point2D.Float point = ((Point2D.Float) poly.pointArray[j]);
                        arg = arg + " " + point.x + " " + point.y;
                    }

                    break;
                default:
                    break;
            }
            writeArgsList.add(arg);
        }
        fileWriter.writeFile(writeArgsList,path);
    }

    /**
     * rgbIntToHex converts the RGB integer value from Java's colour library in to a hex string.
     * @param rgb integer representing the RGB value
     * @return String of HEX colour (including # at the beginning)
     */
    private String rgbIntToHex(int rgb){
        String fillColour = "#" + Integer.toHexString(rgb)
                .substring(2)
                .toUpperCase();
        return fillColour;
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



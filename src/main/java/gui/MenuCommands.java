package main.java.gui;
//import javafx.scene.input.KeyCode;
import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * MenuCommands is a utility class which contains the actions corresponding to keyboard or graphical commands
 * such as selecting a different fill colour or a draw shape. It contains only static methods.
 */
public final class MenuCommands {
    /**
     * refreshComps iterates through an array of components and sets their visibility to false and then true to
     * ensure that all subcomponents are actually displayed.
     *
     * @param guiComps An array of components.
     */
    public static void refreshComps(Component[] guiComps){
        for (Component guiComp : guiComps){
            guiComp.setVisible(false);
            guiComp.setVisible(true);
        }
    }

    /**
     * saveUndoList provides a single location for the returning of a ComponentsClass's undoList to another variable,
     * and for any processing that may have to be done when the undoList is saved.
     *
     * @param comp A ComponentClass object, the 'canvas' on which drawing commands take place.
     * @return The undoLst of the parameter 'comp', a linked list of ComponentsClass.undoListHelper(s).
     */
    public static LinkedList<ComponentsClass.undoListHelper> saveUndoList(ComponentsClass comp){
        return comp.undoList;
    }

    /**
     * undo removes a single shape that has been drawn, and clears the responding JRadioButton from the sideBar.
     * Execution of this method is prevented if undo history is active, with a window alert appearing over the parent
     * frame stating this if it is used whilst undo history is.
     *
     * @param comp A ComponentClass object, the 'canvas' on which drawing commands take place.
     * @param frame A JFrame which serves as the parent of all other components.
     * @param sideBar A JPanel which is used to store JRadioButtons for undo history functionality. These radio buttons
     *                each correspond to a particular shape
     * @param undoHistoryActive A Boolean which is true iff undo history functionality is active.
     */
    public static void undo(ComponentsClass comp, JFrame frame, JPanel sideBar, boolean undoHistoryActive){
        if (!(undoHistoryActive)){
            comp.Undo();
            comp.repaint();
            int length = sideBar.getComponents().length - 1;
            if (length > -1){
                sideBar.remove(length);
            }
        }
        else{
            JOptionPane.showMessageDialog(frame, "Undo history is active, use the history panel to undo");
        }

    }
    /**
     * saveFile instantiates a new JFileChooser and then uses this to open a save dialogue, allowing for the
     * saving of VEC files. The dialoge is displayed over the parent frame of the GUI.
     *
     * @param frame A JFrame which serves as the parent of all other components.
     */
    public static void saveFile(JFrame frame){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(frame);
    }
    /**
     * openFile instantiates a new JFileChooser and then uses this to open an open dialogue, allowing for the
     * opening of VEC files. The dialoge is displayed over the parent frame of the GUI.
     *
     * @param frame A JFrame which serves as the parent of all other components.
     */
    public static void openFile(JFrame frame){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(frame);
    }

    /**
     * exportBMP creates a bitmap from the contents of the drawingBoard, allowing for the user to either use the
     * current dimensions of the drawingBoard for the BMP or to manually enter their own. User dimensions are only
     * accepted that match the regexp \\d+x\\d+ (for example 123x123) and are checked to ensure that they are less than 6000.
     * A FileChooser is also instantiated to allow for the saving of the BMP in a location of the user's choice.
     *
     * @param drawingBoard The container for the ComponentsClass object which handles all drawing. Serves to set dimensions
     *                     and layout for the object.
     */
    public static void exportBMP(JPanel drawingBoard){
        Object[] options = {"Use drawing board's current dimensions", "Manually enter dimensions"};
        int thresholdD = 6000;
        Dimension bmpD = new Dimension(drawingBoard.getWidth(), drawingBoard.getHeight());
        Dimension bmpScaleD = new Dimension();
        boolean useUserDimensions = false;
        String filePath = "C:\\Users\\Comuser\\Documents\\bitmap.bmp";
        int responseInt = JOptionPane.showOptionDialog(drawingBoard,
                "Select if you would like to use the current dimensions or manually enter them",
                "Bitmap Dimension Choice",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (responseInt == 1){
            Boolean validData = false;
            String inputDimension = "";
            useUserDimensions = true;
            while (validData == false){
                String userInput = (String)JOptionPane.showInputDialog(drawingBoard, "Please enter your dimensions" +
                        " in the format 123x123");
                userInput = userInput.trim();
                if (userInput.matches("\\d+x\\d+")){
                    String[] userInputs = userInput.split("x");
                    int width = Integer.valueOf(userInputs[0]);
                    int height = Integer.valueOf(userInputs[1]);
                    if (width < thresholdD && height < thresholdD){
                        bmpScaleD.width = width;
                        bmpScaleD.height = height;
                        validData = true;
                    }
                    else{
                        JOptionPane.showMessageDialog(drawingBoard, "Dimensions must be below the value " +
                                thresholdD);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(drawingBoard, "You must enter two sets of integers" +
                            " combined by an 'x'");
                }
            }
        }
        BufferedImage bufferedImage = new BufferedImage(bmpD.width, bmpD.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphicImage = bufferedImage.createGraphics();
        drawingBoard.paint(graphicImage);
        graphicImage.dispose();
        BufferedImage bufferedImageToWrite = bufferedImage;
        if (useUserDimensions){
            Image scaledImage = bufferedImage.getScaledInstance(bmpScaleD.width, bmpScaleD.height, Image.SCALE_SMOOTH);
            BufferedImage scaledBufferedImage = new BufferedImage(bmpScaleD.width, bmpScaleD.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D scaledGraphicImage = scaledBufferedImage.createGraphics();
            scaledGraphicImage.drawImage(scaledImage, 0, 0, null);
            scaledGraphicImage.dispose();
            bufferedImageToWrite = scaledBufferedImage;
        }
        try {
            ImageIO.write(bufferedImageToWrite, "bmp", new File(filePath));
        } catch (IOException e) {
        }
    }

    /**
     * changeShape provides a single location for the setting of a variable to a new shape. This is used with the
     * private variables of the MainWindow class to control what shape is drawn next.
     *
     * @param newShape The shape from the enumerable ShapesEnum.Shapes.
     * @return The parameter newShape.
     */
    public static ShapesEnum.Shapes changeShape(ShapesEnum.Shapes newShape){
        return newShape;
    }

    /**
     * changeColor controls the current colour of either the GUI's pen or fill colour. This method creates a dialogue
     * from the parameter colorChooser, as such allowing for the persistence of recent colours across calls to the
     * colour change buttons.
     *
     * @param frame A JFrame which serves as the parent of all other components.
     * @param colorChooser A JColorChooser, used to create a new JDialog that is then displayed.
     * @param okListener An ActionListener that acts if the 'Ok' button of the color chooser dialogue is hit.
     * @param cancelListener An ActionListener that acts if the 'Cancel' button of the color chooser dialogue is hit.
     * @param currentColor The Color that the colorChooser is set to. This allows for the colorChooser to have either
     *                     the pen or fill colour already selected, as opposed to the last colour.
     */
    public static void changeColor(JFrame frame, JColorChooser colorChooser, ActionListener okListener,
                                   ActionListener cancelListener, Color currentColor){
        colorChooser.setColor(currentColor);
        JDialog colorDialog = JColorChooser.createDialog(frame, "Choose a colour", true, colorChooser,
                okListener, cancelListener);
        colorDialog.setVisible(true);

    }

    /**
     * addUndoHistory adds JRadioButtons to the sideBar. These buttons are used for the undo history functionality.
     *
     * @param comp A ComponentClass object, the 'canvas' on which drawing commands take place.
     * @param sideBar A JPanel which is used to store JRadioButtons for undo history functionality. These radio buttons
     *      *                each correspond to a particular shape.
     * @param btnGroup A ButtonGroup, all radio buttons are added to this so that only one button can be selected.
     */
    public static void addUndoHistory(ComponentsClass comp, JPanel sideBar, ButtonGroup btnGroup){
        ComponentsClass.undoListHelper lastShape = comp.undoList.getLast();
        String index = String.valueOf(lastShape.index);
        String shape = String.valueOf(lastShape.component);
        JRadioButton newBtn = new JRadioButton(index + ": " + shape);
        newBtn.setEnabled(false);
        newBtn.setSelected(false);
        btnGroup.add(newBtn);
        sideBar.add(newBtn);
    }

    /**
     * showUndoHistory activates the undo history functionality. This disables the user from drawing any new shapes
     * by removing the event listeners from the drawingBoard, as such preventing both new shapes being added to the
     * ComponentsClass object that the drawingBoard contains, as well as using the Undo command.
     *
     * @param frame A JFrame which serves as the parent of all other components.
     * @param sideBar A JPanel which is used to store JRadioButtons for undo history functionality. These radio buttons
     *                            each correspond to a particular shape.
     * @param drawingBoard The container for the ComponentsClass object which handles all drawing. Serves to set dimensions
     *                           and layout for the object.
     * @param il An ItemListener, added to each of the JRadioButtons present within the sideBar.
     * @param undoHistoryActive A Boolean which is true iff undo history functionality is active.
     * @return This method only returns true. The method is called so that the variable controlling if undoHistory is
     * active or not equals the return value (for example undoHistoryActive = MenuCommands.showUndoHistory(params)). The
     * reason for this is that this command only activates undo history, to disable the menu option 'Confirm Undo History'
     * must be pressed.
     */
    public static boolean showUndoHistory(JFrame frame, JPanel sideBar, JPanel drawingBoard,
                                          ItemListener il, boolean undoHistoryActive){
        if (!(undoHistoryActive)){
            JOptionPane.showMessageDialog(frame,
                    "Undo history is activated, this prevents you from adding new shapes, but allows you" +
                            " to see all previous shapes before the selected shape");
            for (int index = 0; index < sideBar.getComponents().length; index++){
                JRadioButton setBtn = (JRadioButton) sideBar.getComponent(index);
                setBtn.setEnabled(true);
                setBtn.setSelected(false);
                setBtn.addItemListener(il);
                if (index + 1 == sideBar.getComponents().length && index != 0){
                    setBtn.setSelected(true);
                }
            }
            for (MouseListener ml : drawingBoard.getMouseListeners()){
                drawingBoard.removeMouseListener(ml);
            }
            for (MouseMotionListener mml : drawingBoard.getMouseMotionListeners()){
                drawingBoard.removeMouseMotionListener(mml);
            }
        }
        else{
            JOptionPane.showMessageDialog(frame, "Undo history is already active");
        }

        return true;
    }

    /**
     * editUndoHistory handles how the user wishes to exit the undo history functionality. A JOptionPane is displayed,
     * allowing the user to either save their selected state, return to the functionality or deactiavte it. Deactiavting
     * undo history will return the drawing canvas to the latest state and reenable drawing whilst saving will erase all
     * those shapes proceeding the selected one and then reenable drawing.
     *
     * @param frame A JFrame which serves as the parent of all other components.
     * @param sideBar A JPanel which is used to store JRadioButtons for undo history functionality. These radio buttons
     *                                  each correspond to a particular shape.
     * @param drawingBoard The container for the ComponentsClass object which handles all drawing. Serves to set dimensions
     *                           and layout for the object.
     * @param btnGroup A ButtonGroup, all radio buttons are added to this so that only one button can be selected.
     * @param comp A ComponentClass object, the 'canvas' on which drawing commands take place.
     * @param mml A Mouse Motion Listener, readded to the drawingBoard if undo history is deactiavted or saved.
     * @param ml A Mouse Listener, readded to the drawingBoard if undo history is deactiavted or saved.
     * @param undoHistoryStore A LinkedList of ComponentsClass.undoListHelper which is used to save the complete the
     *                         list of shapes
     * @param undoHistoryNum An integer representing the number of times that the comp.Undo() method should be called
     *                       so as to save the picture to the state that the user has set.
     * @param undoHistoryActive A Boolean which is true iff undo history functionality is active.
     *
     * @return true or false, true if undo history is continued and false if it is deactivated or saved.
     */
    public static boolean editUndoHistory(JFrame frame, JPanel sideBar, JPanel drawingBoard, ButtonGroup btnGroup,
                                          ComponentsClass comp, MouseMotionListener mml, MouseListener ml,
                                          LinkedList<ComponentsClass.undoListHelper> undoHistoryStore,
                                          int undoHistoryNum, boolean undoHistoryActive){
        if (undoHistoryActive){
            Object[] options = {"Save selected picture", "Return to undo history", "Deactivate undo history"};
            int responseInt = JOptionPane.showOptionDialog(frame,
                    "Select if you would like to continue with the currently displayed picture." +
                            "Saving this will wipe any unselected shapes",
                    "Select undo history option",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (responseInt == 2 || responseInt == 0){
                comp.undoList = undoHistoryStore;
                if (responseInt == 0){
                    int numTimesToUndo = comp.undoList.size() - undoHistoryNum;
                    for (int index = 1; index < numTimesToUndo; index++){
                        comp.Undo();
                    }
                    sideBar.removeAll();
                    for (ComponentsClass.undoListHelper helper : comp.undoList){
                        String index = String.valueOf(helper.index);
                        String shape = String.valueOf(helper.component);
                        JRadioButton newBtn = new JRadioButton(index + ": " + shape);
                        sideBar.add(newBtn);
                        btnGroup.add(newBtn);
                    }
                }
                for (int index = 0; index < sideBar.getComponents().length; index++){
                    JRadioButton setBtn = (JRadioButton) sideBar.getComponent(index);
                    setBtn.setEnabled(false);
                    if (index + 1 == sideBar.getComponents().length){
                        setBtn.setSelected(true);
                    }
                }
                drawingBoard.addMouseMotionListener(mml);
                drawingBoard.addMouseListener(ml);
                undoHistoryActive = false;

            }
        }
        else{
            JOptionPane.showMessageDialog(frame, "Undo history is not active");
        }
        return undoHistoryActive;
    }


}

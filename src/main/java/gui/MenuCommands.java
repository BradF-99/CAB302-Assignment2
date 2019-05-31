package main.java.gui;
//import javafx.scene.input.KeyCode;
import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * MenuCommands is a utility class which contains the actions corresponding to keyboard or graphical commands
 * such as selecting a different fill colour
 */
public final class MenuCommands {
    public static void refreshComps(Component[] guiComps){
        for (Component guiComp : guiComps){
            guiComp.setVisible(false);
            guiComp.setVisible(true);
        }
    }

    public static LinkedList<ComponentsClass.undoListHelper> saveUndoList(ComponentsClass comp){
        return comp.undoList;
    }

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
    public static void saveFile(JFrame frame){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(frame);
    }
    public static void openFile(JFrame frame){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(frame);
    }
    public static void exportBMP(JPanel drawingBoard){
        Object[] options = {"Use drawing board's current dimensions", "Manually enter dimensions"};
        int thresholdD = 6000;
        Dimension bmpD = new Dimension(drawingBoard.getWidth(), drawingBoard.getHeight());
        Dimension bmpScaleD = new Dimension();
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
        try {
            ImageIO.write(bufferedImage, "bmp", new File("C:\\Users\\Comuser\\Documents\\bitmap.bmp"));
        } catch (IOException e) {
        }
    }
    public static ShapesEnum.Shapes changeShape(ShapesEnum.Shapes newShape){
        return newShape;
    }

    public static void changeColor(JFrame frame, JColorChooser colorChooser, ActionListener okListener,
                                   ActionListener cancelListener, Color currentColor){
        colorChooser.setColor(currentColor);
        JDialog colorDialog = JColorChooser.createDialog(frame, "Choose a colour", true, colorChooser,
                okListener, cancelListener);
        colorDialog.setVisible(true);

    }
    public static void addUndoHistory(ComponentsClass comp, JPanel sideBar){
        ComponentsClass.undoListHelper lastShape = comp.undoList.getLast();
        String index = String.valueOf(lastShape.index);
        String shape = String.valueOf(lastShape.component);
        JCheckBox newChkbx = new JCheckBox(index + ": " + shape);
        newChkbx.setEnabled(false);
        newChkbx.setSelected(false);
        sideBar.add(newChkbx);
    }
    public static boolean showUndoHistory(JFrame frame, JPanel sideBar, JPanel drawingBoard,
                                          ItemListener il, boolean undoHistoryActive){
        if (!(undoHistoryActive)){
            JOptionPane.showMessageDialog(frame,
                    "Undo history is activated, this prevents you from adding new shapes, but allows you" +
                            " to see all previous shapes before the selected shape");
            for (int index = 0; index < sideBar.getComponents().length; index++){
                JCheckBox setChkbx = (JCheckBox) sideBar.getComponent(index);
                setChkbx.setEnabled(true);
                setChkbx.setSelected(false);
                setChkbx.addItemListener(il);
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
    public static boolean editUndoHistory(JFrame frame, JPanel sideBar, JPanel drawingBoard, ComponentsClass comp,
                                      MouseMotionListener mml, MouseListener ml,
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
                        JCheckBox newChkbx = new JCheckBox(index + ": " + shape);
                        sideBar.add(newChkbx);
                    }
                }
                for (int index = 0; index < sideBar.getComponents().length; index++){
                    JCheckBox setChkbx = (JCheckBox) sideBar.getComponent(index);
                    setChkbx.setEnabled(false);
                    if (index + 1 == sideBar.getComponents().length){
                        setChkbx.setSelected(true);
                    }
                    setChkbx.setSelected(false);
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

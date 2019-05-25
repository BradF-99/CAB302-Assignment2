package main.java.gui;
import javafx.scene.input.KeyCode;
import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import javax.swing.*;

/**
 * MenuCommands is a utility class which contains the actions corresponding to keyboard or graphical commands
 * such as selecting a different fill colour
 */
public final class MenuCommands {
    public static void undo(ComponentsClass comp, JPanel sideBar){
        comp.Undo();
        comp.repaint();
        int length = sideBar.getComponents().length - 1;
        if (length > 0){
            sideBar.remove(length);
        }
        sideBar.validate(); //doesn't seem to be working
        sideBar.setVisible(false);
        sideBar.setVisible(true);
    }
    public static void saveFile(JFrame frame){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(frame);
    }
    public static void openFile(JFrame frame){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(frame);
    }
    public static ShapesEnum.Shapes changeShape(ShapesEnum.Shapes newShape){
        return newShape;
    }
    public static void changeColor(JFrame frame, JColorChooser colorChooser, ActionListener okListener,
                                   ActionListener cancelListener){
        JDialog colorDialog = JColorChooser.createDialog(frame, "Choose a colour", true, colorChooser,
                okListener, cancelListener);
        colorDialog.setVisible(true);
    }
    public static void showUndoHistory(ComponentsClass comp, JPanel sideBar){
        ComponentsClass.undoListHelper lastShape = comp.undoList.getLast();
        String index = String.valueOf(lastShape.index);
        String shape = String.valueOf(lastShape.component);
        sideBar.add(new JCheckBox(index + ": " + shape));
        sideBar.validate(); //Updates sidebar
    }


}

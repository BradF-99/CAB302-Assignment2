package main.java.gui;
import javafx.scene.input.KeyCode;
import main.java.components.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import javax.swing.*;

public final class MenuCommands {
    public static void undo(ComponentsClass comp){
        comp.Undo();
        comp.repaint();
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
    public static Color changeColor(JFrame frame){
        JOptionPane colorWindow = new JOptionPane();
        return Color.red;
    }


}

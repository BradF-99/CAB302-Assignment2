package main.java;
import main.java.gui.*;

public class Main {
    public static String exampleFunc(boolean result) {
        if (result) {
            return "Hi!";
        } else {
            return "Hello!";
        }
    }
    public static void main(String[] args){
        MainWindow mainWindow = new MainWindow();
        mainWindow.showGUI();
    }
}

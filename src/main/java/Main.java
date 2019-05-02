package main.java;

public class Main {
    public static String exampleFunc(boolean result) {
        if (result) {
            return "Hi!";
        } else {
            return "Hello!";
        }
    }
    public static void main(String[] args){
        System.out.println(exampleFunc(true));
        System.out.println(exampleFunc(false));
    }
}

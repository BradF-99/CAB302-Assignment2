package main.java.exceptions;

public class FileInvalidArgumentException extends Exception {
    public FileInvalidArgumentException(String msg){
        super("Error occurred while reading file. "+msg);
    }
    public FileInvalidArgumentException(Throwable err){
        super("Error occurred while reading file. ", err);
    }
    public FileInvalidArgumentException(){
        super("Error occurred while reading file. Please ensure this file is valid.");
    }
}

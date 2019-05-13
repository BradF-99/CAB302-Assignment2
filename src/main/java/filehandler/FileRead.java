package main.java.filehandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.regex.*;

import main.java.exceptions.*;

public class FileRead {
    private final String[] shapeArgs = {"LINE","PLOT","RECTANGLE","ELLIPSE","POLYGON"};
    private final String[] colourArgs = {"PEN","FILL"};

    private static final String colourRegex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"; // regex magic
    private Pattern regexPattern;
    private Matcher regexMatcher;

    public FileRead(){
        regexPattern = regexPattern.compile(colourRegex);
    }

    public void readFile(String path) throws IOException, FileInvalidArgumentException {
        FileInputStream fileIn = null;
        Scanner scanner = null;

        if(!returnFileType(path)){
            throw new FileInvalidArgumentException("Invalid file type.");
        }

        try {
            fileIn = new FileInputStream(path);
            scanner = new Scanner(fileIn, "UTF-8"); // check for different charset?
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!validateLine(line)) {
                    throw new FileInvalidArgumentException();
                } else {
                    //System.out.println(line);
                    // Do something with the input.
                }

            }
            if (scanner.ioException() != null) { // scanner does not throw exceptions so we have to throw for it
                throw scanner.ioException();
            }
        } catch (IOException err){
            throw err;
        } finally {
            if (fileIn != null) {
                fileIn.close(); // release file after we are done
            }
            if (scanner != null) {
                scanner.close(); // close our scanner also
            }
        }
    }

    /**
     *
     * @param line
     * @return
     */
    private boolean validateLine(String line) throws FileInvalidArgumentException {
        String[] lineSplit = Arrays.stream(line.split(" "))
                            .map(String::trim)
                            .map(String::toUpperCase)
                            .toArray(String[]::new); // trim and split the line

        // Check if line starts with a valid argument for shapes
        if(Arrays.stream(shapeArgs).anyMatch(lineSplit[0]::equals)){ // check if the argument is a shape
            switch (lineSplit[0]) {
                case "LINE":
                case "RECTANGLE":
                case "ELLIPSE":
                    if(lineSplit.length == 5) {
                        for(int i = 1; i < lineSplit.length; i++){
                            validateCoords(lineSplit,i);
                        }
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                case "PLOT":
                    if(lineSplit.length == 3) {
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                case "POLYGON":
                    if(lineSplit.length >= 3) {
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                default:
                    throw new FileInvalidArgumentException("Invalid argument in file.");
            }
        } else if (Arrays.stream(colourArgs).anyMatch(lineSplit[0]::equals)){
            switch (lineSplit[0]) {
                case "PEN":
                case "FILL":
                    regexMatcher = regexPattern.matcher(lineSplit[1]);
                    if(regexMatcher.find()){
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Invalid argument in file.");
                    }
                default:
                    throw new FileInvalidArgumentException("Invalid argument in file.");
            }
        } else if (lineSplit.length == 1) {
            return true; // technically a valid line if nothing is in it
        } else { // the argument is neither a shape or a colour
            throw new FileInvalidArgumentException("Invalid argument in file.");
        }
    }

    private void validateCoords(String[] line, int i) throws FileInvalidArgumentException {
        try {
            float coord = Float.parseFloat(line[i]);
            if(coord > 1.0f || coord < 0.0f){
                throw new FileInvalidArgumentException("Co-ordinates are invalid.");
            }
        } catch (NumberFormatException err) {
            throw new FileInvalidArgumentException(err);
        }
    }

    public boolean returnFileType(String path){
        int index = path.lastIndexOf('.');
        int pathIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

        if (index > pathIndex && index < (path.length()-1)){
           if (path.substring(index + 1).equals("vec")){
               return true;
           } else {
               return false;
           }
        } return false;
    }
}
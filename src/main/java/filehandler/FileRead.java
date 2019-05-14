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

    // Hex Colour Validation
    private static final String colourRegex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"; // regex magic
    private Pattern regexPattern;
    private Matcher regexMatcher;

    public FileRead(){
        regexPattern = regexPattern.compile(colourRegex); // compile our regex (needed for colour validation)
    }

    public void readFile(String path) throws IOException, FileInvalidArgumentException {
        FileInputStream fileIn = null;
        Scanner scanner = null;

        if(!returnFileType(path)){
            throw new FileInvalidArgumentException("Invalid file type.");
        }

        try {
            fileIn = new FileInputStream(path);
            scanner = new Scanner(fileIn, "UTF-8"); // turns out detecting char sets basically impossible
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!validateLine(line)) {
                    throw new FileInvalidArgumentException();
                } else {
                    //System.out.println(line);
                    // Do something with the input.
                }

            }
            if (scanner.ioException() != null) { // scanner does not throw exceptions so we have to check for it
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

        if(line.isEmpty() || line.isBlank()){ // don't read blank lines but don't throw an exception
            return true;
        }

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
                        for(int i = 1; i < lineSplit.length; i++){ // start from 1 because arg is at index 0
                            validateCoords(lineSplit,i);
                        }
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                case "PLOT":
                    if(lineSplit.length == 3) { // plot only has 3 args
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                case "POLYGON":
                    if(lineSplit.length >= 5) { // polygon must have more than 5 args (arg and 2 pairs of coords)
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                default:
                    throw new FileInvalidArgumentException("Invalid argument in file.");
            }
        } else if (Arrays.stream(colourArgs).anyMatch(lineSplit[0]::equals)) {
            switch (lineSplit[0]) {
                case "PEN":
                case "FILL":
                    regexMatcher = regexPattern.matcher(lineSplit[1]); // attempt to match hex colour to regex
                    if (regexMatcher.find()) {
                        return true;
                    } else {
                        if (lineSplit[1].equals("OFF")) {
                            return true;
                        } else {
                            throw new FileInvalidArgumentException("Invalid argument in file.");
                        }
                    }
                default:
                    throw new FileInvalidArgumentException("Invalid argument in file.");
            }
        } else { // the argument is neither a shape or a colour
            throw new FileInvalidArgumentException("Invalid argument in file.");
        }
    }

    private void validateCoords(String[] line, int i) throws FileInvalidArgumentException {
        try {
            float coord = Float.parseFloat(line[i]);
            if(coord > 1.0f || coord < 0.0f){ // this will fail if the co-ord is over 1.0 or below 0.0
                throw new FileInvalidArgumentException("Co-ordinates are invalid.");
            }
        } catch (NumberFormatException err) { // this will fail if the co-ord is not castable to float
            throw new FileInvalidArgumentException(err);
        }
    }

    private boolean returnFileType(String path){ // check if the file is a VEC file
        int index = path.lastIndexOf('.'); // last index due to file folders potentially having dots
        int pathIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

        if (index > pathIndex && index < (path.length() - 1)){
           if (path.substring(index + 1).equals("vec")){ // check for VEC file extension
               return true;
           } else {
               return false;
           }
        } return false;
    }
}
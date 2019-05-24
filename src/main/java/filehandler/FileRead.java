package main.java.filehandler;

import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.regex.*; // used for hex colour validation

import main.java.exceptions.*;

public class FileRead {
    private List<String[]> argsList = new ArrayList<String[]>(); // we store the args to pass back to gui in this list

    private final String[] shapeArgs = {"LINE","PLOT","RECTANGLE","ELLIPSE","POLYGON"};
    private final String[] colourArgs = {"PEN","FILL"};

    /**
     * A note on Regex usage in this class
     *
     * This regex here validates the hexadecimal colour inputs for the PEN and FILL
     * arguments. It does this by checking if there is 1 "#" character, followed by
     * either 3 or 6 characters in hexadecimal range. It will fail if the string
     * does not include the "#" character at the start, or if the characters are
     * not in base 16.
     *
     * I also didn't realise that AWT throws an exception if the colour is invalid
     * until I integrated this class in to the GUI, so this is pretty much useless.
     */
    private static final String colourRegex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    private Pattern regexPattern;
    private Matcher regexMatcher;

    /**
     * Constructor for FileRead Class
     * Only thing we use this for is compiling our regex (see above)
     * No params needed
     */
    public FileRead(){
        regexPattern = regexPattern.compile(colourRegex); // compile our regex (needed for colour validation)
        argsList.clear(); // clear our list of arguments
    }

    /**
     * readFile - Main func for reading a file
     * Call this func with the path for a file, and it reads and validates it. That's about it really.
     *
     * @param path to the selected file to read
     * @return a List of arguments for the GUI to parse
     * @throws IOException  only occurs if the Scanner fails during read - the Scanner doesn't throw so we have to
     * @throws FileInvalidArgumentException FileInvalidArgumentException will only occur here if the file extension
     * is not VEC
     */
    public List<String[]> readFile(String path) throws IOException, FileInvalidArgumentException {
        FileInputStream fileIn = null;
        Scanner scanner = null;

        if(!checkFileType(path)){
            throw new FileInvalidArgumentException("Invalid file type.");
        }

        try {
            fileIn = new FileInputStream(path);
            scanner = new Scanner(fileIn, "UTF-8"); // turns out detecting char sets basically impossible
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!validateLine(line)) {
                    throw new FileInvalidArgumentException(); // just in case something goes massively wrong
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
                scanner.close(); // close our scanner also, garbage collection should clean this up for us (hopefully)
            }
        }
        return argsList;
    }

    /**
     * validateLine - Checks that the line of the file is valid (e.g. valid argument, co-ords, colour etc)
     * @param line is the string the scanner has passed to it from readFile()
     * @return a boolean value of True if the line is valid.
     * @throws FileInvalidArgumentException - only thrown if the file could be read but has invalid arguments, co-ords
     * or colours etc.
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
                    if(lineSplit.length == 5) { // there should be 4 co-ords
                        for(int i = 1; i < lineSplit.length; i++){ // start from 1 because arg is at index 0
                            if(!validateCoords(lineSplit,i)){
                                return false; // no point continuing if there is an invalid co-ord
                            } else {
                                argsList.add(lineSplit);
                            }
                        }
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                case "PLOT":
                    if(lineSplit.length == 3) { // plot only has 3 args
                        for(int i = 1; i < lineSplit.length; i++){ // start from 1 because arg is at index 0
                            if(!validateCoords(lineSplit,i)){
                                return false; // no point continuing if there is an invalid co-ord
                            } else {
                                argsList.add(lineSplit);
                            }
                        }
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                case "POLYGON":
                    if(lineSplit.length >= 5) { // polygon must have more than 5 args (arg and 2 pairs of coords)
                        for(int i = 1; i < lineSplit.length; i++){ // start from 1 because arg is at index 0
                            if(!validateCoords(lineSplit,i)){
                                return false; // no point continuing if there is an invalid co-ord
                            } else {
                                argsList.add(lineSplit);
                            }
                        }
                        return true;
                    } else {
                        throw new FileInvalidArgumentException("Co-ordinates are invalid.");
                    }
                default: // we shouldn't get here but just in case!
                    throw new FileInvalidArgumentException("Invalid argument in file.");
            }
        } else if (Arrays.stream(colourArgs).anyMatch(lineSplit[0]::equals)) {
            switch (lineSplit[0]) {
                case "PEN":
                case "FILL":
                    regexMatcher = regexPattern.matcher(lineSplit[1]); // attempt to match hex colour to regex
                    if (regexMatcher.find()) {
                        argsList.add(lineSplit);
                        return true;
                    } else {
                        if (lineSplit[1].equals("OFF")) {
                            argsList.add(lineSplit);
                            return true;
                        } else {
                            throw new FileInvalidArgumentException("Invalid colour in file.");
                        }
                    }
                default: // again we shouldn't get here but just in case
                    throw new FileInvalidArgumentException("Invalid argument in file.");
            }
        } else { // the argument is neither a shape or a colour
            throw new FileInvalidArgumentException("Invalid argument in file.");
        }
    }

    /**
     * validateCoords - checks if the co-ordinates are castable to float, and are between 0.0f and 1.0f.
     * @param line accepts the split line (array of strings) from validateLine func.
     * @param i is the index of the array which we need to check.
     *          We do it this way so we don't have any odd casting issues or exceptions we didn't account for.
     * @return a boolean value which is True if the co-ordinate is valid.
     * @throws FileInvalidArgumentException if the co-ordinate is invalid.
     */
    private boolean validateCoords(String[] line, int i) throws FileInvalidArgumentException {
        try {
            float coord = Float.parseFloat(line[i]);
            if(coord > 1.0f || coord < 0.0f){ // this will fail if the co-ord is over 1.0 or below 0.0
                throw new FileInvalidArgumentException("Co-ordinates are invalid.");
            }
            return true;
        } catch (NumberFormatException err) { // this will fail if the co-ord is not castable to float
            throw new FileInvalidArgumentException(err);
        }
    }

    /**
     * checkFileType - a func that checks if the file ends in .vec
     * @param path - the path to the file
     * @return a boolean value that is valid if the file has the correct extension.
     */
    private boolean checkFileType(String path){ // check if the file is a VEC file
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
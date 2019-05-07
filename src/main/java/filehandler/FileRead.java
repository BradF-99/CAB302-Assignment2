package main.java.filehandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


public class FileRead {
    public void readFile(String path) throws IOException {
        FileInputStream fileIn = null;
        Scanner scanner = null;
        try {
            fileIn = new FileInputStream(path);
            scanner = new Scanner(fileIn, "UTF-8"); // check for different charset?
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // do something with the file here
            }
            if (scanner.ioException() != null) { // scanner does not throw exceptions
                throw scanner.ioException();
            }
        } finally {
            if (fileIn != null) {
                fileIn.close(); // release file after we are done
            }
            if (scanner != null) {
                scanner.close(); // close our scanner also
            }
        }
    }
}

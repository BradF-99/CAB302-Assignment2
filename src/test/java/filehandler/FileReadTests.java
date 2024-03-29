package test.java.filehandler;

import main.java.filehandler.FileRead;
import main.java.exceptions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FileReadTests {

    /*
     * File Handling Tests
     *
     * Things we need to test for during read:
     * - Testing read of a file that is partially valid (has some incorrect arguments)
     *
     */

    @BeforeAll
    public static void cleanup(){
        FileRead fileReader = new FileRead();
    }

    @Test
    public void testReadValidVECFile() {
        FileRead fileReader = new FileRead();
        assertDoesNotThrow(() -> { // iq 200
                fileReader.readFile("src/test/resources/filehandler/Test1.vec");
        });
    }

    @Test
    public void testReadInvalidVECFile() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test2.vec");
        });
    }

    @Test
    public void testReadNonExistentVECFile() {
        FileRead fileReader = new FileRead();
        assertThrows(FileNotFoundException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test3.vec"); // DO NOT ADD THIS FILE TO RESOURCES
        });
    }

    @Test
    public void testReadNonVECFile() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test4.txt");
        },"Error occurred while reading file. Invalid file type.");
    }

    @Test
    public void testReadBinaryNonVECFile() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test5.vec");  // This is a JPG image and should fail.
        },"Error occurred while reading file. Invalid file type.");
    }

    @Test
    public void testReadVECFileOverflowCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test6.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

    @Test
    public void testReadVECFileInvalidUnderflowCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test7.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

    @Test
    public void testReadVECFileUnspecifiedLineCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test8.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

    @Test
    public void testReadVECFileUnspecifiedRectangleCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test9.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

    @Test
    public void testReadVECFileUnspecifiedEllipseCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test10.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

    @Test
    public void testReadVECFileUnderflowPlotCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test11.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

    @Test
    public void testReadVECFileOverflowPlotCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test12.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

    @Test
    public void testReadVECFileUnderflowPolygonCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test13.vec");
        },"Error occurred while reading file. Invalid argument in file.");
    }

    @Test
    public void testReadVECFilePenValid() {
        FileRead fileReader = new FileRead();
        assertDoesNotThrow(() -> {
            fileReader.readFile("src/test/resources/filehandler/Test14.vec");
        });
    }

    @Test
    public void testReadVECFilePenOff() { // This should pass even though there is no colour.
        FileRead fileReader = new FileRead();
        assertDoesNotThrow(() -> {
            fileReader.readFile("src/test/resources/filehandler/Test15.vec");
        });
    }

    @Test
    public void testReadVECFileFillValid() {
        FileRead fileReader = new FileRead();
        assertDoesNotThrow(() -> {
            fileReader.readFile("src/test/resources/filehandler/Test16.vec");
        });
    }

    @Test
    public void testReadVECFileFillOff() {
        FileRead fileReader = new FileRead();
        assertDoesNotThrow(() -> {
            fileReader.readFile("src/test/resources/filehandler/Test17.vec");
        });
    }

    @Test
    public void testReadVECFilePenInvalid() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test18.vec");
        },"Error occurred while reading file. Invalid argument in file.");
    }

    @Test
    public void testReadVECFileFillInvalid() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test19.vec");
        },"Error occurred while reading file. Invalid argument in file.");
    }

    /*
      None of the next 3 tests should throw exception as they are valid examples.
     */

    @Test
    public void testReadExample1VECFile() {
        FileRead fileReader = new FileRead();
        assertDoesNotThrow(() -> {
            fileReader.readFile("src/test/resources/filehandler/example1.vec");
        });
    }

    @Test
    public void testReadExample2VECFile() {
        FileRead fileReader = new FileRead();
        assertDoesNotThrow(() -> {
            fileReader.readFile("src/test/resources/filehandler/example2.vec");
        });
    }

    @Test
    public void testReadExample3VECFile() {
        FileRead fileReader = new FileRead();
        assertDoesNotThrow(() -> {
            fileReader.readFile("src/test/resources/filehandler/example3.vec");
        });
    }
}

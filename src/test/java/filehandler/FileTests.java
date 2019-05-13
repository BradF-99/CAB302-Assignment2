package test.java.filehandler;

import main.java.filehandler.*;
import main.java.exceptions.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FileTests {
    /*
     * File Handling Tests
     *
     * Things we need to test for during read:
     * - Testing read of a file that is partially valid (has some incorrect arguments)
     * - Testing read of a file that has been deleted or has become inaccessible during processing
     * - Testing read of a file that we do not have permissions to open
     * - Testing read of a file that is wrongly encoded (eg. binary encoded instead of text based)
     *
     * Things we need to test for during write:
     * - Testing write of a VEC file
     * - Verifying a successful write of a VEC file (using read)
     * - Testing write of a VEC file to a read only folder
     * - Testing write of a VEC file to a place where we do not have access (OS permissions)
     * - Testing write of a VEC file where the media becomes inaccessible during write
     * - Testing write of a very detailed and/or large file
     * - Testing overwriting an existing file
     * - Testing overwriting a file that we do not have permission to overwrite
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
            fileReader.readFile("src/test/resources/filehandler/Test5.vec");
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
    public void testReadVECFileUnspecifiedRectangleCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test10.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

    @Test
    public void testReadVECFileUnspecifiedRectangleCoords() {
        FileRead fileReader = new FileRead();
        assertThrows(FileInvalidArgumentException.class, () -> {
            fileReader.readFile("src/test/resources/filehandler/Test9.vec");
        },"Error occurred while reading file. Co-ordinates are invalid.");
    }

}

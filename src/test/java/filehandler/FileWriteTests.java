package test.java.filehandler;

import main.java.filehandler.FileWrite;
import main.java.exceptions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FileWriteTests {
    /*
     * File Writing Tests
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
        FileWrite fileWriter = new FileWrite();
    }

}

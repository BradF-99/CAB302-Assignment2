package test.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import main.java.*;

public class Tests {
    @Test
    public void testExample() {
        int testSum = 3+6+9;
        Assertions.assertEquals(18,testSum);
    }
    @Test
    public void testMainExampleTrue (){
        Assertions.assertEquals(Main.exampleFunc(true),"Hi!");
    }
    @Test
    public void testMainExampleFalse (){
        Assertions.assertEquals(Main.exampleFunc(false),"Hello!");
    }

    /*
     * File Handling Tests
     *
     * Things we need to test for during read:
     * - Testing read of a valid VEC file
     * - Testing read of an invalid VEC file
     * - Testing read of a file that is partially valid (has some incorrect arguments)
     * - Testing read of a non-VEC file
     * - Testing read of a non-existent file
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

}
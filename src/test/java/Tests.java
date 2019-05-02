package test.java;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    @Test
    public void testExample() {
        int testSum = 3+6+9;
        assertEquals(18,testSum);
    }
    @Test
    public void testMainExampleTrue (){
        assertEquals(main.java.Main.exampleFunc(true),"Hi!");
    }
    @Test
    public void testMainExampleFalse (){
        assertEquals(main.java.Main.exampleFunc(false),"Hello!");
    }
}
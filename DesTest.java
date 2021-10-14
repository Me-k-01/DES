
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.*;

public class DesTest {
    @Test
    public void testBinaryToString() {
        String str = "Petit test";
        assertEquals(str, Des.binaryArrayToString(Des.stringToBinaryArray(str)));
    }      
    @Test
    public void testBinaryToInt() {
        
        assertArrayEquals(new boolean[]{false, false, true, false}, Des.intToBinaryArray(2, 4));
        for (int n = 0; n < 64; n++) {
            assertEquals(n, Des.binaryArrayToInt(Des.intToBinaryArray(n, 7)));
        }
    }      

    @Test
    public void testGeneratePermArray() {
        for (int size = 0; size < 64; size++) {

            int[] arr = Des.generatePermArray(size);
            int max = 0;
            // Duplicate test et Max test
            for(int i = 0; i < arr.length; i++) {  
                for(int j = i + 1; j < arr.length; j++) {  
                    if(arr[i] == arr[j])  {
                        fail("Duplicate index pointer"); 
                    }
                }  
                if (arr[i] > max)
                    max = arr[i];
                if (arr[i] < 1) 
                    fail("Index too low");
            }  
            if (max > size) 
                fail("Index out of range");
        }
    } 

    @Test
    public void test() {
        
    }
    
}
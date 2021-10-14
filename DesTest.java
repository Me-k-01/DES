
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.*;

public class DesTest {
    @Test
    public void testBinaryToString() {
        String str = "Petit test";
        boolean[] boolArr = Des.stringToBinaryArray(str);
        assertTrue(boolArr.length == str.length() * 8); // Chaque char est encodé sur 8 bit
        assertEquals(str, Des.binaryArrayToString(boolArr));
    }      
    @Test
    public void testBinaryToInt() {
        assertArrayEquals(new boolean[]{false, false, true, false}, Des.intToBinaryArray(2, 4));

        for (int i = 1; i < 8; i++) {
            for (int n = 0; n < 1 << i; n++) {
                boolean[] boolArr = Des.intToBinaryArray(n, i);
                assertTrue(boolArr.length == i);
                assertEquals(n, Des.binaryArrayToInt(boolArr));
            }
        }
    }      

    @Test
    public void testGeneratePermArray() {
        for (int size = 0; size < 64; size++) {

            int[] arr = Des.generatePermArray(size);
            assertTrue(arr.length == size);
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

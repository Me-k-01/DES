
import static org.junit.Assert.*;

import org.junit.*;

public class DesTest {
    @Test
    public void testBinaryToString() {
        String str = "Petit test";
        boolean[] boolArr = Des.stringToBinaryArray(str);
        assertTrue(boolArr.length == str.length() * 8); // Chaque charactere est encodé sur 8 bit
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
                    assertTrue(arr[i] != arr[j]); // test de redondance
                }  
                if (arr[i] > max)
                    max = arr[i];
                // La valeur des permutation est compris entre 1 et taille inclus.
                assertTrue(arr[i] > 0); 
                assertTrue(arr[i] <= size); 
            }  
            if (max > size) 
                fail("Index out of range");
        }
    } 

    @Test
    public void testSubstitution() {
        for (int i = 0; i < Math.pow(2, 2); i++) {
            for (int j = 0; j < Math.pow(2, 4); j++) {
                
                boolean[] iArr = Des.intToBinaryArray(i, 2);
                boolean[] jArr = Des.intToBinaryArray(j, 4);
                boolean[] arr = new boolean[]{iArr[0], jArr[0], jArr[1], jArr[2], jArr[3], iArr[1]};

                int[][] S = new int[][]{
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {3, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
                };
                assertArrayEquals(Des.intToBinaryArray(S[i][j], 4), Des.substitution(new Bloc(arr), S).toArray());

            }
        }
    }
    @Test
    public void testGenerateKey() {
        Des des = new Des();
        for (int i = 1; i < 128; i++) {
            Bloc key = des.generateKey();
            assertEquals(key.size, 48);
        }
    }
    
    @Test
    public void testDes() {
        Des des = new Des();
        String msg = "Bonjour a tous";
        assertEquals(des.decrypte(des.crypte(msg)), msg);
    }
}

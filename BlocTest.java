

import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.*;

public class BlocTest {
    @Test    
    public void testRandom() {
        for (int i = 0; i < 34; i++) {
            Bloc b = Bloc.random(i);
            assertEquals(b.size, i);
        }
    }
    @Test
    public void testToArray() {
        for (int i = 0; i < 64; i++) {
            boolean[] boolArr = new boolean[i];
            for (int j = 0; j < boolArr.length; j++) {
                boolArr[j] = Math.random() > 0.5;
            }
            assertArrayEquals(boolArr, new Bloc(boolArr).toArray());
        }
        for (int i = 0; i < 64; i++) {
            Bloc b = Bloc.random(i);
            assertEquals(i, b.toArray().length);
        }
    }  
    @Test
    public void testToListSize() {
        for (int i = 0; i < 64; i++) {
            Bloc bloc = Bloc.random(i);
            assertEquals(i, bloc.toList().size());
        }
    } 
    @Test 
    public void testToList() {
        for (int i = 1; i < 64; i++) {
            boolean[] boolArr = new boolean[i];
            List<Boolean> boolList = new ArrayList<Boolean>();  
            System.out.println(i);
            for (int j = 0; j < i; j++) {
                boolean b = Math.random() > 0.5; 

                boolArr[j] = b;
                boolList.add(b);
            }

            assert(boolList.equals((new Bloc(boolArr)).toList()));
        }
    }


    @Test    
    public void smallTestCombine() {
        Bloc A = new Bloc(new boolean[]{false, true, false});
        Bloc B = new Bloc(new boolean[]{false, false});
        assertArrayEquals(new boolean[]{false, true, false, false, false}, Bloc.combine(A, B).toArray());;
               
    }
    @Test 
    public void testCombine() {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                Bloc A = Bloc.random(i);
                Bloc B = Bloc.random(j);
                Bloc comb = Bloc.combine(A, B);
                assertEquals(A.size, B.size, comb.size);
                assertEquals(i+j, comb.toArray().length);
                List<Boolean> expected = Stream.concat(A.toList().stream(), B.toList().stream())
                             .collect(Collectors.toList());

                assert(expected.equals(comb.toList()));
            }
        }
    }
    @Test
    public void testSplit() {
        for (int i = 2; i < 64; i+=2) {
            Bloc bloc = Bloc.random(i);

            assert(bloc.equals(Bloc.combine(bloc.split())));    
        }
    } 
    @Test
    public void testSlice() {
        for (int i = 2; i < 128; i+=2) {
            for (int j = 2; j < i; j+=2) {
                Bloc bloc = Bloc.random(i);
                if (i % j == 0) {
                    Bloc[] b = bloc.slice(j);

                    assert(bloc.equals(Bloc.combine(b)));
                }
            }
        }
    }
    @Test
    public void subBlock() {
        for (int size = 4; size < 64; size++) {
            for (int i = 0; i < size-2; i++) {
                for (int j = i+1; j < size-1; j++) {
                    
                    Bloc A = Bloc.random(size);
                    Bloc B = A.subBlock(i, j);
                    assertEquals(j-i, B.size); // Test de taille

                    for (int k = 0; k < B.size; k++) {
                        assertEquals(A.get(k+i), B.get(k));
                    }   
                }
            }
        }
    }
    @Test
    public void testPermut() {
        for (int size = 1; size < 128; size++) {
            Bloc bloc = Bloc.random(size);

            int[] permutTab = Des.generatePermArray(size);
            Bloc permutedBloc = bloc.permut(permutTab);
            assertEquals(size, permutedBloc.size);
            for (int i = 0; i < permutedBloc.size; i++) {
                assertEquals(bloc.get(permutTab[i]-1), permutedBloc.get(i));
            }

            // Test inverse
            Bloc invPerm = permutedBloc.invPermut(permutTab);
            assertTrue(bloc.equals(invPerm));
        }    

    }
    @Test
    public void testShift() {
        for (int size = 1; size < 64; size++) {
            Bloc A = Bloc.random(size);
            Bloc B = A.shift(); 
            assertEquals(A.size, B.size); // Test de taille
            assertEquals(A.get(0), B.get(size-1)); 
            for (int i = 1; i < size; i++) {
                assertEquals(A.get(i), B.get(i-1));
            }
        }    

    } 
    @Test
    public void testXor() {
        for (int size = 1; size < 64; size++) {
            Bloc A = Bloc.random(size);
            Bloc B = Bloc.random(size);

            Bloc AxorB = A.xor(B);

            for (int i = 0; i < size; i++) {
                boolean xor = (A.get(i) || B.get(i)) && !(A.get(i) && B.get(i));
                assertEquals(xor, AxorB.get(i));
            }
            
        }

    }    
}


import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.*;

public class BlocTest {
    @Test
    public void testToArray() {
        for (int i = 0; i < 64; i++) {
            boolean[] boolArr = new boolean[i];
            for (int j = 0; j < boolArr.length; j++) {
                boolArr[j] = Math.random() > 0.5;
            }
            assertArrayEquals(boolArr, new Bloc(boolArr).toArray());
        }
    }      
    public void testCombine() {
        /*
        List<Boolean> list = new ArrayList<Boolean>(A.size + B.size);
        Collections.addAll(list, A.toArray());
        Collections.addAll(list, B.toArray());*/
        Bloc A = new Bloc(new boolean[]{false, true, false});
        Bloc B = new Bloc(new boolean[]{false, false});
        assertArrayEquals(new boolean[]{false, true, false, true, false, false}, Bloc.combine(A, B).toArray());;
    }
    
}

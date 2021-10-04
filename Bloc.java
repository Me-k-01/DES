import java.util.Arrays;

class Bloc {
    public int size;
    private boolean[] array;

    public static Bloc random(int size) {
        boolean[] randomArr = new boolean[size];
        for (int i = 0; i < size; i++) {
            randomArr[i] = Math.random() * 2 < 1;
        }
        return new Bloc(randomArr);
    }
    public Bloc(int size) {
        this.size = size;
        this.array = new boolean[size];
    }
    public Bloc(boolean[] array) {
        this.size = array.length;
        this.array = array;
    }
    public boolean[] toArray() {
        return this.array;
    } 
    public void shift() {
        // Decalage circulaire de 1 bit vers la gauche
        boolean b = this.array[0];
        for (int i = 0; i < this.array.length - 2; i++) {
            this.array[i] = this.array[i+1];
        } 
        this.array[this.array.length - 1] = b;
    } 
    public void permut(int[] permBloc) {
        boolean[] permArr = new boolean[this.size];
        for (int i = 0; i < this.size; i++) {
            permArr[i] = this.array[permBloc[i]-1]; 
        }
        this.array = permArr;
    }
    public void invPermut(int[] permBloc) {

        boolean[] arr = new boolean[this.size];
        for (int i = 0; i < this.size; i++) {
            arr[permBloc[i]-1] = this.array[i]; 
        }
        this.array = arr;
    }
    @Override
    public String toString() {
        String str = "[";
        for (int i = 0; i < this.size; i++) {
            if (this.array[i])
                str += "1"; 
            else 
                str += "0"; 
            if (i < this.size - 1) 
                str += ", ";
        }
        return str+"]";
    }
    public Bloc subBlock(int i, int j) {
        boolean[] arr = new boolean[this.size - (i-j)];
        
        for (int index = i; index < j; index++) {
            arr[index-i] = this.array[index];
        }
        return new Bloc(arr);
    }
    public Bloc[] split() {
        int i = this.size/2;
        return new Bloc[]{
            new Bloc(Arrays.copyOfRange(this.array, 0, i)), 
            new Bloc(Arrays.copyOfRange(this.array, i, this.size))
        }; 
    }
    static Bloc combine(Bloc blocA, Bloc blocB) {
        boolean[] arr = new boolean[blocA.size + blocB.size];
        System.arraycopy(blocA.toArray(), 0, arr, 0, blocA.size);
        System.arraycopy(blocB.toArray(), 0, arr, blocA.size, blocB.size);
        return new Bloc(arr);
    }
    static Bloc combine(Bloc[] blocs) {
        Bloc blocA = blocs[0];
        Bloc blocB = blocs[1]; 
        boolean[] arr = new boolean[blocA.size + blocB.size];
        System.arraycopy(blocA.toArray(), 0, arr, 0, blocA.size);
        System.arraycopy(blocB.toArray(), 0, arr, blocA.size, blocB.size);
        return new Bloc(arr);
    }
}

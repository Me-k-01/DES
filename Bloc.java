import java.util.Arrays;

class Bloc {
    public int size;
    private int[] array;
    public Bloc(int size) {
        this.size = size;
        this.array = new int[size];
    }
    public Bloc(int[] array) {
        this.size = array.length;
        this.array = array;
    }
    public int[] toArray() {
        return this.array;
    } 
    public void shift() {
        // Decalage de 1 bit vers la gauche
        int n = this.array[0];
        for (int i = 0; i < this.array.length - 2; i++) {
            this.array[i] = this.array[i+1];
        } 
        this.array[this.array.length - 1] = n;
    } 
    /*
    public int get(int i) {
        return this.array[i];
    } */ 
    public void permut(int[] permBloc) {

        int[] permArr = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            permArr[i] = this.array[permBloc[i]-1]; 
        }
        this.array = permArr;
    }
    public void invPermut(int[] permBloc) {

        int[] arr = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            arr[permBloc[i]-1] = this.array[i]; 
        }
        this.array = arr;
    }
    @Override
    public String toString() {
        return Arrays.toString(this.array);
    }
    public Bloc[] split() {
        int i = this.size/2;
        return new Bloc[]{
            new Bloc(Arrays.copyOfRange(this.array, 0, i)), 
            new Bloc(Arrays.copyOfRange(this.array, i, this.size))
        }; 
    }
    static Bloc combine(Bloc blocA, Bloc blocB) {
        int[] arr = new int[blocA.size + blocB.size];
        System.arraycopy(blocA.toArray(), 0, arr, 0, blocA.size);
        System.arraycopy(blocB.toArray(), 0, arr, blocA.size, blocB.size);
        return new Bloc(arr);
    }
    static Bloc combine(Bloc[] blocs) {
        Bloc blocA = blocs[0];
        Bloc blocB = blocs[1]; 
        int[] arr = new int[blocA.size + blocB.size];
        System.arraycopy(blocA.toArray(), 0, arr, 0, blocA.size);
        System.arraycopy(blocB.toArray(), 0, arr, blocA.size, blocB.size);
        return new Bloc(arr);
    }
}

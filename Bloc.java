
import java.util.Arrays;

class Bloc {
    public int size;
    private boolean[] array;

    /////////// Methode statique ///////////
    public static Bloc random(int size) {
        boolean[] randomArr = new boolean[size];
        for (int i = 0; i < size; i++) {
            randomArr[i] = Math.random() * 2 < 1;
        }
        return new Bloc(randomArr);
    }    

    static Bloc combine(Bloc blocA, Bloc blocB) {
        boolean[] arr = new boolean[blocA.size + blocB.size];
        System.arraycopy(blocA.toArray(), 0, arr, 0, blocA.size);
        System.arraycopy(blocB.toArray(), 0, arr, blocA.size, blocB.size);
        return new Bloc(arr);
    }
    static Bloc combine(Bloc[] blocs) {
        int size = blocs.length * blocs[0].size;
        boolean[] arr = new boolean[size]; 
        for (int i = 0; i < blocs.length; i++) {
            boolean[] array = blocs[i].toArray();
            System.arraycopy(array, 0, arr, i*array.length, array.length);
        } 
        return new Bloc(arr);
    }

    /////////// Constructeur ///////////
    public Bloc(int size) {
        this.size = size;
        this.array = new boolean[size];
    }
    public Bloc(boolean[] array) {
        this.size = array.length;
        this.array = array;
    }

    /////////// Decoupage ///////////
    public Bloc[] split() {
        return this.slice(this.size/2);
    }
    public Bloc[] slice(int size) {
        int quantity = (this.size + size - 1) / size; // taille du tableau binaire / size et arrondir au dessus);
        Bloc[] blocs = new Bloc[quantity];
        // Découpage du tableau booleen en un tableau de Bloc
        for (int i = 0; i < this.size; i += size) {
            blocs[i/size] = new Bloc(Arrays.copyOfRange(this.array, i, i + size));
        }
        return blocs; 
    }
    public Bloc subBlock(int i, int j) {
        boolean[] arr = new boolean[this.size - (i-j)];
        
        for (int index = i; index < j; index++) {
            arr[index-i] = this.array[index];
        }
        return new Bloc(arr);
    }
    /////////// Operation sur le Bloc ///////////
    public void shift() {
        // Decalage circulaire de 1 bit vers la gauche
        boolean b = this.array[0];
        for (int i = 0; i < this.array.length - 2; i++) {
            this.array[i] = this.array[i+1];
        } 
        this.array[this.array.length - 1] = b;
    } 
    public Bloc permut(int[] permTab) {
        boolean[] permArr = new boolean[permTab.length];
        for (int i = 0; i < permTab.length; i++) {
            permArr[i] = this.array[permTab[i]-1]; 
        }
        return new Bloc(permArr);
    }
    public Bloc invPermut(int[] permTab) {
        boolean[] arr = new boolean[this.size];
        for (int i = 0; i < permTab.length; i++) {
            arr[permTab[i]-1] = this.array[i]; 
        }
        return new Bloc(arr);
    }
    public Bloc xor(Bloc b) {
        boolean[] arr = new boolean[this.size];
        for (int i = 0; i < this.array.length; i++) {
            arr[i] = this.array[i] ^ b.get(i);
        }
        return new Bloc(arr);
    }
    ////////////////////////////////////////////////
    public boolean get(int i) {
        return this.array[i];
    }

    /////////// Fonction de convertion ///////////
    public boolean[] toArray() {
        return this.array;
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
    ////////////////////////////////////////////////
}

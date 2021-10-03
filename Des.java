import java.util.Arrays;

class Des {
    public int[] permute; 
    public int tailleBloc = 64;

    public int[] permInit = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    }; 
    private int size = 64;

    private Bloc[] blocs;

    public static int[] stringToBinaryArray(String msg) {
        byte[] bytes = msg.getBytes();
        int[] output = new int[bytes.length * 8];
        int j = 0;
        for (int i = 0; i < bytes.length; i++) {
            String binaryString = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');

            for (char c : binaryString.toCharArray()) {
                output[j] = Character.getNumericValue(c);
                j++;
            }
        }
        return output;
    }

    private Bloc[] decoupage(boolean[] binArray) {
        int quantity = (binArray.length + this.size - 1) / this.size; // binArray.length / size et arrondir au dessus
        Bloc[] blocs = new Bloc[quantity];
        for (int i = 0; i < binArray.length; i += this.size) {
            blocs[i/this.size] = new Bloc(Arrays.copyOfRange(binArray, i, i + this.size));
        }
        return blocs;
    }    

    private Bloc generateKey() {
        Bloc masterKey = Bloc.random(this.size); // masterKey
        Bloc key = masterKey.subBlock(0, this.size - 8);

        Bloc[] keys = key.split();      
        keys[0].shift();
        keys[1].shift();
        key = Bloc.combine(keys);
        
        return null;
    }
    private int processK(Bloc G, Bloc D, int n) {
        Bloc masterKey = generateKey() ;
        
        return 0;
    }
    public int[] crypte(String msg) {
        this.blocs = decoupage(stringToBinaryArray(msg));

        for (int i = 0; i < this.blocs.length; i++) {
            Bloc b = this.blocs[i];
            // Permutation initial
            b.permut(this.permInit);
            // Decoupage en deux partie
            Bloc[] splitedBloc = b.split();
            
            // Determination de clé
            processK(splitedBloc[0], splitedBloc[1], 16);
            // Deux partie sont recollées
            this.blocs[i] = Bloc.combine(splitedBloc);
            // Permutation inverse
            this.blocs[i].invPermut(this.permInit);
        }
        System.out.println(Arrays.deepToString(this.blocs));
        
        return null;
    }

    public String decrypte(int[] decrypte) {return null;
    }

    public static void main(String[] args) {
        Des d = new Des();
        int[] messCrypt = d.crypte("Bonjour a tous");
        //System.out.println(d.decrypte(messCrypt));
    }
}
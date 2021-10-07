import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
    crypte

    decrypte
    Dn = Gn+1
    Gn = Dn+1 xor F(Kn, Dn) 
*/
class Des {
    public int[] permute; 
    public Bloc masterKey;

    private int[] permInit = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        
        57, 49, 41, 33, 25, 17,  9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    }; 

    private int[] compPerm = { // Compression permutation 56 -> 48 bits
        14, 17, 11, 24,  1,  5,  3, 28, 15,  6, 21, 10,
        23, 19, 12,  4, 26,  8, 16,  7, 27, 20, 13,  2,
        41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 
    };

    private int[] expPerm = {  // Expansion permutation 32 -> 48 bits 
        32,  1,  2,  3,  4,  5,  4,  5,
         6,  7,  8,  9,  8,  9, 10, 11,
        12, 13, 12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21, 20, 21,
        22, 23, 24, 25, 24, 25, 26, 27,
        28, 29, 28, 29, 30, 31, 32,  1 
    };

    private int[][] S1 = {
        {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
        {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
        {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
        {3, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
    };

    private int size = 64;

    private Bloc[] blocs;


    public static boolean[] stringToBinaryArray(String msg) {
        byte[] bytes = msg.getBytes();
        boolean[] output = new boolean[bytes.length * 8];
        int j = 0;
        for (byte b : bytes) {
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'); // convertion en chaine de character de 0 et de 1
            for (char c : binaryString.toCharArray()) {
                output[j] = (c == '1'); // remplir le tableau de booleen
                j++;
            }
        }
        return output;
    }

    public int[] generatePermArray(int size) {
        int[] arr = new int[size];
        List<Integer> indicesPoss = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) indicesPoss.add(i+1);
        
        for (int i = 0; i < size; i++) {
            int index = (int)(Math.random() * indicesPoss.size());
            int n = indicesPoss.remove(index);
            arr[i] = n;
        }
        return arr;
    }

    private Bloc generateKey() {
        // calcul une clé de 48 bits 

        this.masterKey = Bloc.random(this.size); // masterKey de 64 bits
        // Suppression des 8 derniers bits
        Bloc key = this.masterKey.subBlock(0, this.size - 8); // 58 bits 
        // Permutation random de la clé de 58 bits
        key.permut(generatePermArray(this.size - 8));
        // Découpage en deux clé
        Bloc[] keys = key.split();      // 28 bits
        // Décalage circulaire de 1 bit vers la gauche
        keys[0].shift();
        keys[1].shift();
        // Recoller les deux blocs 
        key = Bloc.combine(keys); // 58bits
        // Compression et permutation 
        key.permut(this.compPerm); // Reduction en une clé de 48 bit
        
        return key;
    }

    private int binaryArrayToInt(boolean[] bools) {
        int n = 0;
        for (int i = 0; i < bools.length; i++) {
            // decalage de bits vers la gauche pour calculer les puissances de deux
            if (bools[i])
                n += 1 << i; // = 2 ^ i
        }
        return n;
    }
    private boolean[] intToBinaryArray(int n, int size) {
        boolean[] b = new boolean[size];
        int i = size-1;  // on remplie a l'envers
        for (char c : Integer.toBinaryString(n).toCharArray()) {
            b[i] = c == '1';
            i--;
        }
        return b;
    }


    private Bloc substitution(Bloc b) {
        int i = binaryArrayToInt(new boolean[]{b.get(0), b.get(5)});
        int j = binaryArrayToInt(new boolean[]{b.get(1), b.get(2), b.get(3), b.get(4)}); 

        return new Bloc(intToBinaryArray(S1[i][j], 4));
    } 
    
    public Bloc fonction_F(Bloc K, Bloc D) {
        D.permut(this.expPerm); // Expansion permutation 32 -> 48
        
        Bloc[] Ds = D.xor(K).slice(6); // Decoupage en 8 bloc de 6 bits
        for (int i = 0; i < Ds.length; i++) {
            Ds[i] = substitution(Ds[i]); // Substitution S1
        }
        
        /*
        Bloc[] Ds = D.slice(6); // Decoupage en 8 bloc de 6 bits
        for (int i = 0; i < Ds.length; i++) {
            Ds[i] = substitution(Ds[i]); // Substitution S1
        }
        System.out.println(Arrays.toString(Ds));
        Bloc FKD = Bloc.combine(Ds); // F(Kn, Dn) sur 32 bit
        */
        return null;
    }

    private Bloc processK(Bloc G, Bloc D, int n) {
        // Faire 16 fois:

        // Determination d'une clé Kn
        Bloc key = generateKey() ; 
        // Dn+1 = Gn XOR F(Kn ,Dn )
        Bloc Dn = G.xor(fonction_F(key, D));
        // Gn+1 = Dn
        Bloc Gn = D;


        // Deux parties sont recollées
        return Bloc.combine(Gn, Dn);
    }
    
    public int[] crypte(String msg) {

        this.blocs = new Bloc(stringToBinaryArray(msg)).slice(this.size);
        for (int i = 0; i < this.blocs.length; i++) {
            Bloc b = this.blocs[i];
            // 2.1 Permutation initial
            b.permut(this.permInit);
            // 2.2 Découpage en deux parties
            Bloc[] splitedBloc = b.split();
            
            // 2.3 et 2.4 recollé
            b = processK(splitedBloc[0], splitedBloc[1], 16);
            // 2.5 Permutation inverse
            this.blocs[i].invPermut(this.permInit);
        }
        //System.out.println(Arrays.deepToString(this.blocs));
        
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

import java.util.ArrayList;
import java.util.List;

class Des {
    private Bloc[][] keys; 
    private int roundQuant = 16; // Nombre de ronde

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
    private int[][][] subArr; // Tableau de matrice de substitution 
    private int[][] currS; // Matrice de substitution

    public Des() {
        this.roundQuant = 16;
        setUpSubstitution();
    }
    public Des(int roundQuant) {
        /* 
        S1 = new int[][]{
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {3, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
        };
        */
        this.roundQuant = roundQuant;
        setUpSubstitution();
    }
    public void setUpSubstitution() {
        this.subArr = new int[this.roundQuant][4][16];
        for (int i = 0; i < this.roundQuant; i++) {
            this.subArr[i] = generateSubArray();
        }
    }

    /////////// Fonction de generation aléatoire  ///////////
    static public int[][] generateSubArray() { // Tableau de subtitution
        int n = 4;
        int p = 16;
        int max = 16;
        int[][] arr = new int[n][p];
        List<Integer> indicesPoss = new ArrayList<Integer>();

        for (int i = 0; i < max; i++) 
            indicesPoss.add(i);
        for (int i = 0; i < n*p - max; i++) 
            indicesPoss.add((int)(Math.random() * max));

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                int index = (int)(Math.random() * indicesPoss.size());
                arr[i][j] = indicesPoss.remove(index);
            }
        }
        return arr;
    }
    static public int[] generatePermArray(int size) { // Tableau de permutation
        int[] arr = new int[size];
        List<Integer> indicesPoss = new ArrayList<Integer>();
        for (int i = 1; i < size+1; i++) 
            indicesPoss.add(i);
        
        for (int i = 0; i < size; i++) {
            int index = (int)(Math.random() * indicesPoss.size());
            arr[i] = indicesPoss.remove(index);
        }
        return arr;
    }

    public Bloc generateKey() {
        // calcul une clé de 48 bits 

        Bloc masterKey = Bloc.random(64); // masterKey de 64 bits
        // Suppression des 8 derniers bits
        Bloc key = masterKey.subBlock(0, 58); // 58 bits 
        // Permutation random de la clé de 58 bits
        key = key.permut(generatePermArray(58));
        // Découpage en deux clé
        Bloc[] keys = key.split();      // 28 bits
        // Décalage circulaire de 1 bit vers la gauche
        keys[0] = keys[0].shift(); 
        keys[1] = keys[1].shift();
        // Recoller les deux blocs 
        key = Bloc.combine(keys); // 58bits
        // Compression et permutation 
        key = key.permut(this.compPerm); // Reduction en une clé de 48 bit
        
        return key;
    }
    
    /////////// Fonction de convertion ///////////
    static public boolean[] stringToBinaryArray(String msg) {
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
    static public String binaryArrayToString(boolean[] bools) {
        String msg = "";
        Bloc[] blocs = new Bloc(bools).slice(8);
        for (Bloc bloc : blocs) {
            byte b = 0;
            for (boolean bool : bloc.toArray()) {
                b <<= 1;
                if (bool) b |= 1;
            }
            msg += (char)b;
        }
        return msg;
    }
    static public int binaryArrayToInt(boolean[] bools) {
        int n = 0;
        for (int i = 0; i < bools.length; i++) {
            // Décalage de i bits vers la gauche pour calculer les puissances de deux
            if (bools[i])
                n += 1 << (bools.length -1 - i); // équivalent à: 2 ^ (bools.length - i)
        }
        return n;
    }
    static public boolean[] intToBinaryArray(int n, int size) {
        boolean[] b = new boolean[size];
        String str = Integer.toBinaryString(n);
        int i = size - str.length();  // On remplit à partir du permier 1

        for (char c : str.toCharArray()) {
            b[i] = c == '1';
            i++;
        }
        return b;
    }
    ////////////////////////////////////////////////

    static public Bloc substitution(Bloc b, int[][] Sn) {
        // Fonction de substitution qui permet de passer de 6 à 4 bits 
        int i = binaryArrayToInt(new boolean[]{b.get(0), b.get(5)}); // les bits 1 et 6 code i
        int j = binaryArrayToInt(new boolean[]{b.get(1), b.get(2), b.get(3), b.get(4)});  // les bits 2 à 5 encodes j

        return new Bloc(intToBinaryArray(Sn[i][j], 4)); // Bloc de 4 bit
    } 

    private Bloc fonction_F(Bloc K, Bloc D) {

        // Expansion permutation
        D = D.permut(this.expPerm); // 32 bits vers 48 bits

        // Dn* = E XOR Kn   et    Découpage en 8 blocs de 6 bits
        Bloc[] Ds = D.xor(K).slice(6); // 8 Blocs de 6 bits
        
        // On passe chaque bloc de 6 bits dans une fonction de substitution
        for (int i = 0; i < Ds.length; i++) {
            Ds[i] = substitution(Ds[i], this.currS); // 8 Blocs de 4 bits
        }
        return Bloc.combine(Ds); // F(Kn, Dn) sur 32 bit
    }
    
    public boolean[] crypte(String msg) {
        // Crypte un message en un tableau de booléens

        // 1 Le texte est fractionné en bloc de 64 bits
        Bloc[] blocs = new Bloc(stringToBinaryArray(msg)).slice(64);
        this.keys = new Bloc[blocs.length][this.roundQuant];


        for (int i = 0; i < blocs.length; i++) {
            Bloc b = blocs[i];
            Bloc[] k = this.keys[i];
            // 2.1 Permutation initial
            b = b.permut(this.permInit);

            // 2.2 Découpage en deux parties
            Bloc G = b.left(); Bloc D = b.right(); // G et D sur 32 bits

            // 2.3 Faire n fois:
            for (int n = 0; n < this.roundQuant; n++) {
                k[n] = generateKey() ;  // Determination d'une clé Kn sur 48 bits
                this.currS = this.subArr[n];

                Bloc Dn = G.xor(fonction_F(k[n], D)); // Dn+1 = Gn XOR F(Kn, Dn)
                G = D; // Gn+1 = Dn
                D = Dn;
            } 


            // 2.4 Deux parties sont recollées
            b = Bloc.combine(G, D); // 64 bits

            // 2.5 Permutation inverse
            blocs[i] = b.invPermut(this.permInit);
        }
        Bloc bloc = Bloc.combine(blocs);

        return bloc.toArray();    
    }

    public String decrypte(boolean[] decrypte) {
        // Decrypte un message encrypté en un tableau de booléens
        Bloc[] blocs = new Bloc(decrypte).slice(64);
        for (int i = 0; i < blocs.length; i++) {
            Bloc b = blocs[i];
            Bloc[] k = this.keys[i];
            // 2.1 Permutation initial

            b = b.permut(this.permInit);
            // 2.2 Découpage en deux parties
            Bloc Gn = b.left(); Bloc Dn = b.right(); // G et D sur 32bits
            
            // 2.3 Faire n fois:
            for (int n = this.roundQuant-1; n >= 0; n--) {
                this.currS = this.subArr[n];
                Bloc D = Gn;  // Dn = Gn+1
                Gn = Dn.xor(fonction_F(k[n], D)); // Gn = Dn+1 xor F(Kn, Dn)
                Dn = D;
            } 

            
            // 2.4 Deux parties sont recollées
            b = Bloc.combine(Gn, Dn); // 64 bits
            // 2.5 Permutation inverse
            blocs[i] = b.invPermut(this.permInit);
        }    
        return binaryArrayToString(Bloc.combine(blocs).toArray()).trim();
    }

    static public void main(String[] args) {
        Des d = new Des();
        boolean[] messCrypt = d.crypte("Bonjour a tous");
        System.out.println(binaryArrayToString(messCrypt));
        System.out.println(d.decrypte(messCrypt));
    }
}
package org.example;

import java.util.ArrayList;
import java.util.List;

public class Block {
    //Contient 16 mots originaux : W0,...,W15
    // et 48 générés
    //Contient au plus 64 mots
    private static final int NB_WORDS = 64;
    private Word[] words;
    public Block() {
        this.words = new Word[NB_WORDS];
    }
    public Block(int[] block){
        // diviser message en 16 mots de 32 bits
        this.words = new Word[NB_WORDS];
        transformToWords(block);
        // rajoute les 48 autres mots
        generateWords();
    }

    // Traitement des blocks
    // méthodes pour obtenir les 48 mots supplémentaires en fonction des 16 premiers
    private void generateWords(){
        int posWt;
        Word wt2, wt7, wt15, wt16;

        for (int i=16; i< NB_WORDS; i++){
            // TODO supp
            System.out.println("generateWords : i :" +i);
            wt2 = words[i - 2];
            wt7 = words[i - 7];
            wt15 = words[i - 15];
            wt16 = words[i - 16];

            if (wt2 == null || wt7 == null || wt15 == null || wt16 == null) {
                throw new IllegalStateException("Les mots nécessaires pour générer W" + i + " ne sont pas initialisés.");
            }

            this.words[i]=new Word((sigma1(wt2)+ wt7.getValue() + sigma0(wt15) + wt16.getValue()));
            // TODO supp
            System.out.println("W" + i + ": " + this.words[i].getValue());

        }
    }

    public int sigma0 (Word x){
        return (x.rotateRight(7) ^ x.rotateRight(18) ^ x.shiftRight(3));
    }
    public int sigma1 (Word x){
        return (x.rotateRight(17) ^ x.rotateRight(19) ^ x.shiftRight(10));
    }
    public void transformToWords (int[] block){
        // TODO supp
        System.out.println(block.length);
        // On doit impérativement travailler avec des blocs de 512 bits
        if (block.length != 16) {
            throw new IllegalArgumentException("Le bloc doit contenir exactement 16 entiers (512 bits). " +block.length);
        }
        for (int i = 0; i < 16; i++) {
            this.words[i] = new Word(block[i]);
//            int value = ((block[i * 4] & 0xFF) << 24) |
//                    ((block[i * 4 + 1] & 0xFF) << 16) |
//                    ((block[i * 4 + 2] & 0xFF) << 8) |
//                    (block[i * 4 + 3] & 0xFF);
//            words[i]=new Word(value);
            // TODO supp
            System.out.println("i : " +i);
        }
    }

    public Word[] getWords() {
        return  this.words;
    }
}

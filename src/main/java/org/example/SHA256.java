package org.example;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SHA256 {
    private List<Block> blocks;
    private int[]H; // Constantes de hashage initiales
    // Variables basées sur les constantes H
    private int[]S; //  Variables temporaires issues de H[]
    private int[]K; // Constantes pour les 64 itérations

    // Déroulement algo
    // 1 : transforme message en binaire
    // 2 : ajoute 1 à la fin du message
    // 3 : rajoute 0 jusqu'à obtenir un multiple de 512 bits - 64 bits de longeur du message initial
    // 4 : diviser en block de 512 bits
    // 5 : dérouler l'algo pour chaque block


    public String sha256(String message){
        // Initialisation des constantes
        initH();
        initK();

        // Padding du message
        //int[] paddedMessage = padding(message);
        byte[] byteMessage = message.getBytes(StandardCharsets.UTF_8);
        byte[] paddedMessage = padMessage(byteMessage);

        // Diviser en blocs de 512 bits
        //divideIntoBlocks(paddedMessage);
        // TODO après
        divideIntoBlocks(paddedMessage);

        // Processus de cryptage pour chaque bloc
        for(Block block : blocks){
            // Copier H dans S pour chaque bloc
            this.S = H.clone();

            // 64 itérations
            for (int t=0; t<64; t++){
                int wt = block.getWords()[t].getValue();
                updateS(wt, t);
            }
            // Mise à jour de H en fonction de S[]
            updateH();
        }

        // Construction du hash final à partir de H
        StringBuilder hash = new StringBuilder();
        for (int h : H) {
            hash.append(String.format("%08x", h));
        }
        // Résultat final
        return hash.toString();

    }
    public byte[] padMessage(byte[] message){
        int messageLength = message.length;
        int padMessageLength = ((messageLength + 8)/ 64 + 1) * 64;
        // TODO supp
        System.out.println("messageLength " +messageLength);
        System.out.println("padMessageLength " +padMessageLength);

        byte[] paddedMessage = new byte[padMessageLength];
        System.arraycopy(message, 0, paddedMessage, 0, messageLength);

        paddedMessage[messageLength] = (byte) 0x80;

        long bitLength = (long) messageLength * 8;
        for(int i=0; i<8; i++){
            paddedMessage[padMessageLength - 1 -i] = (byte) (bitLength >>> (i*8));
        }

        // TODO supp
        System.out.println(paddedMessage.toString());
        System.out.println("Padded message:");
        for (byte b : paddedMessage) {
            System.out.print(String.format("%02x", b) + " ");
        }
        System.out.println();

        return paddedMessage;
    }

    // TODO supp si autre fonction marche
    public int[] padding(String message){

        byte[] messageBytes = message.getBytes();
        int messageBitLength = messageBytes.length * 8;

        int paddingBits = (448 - (messageBitLength + 1) % 512 + 512) % 512;
        int totalBits = messageBitLength + 1 + paddingBits + 64;
        int totalInts = totalBits / 32;

        int[] paddedMessage = new int[totalInts];
        ByteBuffer buffer = ByteBuffer.wrap(messageBytes);

        for (int i = 0; i < messageBytes.length; i++) {
            paddedMessage[i / 4] |= (buffer.get(i) & 0xFF) << (24 - (i % 4) * 8);
        }

        paddedMessage[messageBytes.length / 4] |= 0x80 << (24 - (messageBytes.length % 4) * 8);

        long bitLength = (long) messageBitLength;
        paddedMessage[totalInts - 2] = (int) (bitLength >>> 32);
        paddedMessage[totalInts - 1] = (int) (bitLength & 0xFFFFFFFF);

        return paddedMessage;
    }

    public void divideIntoBlocks(byte[] paddedMessage){
        this.blocks = new ArrayList<>();

        for(int i=0; i<paddedMessage.length; i+=64){
            int[] data = new int[16];
            for(int j=0; j<16; j++){
                int x = i + j * 4;
                // TODO
                data[j] = ((paddedMessage[x] & 0xff) << 24) |
                        ((paddedMessage[x + 1] & 0xff) << 16) |
                        ((paddedMessage[x + 2] & 0xff) << 8) |
                        (paddedMessage[x + 3] & 0xff);

            }
            this.blocks.add(new Block(data));
            // TODO supp
            System.out.println(paddedMessage.length);
        }
    }


    public void updateS (int wt, int t){
        int t1 = t1(wt, t);
        int t2 = t2(t);
        this.S[7] = this.S[6];
        this.S[6] = this.S[5];
        this.S[5] = this.S[4];
        this.S[4] = this.S[3] + t1;
        this.S[3] = this.S[2];
        this.S[2] = this.S[1];
        this.S[1] = this.S[0];
        this.S[0] = t1 + t2;
    }
    public void updateH(){
        for (int i=0; i< this.H.length; i++){
            this.H[i] += this.S[i];
        }
    }

    // Initialisation variables H[] et K[]
    private void initH(){
        this.H = new int[] {
                0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
                0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
        };
    }
    private void initK() {
        this.K = new int[] {
                0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
                0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
                0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
                0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
                0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
                0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
                0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
                0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
        };
    }

    private int t1(int wt, int x){
        return (this.S[7] + sigma1(this.S[4]) + ch(this.S[4],this.S[5],this.S[6]) + this.K[x] + wt);
    }
    private int t2(int x){
       return (sigma0(this.S[0]) + maj(this.S[0], this.S[1], this.S[2]));
    }
    private int sigma0(int x) {
        return (Integer.rotateRight(x,2) ^ Integer.rotateRight(x,13) ^Integer.rotateRight(x,22));
    }
    private int sigma1(int x) {
        return ((x >> 6) ^ (x >> 11) ^ (x >> 25));
    }
    private int ch(int x, int y, int z) {
        return ((x & y) ^ ((~x) & z));
    }
    private int maj(int x, int y, int z) {
        return ((x & y) ^ (x & z) ^ (y & z));
    }

}

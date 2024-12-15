package org.example;

public class Main {
    public static void main(String[] args) {
        SHA256 sha256 = new SHA256();

        // Messages à coder
        String message1 = "abc";
        String hash1 = sha256.sha256(message1);

        System.out.println("Message1 : " +message1);
        System.out.println("Hash1 : " +hash1);
    }


}


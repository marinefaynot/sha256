package org.example;

public class Word {
    private int value;

    public Word(int value) {
        this.value = value;
    }

    public Word(Word w){
        this.value = w.getValue();
    }
    public Word(){}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int rotateRight(int n){
        return Integer.rotateRight(this.value, n);
    }

    public int shiftRight(int n){
        return this.value >> n;
    }
}

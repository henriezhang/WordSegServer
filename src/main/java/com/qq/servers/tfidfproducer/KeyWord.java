package com.qq.servers.tfidfproducer;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-18
 * Time: 下午7:24
 */
public class KeyWord implements Comparable<KeyWord> {

    private String name;
    //        private double idf;
    private int freq;
    //        private int weight;
    private double unit;

    public KeyWord(String name, double idf, int weight) {
        this.name = name;
//            this.idf = idf;
        this.unit = idf * weight;
//            this.weight = weight;
        freq = 1;
    }

    public void incrementOccurs(int count) {
        freq += count;
    }

    public String getName() {
        return this.name;
    }

    public double getScore() {
        return freq * unit;
    }

    /**
     * key word with high score come first. do reverse the result of value comparation.
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(KeyWord other) {
        double thisScore = freq * unit;
        double otherScore = other.freq * other.unit;

        if (thisScore < otherScore) {
            return 1;
        } else if (thisScore > otherScore) {
            return -1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return name;
    }


}

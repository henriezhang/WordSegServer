package com.qq.servers;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-13
 * Time: 下午6:46
 */
public class MathTest
{

    public static void main(String[] args)
    {
        int PRECISION = 1000;
        double value = 11.23423423d;
        value = (double) Math.round(value * PRECISION) / PRECISION;
        System.out.println(value);

    }
}

package com.qq.servers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by antyrao on 14-1-14.
 */
public class RegularTest
{
    public static void main(String[] args)
    {
        Pattern pattern = Pattern.compile("(?s)[\\d|%|'].*");

        Matcher matcher = pattern.matcher("meig");
        if (matcher.find())
        {
            System.out.println("Found");
        }

    }
}

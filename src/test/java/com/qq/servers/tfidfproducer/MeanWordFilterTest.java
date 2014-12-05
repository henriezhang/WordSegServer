package com.qq.servers.tfidfproducer;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by antyrao on 14-1-14.
 */
public class MeanWordFilterTest extends TestCase
{
    @Test
    public void testAccept() throws Exception
    {
        Pattern pattern = Pattern.compile("(?s)[\\d|%|']");

        Matcher matcher = pattern.matcher("一块1mXX13");

        if (matcher.find())
        {
            System.out.println("Matcher");
        }

    }
}

package com.qq.servers.tfidfproducer;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;


/**
 * Created by antyrao on 14-2-25.
 */
public class StopWordDict
{
    private static Set<String> stopWords = Sets.newHashSet();
    private static final String STOP_WORDS = "stopwords";

    private static Logger LOG = LoggerFactory.getLogger(StopWordDict.class);

    static
    {


        try
        {
            InputStream is = IdfDict.class.getClassLoader().getResourceAsStream(STOP_WORDS);

            if (is == null)
                throw new RuntimeException("Can't find idf dictionary : " + STOP_WORDS);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            try
            {
                double total = 0;
                String line;
                while ((line = reader.readLine()) != null)
                {
                    stopWords.add(line);
                    total++;
                }
                LOG.info("Successfully load stopwords dictionary " + STOP_WORDS + " with total record " + total);

            }
            catch (IOException e)
            {
                throw new RuntimeException(Throwables.getStackTraceAsString(e), e);
            }
            finally
            {
                try
                {
                    reader.close();
                }
                catch (IOException ignored)
                {
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static boolean containDigit(String word)
    {
        char[] chars = word.toCharArray();
        for (char c : chars)
        {
            if (Character.isDigit(c))
                return true;
        }
        return false;
    }

    public static boolean isStopWord(String word)
    {
        return stopWords.contains(word) || containDigit(word) || word.length() == 1;
    }

    public static void main(String[] args)
    {
        String str = "中国";
        System.out.println(isStopWord(str));

    }
}

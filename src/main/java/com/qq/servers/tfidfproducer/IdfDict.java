package com.qq.servers.tfidfproducer;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

/**
 * Created by antyrao on 14-1-15.
 */
public class IdfDict
{
    private static final Logger LOG = LoggerFactory.getLogger(IdfDict.class);

    private static final Charset utf8 = Charset.forName("utf-8");
    private static final String SEPARATOR_REGEX = "\\t";


    protected Hashtable<String, Double> wordToIdfMap = new Hashtable<String, Double>();

    protected String idfName;
    protected Double median;

    protected IdfDict(String idfName)
    {
        this.idfName = idfName;
        loadIdf();
    }

    private void loadIdf()
    {

        try
        {
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream(idfName);
            InputStream is = IdfDict.class.getClassLoader().getResourceAsStream(idfName);

            if (is == null)
                throw new RuntimeException("Can't find idf dictionary : " + idfName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, utf8));

            try
            {
                double total = 0;
                int badRecord = 0;
                String line;
                while ((line = reader.readLine()) != null)
                {
                    String[] wordAndIdf = line.split(SEPARATOR_REGEX);
                    if (wordAndIdf.length != 2)
                    {
                        badRecord++;
                        continue;
                    }
                    Double idf = Double.valueOf(wordAndIdf[1]);
                    String word = wordAndIdf[0];
                    wordToIdfMap.put(word, idf);
                    total++;
                }
                Collection<Double> values = wordToIdfMap.values();
                median = computeMedian(values);

                LOG.info("Successfully load idf dictionary " + idfName + " with total record " + total + " and " +
                        " bad record " + badRecord);

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

    private Double computeMedian(Collection<Double> values)
    {
        Double[] weights = values.toArray(new Double[values.size()]);
        Arrays.sort(weights);
        if (weights.length % 2 == 0)
        {
            return (weights[weights.length / 2] + weights[weights.length / 2 - 1]) / 2;
        }
        else
        {
            return weights[weights.length / 2];
        }
    }

    public Double getIdf(String word)
    {
        return wordToIdfMap.get(word);
    }

    public Double getMedian()
    {
        return median;
    }

}

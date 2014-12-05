package com.qq.servers;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.qq.servers.tfidfproducer.*;
import org.ansj.domain.Term;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-19
 * Time: 下午3:37
 */
public class KeywordExtractCommand implements Command
{

    public static TFIDFCalculator tfidfCalculator = new TFIDFCalculator();

    public static WordFilter filter = new MeanWordFilter();

    //TODO read from configuration.
    public static Weight titleWeight = new Weight.TitleWeight(5, filter);
    //    public static Weight contentWeight = new Weight.ContentWeight(3, filter);
    public static Weight contentWeight = new Weight.ContentWeightWithoutPos(3, filter);


    private String title;
    private String content;
    private String site;
    private int numCount;
    private boolean hasWeight;


    public KeywordExtractCommand(String title, String content, String site, int numCount, boolean hasWeight)
    {
        this.title = title;
        this.content = content;
        this.numCount = numCount;
        this.hasWeight = hasWeight;
        this.site = site;
    }

    @Override
    public int getPreference()
    {
        return 0;
    }

    @Override
    public boolean getAnnotation()
    {
        return false;
    }

    public List<Fragment> getFragments(boolean annotation)
    {
        List<Fragment> fragments = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(title))
        {
            List<Term> words = segmenter.segmentWord(title, annotation);
            fragments.add(new Fragment(words, titleWeight));
        }

        if (!Strings.isNullOrEmpty(content))
        {
            List<Term> words = segmenter.segmentWord(content, annotation);
            fragments.add(new Fragment(words, contentWeight, content.length()));
        }
        return fragments;
    }


    private static final Joiner colonJoiner = Joiner.on(":");
    private static int PRECISION = 10;

    @Override
    public void execute(List<Fragment> fragments, ObjectNode result)
    {
        ArrayNode arrayNode = result.putArray(getName());
        List<KeyWord> keyWords = tfidfCalculator.computeTfidf(fragments, numCount, site);
        if (hasWeight)
        {
            for (KeyWord kw : keyWords)
            {
                //keep
                double value = kw.getScore();
                value = (double) Math.round(value * PRECISION) / PRECISION;
                arrayNode.add(colonJoiner.join(kw.getName(), value));
            }
        }
        else
        {
            for (KeyWord kw : keyWords)
            {
                arrayNode.add(kw.getName());
            }
        }
    }

    @Override
    public String getName()
    {
        return Opcode.KW.getName();
    }

    public static Command createCommand(Map<String, List<String>> parameters)
    {

        String title = null;

        List<String> titles = parameters.get(Command.TITLE_KEY);
        if (titles != null)
        {
            title = titles.get(0);
        }

        String content = null;
        List<String> contents = parameters.get(Command.CONTENT_KEY);
        if (contents != null)
        {
            content = contents.get(0);
        }

        String site = "default";
        List<String> sites = parameters.get(Command.SITE_KEY);
        if (sites != null)
        {
            site = sites.get(0);
        }


        int kwCount = 5;
        boolean hasWeight = false;
        if (parameters.get(COUNT_KEY) != null)
        {
            kwCount = Integer.valueOf(parameters.get(COUNT_KEY).get(0));
        }

        if (parameters.get(WEIGHT_KEY) != null)
        {
            hasWeight = Boolean.valueOf(parameters.get(WEIGHT_KEY).get(0));
        }
        return new KeywordExtractCommand(title, content, site, kwCount, hasWeight);
    }

}

package com.qq.servers;

import com.google.common.collect.Lists;
import com.qq.servers.tfidfproducer.StopWordDict;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

/**
 * This class use Ansj word segmenter to segment words
 * <p/>
 * Created with IntelliJ IDEA.
 * User: Rao
 * Date: 13-10-13
 * Time: 上午11:04
 */
public class AnsjWordSegmenter implements WordSegmenter {
    /**
     * the format of result string is :<p>
     * 1. words separated by black space<p>
     * 2. word and annotation separated by "/"
     * e.g.
     * 我爱北京天安门
     * ==>
     * 我/r 爱/v 北京/ns 天安门/ns
     *
     * @param words
     * @param annotation
     * @return
     */
    protected static String buildResult(List<Term> words, boolean annotation) {
        StringBuilder sb = new StringBuilder();
        for (Term term : words) {
            sb.append(term.getName());
            if (annotation) {
                sb.append("/");
                sb.append(term.getNatrue().natureStr);
            }
            sb.append(" ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public List<Term> segmentWord(String sentence, boolean annotation) {
        List<Term> parsed = ToAnalysis.parse(sentence);
        parsed = filterWords(parsed);
        if (!annotation) {
            return parsed;
        }
        NatureRecognition recognition = new NatureRecognition(parsed);
        recognition.recognition();

        return parsed;
    }

    private List<Term> filterWords(List<Term> words) {
        List<Term> result = Lists.newArrayList();
        for (Term word : words) {
            if (!StopWordDict.isStopWord(word.getName())) {
                result.add(word);
            }
        }
        return result;
    }
}

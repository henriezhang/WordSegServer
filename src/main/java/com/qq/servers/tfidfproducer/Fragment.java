package com.qq.servers.tfidfproducer;

import org.ansj.domain.Term;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-9
 * Time: 下午3:24
 * <p/>
 * a document consist of different parts, title,sub-title,content,each correspond to a fragment with different weight
 */
public class Fragment {
    /**
     * words produced by word segmenter
     */
    public List<Term> words;
    /**
     * the weight associate with this paragraph
     */
    public Weight weighter;
    /**
     * the length of paragraph
     */
    public int length;

    public Fragment(List<Term> data, Weight weighter, int length) {
        this.words = data;
        this.weighter = weighter;
        this.length = length;
    }

    public Fragment(List<Term> data, Weight weighter) {
        this(data, weighter, -1);
    }
}

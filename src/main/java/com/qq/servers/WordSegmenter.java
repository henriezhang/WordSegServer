package com.qq.servers;

import org.ansj.domain.Term;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rao
 * Date: 13-10-13
 * Time: 上午11:03
 */
public interface WordSegmenter
{
    public List<Term> segmentWord(String sentence, boolean annotation);
}

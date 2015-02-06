package com.qq.servers.tfidfproducer;

import org.ansj.domain.Term;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-18
 * Time: 下午7:03
 */
public class WordFreFilter implements WordFilter {

    private Pattern pattern = Pattern.compile("(?s)\\d.*");
//    private Pattern pattern = Pattern.compile("(?s)[a-zA-Z_0-9]*");

    @Override
    public boolean accept(Object wordObj) {
        Term word = (Term) wordObj;

        Matcher matcher = pattern.matcher(word.getName());
        if (matcher.matches()) {
            return false;
        }

        if (word.getName().trim().length() < 2) {
            return false;
        }

        String wordProperity = word.getNatrue().natureStr;

        if (!wordProperity.startsWith("n")) {
            return false;
        }

        return true;
    }
}

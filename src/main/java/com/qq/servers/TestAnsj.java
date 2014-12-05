package com.qq.servers;

import org.ansj.app.newWord.LearnTool;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rao
 * Date: 13-10-12
 * Time: 下午10:07
 */
public class TestAnsj
{

    public static void testToAnanalyzer(String sentence)
    {
        List<Term> parsed = ToAnalysis.parse(sentence);

        System.out.println("word segment without annotation");
        System.out.println("#"+build(parsed, true)+"#");

        NatureRecognition recognition = new NatureRecognition(parsed);
        recognition.recognition();
        System.out.println("word segment with annotation");
        System.out.println("#"+build(parsed,true)+"#");

    }

    private static String build(List<Term> words, boolean annotation)
    {
        StringBuilder sb = new StringBuilder();
        for (Term term : words)
        {
            sb.append(term.getName());
            if (annotation)
            {
                sb.append("/");
                sb.append(term.getNatrue().natureStr);
            }
            sb.append(" ");
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();

    }

    public static void testNLP(String sentence)
    {
        LearnTool learn = new LearnTool();
        List<Term> parsed = NlpAnalysis.parse(sentence, learn);
        System.out.println(parsed);
    }

    public static void main(String[] args)
    {

        System.out.println("=====ToAnalayzer========");
        testToAnanalyzer("我爱北京天安门");
        System.out.println("=====NLP==========");
        testNLP("亚太经合组织");

    }

}

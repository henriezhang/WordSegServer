package com.qq.servers.tfidfproducer;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.qq.servers.AnsjWordSegmenter;
import org.ansj.domain.Term;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ArticleProcessorTitle {
    private static final AnsjWordSegmenter segmenter = new AnsjWordSegmenter();
    private static final Splitter tabSplitter = Splitter.on('\t');
    private static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static String processOneLine(String line, boolean verbose) throws ParseException {
//        StringBuilder sb = new StringBuilder();
        List<String> items = tabSplitter.splitToList(line);

        String articleId = items.get(0);
        String pv = items.get(1);
        String title = items.get(2);

        if (verbose) {
            System.out.println("articleId = " + articleId + ",pv = " + pv + " title = " + title);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(pv);
        sb.append("\t");
        sb.append(wordSegAndTrim(title));
        return sb.toString();
//
//
//        //pubtime,ltitle,infotype,flag,topic,abstract;
//        String appid = items.get(0);
////        String pubTime = items.get(1);//convert to hours
//        String ltitle = items.get(1); // word segment
//        String infoType = items.get(2);//intact
//        String flag = items.get(3);//intact
//        String topic = items.get(4);//intact
//        String articleAbstract = items.get(5);//word segment
//
//        String content = items.get(6);
////        if (items.size() > 6)
////        {
////        content = items.get(6);
////        }
////        if (!Strings.isNullOrEmpty(pubTime))
////        {
////            pubTime = String.valueOf(dataFormat.parse(pubTime).getHours());
////        }
//        if (!Strings.isNullOrEmpty(ltitle))
//        {
//            ltitle = wordSegAndTrim(ltitle);
//        }
//        if (!Strings.isNullOrEmpty(articleAbstract))
//        {
//            articleAbstract = wordSegAndTrim(articleAbstract);
//        }
//        sb.append(appid);
//        sb.append("\t");
////        sb.append(pubTime);
////        sb.append("\t");
//        sb.append(ltitle);
//        sb.append("\t");
//        sb.append(infoType);
//        sb.append("\t");
//        sb.append(flag);
//        sb.append("\t");
//        sb.append(topic);
////        sb.append(normalizeTopic(topic));
//        sb.append("\t");
//        sb.append(articleAbstract);
//        if (content != null)
//        {
//            sb.append("\t");
//            sb.append(wordSegAndTrim(content));
//        }
//        return sb.toString();
    }

    private static String prepareDataForBayes2(String line) throws ParseException {
        StringBuilder sb = new StringBuilder();

        List<String> items = tabSplitter.splitToList(line);
        //pubtime,ltitle,infotype,flag,topic,abstract;
        String clickThrough = items.get(0);
//        String pubTime = items.get(1);//convert to hours
        String ltitle = items.get(1); // word segment
        String infoType = items.get(2);//intact
        String flag = items.get(3);//intact
        String topic = items.get(4);//intact
        String articleAbstract = items.get(5);//word segment

//        if (!Strings.isNullOrEmpty(pubTime))
//        {
//            pubTime = String.valueOf(dataFormat.parse(pubTime).getHours());
//        }
        if (!Strings.isNullOrEmpty(ltitle)) {
            ltitle = wordSegAndTrim(ltitle);
        }
        if (!Strings.isNullOrEmpty(articleAbstract)) {
            articleAbstract = wordSegAndTrim(articleAbstract);
        }
        sb.append(clickThrough);
//        sb.append("\t");
//        sb.append(pubTime);
        sb.append("\t");
        sb.append(ltitle).append(" ").append(normalizeTopic(topic)).append(" ").append(articleAbstract);
//        sb.append("\t");
//        sb.append(infoType);
//        sb.append("\t");
//        sb.append(flag);
//        sb.append("\t");
//        sb.append(normalizeTopic(topic));
//        sb.append("\t");
//        sb.append(articleAbstract);
        return sb.toString();
    }

    private static String normalizeTopic(String topic) {
        StringBuilder sb = new StringBuilder();
        for (String wordAndWeight : Splitter.on(",").split(topic)) {
            sb.append(wordAndWeight.split(":")[0]);
            sb.append(" ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }


    private static String prepareDataForBayes(String line) throws ParseException {
        StringBuilder sb = new StringBuilder();

        String[] tmp = line.split("\t");
        if (tmp.length != 2)
            return null;
        String target = tmp[0];
        String content = tmp[1];
        String segmentedContent = null;
        if (content != null)
            segmentedContent = wordSegAndTrim(content);
        sb.append(target);
        sb.append("\t");
        sb.append(segmentedContent);
        return sb.toString();
    }


    public static String wordSegAndTrim(String line) {
        List<Term> words = segmenter.segmentWord(line, false);
//        Joiner joiner = Joiner.on(" ");
        StringBuilder sb = new StringBuilder();
        for (Term term : words) {
            String word = term.getName();
            int index = word.indexOf("/");
            if (index >= 0) {
                word = word.substring(0, index);
            }
            sb.append(word);
            sb.append(" ");
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
//
//    public static void ProcessOneLineTest() throws ParseException
//    {
//        String data = "";
//        System.out.println(processOneLine(data));
//
//    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage : <file name> <output file name>");
            return;
        }
        String fileName = args[0];
        String output = args[1];
        boolean verbose = false;
        if (args.length > 2) {
            verbose = Boolean.valueOf(args[2]);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(output)));
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;


        try {
            while ((line = reader.readLine()) != null) {
                try {
//                    String str = prepareDataForBayes(line);
//                    String str = prepareDataForBayes2(line);
                    String str = processOneLine(line, verbose);
                    if (str == null)
                        continue;
                    writer.write(str);
                } catch (ParseException e) {
                    continue;
                }
                writer.newLine();
            }
        } finally {
            Closeables.close(writer, true);
            Closeables.close(reader, true);
        }
    }
}

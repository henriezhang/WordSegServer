package com.qq.servers.tfidfproducer;

import com.google.common.base.Joiner;
import com.google.common.io.Closeables;
import com.qq.servers.AnsjWordSegmenter;
import org.ansj.domain.Term;

import java.io.*;
import java.util.List;

/**
 * Created by antyrao on 14-2-25.
 */
public class WordSegClient {
    public static void main(String[] args) throws IOException {

        wordSegment(args);
        if (true) {
            return;
        }

        if (args.length < 2) {
            System.out.println("Usage : <file name> <output file name>");
            return;
        }
        AnsjWordSegmenter segmenter = new AnsjWordSegmenter();
        String fileName = args[0];
        String output = args[1];
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(output)));
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                List<Term> words = segmenter.segmentWord(line, false);
                writer.write(Joiner.on(" ").join(words));
                writer.newLine();
            }
        } finally {
            Closeables.close(writer, true);
            Closeables.close(reader, true);
        }
    }

    public static void wordSegment(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage : <file name> <output file name>");
            System.exit(1);
        }
        AnsjWordSegmenter segmenter = new AnsjWordSegmenter();
        String fileName = args[0];
        String output = args[1];
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(output)));
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] tmp = line.split("\t");
                String target = tmp[0];
                String content = tmp[1];

                List<Term> words = segmenter.segmentWord(content, false);
                writer.write(target);
                writer.write("\t");
                writer.write(Joiner.on(" ").join(words));
                writer.newLine();
            }
        } finally {
            Closeables.close(writer, true);
            Closeables.close(reader, true);
        }

    }
}

package com.qq.servers.application;

import com.qq.servers.tfidfproducer.ArticleProcessorContent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by antyrao on 14-6-5.
 */
public class ContentExtraction {
    public static void process(String inputPath, String outputPath) throws IOException {
        PrintWriter writer = new PrintWriter(outputPath);
        long badRecord = 0;
        BufferedReader br = new BufferedReader(new FileReader(inputPath));
        String line;
        while ((line = br.readLine()) != null) {
            try {
                String wordSeged = ArticleProcessorContent.wordSegAndTrim(line);
                writer.print(wordSeged);
                writer.print(" ");
            } catch (Exception e) {
                badRecord++;
            } //try catch
        }
        br.close();
        writer.flush();
        writer.close();
        System.out.println("There are " + badRecord + " records");
    }

    public static void main(String[] args) throws IOException {
        String inputFileName = args[0];
        String outputFileName = args[1];
        process(inputFileName, outputFileName);
    }
}

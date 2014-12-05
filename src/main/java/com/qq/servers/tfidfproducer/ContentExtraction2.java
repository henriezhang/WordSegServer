package com.qq.servers.tfidfproducer;

import org.apache.hadoop.io.Text;

import java.io.*;

/**
 * Created by antyrao on 14-6-5.
 */
public class ContentExtraction2 {
    public static void process(String dirPath, String outputPath) throws IOException {
        DocumentParser parser = new DocumentParser();
        DocumentParser.Filter filter = new DocumentParser.Filter() {
            @Override
            public boolean accept(String url) {
                return true;
            }
        };

        PrintWriter writer = new PrintWriter(outputPath);
        Text text = new Text();
        File dir = new File(dirPath);
        long badRecord = 0;
        for (File file : dir.listFiles()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
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
        }
        writer.flush();
        writer.close();
        System.out.println("There are " + badRecord + " records");
    }

    public static void main(String[] args) throws IOException {
        String fileFolder = args[0];
        String outputFileName = args[1];
        process(fileFolder, outputFileName);
    }
}

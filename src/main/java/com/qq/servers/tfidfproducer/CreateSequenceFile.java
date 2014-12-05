package com.qq.servers.tfidfproducer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-15
 * Time: 下午7:37
 */
public class CreateSequenceFile
{



    public static void main(String args[]) throws IOException
    {

        if (args.length != 2)
        {
            System.err.println("Usage CreateSequenceFile <input directory> <output path");
            System.exit(1);
        }

        Path inputDir = new Path(args[0]);
        Path outputPath = new Path(args[1]);

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, outputPath, NullWritable.class, Text.class);
        NullWritable key = NullWritable.get();
        Text example = new Text("example");
        writer.append(key, example);

    }



}

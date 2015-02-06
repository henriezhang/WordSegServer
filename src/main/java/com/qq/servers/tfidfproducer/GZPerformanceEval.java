package com.qq.servers.tfidfproducer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-16
 * Time: 下午3:09
 * <p/>
 * <p/>
 * line counter of text file
 */
public class GZPerformanceEval extends Configured implements Tool {


    public static void main(String args[]) throws Exception {
        ToolRunner.run(new Configuration(), new GZPerformanceEval(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: DocumentCounter  <InputDir> <OutputDir>");
            System.exit(2);
        }

        Job job = new Job(conf, "antyrao-GZ performance evaluation");
        job.setJarByClass(this.getClass());
        job.setMapperClass(DocumentCounterJobMapper.class);
        job.setReducerClass(Reducer.class);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(0);


        FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
        return 0;
    }

    public static class DocumentCounterJobMapper extends Mapper<LongWritable, Text, NullWritable, NullWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
        }

    }
}

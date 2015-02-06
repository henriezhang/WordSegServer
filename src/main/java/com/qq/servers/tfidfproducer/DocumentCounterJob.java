package com.qq.servers.tfidfproducer;

import com.qq.servers.tfidfproducer.io.MultipleTextInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
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
public class DocumentCounterJob extends Configured implements Tool {


    public static void main(String args[]) throws Exception {
        ToolRunner.run(new Configuration(), new DocumentCounterJob(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: DocumentCounter  <InputDir> <OutputDir>");
            System.exit(2);
        }


        Job job = new Job(conf, "antyrao-Total Document Count");
        job.setJarByClass(this.getClass());
        job.setMapperClass(DocumentCounterJobMapper.class);
        job.setReducerClass(DocumentCounterJobReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);


        job.setInputFormatClass(MultipleTextInputFormat.class);

        job.setNumReduceTasks(1);

        FileInputFormat.setMaxInputSplitSize(job, 300 * 1024 * 1024L);
        FileInputFormat.setMinInputSplitSize(job, 200 * 1024 * 1024L);

        FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
        return 0;
    }

    public static class DocumentCounterJobMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        private static Text KEY = new Text("key");
        private long count = 0;

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            count++;
            //TODO
            //need to report progress status
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(KEY, new LongWritable(count));
        }

    }

    public static class DocumentCounterJobReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            //there is only one key, following code should run one time.
            long sum = 0;
            for (LongWritable value : values) {
                sum += value.get();
            }

            context.write(new Text(""), new LongWritable(sum));
        }
    }
}

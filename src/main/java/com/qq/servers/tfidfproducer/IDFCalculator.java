package com.qq.servers.tfidfproducer;

import com.qq.servers.tfidfproducer.io.MultipleTextInputFormat;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-15
 * Time: 下午6:05
 */
public class IDFCalculator extends Configured implements Tool {

    private static String IDS_CALCULATOR_DOCUMENT_COUNT_KEY = "idf.calculator.document.count.key";

    private static String IDF_CALCULATOR_DOMAIN_FILTER_KEY = "idf.calculator.domain.filter.key";


    public static void setDomain(Configuration conf, String domain) {
        conf.set(IDF_CALCULATOR_DOMAIN_FILTER_KEY, domain);
    }

    public static String getDomain(Configuration conf) {
        return conf.get(IDF_CALCULATOR_DOMAIN_FILTER_KEY, "");
    }

    public static void setDocumentCount(Configuration conf, long count) {
        conf.setLong(IDS_CALCULATOR_DOCUMENT_COUNT_KEY, count);
    }

    public static long getDocumentCount(Configuration conf) {
        return conf.getLong(IDS_CALCULATOR_DOCUMENT_COUNT_KEY, 0);
    }

    public static void main(String args[]) throws Exception {
        //maybe need pre-processing.
        int res = ToolRunner.run(new Configuration(), new IDFCalculator(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 3) {
            System.err.println("other argument length:" + otherArgs.length);
            System.out.println("#");
            for (String arg : otherArgs) {
                System.err.println(arg);
            }
            System.out.println("#");
            System.err.println("Usage: idfcalculate <document count>  <InputDir> <OutputDir> <domain>");
            System.exit(2);
        }
        long documentCount = Long.valueOf(otherArgs[0]);
        String strInput = otherArgs[1];
        String strOutput = otherArgs[2];

        if (otherArgs.length >= 4) {
            String domain = otherArgs[3];
            setDomain(conf, domain);
        }


        Job job = new Job(conf, "antyrao-IDF Calculator");
        job.setJarByClass(this.getClass());
        job.setMapperClass(IDFCalculatorMapper.class);
        job.setReducerClass(IDFCalculatorReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setInputFormatClass(MultipleTextInputFormat.class);

        job.setNumReduceTasks(1);

        //command line argument take precedence over hard code value

        if (job.getConfiguration().getLong("mapred.max.split.size", -1) == -1) {
            FileInputFormat.setMaxInputSplitSize(job, 500 * 1024 * 1024L);
        }
        if (job.getConfiguration().getLong("mapred.min.split.size", -1) == -1) {
            FileInputFormat.setMinInputSplitSize(job, 256 * 1024 * 1024L);
        }


        setDocumentCount(job.getConfiguration(), documentCount);

        FileInputFormat.setInputPaths(job, new Path(strInput));
        FileOutputFormat.setOutputPath(job, new Path(strOutput));


        System.exit(job.waitForCompletion(true) ? 0 : 1);
        return 0;
    }

    //prerequisite:
    //we are assuming each document are unique,documents come from our own database.
    public static class IDFCalculatorMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        private final static LongWritable one = new LongWritable(1);
        private static final DocumentParser.Filter ACCEPT_ALL_FILTER = new DocumentParser.Filter() {
            @Override
            public boolean accept(String url) {
                return true;
            }
        };
        private final DocumentParser docParser = new DocumentParser();
        private Set<String> uniqueWordsInOneDocument = new HashSet<String>();
        //for use
        private Text reuse = new Text();
        private WordFilter filter = new MeanWordFilter();
        private DocumentParser.Filter domainFilter;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            final String domain = getDomain(context.getConfiguration());
            if (domain.equals("")) {
                domainFilter = ACCEPT_ALL_FILTER;
            } else {
                domainFilter = new DocumentParser.Filter() {
                    @Override
                    public boolean accept(String url) {
                        return url.startsWith(domain);
                    }
                };
            }
        }

        //one document per line
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.getCounter(MyCounter.totalrecord).increment(1);
            uniqueWordsInOneDocument.clear();
            List<String> sentences = null;
            try {
                //FIXME
                sentences = docParser.extractMainBodyFromJson(value, domainFilter);
            } catch (IOException e) {
                context.getCounter(MyCounter.badrecord).increment(1);
                return;
            }
            if (sentences.size() == 0) {
                return;
            }
            //1. get unique words in one document
            for (String sentence : sentences) {
                List<Term> parsed = ToAnalysis.parse(sentence);
                for (Term term : parsed) {
                    if (filter.accept(term)) {
                        uniqueWordsInOneDocument.add(term.getName().trim());
                    }
                }
            }
            //2. for each unique word emit a key-value
            for (String word : uniqueWordsInOneDocument) {
                reuse.set(word);
                context.write(reuse, one);
            }
        }

        enum MyCounter {
            badrecord,
            totalrecord
        }

    }

    public static class IDFCalculatorReducer
            extends Reducer<Text, LongWritable, Text, DoubleWritable> {

        private double documentCount;
        private DoubleWritable result = new DoubleWritable();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            documentCount = (double) IDFCalculator.getDocumentCount(context.getConfiguration());
        }

        public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable val : values) {
                sum += val.get();
            }

            result.set(computeIDF(sum));
            context.write(key, result);
        }

        private double computeIDF(long docFrequency) {
            return Math.log(documentCount / (1 + docFrequency));
        }
    }
}

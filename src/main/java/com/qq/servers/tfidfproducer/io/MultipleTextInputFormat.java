package com.qq.servers.tfidfproducer.io;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-17
 * Time: 下午3:13
 */
public class MultipleTextInputFormat extends FileInputFormat<LongWritable, Text> {
    private static final PathFilter hiddenFileFilter = new PathFilter() {
        public boolean accept(Path p) {
            String name = p.getName();
            return !name.startsWith("_") && !name.startsWith(".");
        }
    };

    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        return new MultiRecordReader();
    }

    //most of the code copy from FileInputFormat,since these classes has visibility of private
    //re-implement following method to recursively list directory.
    @Override
    protected List<FileStatus> listStatus(JobContext job) throws IOException {

        List<FileStatus> result = new ArrayList<FileStatus>();
        Path[] dirs = getInputPaths(job);
        if (dirs.length == 0) {
            throw new IOException("No input paths specified in job");
        }

//        // get tokens for all the required FileSystems..
//        TokenCache.obtainTokensForNamenodes(job.getCredentials(), dirs,
//                job.getConfiguration());

        List<IOException> errors = new ArrayList<IOException>();

        // creates a MultiPathFilter with the hiddenFileFilter and the
        // user provided one (if any).
        List<PathFilter> filters = new ArrayList<PathFilter>();
        filters.add(hiddenFileFilter);
        PathFilter jobFilter = getInputPathFilter(job);
        if (jobFilter != null) {
            filters.add(jobFilter);
        }
        PathFilter inputFilter = new MultiPathFilter(filters);
        FileSystem fs = FileSystem.get(job.getConfiguration());
        for (Path dir : dirs) {
            listDir(dir, result, fs, inputFilter, errors);
        }
        return result;
    }

    private void listDir(Path dir, List<FileStatus> result, FileSystem fs, PathFilter inputFilter,
                         List<IOException> errors) throws IOException {

        FileStatus[] matches = fs.globStatus(dir, inputFilter);
        if (matches == null) {
            errors.add(new IOException("Input path does not exist: " + dir));
        } else if (matches.length == 0) {
            errors.add(new IOException("Input Pattern " + dir + " matches 0 files"));
        } else {
            for (FileStatus globStat : matches) {
                if (globStat.isDir()) {
                    for (FileStatus stat : fs.listStatus(globStat.getPath(),
                            inputFilter)) {
                        listDir(stat.getPath(), result, fs, inputFilter, errors);
                    }
                } else {
                    result.add(globStat);
                }
            }
        }
    }

    //TODO
    //take minimum split size into consideration.
    @Override
    public List<InputSplit> getSplits(JobContext job) throws IOException {
        List<InputSplit> fileSplits = super.getSplits(job);
        List<InputSplit> multiSplits = new ArrayList<InputSplit>();

        long minSplitSize = FileInputFormat.getMinSplitSize(job);
        long maxSplitSize = FileInputFormat.getMaxSplitSize(job);

        long perSplitSize = 0;
        List<FileSplit> perSplit = new ArrayList<FileSplit>();

        for (InputSplit split : fileSplits) {
            try {
                perSplitSize += split.getLength();
            } catch (InterruptedException e) {
                throw new IOException(e);
            }

            perSplit.add((FileSplit) split);
            if (perSplitSize > maxSplitSize) {

                multiSplits.add(new MultipleFileSplit(perSplit));
                //reset
                perSplit = new ArrayList<FileSplit>();
                perSplitSize = 0;
            }
        }

        if (perSplit.size() > 0) {
            multiSplits.add(new MultipleFileSplit(perSplit));
        }

        return multiSplits;
    }

    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

    /**
     * Proxy PathFilter that accepts a path only if all filters given in the
     * constructor do. Used by the listPaths() to apply the built-in
     * hiddenFileFilter together with a user provided one (if any).
     */
    private static class MultiPathFilter implements PathFilter {
        private List<PathFilter> filters;

        public MultiPathFilter(List<PathFilter> filters) {
            this.filters = filters;
        }

        public boolean accept(Path path) {
            for (PathFilter filter : filters) {
                if (!filter.accept(path)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class MultiRecordReader extends RecordReader<LongWritable, Text> {
        //field:
        private List<FileSplit> fileSplits;
        private TaskAttemptContext context;

        //context
        private int current;
        private int length;
        private LineRecordReader reader = null;

        @Override
        public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {

            MultipleFileSplit multipleSplits = (MultipleFileSplit) split;
            fileSplits = multipleSplits.getFileSplits();
            this.context = context;
            current = 0;
            length = fileSplits.size();
            //TODO if current == length ==0
            createReader(current);
        }

        /**
         * make sure passed in <code>current</code> is in range of array size
         *
         * @param current
         * @throws java.io.IOException
         */
        private void createReader(int current) throws IOException {
            if (reader != null) {
                reader.close();
            }

            reader = new LineRecordReader();
            reader.initialize(fileSplits.get(current), context);
        }


        @Override
        public boolean nextKeyValue() throws IOException, InterruptedException {

            boolean hasMore = reader.nextKeyValue();

            if (hasMore) {
                return true;
            } else {
                //pick next
                current++;
                if (current < length) {
                    createReader(current);
                    return nextKeyValue();
                } else {
                    return false;
                }
            }
        }

        @Override
        public LongWritable getCurrentKey() throws IOException, InterruptedException {
            return reader.getCurrentKey();
        }

        @Override
        public Text getCurrentValue() throws IOException, InterruptedException {
            return reader.getCurrentValue();
        }

        @Override
        public float getProgress() throws IOException, InterruptedException {
            return (float) current / length;
        }

        @Override
        public void close() throws IOException {
            if (reader != null) {
                reader.close();
            }
        }
    }

}

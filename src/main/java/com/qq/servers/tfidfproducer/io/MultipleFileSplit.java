package com.qq.servers.tfidfproducer.io;

import com.google.common.collect.Lists;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-17
 * Time: 下午3:06
 * <p/>
 * combine multiple files as a single source of data for map.
 * <p/>
 * take data locality into consideration.
 */
public class MultipleFileSplit extends InputSplit implements Writable {
    private List<FileSplit> fileSplits;

    public MultipleFileSplit() {
        fileSplits = Lists.newArrayList();
    }

    /**
     * Constructs a split with host information
     */
    public MultipleFileSplit(List<FileSplit> fileSplits) {
        this.fileSplits = fileSplits;
    }


    public List<FileSplit> getFileSplits() {
        return this.fileSplits;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(fileSplits.size());
        for (FileSplit split : fileSplits) {
            split.write(out);
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        fileSplits.clear();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            FileSplit split = new FileSplit(new Path("/any"), 0, 0, new String[]{});
            split.readFields(in);
            fileSplits.add(split);
        }
    }

    @Override
    public long getLength() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public String[] getLocations() throws IOException, InterruptedException {
        //TODO
        /* seems {@ link FileSplit} has already lost file location inf */
        return new String[]{};
    }
}

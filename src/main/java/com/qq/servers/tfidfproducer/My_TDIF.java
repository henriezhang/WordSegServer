package com.qq.servers.tfidfproducer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-15
 * Time: 下午5:31
 */
public class My_TDIF
{


    public static void main(String[] args)
    {
//        System.out.println(System.getProperty("java.class.path"));
//        List<Term> words = ToAnalysis.parse("亚太经合组织");
//        System.out.println(Arrays.toString(words.toArray()));

//        Pattern pattern = Pattern.compile("(?s)[a-zA-Z_0-9]*");

        Pattern pattern = Pattern.compile("(?s)[\\d|%|'|a-zA-Z].*");
//        String input = "sfasdfs66中国\r\n";
//        String input = "%%2";
//        String input = "a'b";
//        String input = "66";
        String input = "''bitchin'";
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches())
        {

            System.out.println("Matcher!");
        }
        else
        {
            System.out.println("unmatchered");

        }


    }


//    //part1------------------------------------------------------------------------
//
//    public static class Mapper_Part1 extends Mapper<LongWritable, Text, Text, Text>
//    {
//        String File_name = ""; //保存文件名，根据文件名区分所属文件
//        int all = 0;  //单词总数统计
//        static Text one = new Text("1");
//        String word;
//
//        public void map(LongWritable key, Text value, Context context)
//                throws IOException, InterruptedException
//        {
//            FileSplit split = (FileSplit) context.getInputSplit();
//            String str = split.getPath().toString();
//            File_name = str.substring(str.lastIndexOf("/") + 1); //获取文件名
//            StringTokenizer itr = new StringTokenizer(value.toString());
//            while (itr.hasMoreTokens())
//            {
//                word = File_name;
//                word += " ";
//                word += itr.nextToken();  //将文件名加单词作为key es: test1 hello  1
//                all++;
//                context.write(new Text(word), one);
//            }
//        }
//
//        public void cleanup(Context context) throws IOException,
//                InterruptedException
//        {
//            //Map的最后，我们将单词的总数写入。下面需要用总单词数来计算。
//            String str = "";
//            str += all;
//            context.write(new Text(File_name + " " + "!"), new Text(str));
//            //主要这里值使用的 "!"是特别构造的。 因为!的ascii比所有的字母都小。
//        }
//    }
//
//    public static class Combiner_Part1 extends Reducer<Text, Text, Text, Text>
//    {
//        float all = 0;
//
//        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
//        {
//            int index = key.toString().indexOf(" ");
//            //因为!的ascii最小，所以在map阶段的排序后，!会出现在第一个
//            if (key.toString().substring(index + 1, index + 2).equals("!"))
//            {
//                for (Text val : values)
//                {
//                    //获取总的单词数。
//                    all = Integer.parseInt(val.toString());
//                }
//                //这个key-value被抛弃
//                return;
//            }
//            float sum = 0;  //统计某个单词出现的次数
//            for (Text val : values)
//            {
//                sum += Integer.parseInt(val.toString());
//            }
//            //跳出循环后，某个单词数出现的次数就统计完了，所有 TF(词频) = sum / all
//            float tmp = sum / all;
//            String value = "";
//            value += tmp;  //记录词频
//
//            //将key中单词和文件名进行互换。es: test1 hello -> hello test1
//            String p[] = key.toString().split(" ");
//            String key_to = "";
//
//            key_to += p[1];
//            key_to += " ";
//            key_to += p[0];
//
//            context.write(new Text(key_to), new Text(value));
//        }
//    }
//
//    public static class Reduce_Part1 extends Reducer<Text, Text, Text, Text>
//    {
//
//        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
//        {
//            for (Text val : values)
//            {
//
//                context.write(key, val);
//            }
//        }
//    }
//
//    public static class MyPartitoner extends Partitioner<Text, Text>
//    {
//        //实现自定义的Partitioner
//        @Override
//        public int getPartition(Text key, Text value, int numPartitions)
//        {
//            //我们将一个文件中计算的结果作为一个文件保存
//            //es： test1 test2
//            String ip1 = key.toString();
//            ip1 = ip1.substring(0, ip1.indexOf(" "));
//            Text p1 = new Text(ip1);
//            return Math.abs((p1.hashCode() * 127) % numPartitions);
//        }
//    }
//
//    //part2-----------------------------------------------------
//    public static class Mapper_Part2 extends
//            Mapper<LongWritable, Text, Text, Text>
//    {
//        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
//        {
//            String val = value.toString().replaceAll("  ", " "); //将vlaue中的TAB分割符换成空格 es: Bank test1    0.11764706 -> Bank test1 0.11764706
//            int index = val.indexOf(" ");
//            String s1 = val.substring(0, index); //获取单词 作为key es: hello
//            String s2 = val.substring(index + 1); //其余部分 作为value es: test1 0.11764706
//            s2 += " ";
//            s2 += "1";  //统计单词在所有文章中出现的次数, “1” 表示出现一次。 es: test1 0.11764706 1
//            context.write(new Text(s1), new Text(s2));
//        }
//    }
//
//    public static class Reduce_Part2 extends
//            Reducer<Text, Text, Text, Text>
//    {
//        int file_count;
//
//        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
//        {
//            //同一个单词会被分成同一个group
//            file_count = context.getNumReduceTasks();  //获取总文件数
//            float sum = 0;
//            List<String> vals = new ArrayList<String>();
//            for (Text str : values)
//            {
//                int index = str.toString().lastIndexOf(" ");
//                sum += Integer.parseInt(str.toString().substring(index + 1)); //统计此单词在所有文件中出现的次数
//                vals.add(str.toString().substring(0, index)); //保存
//            }
//            float tmp = sum / file_count;  //单词在所有文件中出现的次数除以总文件数 = DF
//            for (int j = 0; j < vals.size(); j++)
//            {
//                String val = vals.get(j);
//                String end = val.substring(val.lastIndexOf(" "));
//                float f_end = Float.parseFloat(end); //读取TF
//                val += " ";
//                val += tmp;
//                val += " ";
//                val += f_end / tmp;  // f_end / tmp = tf-idf值
//                context.write(key, new Text(val));
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception
//    {
//
//        Path tmp = new Path("tmp"); //设置中间文件临时存储目录
//        //part1----------------------------------------------------
//        Configuration conf1 = new Configuration();
//        //设置文件个数，在计算DF(文件频率)时会使用
//        FileSystem hdfs = FileSystem.get(conf1);
//        FileStatus p[] = hdfs.listStatus(new Path(args[1]));
//
//        //获取输入文件夹内文件的个数，然后来设置NumReduceTasks
//        Job job1 = new Job(conf1, "My_tdif_part1");
//
//        job1.setJarByClass(My_TDIF.class);
//
//        job1.setMapperClass(Mapper_Part1.class);
//        job1.setCombinerClass(Combiner_Part1.class); //combiner在本地执行，效率要高点。
//        job1.setReducerClass(Reduce_Part1.class);
//
//        job1.setMapOutputKeyClass(Text.class);
//        job1.setMapOutputValueClass(Text.class);
//        job1.setOutputKeyClass(Text.class);
//        job1.setOutputValueClass(Text.class);
//
//        job1.setNumReduceTasks(p.length);
//
//        job1.setPartitionerClass(MyPartitoner.class); //使用自定义MyPartitoner
//
//        FileInputFormat.addInputPath(job1, new Path(args[1]));
//
//        FileOutputFormat.setOutputPath(job1, tmp);
//
//        job1.waitForCompletion(true);
//        //part2----------------------------------------
//        Configuration conf2 = new Configuration();
//
//        Job job2 = new Job(conf2, "My_tdif_part2");
//
//        job2.setJarByClass(My_TDIF.class);
//
//        job2.setMapOutputKeyClass(Text.class);
//        job2.setMapOutputValueClass(Text.class);
//
//        job2.setOutputKeyClass(Text.class);
//        job2.setOutputValueClass(Text.class);
//
//        job2.setMapperClass(Mapper_Part2.class);
//        job2.setReducerClass(Reduce_Part2.class);
//        //需要提醒下，我这里没有使用自定义Partitioner,默认的Partitioner会根据key来划分，而我们正
//        //好需要这种方式来将所有文件中同一个单词化为同一个组，方便我们统计一个单词在所以文件中出现的次数。
//        job2.setNumReduceTasks(p.length);
//
//        FileInputFormat.setInputPaths(job2, tmp);
//        FileOutputFormat.setOutputPath(job2, new Path(args[2]));
//
//        job2.waitForCompletion(true);
//
//        hdfs.delete(tmp, true);
//    }

}

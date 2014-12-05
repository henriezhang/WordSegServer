package com.qq.servers.tfidfproducer;

import com.qq.servers.CommandExecutor;
import com.qq.servers.KeywordExtractCommand;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.io.*;

/**
 * Created by antyrao on 14-6-5.
 */
public class WordSegTrimFirstField
{


    public static void process(String dirPath, String outputPath) throws IOException
    {

//        DocumentParser parser = new DocumentParser();
//        DocumentParser.Filter filter = new DocumentParser.Filter()
//        {
//            @Override
//            public boolean accept(String url)
//            {
//                return true;
//            }
//        };

        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
        File dir = new File(dirPath);
        long badRecord = 0;
        ObjectMapper mapper = new ObjectMapper();

        for (File file : dir.listFiles())
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
//                text.set(line);
                try
                {

//                    List<String> result = parser.extractMainBodyFromJson(text, filter);

//                    for (String item : result)
//                    {
//                        String wordSeged = ArticleProcessorContent.wordSegAndTrim(item);
//                        writer.print(wordSeged);
//                        writer.print(" ");
//                    }

                    String[] data = line.split("\t");
                    if (data.length != 2)
                    {
                        throw new RuntimeException(line);
                    }

                    String id = data[0];
                    String content = data[1];
                    KeywordExtractCommand command = new KeywordExtractCommand("", content, "default", 20, false);
                    CommandExecutor executor = new CommandExecutor();
                    String wordSeged = executor.execute(command);
                    ArrayNode node = (ArrayNode) mapper.readTree(wordSeged).get("keyword");
//                    String wordSeged = ArticleProcessorContent.wordSegAndTrim(content);
                    writer.print(id);
                    writer.print("\t");
                    writer.print(nodeToString(node));
                    writer.print("\n");
                }
                catch (Exception e)
                {
                    badRecord++;
                }
            }
            br.close();//try catch
        }
        writer.flush();
        writer.close();
        System.out.println("There are " + badRecord + " records");

    }

    private static String nodeToString(ArrayNode node)
    {
        StringBuilder sb = new StringBuilder();
        for (JsonNode item : node)
        {
            sb.append(item.toString().replaceAll("\"", ""));
            sb.append(" ");
        }
        if (sb.length() > 0)
        {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException
    {
        String fileFolder = args[0];
        String outputFileName = args[1];
        process(fileFolder, outputFileName);

//        String strLine = "astro.fashion.qq.com/a/20140327/021251.htm\t{\"response\":{\"code\":0,\"msg\":\"Sucess\",\"dext\":\"\"},\"data\":{\"url\":\"http:\\/\\/astro.fashion.qq.com\\/a\\/20140327\\/021251.htm\",\"source\":{\"source_id\":\"10\",\"source_name\":\"腾讯星座\",\"source_ename\":\"0xinzuo\",\"source_desc\":\"&nbsp;\",\"source_catalog\":\"\",\"source_sequence\":\"0\",\"source_url\":\"\",\"source_status\":\"1\",\"source_img\":\"\",\"source_signname\":\"\",\"source_signflag\":\"1\",\"FSignName\":\"\",\"source_desc_replace\":\"腾讯星座\"},\"Fshortening\":null,\"relatelink\":[],\"cltitle\":\"12星座男选妻潜规则\",\"cstitle\":\"http:\\/\\/img1.gtimg.com\\/astro\\/pics\\/hv1\\/250\\/60\\/1554\\/101064400.jpg\",\"pubtime\":\"2014-03-27 17:19:21\",\"type\":\"0\",\"keyword\":\"\",\"level\":\"1\",\"desc\":\"男人并不像我们女生想得那么简单，有时你不得不感叹男人理智得可怕。在他们看来恋爱和结婚是不一样的，因此对于选择伴侣的标准也不一样，对于恋人，他们更希望是...\",\"imgurl\":\"http:\\/\\/img1.gtimg.com\\/astro\\/pics\\/hv1\\/171\\/60\\/1554\\/101064321_small.jpg\",\"imgurl2\":\"http:\\/\\/img1.gtimg.com\\/astro\\/pics\\/hv1\\/171\\/60\\/1554\\/101064321_small2.jpg\",\"fimg\":\"http:\\/\\/img1.gtimg.com\\/astro\\/pics\\/hv1\\/171\\/60\\/1554\\/101064321.jpg\",\"image\":[{\"img\":\"http:\\/\\/img1.gtimg.com\\/astro\\/pics\\/hv1\\/171\\/60\\/1554\\/101064321.jpg\",\"desc\":\"\"}],\"fvideo\":false,\"video\":false,\"content\":[{\"type\":\"img\",\"img\":\"http:\\/\\/img1.gtimg.com\\/astro\\/pics\\/hv1\\/171\\/60\\/1554\\/101064321.jpg\"},{\"type\":\"text\",\"info\":\"男人并不像我们女生想得那么简单，有时你不得不感叹男人理智得可怕。在他们看来恋爱和结婚是不一样的，因此对于选择伴侣的标准也不一样，对于恋人，他们更希望是漂亮的，身材好；而对于娶回家的女人，则会以过日子为重，选择的要求也从外表转为内在，会有着他们自己的潜规则。想知道十二星座男选妻都有哪些潜规则吗？一起来看看吧！\"},{\"type\":\"text\",\"info\":\"火象星座（白羊、狮子、射手）：真实自然才最重要\"},{\"type\":\"text\",\"info\":\"在恋爱初期，为了赢得对方的好感，很多人习惯掩饰真实的自己，各种装各种“作”。恋爱时女人要装就装吧，但是在选择结婚对象时，火象星座男却会表现得特别谨慎，他反而会希望了解未来妻子更为真实的一面，知道脾性是否对味，才更有利于将来的相处。\"},{\"type\":\"text\",\"info\":\"土象星座（金牛、处女、摩羯）：最好有比较好的经济基础\"},{\"type\":\"text\",\"info\":\"土象男人都比较务实，结婚不同于恋爱，是两个家庭之间的事。如果女方的家庭条件不错，或是有赚钱能力，至少可以减少赡养女方父母的压力。男人赚钱能力强倒好说，要是还达不到赡养双方父母的程度，那么父母的经济基础就会直接影响小两口的生活质量。有了经济基础作为保证，未来的婚姻才会更为幸福，更为快乐。\"},{\"type\":\"text\",\"info\":\"风象星座（双子、天秤、水瓶）：相貌普普通通就好\"},{\"type\":\"text\",\"info\":\"为了审美，风象男人恋爱时多半会选择跟美女做朋友，满足一下虚荣心。当到了要结婚时，却反而不会选择这样的女人，毕竟美丽的女人娶回家太耀眼，自己还要时刻提防。并且美女对物质生活要求也高，甚至不懂得顾家和理财。结婚是要每天一起面对柴米油盐的生活，所以娶一个相貌平平的老婆会很安稳。\"},{\"type\":\"text\",\"info\":\"水象星座（巨蟹、天蝎、双鱼）：过去纯洁无邪为最好\"},{\"type\":\"text\",\"info\":\"女人往往喜欢调查男人的过去，以便进行进一步的交往。其实水象星座的男人同样也喜欢调查女人的过去，尤其是自己心仪的想要娶回家的女人，对她们的过去更是要调查得滴水不漏。对于那种性方面太开放或者是有丰富经验的女孩，又怎么能娶回家？\"}]}}";
//        Text line = new Text(strLine);
//
//        DocumentParser parser = new DocumentParser();
//        List<String> result = parser.extractMainBodyFromJson(line, new DocumentParser.Filter()
//        {
//            @Override
//            public boolean accept(String url)
//            {
//                return true;
//            }
//        });
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//
//        PrintWriter pw = new PrintWriter(baos);
//
//        for (String item : result)
//        {
//            String wordSeged = ArticleProcessorContent.wordSegAndTrim(item);
//            pw.print(wordSeged);
//            pw.print(" ");
//        }
//        pw.flush();
//
//        System.out.println(baos.toString());

    }
}

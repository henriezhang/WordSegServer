package com.qq.servers.tfidfproducer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ansj.domain.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-18
 * Time: 上午11:32
 */
public class TFIDFCalculator {

    private static final Logger LOG = LoggerFactory.getLogger(TFIDFCalculator.class);

//    private static final String IDF = "idf.txt";
//    private static final String AUTO_IDF = "auto.idf";
//    private static final String FINANCE_IDF = "finance.idf";
//
//    private static final String SEPERATEOR_REGEX = "\\t";
//
//
//    //TODO hash table is not the most efficient data structure to store words and their frequency.Try trie tree
//    public static Hashtable<String, Double> wordsIdf = new Hashtable<String, Double>();
//    private static Double median;
//
//    private static void loadIdf(Hashtable<String, Double> idfDict, String idfName)
//    {
//        Charset utf8 = Charset.forName("utf-8");
//        InputStream is = TFIDFCalculator.class.getClassLoader().getResourceAsStream(idfName);
//
//        if (is == null)
//        {
//            throw new RuntimeException("Can't find idf.txt file");
//        }
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is, utf8));
//
//        double total = 0;
//        String line;
//        try
//        {
//            int badRecord = 0;
//            int totalRecord = 0;
//            while ((line = reader.readLine()) != null)
//            {
//                String[] wordAndIdf = line.split(SEPERATEOR_REGEX);
//                if (wordAndIdf.length != 2)
//                {
//                    continue;
//                }
//                Double idf = Double.valueOf(wordAndIdf[1]);
//                total += idf;
//                wordsIdf.put(wordAndIdf[0], idf);
//            }
//            Collection<Double> values = wordsIdf.values();
//            Double[] weights = values.toArray(new Double[values.size()]);
//
//            Arrays.sort(weights);
//
//            if (weights.length % 2 == 0)
//            {
//                median = (weights[weights.length / 2] + weights[weights.length / 2 - 1]) / 2;
//            }
//            else
//            {
//                median = weights[weights.length / 2];
//            }
//
//        }
//        catch (IOException e)
//        {
//            new RuntimeException(Throwables.getStackTraceAsString(e), e);
//        }
//        finally
//        {
//            try
//            {
//                reader.close();
//            }
//            catch (IOException ignored)
//            {
//            }
//        }
//    }
//
//
//    static
//    {
//
////        System.out.println(System.getProperty("java.class.path"));
//
//        Charset utf8 = Charset.forName("utf-8");
//        InputStream is = TFIDFCalculator.class.getClassLoader().getResourceAsStream(IDF);
//
//        if (is == null)
//        {
//            throw new RuntimeException("Can't find idf.txt file");
//        }
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is, utf8));
//
//        double total = 0;
//        String line;
//        try
//        {
//            int badRecord = 0;
//            int totalRecord = 0;
//            while ((line = reader.readLine()) != null)
//            {
//                String[] wordAndIdf = line.split(SEPERATEOR_REGEX);
//                if (wordAndIdf.length != 2)
//                {
//                    continue;
//                }
//                Double idf = Double.valueOf(wordAndIdf[1]);
//                total += idf;
//                wordsIdf.put(wordAndIdf[0], idf);
//            }
//            Collection<Double> values = wordsIdf.values();
//            Double[] weights = values.toArray(new Double[values.size()]);
//
//            Arrays.sort(weights);
//
//            if (weights.length % 2 == 0)
//            {
//                median = (weights[weights.length / 2] + weights[weights.length / 2 - 1]) / 2;
//            }
//            else
//            {
//                median = weights[weights.length / 2];
//            }
//
//        }
//        catch (IOException e)
//        {
//            new RuntimeException(Throwables.getStackTraceAsString(e), e);
//        }
//        finally
//        {
//            try
//            {
//                reader.close();
//            }
//            catch (IOException ignored)
//            {
//            }
//        }
//
//    }


    private Map<String, IdfDict> idfDicts = Maps.newHashMap();

    public TFIDFCalculator() {
        idfDicts.put("default", new IdfDict("idf.txt"));
        idfDicts.put("auto", new IdfDict("auto.idf"));
        idfDicts.put("finance", new IdfDict("finance.idf"));
    }

    public static void main(String[] args) {

        TFIDFCalculator tfidf = new TFIDFCalculator();
//        String title = "李克强会见澳大利亚总督 称中国高铁先进安全";
//        String title = "分析称经济数据在合理区间内 稳定为主旋律";
//        String title = "";
//        String content = "国家统计局18日发布数据，三季度中国经济同比增长7.8%，增速比上季度反弹0.3个百分点。前三季度中国经济增长7.7%。\n" +
//                "\n" +
//                "南方基金首席策略分析师杨德龙向腾讯财经表示，三季度GDP增长7.8%并没有超出预期，此前市场预期7.9%。\n" +
//                "\n" +
//                "他指出，三季度GDP有所回升主要在于库存需求的提振，以及下半年以来稳增长的相关政策。\n" +
//                "\n" +
//                "不过，杨德龙认为，这一轮的经济回暖并不强劲，四季度GDP仍面临回落。\n" +
//                "\n" +
//                "他分析，目前外需仍然不振，而且内需在三季度补完库存后，四季度仍面临去库存的压力；此外，重要此外，中央对经济的调控容忍度放宽，主要关注转型升级，稳增长的政策会少些。预计全年GDP为7.6%到7.7%水平。\n" +
//                "\n" +
//                "申银万国首席市场分析师桂浩明亦表示，如果不出现意外，全年GDP增长有望达到7.6%至7.7%的水平。\n" +
//                "\n" +
//                "桂浩明表示，三季度GDP与第二季度相比实现0.3个百分点的反弹，表明经济逐渐企稳，但强度有限。四季度面可能面临通胀回升的压力；且财政政策和货币政策不会有太大的变化，对于政策不能有太高的预期。\n" +
//                "\n" +
//                "10月17日，中国人民银行意外没有在公开市场上进行任何操作，此为7月30日逆回购操作重启以来的首次暂停。桂浩明表示，这是收紧银根的措施，资金面面临着压力。\n" +
//                "\n" +
//                "国际金融论坛城镇化研究中心主任易鹏分析认为，三季度GDP增速7.8%，表明今年的7.5%目标是完全没有任何压力可以完成了。既然在合理区间之内，那么相应的调控政策也不会大动，稳定是主旋律。\n" +
//                "\n" +
//                "“相信中国经济实现全年主要目标是完全有基础的，中国经济向好的趋势是会继续的。”日前国务院总理李克强在越南访问时表示，我们还会继续深化经济体制改革和经济结构调整，使中国经济持续健康向前发展，使全体人民感受得到实实在在的成果。\n" +
//                "\n" +
//                "自从8月底以来，多家国际投行纷纷上调了对今年中国经济增长的预期。他们认为，中国经济已经触底，并可能维持回升势头。中国经济还将持续复苏回升，不过由于基数原因，今年四季度经济增速环比可能小幅回落。尽管四季度增速可能回落，但多数专家判断全年经济增速一定会高于7.5%的政府调控目标。\n" +
//                "\n" +
//                "野村证券中国区首席经济学家张智威指出，受内需推动，中国内地经济回稳，下半年下行风险降低，不过四季度起经济会开始下滑，尤其是到了明年上半年经济下滑的速度会比较快，可能跌到7%以下。\n" +
//                "\n" +
//                "张智威称，新一届政府致力于通过结构改革推动经济增长，同时注重防范金融风险，有利中国经济的长远发展；近期反腐力度加强，充分显示了中国政府的决心，令外界对11月召开的十八届三中全会的内容也有更高期许。（腾讯财经 李慧敏 张贾龙 卢肖红 发自北京 深圳";

//        String title = "";
////        String title = "李克强会见澳大利亚总督 称中国高铁先进安全";
//        String content = "李克强会见澳大利亚总督布赖斯时强调\n" +
//                "\n" +
//                "深化务实合作推动中澳关系取得更大发展\n" +
//                "\n" +
//                "新华网北京10月17日电（记者张艺）国务院总理李克强17日下午在人民大会堂会见澳大利亚总督布赖斯。\n" +
//                "\n" +
//                "李克强说，习近平主席同你举行了富有成果的会谈。今年4月，中澳建立战略伙伴关系和总理年度会晤机制，成为两国关系新的里程碑。希望双方尊重彼此核心利益和重大关切，加强在贸易投资、能源资源、教育人文等领域交流与合作，推动中澳关系在新的起点上取得更大发展。\n" +
//                "\n" +
//                "李克强表示，澳大利亚正在积极升级基础设施建设，已就建造澳第一条高铁进行可行性研究。中国高铁技术先进，安全可靠，具有成本优势。希望双方就此探讨开展合作。\n" +
//                "\n" +
//                "李克强指出，中国发展最大的回旋余地在中西部。欢迎更多澳企业积极参与中国西部大开发，在互利共赢基础上开辟中澳合作新天地。\n" +
//                "\n" +
//                "布赖斯表示，澳大利亚和中国是好朋友，两国友谊日益加深，合作不断扩大。澳方重视对华关系，愿与中方扩大和深化各领域务实合作，加快双边自贸区谈判进程，加强人员往来，密切在多边机制中的沟通协调，使两国关系取得更大发展。";

//        String title = "宁夏灭门案嫌犯落网 与妻争吵追至岳父家行凶";
//        String content = "10月18日20时30分，宁夏彭阳“10·14”命案在逃犯罪嫌疑人麻永东，在吴忠市利通区南门凯悦建材城被公安机关抓获。经初步审讯，犯罪嫌疑人麻永东对其杀害兰油布一家7口的犯罪事实供认不讳。\n" +
//                "\n" +
//                "10月14日，宁夏彭阳县红河乡文沟村村民兰油布一家7口被杀身亡，经侦查，兰油布的女婿麻永东有重大作案嫌疑。\n" +
//                "\n" +
//                "一家7口家中遇害\n" +
//                "\n" +
//                "据彭阳县公安局刑侦大队介绍，警方于15日上午接到群众报案，被害人包括兰油布本人和妻子、父母、女儿、两个孙女。兰油布父母的年龄为75岁、70岁，两个孙女分别只有3岁半和1岁9个月，另外兰油布的女儿(麻永东的妻子)被害时已经怀孕约6个月。10月15日8时许，邻居给其家送东西时发现后报案。\n" +
//                "\n" +
//                "宁夏警方透露，嫌疑人系兰油布的女婿麻永东，案发后麻永东逃逸。\n" +
//                "\n" +
//                "夫妻争吵或是根源\n" +
//                "\n" +
//                "有报道称，7名死者全被割喉。对此，宁夏警方未予回应。另据记者了解，死亡的两名幼女是麻永东妻子兰小红的哥哥的孩子，目前其哥哥也已回家。对于网传的凶案是麻永东在西安雇凶杀人一说，彭阳县红河派出所民警予以否认，称系嫌疑人个人作案。\n" +
//                "\n" +
//                "记者昨日上午从负责这起案件的彭阳县红河派出所了解到，今年24岁的嫌疑人麻永东是兰油布的女婿，与兰油布的女儿结婚多年，兰的女儿已有6个月身孕。\n" +
//                "\n" +
//                "案发当日下午，嫌疑人麻永东与妻子发生争吵，妻子返回娘家。随后麻永东追至岳父兰油布家，并与岳父一家发生争吵，麻永东随后持刀将岳父一家7人杀害。\n" +
//                "\n" +
//                "警方曾悬赏10万缉凶\n" +
//                "\n" +
//                "此前，宁夏警方曾公布嫌犯体貌特征，征集破案线索，并宣称公安机关对举报人、缉捕有功的个人或单位奖励人民币10万元，提供其他有价值线索者，奖励人民币2万元。";

//        String title = "知情人曝谢亚龙南勇遭遇迥异：探望后者人数多";
//        String content = "近日，上海足球名将申思在狱中参加文艺表演的照片在网上很火，他穿着民俗服装、涂着腮红、打着腰鼓的形象，再度勾起了球迷对于“足坛反腐案”落马者狱中生活的关注。昨天，某网站趁势推出《前足协高官狱中生活：谢亚龙帮人按摩，南勇打牌》的报道，引发网友强烈关注。\n" +
//                "\n" +
//                "　　对此，华西都市报记者昨天采访了几位知情人士，他们都以好友的身份前往监狱探视过这些官员。起初他们都不愿触碰这个话题，当听闻记者转述流传于网络的种种传言之后，还是忍不住透露了一些亲历的情况。他们对这篇报道的看法基本一致：里面讲的大多不是新闻，而是旧闻—是前足协高官们在看守所的生活，都是江湖段子，其中不乏注水编撰成分。\n" +
//                "\n" +
//                "　　一位亲自去监狱看望过南勇、谢亚龙等前足协大佬的知情人士说，“这都是三年前的事了，东拼西凑的。除了上海的那四个球员，其他的犯案人大多在一块，在燕城监狱，就在北京附近。”这所监狱是司法部唯一直属的中央监狱，位于河北省三河市燕郊开发区，是河北省距北京最近的地方。“这个地方离北京很近，所以家属们基本上每周都要去，他们的探视时间一个月一次，但家里人可以每周送一次东西。”知情人士透露，前往监狱探视南勇的朋友同事等比看望谢亚龙的明显要多一些，“基本上很少有人看谢亚龙，看南勇的特别多，也可以看得出他们以前的人缘。”\n" +
//                "\n" +
//                "　　另一位曾去监狱探望过的知情人士并不愿多谈，“人最大的东西，人身自由都没了，说好还能好到哪儿去？只能说，他们现在生活规律，虽然要干一些体力活，但从生理状况和精神状况来说，恢复得都还不错。”（华西都市报）返回腾讯网首页>>";
//        List<KeyWord> keywords = tfidf.computeTfidf(title, content, 5);


//        double idf = wordsIdf.get("分析");
//        System.out.println("idf = " + idf);

//        System.out.println(Arrays.toString(keywords.toArray()));


    }

//    public List<KeyWord> computeTfidf(List<Fragment> fragments, int kwCount)
//    {
//        Map<String, KeyWord> kws = Maps.newHashMap();
//
//        for (Fragment fragment : fragments)
//        {
//            computeTfidfInternal(fragment, kws, idfDict);
//        }
//
//
//        return getTopNByTfidf(kws, kwCount);
//    }

    public List<KeyWord> computeTfidf(List<Fragment> fragments, int kwCount, String idfName) {
        IdfDict idfDict = idfDicts.get(idfName);
        if (idfDict == null)
            return Lists.newLinkedList();
        Map<String, KeyWord> kws = Maps.newHashMap();

        for (Fragment fragment : fragments) {
            computeTfidfInternal(fragment, kws, idfDict);
        }
        return getTopNByTfidf(kws, kwCount);
    }

    private List<KeyWord> getTopNByTfidf(Map<String, KeyWord> kws, int kwCount) {
        TreeSet<KeyWord> kwSet = new TreeSet<KeyWord>(kws.values());

        int total = kwSet.size() < kwCount ? kwSet.size() : kwCount;
        List<KeyWord> result = Lists.newArrayList();
        int count = 0;
        for (KeyWord kw : kwSet) {
            result.add(kw);
            count++;
            if (count >= total)
                break;
        }
        return result;
    }

//    private void computeTfidfInternal(Fragment fragment, Map<String, KeyWord> kws)
//    {
//        for (Term word : fragment.words)
//        {
//            int weight = (int) fragment.weighter.computeWeight(word, fragment.length);
//
//            if (weight == 0)
//            {
//                continue;
//            }
//
//            KeyWord kw = kws.get(word.getName().trim());
//
//
//            if (kw == null)
//            {
//                Double idf = wordsIdf.get(word.getName());
//                if (idf == null)
//                {
//                    idf = median;
//                }
//
//                kw = new KeyWord(word.getName(), idf, weight);
//                kws.put(word.getName().trim(), kw);
//            }
//            else
//            {
//                kw.incrementOccurs(1);
//            }
//        }
//    }


//    /**
//     * give each component of a news document different weight in order to better extract key word of document.
//     */
//    interface Weight
//    {
//        public double computeWeight(Term word, int length);
//    }
//
//    /**
//     * this class used to computeWeight weight of title<p>
//     * in theory, a title of document should have highest weight
//     */
//    public static class TitleWeight implements Weight
//    {
//        private final double baseWeight;
//        private final WordFilter filter;
//
//        public TitleWeight(double baseWeight, WordFilter filter)
//        {
//            this.filter = filter;
//            this.baseWeight = baseWeight;
//        }
//
//        @Override
//        public double computeWeight(Term word, int length)
//        {
//            if (!filter.accept(word))
//            {
//                return 0;
//            }
//            return baseWeight;
//        }
//    }
//
//    /**
//     * this class used to computeWeight weight of words in content of document<p>
//     * <p>
//     * computeWeight weight of words in content of document based on their position in document.
//     * Give a higher weight to words near beginning than words in end of document.
//     * </p>
//     */
//    public static class ContentWeight implements Weight
//    {
//        private final double baseWeight;
//        private final WordFilter filter;
//
//        public ContentWeight(double baseWeight,WordFilter filter)
//        {
//            this.baseWeight = baseWeight;
//            this.filter = filter;
//        }
//
//        @Override
//        public double computeWeight(Term word, int length)
//        {
//            if (!filter.accept(word))
//            {
//                return 0;
//            }
//            double position = ((double) (word.getOffe())) / length;
//
//            if (position < 0.05)
//            {
//                return baseWeight;
//            }
//            return (baseWeight - baseWeight * position);
//        }
//    }

    private void computeTfidfInternal(Fragment fragment, Map<String, KeyWord> kws, IdfDict idfDict) {
        for (Term word : fragment.words) {
            int weight = (int) fragment.weighter.computeWeight(word, fragment.length);

            if (weight == 0) {
                continue;
            }

            KeyWord kw = kws.get(word.getName().trim());


            if (kw == null) {
                Double idf = idfDict.getIdf(word.getName());
                if (idf == null) {
                    idf = idfDict.getMedian();
                }

                kw = new KeyWord(word.getName(), idf, weight);
                kws.put(word.getName().trim(), kw);
            } else {
                kw.incrementOccurs(1);
            }
        }

    }

}

package com.qq.servers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-5
 * Time: 下午12:37
 */
public class KeywordExtractCommandTest extends TestCase
{
    @Before
    public void setUp() throws Exception
    {

    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testExecute() throws Exception
    {
        String content = "南都讯 记者王丹丹 深圳光明新区投资5518万元建造的明湖城市公园，在中心湖区，广阔的湖面上未见小鱼击起的层层涟漪，却见漂浮着大量死鱼，大则一两斤，尤其在一排污口，密密麻麻的死鱼翻起了白肚，恶臭熏天，几百米外都能闻到。据附近施工人员反映，半个月前湖面就出现了漂浮死鱼现象，是湖水受到了污染还是人为投毒呢？\n" +
                "\n" +
                "明湖城市公园位于南光高速塘明出口不远处，昨日中午，南都记者赶到该公园，还未走近就闻到湖区散发出浓烈的腥臭味，就打开车门一瞬间，车里就弥漫着这股恶臭味，令人作呕。南都记者沿着公园的木桥栈道往西走，只见湖水已经退去大半，湖底边沿干涸裸露的石块清晰可见，靠东侧的湖里漂浮着大量的死鱼，有福寿鱼、大头鱼、鲫鱼以及鲤鱼等。大则一两斤，小则几两。有一条污水渠直排湖内，污水漂着白沫发出阵阵臭味，这些污水沟将湖内的沙滩冲出一条小沟壑，源源不断的污水正排入沟内，再往西走10多米看到另一条水泥管做的排水管，大量的污水排入湖内，排污渠的铁栅栏上档住了厚厚的一层塑料袋等垃圾。\n" +
                "\n" +
                "附近居民吴先生称，明湖城市公园建成有4年，以前湖水比较清澈。他隐约记得，大概是从上个月20号开始，湖内水位开始降低，湖水变臭，起初还没有发现死鱼，只有少量的树叶和污垢，可是没几天，就发现湖面上开始出现死鱼，并且越来越多，部分鱼泡在湖里甚至已经开始腐烂，散发出难闻的臭味。\n" +
                "\n" +
                "采访时，公园管理方派来了几名工人开始对漂浮在水面的死鱼进行打捞，工人们称打捞起来的死鱼将会拉到附近进行深埋处理。";
        int count = 10;
//        PlaceExtractCommand command = new PlaceExtractCommand(content, count);

//        System.out.println(command.execute());

    }

    @Test
    public void testCreateCommand() throws Exception
    {

        String content = "南都讯 记者王丹丹 深圳光明新区投资5518万元建造的明湖城市公园，在中心湖区，广阔的湖面上未见小鱼击起的层层涟漪，却见漂浮着大量死鱼，大则一两斤，尤其在一排污口，密密麻麻的死鱼翻起了白肚，恶臭熏天，几百米外都能闻到。据附近施工人员反映，半个月前湖面就出现了漂浮死鱼现象，是湖水受到了污染还是人为投毒呢？\n" +
                "\n" +
                "明湖城市公园位于南光高速塘明出口不远处，昨日中午，南都记者赶到该公园，还未走近就闻到湖区散发出浓烈的腥臭味，就打开车门一瞬间，车里就弥漫着这股恶臭味，令人作呕。南都记者沿着公园的木桥栈道往西走，只见湖水已经退去大半，湖底边沿干涸裸露的石块清晰可见，靠东侧的湖里漂浮着大量的死鱼，有福寿鱼、大头鱼、鲫鱼以及鲤鱼等。大则一两斤，小则几两。有一条污水渠直排湖内，污水漂着白沫发出阵阵臭味，这些污水沟将湖内的沙滩冲出一条小沟壑，源源不断的污水正排入沟内，再往西走10多米看到另一条水泥管做的排水管，大量的污水排入湖内，排污渠的铁栅栏上档住了厚厚的一层塑料袋等垃圾。\n" +
                "\n" +
                "附近居民吴先生称，明湖城市公园建成有4年，以前湖水比较清澈。他隐约记得，大概是从上个月20号开始，湖内水位开始降低，湖水变臭，起初还没有发现死鱼，只有少量的树叶和污垢，可是没几天，就发现湖面上开始出现死鱼，并且越来越多，部分鱼泡在湖里甚至已经开始腐烂，散发出难闻的臭味。\n" +
                "\n" +
                "采访时，公园管理方派来了几名工人开始对漂浮在水面的死鱼进行打捞，工人们称打捞起来的死鱼将会拉到附近进行深埋处理。";
        int count = 10;

        Map<String, List<String>> paras = Maps.newHashMap();
        List<String> opCodeList = Lists.newArrayList();
        List<String> contentList = Lists.newArrayList();
        List<String> countList = Lists.newArrayList();
        opCodeList.add(Command.OP_CODE_KEY);
        contentList.add(content);
        countList.add(String.valueOf(count));
        paras.put(Command.OP_CODE_KEY, opCodeList);
        paras.put("content", contentList);
        paras.put("count", countList);

        Command command = PlaceExtractCommand.createCommand(paras);
        assertEquals(false, command instanceof Command.NULLCommand);

//        System.out.println(command.execute());
        assertEquals(1, 1);
    }
}

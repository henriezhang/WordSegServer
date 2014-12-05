package com.qq.servers;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-11
 * Time: 下午2:27
 */
public class WordSegTest
{

    public static String keywordExtract(CloseableHttpClient client, HttpPost post, String title, String content) throws IOException
    {
        List<BasicNameValuePair> paras = new ArrayList<BasicNameValuePair>();
        paras.add(new BasicNameValuePair("op", Command.Opcode.KW.getName()));
//        paras.add(new BasicNameValuePair("title", title));
        paras.add(new BasicNameValuePair("content", content));
        paras.add(new BasicNameValuePair("weight", "true"));
        paras.add(new BasicNameValuePair("count", String.valueOf(10)));
        post.setEntity(new UrlEncodedFormEntity(paras, HTTP.UTF_8));
        System.out.println("#####");
        System.out.println(post.toString());
        System.out.println("#####");
        HttpResponse response2 = client.execute(post);
        return EntityUtils.toString(response2.getEntity());
    }

    public static String wordSegmentTest(CloseableHttpClient client, HttpPost post, String title, String content) throws IOException
    {
        List<BasicNameValuePair> paras = new ArrayList<BasicNameValuePair>();
        paras.add(new BasicNameValuePair("op", Command.Opcode.PLACE.getName()+","+Command.Opcode.KW.getName()));

        paras.add(new BasicNameValuePair("title", title));
        paras.add(new BasicNameValuePair("content", content));
        paras.add(new BasicNameValuePair("weight", "true"));
        paras.add(new BasicNameValuePair("count", "10"));
        post.setEntity(new UrlEncodedFormEntity(paras, HTTP.UTF_8));
        HttpResponse response2 = client.execute(post);
        return EntityUtils.toString(response2.getEntity());
    }


    public static void main(String[] args) throws IOException
    {

        String title1 = "吉林省松原发生5.3级地震,";
        String content1 = "中国地震台网正式测定：11月22日16时18分...24.1度）发生5.3级地震，震源深度8千米";
//        String title2 = "全军整风：清退军职干部的秘书";
//        String content2 = "[导读]对超占的住房作出腾退承诺，对固定使用越野车实行集中统管。\n" +
//                "\n" +
//                "新华网北京11月5日电 召开高质量的专题民主生活会，集中查摆纠治问题、开展批评与自我批评，是党的群众路线教育实践活动的重要环节。根据党中央、中央军委的部署安排，全军和武警部队各大单位党委近期相继召开了专题民主生活会。\n" +
//                "\n" +
//                "各大单位党委坚决贯彻落实“标准更高、走在前列”指示要求，认真学习对照习近平主席重要讲话精神，充分借鉴贯彻河北经验和体现整风精神，聚焦“四风”查摆问题，严肃开展批评和自我批评，研究制定深入推进教育实践活动、加强党委班子建设的措施办法，取得重要认识成果、实践成果、制度成果。\n" +
//                "\n" +
//                "——各项准备工作扎实充分。会前听取意见既面对面又背靠背，既请进来又走出去；思想动员深入及时，查摆问题联系个人实际，各大单位党委常委认真撰写检查材料；谈心交心敞开心扉、坦诚相见，深入沟通思想、交换意见，为开好会议奠定了基础。\n" +
//                "\n" +
//                "——会前解决问题见底见效。各大单位党委围绕军委明确的9个方面问题，采取“说清楚”“交明白账”的办法，即知即改、立说立行，对超占的住房都作出腾退承诺，对固定使用的越野车实行集中统管，对军职领导干部实际使用的秘书全部清退，并向军委提交自查自纠情况报告。\n" +
//                "\n" +
//                "——开展思想交锋积极健康。主动拿起批评和自我批评武器，书记副书记带头揭短、敢喊“向我开炮”，点评班子成员讲到具体人具体事；班子成员自我批评不遮不掩，触及思想和灵魂，相互批评开诚布公，开出整风味道。\n" +
//                "\n" +
//                "——制定整改措施具体实在。各大单位班子成员根据自我“画像”和大家的批评意见，自我把脉开“药方”，拿出实打实的整改措施。党委班子聚焦“四风”突出问题，分门别类制定整改的任务书、路线图，切实让官兵看到活动带来的新气象新变化。\n" +
//                "\n" +
//                "——跟进检查督导从严从实。军委总部成立工作组到各大单位检查督导，做到思想认识不深刻的不放过，查摆问题不真切的不放过，自我剖析不到位的不放过，自查自纠不彻底的不放过，民主测评不满意的不放过。\n" +
//                "\n" +
//                "这次大单位党委专题民主生活会，进一步检验了学习教育、听取意见的成效，聚焦了查摆问题、开展批评的重点，明确了整改落实、建章立制的方向。以此为标志，军队第一批党的群众路线教育实践活动取得重要阶段性成果：\n" +
//                "\n" +
//                "——思想认识明显提高，宗旨意识和党性观念进一步增强。各大单位党委班子的思想认识不断升华，理想信念和军魂意识牢固确立，对党的群众路线认识更加深刻，对抓活动反“四风”的战略意图理解更加深透，对改作风正风气的信心决心更加坚定，对领导带头、层层示范的责任担当更加自觉。\n" +
//                "\n" +
//                "——自查自纠初见成效，“四风”突出问题得到有力整治。在党委班子成员带动下，各大单位共清退不合理住房8100余套、车辆25000多台；普遍落实秘书和公勤人员配备使用规定；坚持以好的作风选人，树立正确用人导向；扎实开展基层风气专项整治，着力纠治发生在士兵身边的不正之风；认真贯彻厉行节约反对铺张浪费、严格经费管理的有关规定，加强工程建设项目审核，大幅压减行政消耗性开支。\n" +
//                "\n" +
//                "——工作作风得到转变，领导方式和工作方法不断改进。训风演风考风进一步端正，重点纠治议军议训质量不高、真打实备意识不强、选人用人打仗导向不够、训练演习与实战脱节等问题；文风会风明显好转，各大单位党委机关带头转文风改会风，精简文件电报，严格控制会议活动和工作组数量规模，文电同比下降最多的达46%；服务指导基层深入扎实，既坚持重心下移抓帮带，又注重排忧解难办实事，760多名军职以上领导干部进班排、上哨所、接地气，在执行大项任务中身先士卒、冲锋在前，忠实履行使命任务。\n" +
//                "\n" +
//                "——制度机制进一步完善，为推动改进作风常态长效提供硬约束。各大单位坚持纠建并举，结合专题民主生活会，普遍做好建章立制工作。研究制定新的制度规定，把权力关进制度的“笼子”，完善现有制度机制，对滞后形势任务和实践发展需要的搞好清理，固化成功经验做法，及时把行之有效的做法上升为制度成果。\n" +
//                "\n" +
//                "当前，全军第一批教育实践活动陆续进入专项整治、建章立制的重要环节。各大单位将着力在提高思想认识上求深化，在巩固已有成果上求深化，在推进专项整治上求深化，在完善制度机制上求深化，驰而不息、久久为功，努力实现作风建设根本性好转，为推动实现党在新形势下的强军目标提供坚强保证。";

        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost("http://localhost:8083");
        HttpPost httpPost = new HttpPost("http://10.129.138.54:8081");

//        List<BasicNameValuePair> paras = new ArrayList<BasicNameValuePair>();
//        paras.add(new BasicNameValuePair("op", Command.Opcode.KW.getName()));
//        paras.add(new BasicNameValuePair("title", title1));
//        paras.add(new BasicNameValuePair("content", content2));
//        paras.add(new BasicNameValuePair("count", String.valueOf(10)));
//        httpPost.setEntity(new UrlEncodedFormEntity(paras, HTTP.UTF_8));
//        HttpResponse response2 = httpclient.execute(httpPost);
//        String result = EntityUtils.toString(response2.getEntity());
//
//        System.out.println(result);

//        String kw1 = keywordExtract(httpclient, httpPost, title1, content1);
//        System.out.println(kw1);
//
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = mapper.readTree(kw1);
//        JsonNode kwNode = node.get(Command.Opcode.KW.getName());
//        ArrayNode kwArrayNode = (ArrayNode) kwNode;
//        System.out.println("##");
//        for (int i = 0; i < kwArrayNode.size(); i++)
//        {
//            System.out.println(kwArrayNode.get(i));
//        }
//        System.out.println("##");
//        System.out.println(kwNode.toString());

        System.out.println("##");
//        String kw2 = wordSegmentTest(httpclient, httpPost, title2, "");
//        System.out.println(kw2);
        System.out.println("##");
        String kw2 = keywordExtract(httpclient, httpPost, title1, content1);
        System.out.println(kw2);
        System.out.println("##");



        httpclient.close();


    }
}

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
        //paras.add(new BasicNameValuePair("op", Command.Opcode.KW.getName()));
        paras.add(new BasicNameValuePair("op", "keyword"));
        paras.add(new BasicNameValuePair("title", title));
        paras.add(new BasicNameValuePair("content", content));
        paras.add(new BasicNameValuePair("weight", "true"));
        paras.add(new BasicNameValuePair("count", String.valueOf(10)));
        post.setEntity(new UrlEncodedFormEntity(paras, HTTP.UTF_8));
        System.out.println("#####A");
        System.out.println(post.toString());
        System.out.println(paras.toString());
        System.out.println("#####B");
        HttpResponse response2 = client.execute(post);
        System.out.println("#####C");
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
        String content1 = "中国地震台网正式测定：11月22日16时18分...24.1度）发生5.3级地震，震源深度8千米" +
                "新华网北京11月5日电 召开高质量的专题民主生活会，集中查摆纠治问题、开展批评与自我批评，是党的群众路线教育实践活动的重要环节。根据党中央、中央军委的部署安排，全军和武警部队各大单位党委近期相继召开了专题民主生活会。";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        //HttpPost httpPost = new HttpPost("http://localhost:8081");
        //HttpPost httpPost = new HttpPost("http://10.30.112.84:8081");
        HttpPost httpPost = new HttpPost("http://10.241.97.161:8081");

        //System.out.println("##1");
        //String kw1 = wordSegmentTest(httpclient, httpPost, title1, content1);
        //System.out.println(kw1);
        //System.out.println("##2");

        String kw2 = keywordExtract(httpclient, httpPost, title1, content1);
        System.out.println(kw2);
        System.out.println("##3");
        httpclient.close();

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
   }
}

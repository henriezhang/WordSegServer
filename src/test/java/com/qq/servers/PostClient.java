package com.qq.servers;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-19
 * Time: 下午5:35
 */
public class PostClient
{


    public static void main(String[] args) throws Exception
    {

        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new EchoHttpPipelineFactory());

        ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 8083));


        // Wait until the connection attempt succeeds or fails.
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess())
        {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }


        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");

        HttpDataFactory factory = new DefaultHttpDataFactory(false);
        HttpPostRequestEncoder postEncoder = new HttpPostRequestEncoder(factory, request, false);

        postEncoder.addBodyAttribute("title", "哈登魔兽霸气两双 书豪11+6+4火箭大胜马刺");
        postEncoder.addBodyAttribute("content", "腾讯体育讯 北京时间10月25日，火箭在季前赛中客场对阵马刺，在哈登与霍华德(微博)均砍下精彩两双的表现带动下，火箭在第三节一度建立22分优势，最终109-92大胜马刺。火箭取得季前赛5连胜。\n" +
                "点击查看本场技术统计\n" +
                "火箭队\t数据\t马刺队\t数据\n" +
                "哈登\t22分\t11助攻\t3抢断\t丹尼-格林(微博)\t12分\t2三分\t2抢断\n" +
                "霍华德\t15分\t16篮板\t2封盖\t邓肯\t6分\t8篮板\t4失误\n" +
                "林书豪(微博)\t11分\t6助攻\t4篮板\t帕克(微博)\t8分\t4助攻\t3篮板\n" +
                "\n" +
                "本场之星：“魔登”显威压马刺\n" +
                "\n" +
                "马刺阵营赛前最关注的是：霍华德与哈登能有怎样的组合效果？答案在第三节充分显现——随着霍华德在1分半钟内接连3次灌篮（两次是非助攻情况下的自行强打），火箭内线确立优势，防守中也限制得邓肯5投连失；哈登随即也在外围开火，连入三分球并有冲篮打三分，火箭就此形成绝对优势，一度对马刺主力阵容领先到22分！\n" +
                "\n" +
                "比赛回顾：火箭展现实力 客场大胜马刺\n" +
                "\n" +
                "开场哈登即助霍华德灌篮，哈登冲篮入球后帕森斯射入三分球，火箭打出7-2开局。阿西克与霍华德合围邓肯，魔兽正对石佛送出封盖。但随着迪奥、格林相继射入三分球，马刺逐步超出一度18-14领先。书豪出场后连送助攻助火箭追上，在其快攻造犯规2罚全中后，火箭22-20领先首节。\n" +
                "\n" +
                "次节前段马刺围绕贝里内利演练边缘替补；书豪连送助攻也冲篮入球，卡西比则两入三分球，火箭34-27领先。马刺主将回场后，格林命中三分球，帕克也助斯普利特打三分，马刺8-0攻势再度超出。加西亚与帕森斯的远投助火箭重又领跑，半场火箭47-45领先。\n" +
                "\n" +
                "进入第三节霍华德接连3次扣篮(微博)，哈登也连入三分球并冲篮打三分，火箭全面压制住马刺主力阵容；在哈登声东击西妙传助帕森斯灌篮后，火箭已66-53领先。打出兴致的魔兽又有打三分表现，书豪与卡西比连入远投后，火箭81-59一度领先到22分。邦纳此后虽连续得分，三节结束火箭仍84-70领先。\n" +
                "\n" +
                "末节前段火箭由书豪、贝弗利与阿西克主打，进攻效率却是走低；马刺替补追至76-86。贝弗利下场后，书豪连续得分并助莫蒂埃尤纳斯射入三分球，火箭7-0攻势重将比分拉开。书豪下场后，莫蒂与布鲁克斯继续带动火箭涨分，史密斯接力灌篮后火箭一度重建20分以上优势。最终火箭109-92大胜马刺。\n" +
                "\n" +
                "数据分析：有篮板 放手飙三分！\n" +
                "\n" +
                "霍华德的16篮板，直接带动火箭于篮板球数据上51-41压制了马刺。火箭就此胆气喷薄——三分球38投13中，效率其实并不算出色，但敢于远投出手38次，球队特色绝对尽显。马刺远投则是18投7中。火箭的防守效果也很直观——邓肯此战12投仅2中，丹尼-格林13投5中也不理想，马刺88投仅35中（命中率不及40%）；火箭89投40中，命中率接近45%。\n" +
                "\n" +
                "花絮：魔兽发威敌友通杀\n" +
                "\n" +
                "火箭此战继续双塔战术，霍华德的威猛开场就告体现——争抢防守篮板时，霍华德助跑跳抢，事实上当时火箭篮下并没有马刺球员，篮板球也弹向了阿西克，可魔兽是将阿西克撞的趔趄栽出场、从搭档手中抢走了篮板。阿西克似乎就此让戏了，目送霍华德半场即篮板上双。\n" +
                "\n" +
                "进入第三节霍华德更是威猛——内线强行要位将邓肯撞得仰摔在地，随即发力灌篮，就此给出了接连3次灌篮的表演，绝对霸气十足。而邓肯就随着那一摔，出现了第三节前半节5投连失加2次失误的状况。\n" +
                "\n" +
                "首发阵容：\n" +
                "\n" +
                "火箭：阿西克、霍华德、帕森斯、哈登、贝弗利\n" +
                "\n" +
                "马刺：斯普利特、邓肯、迪奥、丹尼-格林、帕克");
        postEncoder.addBodyAttribute("count", "7");

//        System.out.println(postEncoder.isMultipart());
//
//        request.setHeader(HttpHeaders.Names.HOST, "localhost");
//        request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
//        request.setHeader(HttpHeaders.Names.CONTENT_LENGTH,);
//        request.setChunked(false);

//        queryStrEncoder.addParam
//        queryStrEncoder.addParam(
//        System.out.println("Query String = "+queryStrEncoder.toString());
//
//        ChannelBuffer content = ChannelBuffers.copiedBuffer(queryStrEncoder.toString(), CharsetUtil.UTF_8);
//        request.setContent(content);

        request = postEncoder.finalizeRequest();
//        request.removeHeader("Content-Length");
//        request.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain;charset=UTF-8");

//        request.setChunked(true);

//        for(InterfaceHttpData data : postEncoder.getBodyListAttributes())
//        {
//            System.out.println(data.getName());
//        }

        System.out.println("#" + request.toString() + "#");

//        while(postEncoder.hasNextChunk())
//        {
//            System.out.println("#"+postEncoder.nextChunk().getContent().toString(CharsetUtil.UTF_8)+"#");
//        }

        System.out.println(System.getProperty("java.class.path"));
        channel.write(request);

        if (postEncoder.isChunked())
        {
            channel.write(postEncoder).awaitUninterruptibly();

        }





//        if (postEncoder.isChunked())
//        {
//            while (postEncoder.hasNextChunk())
//            {
//                channel.write(postEncoder.nextChunk());
//            }
//        }


//        if (postEncoder.isChunked())
//        { // could do either request.isChunked()
//            // either do it through ChunkedWriteHandler
//            channel.write(postEncoder).awaitUninterruptibly();
//        }
        channel.getCloseFuture().awaitUninterruptibly();

        bootstrap.releaseExternalResources();
    }
}

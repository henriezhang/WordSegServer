package com.qq.servers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-19
 * Time: 下午5:40
 */
public class PrintContextHttpHandler extends SimpleChannelUpstreamHandler
{

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
    {
        HttpResponse response = (HttpResponse) e.getMessage();

        System.out.println("#"+response.getContent().toString(CharsetUtil.UTF_8)+"#");
        ctx.getChannel().close();

    }
}

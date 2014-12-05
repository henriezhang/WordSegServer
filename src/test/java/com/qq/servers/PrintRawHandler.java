package com.qq.servers;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.util.CharsetUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Rao
 * Date: 13-10-19
 * Time: 下午10:22
 */
public class PrintRawHandler  extends SimpleChannelUpstreamHandler
{
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
    {
        System.out.println("@#"+((ChannelBuffer)e.getMessage()).toString(CharsetUtil.UTF_8)+"#@");
    }
}

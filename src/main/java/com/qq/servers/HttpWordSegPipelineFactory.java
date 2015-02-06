package com.qq.servers;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-12
 * Time: 下午1:16
 */
public class HttpWordSegPipelineFactory implements ChannelPipelineFactory {

    /*public class PrintRawHandler  extends SimpleChannelUpstreamHandler
    {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
        {
            System.out.println("@#"+((ChannelBuffer)e.getMessage()).toString(CharsetUtil.UTF_8)+"#@");
            super.messageReceived(ctx, e);
        }
    }*/

    @Override
    public ChannelPipeline getPipeline() throws Exception {

        ChannelPipeline pipeline = Channels.pipeline();

        //pipeline.addLast("printraw",new PrintRawHandler());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(10485760));
        pipeline.addLast("encode", new HttpResponseEncoder());
        pipeline.addLast("chunkedwriter", new ChunkedWriteHandler());
        pipeline.addLast("handler", new HttpWordSegServerHandler());

        return pipeline;
    }
}

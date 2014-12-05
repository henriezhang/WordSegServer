package com.qq.servers;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-19
 * Time: 下午5:37
 */
public class EchoHttpPipelineFactory implements ChannelPipelineFactory
{
    @Override
    public ChannelPipeline getPipeline() throws Exception
    {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("decode", new HttpResponseDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        pipeline.addLast("encode", new HttpRequestEncoder());
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

//        pipeline.addLast("printraw", new PrintRawHandler());
        pipeline.addLast("print", new PrintContextHttpHandler());
        return pipeline;


    }
}

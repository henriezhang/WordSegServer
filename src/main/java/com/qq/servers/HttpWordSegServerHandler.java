package com.qq.servers;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.util.CharsetUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


/**
 * accept GET and POST message.
 * <ul>
 * <li>
 * for small size request, you can embed request in url query string
 * e.g.
 * http://localhost:8080/?w=我爱天安门&t=true
 * <code>t</code> indicate whether to attach annotation with words or not, false default
 * </li>
 * <li>
 * if request is larger than url allowed,you should store request in POST message body
 * in this case, you can also specify parameter <code>t</code> to include annotation or not
 * <p/>
 * </li>
 * </ul>
 * <p/>
 * result format
 * 1. words separated by blank space.
 * 2. word and annotation(if exist) separated by "/"
 */
public class HttpWordSegServerHandler extends SimpleChannelUpstreamHandler
{

    private static final Log LOG = LogFactory.getLog(HttpWordSegServerHandler.class);

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(false);
    private HttpPostRequestDecoder decoder = null;
    //    private String responseContent = null;
    private HttpRequest request;
    private WordSegmenter segmenter = new AnsjWordSegmenter();

    private CommandExecutor executor = new CommandExecutor();
    private static String usage = "";


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception
    {
        HttpRequest request = (HttpRequest) e.getMessage();
        this.request = request;
        Command command = CommandFactory.createCommand(request);
        String result = "";
        if (command != null)
        {
            result = executor.execute(command);
        }
        writeResponse(e.getChannel(), result);
    }


    private void writeResponse(Channel channel, String responseContent)
    {
        // Convert the response content to a ChannelBuffer.
        ChannelBuffer buf = ChannelBuffers.copiedBuffer(responseContent,
                CharsetUtil.UTF_8);

        // Decide whether to close the connection or not.
        boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request
                .getHeader(HttpHeaders.Names.CONNECTION))
                || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request
                .getHeader(HttpHeaders.Names.CONNECTION));

        // Build the response object.
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK);
        response.setContent(buf);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        if (!close)
        {
            // There's no need to add 'Content-Length' header
            // if this is the last response.
            response.setHeader(HttpHeaders.Names.CONTENT_LENGTH,
                    String.valueOf(buf.readableBytes()));
        }

        // Write the response.
        ChannelFuture future = channel.write(response);
        // Close the connection after the write operation is done if necessary.
        if (close)
        {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception
    {

        LOG.warn("Unexpected exception from downstream", e.getCause());
        e.getChannel().close();

    }
}

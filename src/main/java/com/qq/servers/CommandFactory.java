package com.qq.servers;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-19
 * Time: 下午5:06
 */
public class CommandFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(CommandFactory.class);
//    public static WordFilter filter = new MeanWordFilter();
//    public static Weight titleWeight = new Weight.TitleWeight(5, filter);
//    public static Weight contentWeight = new Weight.ContentWeight(3, filter);
//
//    public static WordSegmenter segmenter = new AnsjWordSegmenter();


    private static Charset getContentCharset(HttpRequest request)
    {
        String contentType = request.getHeader(HttpHeaders.Names.CONTENT_TYPE);
        if (contentType == null)
        {
            return CharsetUtil.UTF_8;
        }
        int indexOfCharset = contentType.indexOf("charset");
        if (indexOfCharset == -1)
        {
            return CharsetUtil.UTF_8;
        }
        String charsetName = contentType.substring(indexOfCharset + "charset".length() + 1).trim();
        return Charset.forName(charsetName);
    }


    public static Command createCommand(HttpRequest request) throws URISyntaxException
    {
        if (request.getMethod().equals(HttpMethod.GET))
        {
            URI uri = new URI(request.getUri());
            QueryStringDecoder decodeQuery = new QueryStringDecoder(uri);
            Map<String, List<String>> parameters = decodeQuery.getParameters();
            return WordSegCommand.createCommand(parameters);
        }
        else
        {
            if (request.getMethod().equals(HttpMethod.POST))
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Received request : " + request.toString());
                }
                String body = request.getContent().toString(getContentCharset(request));
                QueryStringDecoder postQueryDecoder = new QueryStringDecoder("/?" + body);

                Map<String, List<String>> postParameters = postQueryDecoder.getParameters();

//                for (Map.Entry<String, List<String>> entry : postParameters.entrySet())
//                {
//                    System.out.println(entry.getKey());
//                }

                if (postParameters.get(Command.OP_CODE_KEY) != null)
                {
                    String value = postParameters.get(Command.OP_CODE_KEY).get(0);

                    //support commands multiplex
                    if (value.indexOf(",") > 0)
                    {
                        return CompoundCommand.createCommand(postParameters);
                    }
                    return createSingleCommand(value, postParameters);
                }
            }
        }
        return Command.NULLCommand.get();
    }

    public static Command createSingleCommand(String strOpcode, Map<String, List<String>> postParameters)
    {
        Command.Opcode opcode = Command.Opcode.fromString(strOpcode);
        if (opcode == null)
        {
            Command.NULLCommand.get();
        }

        Command command = Command.NULLCommand.get();

        switch (opcode)
        {
            case KW:
            {
                command = KeywordExtractCommand.createCommand(postParameters);
                break;
            }
            case PLACE:
            {
                command = PlaceExtractCommand.createCommand(postParameters);
                break;
            }
            case SEGMENT:
            {
                command = WordSegCommand.createCommand(postParameters);
                break;
            }
        }

        return command;
    }

}

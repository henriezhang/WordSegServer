package com.qq.servers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-9
 * Time: 下午2:32
 */
public class JsonTest
{

    public static void test() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"hubei\",\"tianmen\"]");

        Iterator<JsonNode> iterator = node.getElements();
        while (iterator.hasNext())
        {
            JsonNode jn = iterator.next();
            System.out.println(jn.getTextValue());
        }

    }

    public static void main(String[] args) throws IOException
    {
        test();
        if (true)
        {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode dataTable = mapper.createObjectNode();
        ArrayNode kw = dataTable.putArray("kw");
        kw.add("11/12");
        kw.add("12/12");

        ArrayNode place = dataTable.putArray("place");
        place.add("hubei");
        place.add("tianmen");

        System.out.println(dataTable.toString());

        JsonNode node = dataTable.get("place");

        ArrayNode arrayNode = (ArrayNode) node;

        Iterator<JsonNode> iterator = arrayNode.getElements();
        while (iterator.hasNext())
        {
            JsonNode jn = iterator.next();
            System.out.println(jn.getTextValue());
        }


    }


}

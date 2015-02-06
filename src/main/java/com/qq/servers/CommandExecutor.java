package com.qq.servers;

import com.google.common.base.Throwables;
import com.qq.servers.tfidfproducer.Fragment;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-9
 * Time: 下午3:43
 */
public class CommandExecutor {
    private static Logger LOG = LoggerFactory.getLogger(CommandExecutor.class);
    private static ObjectMapper mapper = new ObjectMapper();


    /**
     * passed
     *
     * @param command the command to execute
     * @return the result string
     */
    public String execute(Command command) {
        boolean annotation = command.getAnnotation();
        List<Fragment> fragments = command.getFragments(annotation);
        ObjectNode dataTable = mapper.createObjectNode();
        try {
            command.execute(fragments, dataTable);
            dataTable.put("code", "0");
        } catch (Exception e) {
            //write error code -1
            dataTable.put("code", "-1");
            LOG.error(Throwables.getStackTraceAsString(e));
        }

        return dataTable.toString();
    }
}
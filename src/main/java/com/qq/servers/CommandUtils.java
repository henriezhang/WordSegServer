package com.qq.servers;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-11
 * Time: 下午5:16
 * <p/>
 * utility methods to facilitate common command manipulation.
 */
public class CommandUtils {

    /**
     * get parameter shared by all command.
     *
     * @param parameters parameter in decoded from url query string
     * @return a list contains title, followed by content.
     */
    public static List<String> getCommonContent(Map<String, List<String>> parameters) {
        List<String> contents = Lists.newArrayList();
        List<String> title = parameters.get(Command.TITLE_KEY);
        if (title != null) {
            contents.add(title.get(0));
        }
        List<String> content = parameters.get(Command.CONTENT_KEY);
        if (content != null) {
            contents.add(content.get(0));
        }
        return contents;
    }
}

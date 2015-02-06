package com.qq.servers;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.qq.servers.tfidfproducer.Fragment;
import com.qq.servers.tfidfproducer.Weight;
import org.ansj.domain.Term;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-19
 * Time: 下午3:34
 */
public class WordSegCommand implements Command {

    private static final Joiner slashJoinner = Joiner.on("/");
    private static Weight.BaseWeight NO_WEIGHTER = new Weight.BaseWeight();
    private final List<String> contents;
    private final boolean annotation;

    public WordSegCommand(List<String> contents, boolean annotation) {
        this.contents = contents;
        this.annotation = annotation;
    }

    public static Command createCommand(Map<String, List<String>> parameters) {
        //get common command parameters
        List<String> contents = CommandUtils.getCommonContent(parameters);
        if (contents.size() == 0) {
            return NULLCommand.get();
        }

        //get word segment specific parameters
        boolean annotation = false;
        if (parameters.get(ANNOTATION_KEY) != null) {
            annotation = Boolean.valueOf(parameters.get(ANNOTATION_KEY).get(0));
        }
        return new WordSegCommand(contents, annotation);
    }

    @Override
    public int getPreference() {
        return 2;
    }

    @Override
    public boolean getAnnotation() {
        return this.annotation;
    }

    @Override
    public String getName() {
        return Opcode.SEGMENT.getName();
    }

    @Override
    public List<Fragment> getFragments(boolean annotation) {
        List<Fragment> fragments = Lists.newArrayList();
        for (String content : contents) {
            fragments.add(new Fragment(segmenter.segmentWord(content, annotation), NO_WEIGHTER));
        }
        return fragments;
    }

    @Override
    public void execute(List<Fragment> fragments, ObjectNode result) {

        ArrayNode place = result.putArray(getName());

        //move predication up level to avoid predication mismatch
        if (annotation) {
            for (Fragment fragment : fragments) {
                for (Term word : fragment.words) {
                    place.add(slashJoinner.join(word.getName(), word.getNatrue().natureStr));
                }
            }

        } else {
            for (Fragment fragment : fragments) {
                for (Term word : fragment.words) {
                    place.add(word.getName());
                }
            }
        }
    }

}

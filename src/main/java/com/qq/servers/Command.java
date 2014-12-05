package com.qq.servers;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.qq.servers.tfidfproducer.Fragment;
import org.codehaus.jackson.node.ObjectNode;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-19
 * Time: 下午3:33
 */
public interface Command
{

    /**
     * key for specifying operation code
     */
    public static final String OP_CODE_KEY = "op";

    /**
     * key for specifying document content
     */
    public static final String CONTENT_KEY = "content";
    /**
     * key for specifying the number of key words extracted or the number of places extracted.
     */
    public static final String COUNT_KEY = "count";
    /**
     * since give document title higher weigher than document content, use this key to specify document title separately
     */
    public static final String TITLE_KEY = "title";
    /**
     * key for specifying extracted key words has TF-IDF value associated with it or not ,or word frequency value
     * is attached to extracted places.
     */
    public static final String WEIGHT_KEY = "weight";

    /**
     * key indicates segmented words has annotation attached with it
     */
    public static final String ANNOTATION_KEY = "t";

    /**
     * key for specifying specific site, different site have its own idf dictionary.
     */
    public static final String SITE_KEY = "site";

    /**
     * objects can be reused
     */
    public static final Joiner slashJoiner = Joiner.on("/");
    public static WordSegmenter segmenter = new AnsjWordSegmenter();

    /**
     * @return command name
     */
    public String getName();

    /**
     * indicate whether this command need annotation associated with words when do word segmentation or not
     *
     * @return true if need annotation,false otherwise
     */
    public boolean getAnnotation();

    /**
     * in order to support command multiplex, give each specific command a preference value to determine their ordering
     * in command collections.
     * <p>
     * more smaller value,higher preference.
     * </p>
     *
     * @return the preference value
     */
    public int getPreference();

    /**
     * return fragment list, each corresponding to a paragraph
     *
     * @return fragments list
     */
    public List<Fragment> getFragments(boolean annotation);


    /**
     * executor this command.
     *
     * @param fragments the fragment list
     * @param result    the result object
     */
    public void execute(List<Fragment> fragments, ObjectNode result);


    public static enum Opcode
    {
        PLACE("place"), KW("keyword"), SEGMENT("segment"), COMPOUND("compound");
        private String opcode;

        private Opcode(String opcode)
        {
            this.opcode = opcode;
        }

        public String getName()
        {
            return this.opcode;
        }

        public static Opcode fromString(String opStr)
        {
            for (Opcode opCode : Opcode.values())
            {
                if (opCode.getName().equals(opStr))
                {
                    return opCode;
                }
            }
            return null;
        }
    }


    public static class NULLCommand implements Command
    {
        private static final NULLCommand THIS = new NULLCommand();
        private static final List<Fragment> EMPTY_LIST = Lists.newArrayList();

        public static NULLCommand get()
        {
            return THIS;
        }

        @Override
        public boolean getAnnotation()
        {
            return false;
        }

        @Override
        public int getPreference()
        {
            return Integer.MAX_VALUE;
        }

        @Override
        public List<Fragment> getFragments(boolean annotation)
        {
            return EMPTY_LIST;
        }

        @Override
        public void execute(List<Fragment> fragments, ObjectNode place)
        {
            //do nothing.
        }


        private NULLCommand()
        {
        }

        @Override
        public String getName()
        {
            return "";
        }
    }


}

package com.qq.servers;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Ints;
import com.qq.servers.tfidfproducer.Fragment;
import com.qq.servers.tfidfproducer.Weight;
import org.ansj.domain.Term;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-5
 * Time: 上午10:46
 */
public class PlaceExtractCommand implements Command
{
//    public static final String OP_CODE = "placeextract";

    private static final String PLACE_ANNOTATION_PREFIX = "ns";
    private static final int DEFAULT_COUNT = 10;

    //fields
    private List<String> contents;
    private int count;

    public PlaceExtractCommand(List<String> contents, int count)
    {
        this.contents = contents;
        this.count = count;
    }
//
//    @Override
//    public String execute()
//    {

    @Override
    public int getPreference()
    {
        return 1;
    }


    @Override
    public String getName()
    {
        return Opcode.PLACE.getName();
    }

    @Override
    public boolean getAnnotation()
    {
        return true;
    }

    @Override
    public List<Fragment> getFragments(boolean annotation)
    {
        List<Fragment> fragments = Lists.newArrayList();
        for (String content : contents)
        {
            fragments.add(new Fragment(segmenter.segmentWord(content, annotation), new Weight.BaseWeight()));
        }
        return fragments;
    }

    @Override
    public void execute(List<Fragment> fragments, ObjectNode result)
    {

        ArrayNode place = result.putArray(getName());
        final Multiset<String> places = HashMultiset.create();
        int wordCount = getAllPlaces(fragments, places);
        final List<String> placesList = sortByPlaceFrequency(places);
        int realCount = (placesList.size() < count) ? placesList.size() : count;
        for (int i = 0; i < realCount; i++)
        {
            place.add(slashJoiner.join(placesList.get(i), places.count(place) / ((double) wordCount)));
        }
    }

    private int getAllPlaces(List<Fragment> fragments, Multiset<String> places)
    {
        int wordCount = 0;
        for (Fragment fragment : fragments)
        {
            for (Term term : fragment.words)
            {
                wordCount++;
                if (term.getNatrue().natureStr.startsWith(PLACE_ANNOTATION_PREFIX))
                {
                    places.add(term.getName());
                }
            }
        }
        return wordCount;
    }

    private List<String> sortByPlaceFrequency(final Multiset<String> places)
    {
        final List<String> placesList = Lists.newArrayList(places.elementSet());
        Collections.sort(placesList, new Comparator<String>()
        {
            @Override
            public int compare(String str1, String str2)
            {
                //sort by word occurrence in descending order,so reverse comparison result.
                return Ints.compare(places.count(str1), places.count(str2)) * (-1);
            }
        });
        return placesList;
    }


    public static Command createCommand(Map<String, List<String>> parameters)
    {
        List<String> contents = CommandUtils.getCommonContent(parameters);
        if (contents.size() == 0)
        {
            return NULLCommand.get();
        }

        int count = DEFAULT_COUNT;
        if (!(parameters.get(Command.COUNT_KEY) == null))
        {
            count = Integer.valueOf(parameters.get(Command.COUNT_KEY).get(0));
        }
        return new PlaceExtractCommand(contents, count);
    }

}

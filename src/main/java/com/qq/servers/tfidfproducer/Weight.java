package com.qq.servers.tfidfproducer;

import org.ansj.domain.Term;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-11-9
 * Time: 下午3:08
 */
public interface Weight
{
    public double computeWeight(Term word, int length);


    public static class BaseWeight implements Weight
    {
        @Override
        public double computeWeight(Term word, int length)
        {
            return 1.0;
        }
    }

    /**
     * this class used to computeWeight weight of title<p>
     * in theory, a title of document should have highest weight
     */
    public static class TitleWeight implements Weight
    {
        private final double baseWeight;
        private final WordFilter filter;

        public TitleWeight(double baseWeight, WordFilter filter)
        {
            this.filter = filter;
            this.baseWeight = baseWeight;
        }

        @Override
        public double computeWeight(Term word, int length)
        {
            if (!filter.accept(word))
            {
                return 0;
            }
            return baseWeight;
        }
    }

    /**
     * this class used to computeWeight weight of words in content of document<p>
     * <p>
     * computeWeight weight of words in content of document based on their position in document.
     * Give a higher weight to words near beginning than words in end of document.
     * </p>
     */
    public static class ContentWeight implements Weight
    {
        private final double baseWeight;
        private final WordFilter filter;

        public ContentWeight(double baseWeight, WordFilter filter)
        {
            this.baseWeight = baseWeight;
            this.filter = filter;
        }

        @Override
        public double computeWeight(Term word, int length)
        {
            if (!filter.accept(word))
            {
                return 0;
            }
            double position = ((double) (word.getOffe())) / length;

            if (position < 0.05)
            {
                return baseWeight;
            }
            return (baseWeight - baseWeight * position);
        }
    }

    public static class ContentWeightWithoutPos implements Weight
    {
        private final double baseWeight;
        private final WordFilter filter;

        public ContentWeightWithoutPos(double baseWeight, WordFilter filter)
        {
            this.baseWeight = baseWeight;
            this.filter = filter;
        }

        @Override
        public double computeWeight(Term word, int length)
        {
            if (!filter.accept(word))
            {
                return 0;
            }

            return baseWeight;
        }
    }


}

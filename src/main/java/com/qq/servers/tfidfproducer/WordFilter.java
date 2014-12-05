package com.qq.servers.tfidfproducer;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-17
 * Time: 下午12:46
 */
public interface WordFilter
{

    /**
     *
     * based on some strategy to filter word or not
     * @param word
     * @return true if accept,false otherwise
     */
    public boolean accept(Object word);

}

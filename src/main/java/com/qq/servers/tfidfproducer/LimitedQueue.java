package com.qq.servers.tfidfproducer;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: antyrao
 * Date: 13-10-31
 * Time: 下午5:57
 */

public class LimitedQueue<E> extends LinkedBlockingQueue<E>
{

    public LimitedQueue(int size)
    {
        super(size);

    }

    @Override
    public boolean offer(E e)
    {
        // turn offer() and add() into a blocking calls (unless interrupted)
        try
        {
            put(e);
            return true;
        }
        catch (InterruptedException ie)
        {
            Thread.currentThread().interrupt();
        }
        return false;
    }
}

package com.qq.servers.tfidfproducer;

import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.collect.Ordering;

/**
 * Created by antyrao on 14-6-5.
 */
public class PriorityQueueTest
{
    public static void main(String[] args)
    {
        MinMaxPriorityQueue<Integer> queue;
        queue = MinMaxPriorityQueue.orderedBy(Ordering.natural().reverse()).maximumSize(5).create();
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
        queue.add(6);
        queue.add(7);
        queue.add(8);
        System.out.println(queue.toString());

    }
}

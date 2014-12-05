#!/usr/bin/env bash
mkdir -p /home/admin/anty/push_recommendation/article_info2_tmp2/$base
bin/bigdata com.qq.servers.tfidfproducer.ArticleProcessorContent /home/admin/anty/push_recommendation/article_info2_tmp/$base/part-m-00000 /home/admin/anty/push_recommendation/article_info2_tmp2/$base/article_info_normalized2 
#/home/admin/anty/push_recommendation/article_info2_tmp
#bin/bigdata com.qq.servers.tfidfproducer.ArticleProcessor /data/home/admin/anty/push_recommendation/tmp/result/126232278889859023/part-r-00000 /home/admin/anty/push_recommendation/article_info_normalized 

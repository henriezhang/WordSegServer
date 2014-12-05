#!/usr/bin/env bash

this="$0"
bin=`dirname $this`
bin=`cd $bin;pwd -P`

exec $bin/bigdata-daemon.sh stop wordseg

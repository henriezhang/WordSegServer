#!/usr/bin/env bash

this="$0"
bin=`dirname $this`
bin=`cd $bin;pwd -P`

if [[ $# > 1 ]]; then
    
   echo "Usage: startup.sh [port]"
   exit 1
fi

exec $bin/bigdata-daemon.sh start wordseg "$@"

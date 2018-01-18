#!/bin/sh
SEARCH_ENGINE_DEPLOYMENT_DIR=/home/searcher
SEARCH_ENGINE_OPS_DIR=$SEARCH_ENGINE_DEPLOYMENT_DIR/ops
SEARCH_ENGINE_PID=$SEARCH_ENGINE_OPS_DIR/pid

if [ ! -d "$SEARCH_ENGINE_DEPLOYMENT_DIR" ]; then
    echo 'The search engine deployment directory is not exist'
    exit 1
fi

if [ ! -d "$SEARCH_ENGINE_OPS_DIR" ]; then
    echo 'The search engine ops directory is not exist'
    exit 1
fi

if [ ! -f "$SEARCH_ENGINE_PID" ]; then
    echo 'The search engine pid file is not exist'
    exit 1
fi

EXECUTION_PID=`cat $SEARCH_ENGINE_PID`
 
kill -9 $EXECUTION_PID
if [ $? -eq 0 ];then
    echo 'Search Engine application shutdown succeed'
else 
    echo 'Search Engine application shutdown failed'
fi

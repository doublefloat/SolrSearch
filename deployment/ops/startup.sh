#!/bin/sh
SEARCH_ENGINE_DEPLOYMENT_DIR=/home/searcher
SEARCH_ENGINE_APP_DIR=$SEARCH_ENGINE_DEPLOYMENT_DIR/app/
SEARCH_ENGINE_LOG_DIR=$SEARCH_ENGINE_DEPLOYMENT_DIR/logs/
SEARCH_ENGINE_OPS_DIR=$SEARCH_ENGINE_DEPLOYMENT_DIR/ops/
SEARCH_ENGINE_PID=$SEARCH_ENGINE_OPS_DIR/pid
CURRENT_DATE="$(date +'%Y-%m-%d')"
LISTEN_PORT=10020

if [ ! -d "$SEARCH_ENGINE_DEPLOYMENT_DIR" ]; then
    echo 'The search engine deployment directory[/home/searcher/search_engine] is not exist'
    exit 1
fi

if [ ! -d "$SEARCH_ENGINE_APP_DIR" ]; then
    echo 'The search engine application directory[/home/searcher/search_engine/app] is not exist'
    exit 1
fi

if [ ! -d "$SEARCH_ENGINE_LOG_DIR" ]; then
    echo 'The search engine log directory[/home/searcher/search_engine/logs] is not exist'
    exit 1
fi

if [ ! -f "$SEARCH_ENGINE_PID" ]; then
    echo 'The search engine pid file is not exist'
    exit 1
fi

nohup /home/searcher/software/jdk1.8.0_121/bin/java -Dserver.port=$LISTEN_PORT -Dserver.tomcat.access-log-enabled=true -Dserver.tomcat.access-log-pattern="%h %l %u %t '%r' %s %b %D" -Dserver.tomcat.basedir=$SEARCH_ENGINE_LOG_DIR -jar $SEARCH_ENGINE_APP_DIR/hypers-search-engine-1.0.0-SNAPSHOT.jar --spring.config.location=classpath:/application.properties,classpath:/collection-filtered-items.yaml >> $SEARCH_ENGINE_LOG_DIR/app_log_$CURRENT_DATE &

EXECUTION_PID=$!
echo $EXECUTION_PID > $SEARCH_ENGINE_PID

echo 'Application started and Wait for application execution finish'
echo "Application pid is $EXECUTION_PID"

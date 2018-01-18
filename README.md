Hypers Search Engine
---

### Running Solr
```
:solr> cd example
:example> java -jar start.jar
:example> cd exampledocs
:exampledocs> java -jar post.jar *.xml
```

Access via [localhost:8983/solr/](http://localhost:8983/solr/#/collection1)

### Running Showcase

```
search-engine> nohup java -Dserver.port=10020 -Dserver.tomcat.access-log-enabled=true -Dserver.tomcat.access-log-pattern="%h %l %u %t '%r' %s %b %D" -Dserver.tomcat.basedir=./logs -jar target/hypers-search-engine-1.0.0-SNAPSHOT.jar >> ./logs/app_log_930 &

```

Access via [http://127.0.0.1:10020/solr/search/request?collection=search&query=name:long&timeRange="thisyear"&aggregateField=timestamp&pageSize=3](http://127.0.0.1:10020/solr/search/request?collection=search&query=name:long&timeRange="thisyear"&aggregateField=timestamp&pageSize=3)

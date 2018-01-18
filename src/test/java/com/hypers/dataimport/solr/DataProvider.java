package com.hypers.dataimport.solr;

import java.net.MalformedURLException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;

/**
 * @author long
 * @since 17-10-11
 */
public class DataProvider {

  public static CloudSolrServer cloudSolrServer(String zkHost) {
    CloudSolrServer server = null;
    try {
      server = new CloudSolrServer(zkHost);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return server;
  }

  public static SolrUpdater updater(String zkHost) {
    SolrUpdater updater = new SolrUpdater(DataProvider.cloudSolrServer(zkHost));
    updater.setMapper(new TempFieldMapper());
    return updater;
  }

  public static LineParser lineParser() {
    return new TempLineParser();
  }

  public static LogResolver logResolver() {
    LogResolver logResolver = new LogResolver();
    logResolver.setParser(lineParser());
    return logResolver;
  }
}

package com.hypers.dataimport.solr.config;

import com.hypers.dataimport.solr.SolrUpdater;
import java.net.MalformedURLException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author long
 * @since 17-10-30
 */
@Configuration
public class DataImportContext {

  @Value("${zk.host}")
  private String zkHost;

  @Bean
  public CloudSolrServer cloudSolrServer() throws MalformedURLException {
    return new CloudSolrServer(zkHost);
  }

  @Bean
  public SolrUpdater solrUpdater() throws MalformedURLException {
    return new SolrUpdater(cloudSolrServer());
  }
}

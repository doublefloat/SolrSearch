package com.hypers.search.config;

import com.hypers.search.utils.collection.DynamicFieldFilter;
import com.hypers.search.utils.collection.FieldTypeFinder;
import com.hypers.search.utils.collection.InnerRangeFacetFinder;
import com.hypers.search.utils.collection.RangeFacetFinder;
import java.net.MalformedURLException;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hypers.search.utils.collection.CollectionFinder;
import com.hypers.search.utils.collection.FieldFacetFinder;
import com.hypers.search.utils.collection.FieldFinder;

@Configuration
@EnableConfigurationProperties(FieldFilterProperties.class)
public class SearchContext {

  @Value("${solr.host}")
  private String solrHost;
  @Value("${zk.host}")
  private String zkHost;
  @Value("${filter.source}")
  private String filterSource;

  @Bean
  public CloudSolrServer solrServer() throws MalformedURLException {
    return new CloudSolrServer(zkHost);
  }

  @Bean
  public FieldFinder fieldFinder() throws MalformedURLException {
    return new FieldFinder(solrServer());
  }

  @Bean
  public FieldFacetFinder facetFieldFinder() throws MalformedURLException {
    return new FieldFacetFinder(solrServer());
  }

  @Bean
  public RangeFacetFinder rangeFacetFinder() throws MalformedURLException {
    return new RangeFacetFinder(solrServer());
  }

  @Bean
  public InnerRangeFacetFinder innerRangeFacetFinder() throws MalformedURLException {
    return new InnerRangeFacetFinder(solrServer());
  }

  @Bean
  public CollectionFinder collectionFinder() throws MalformedURLException {
    return new CollectionFinder(solrServer());
  }

  @Bean
  public DynamicFieldFilter dynamicFieldFilter() throws MalformedURLException {
    return new DynamicFieldFilter(solrServer());
  }

  @Bean
  public FieldTypeFinder fieldTypeFinder() throws MalformedURLException {
    return new FieldTypeFinder(solrServer());
  }
}

package com.hypers.search.utils.collection;

import java.util.HashSet;
import java.util.Set;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.ZkStateReader;

/**
 * @author long
 * @since 17-8-24
 */
public class CollectionFinder {
  private CloudSolrServer server;

  public CollectionFinder(CloudSolrServer server) {
    this.server = server;
  }

  public void setServer(CloudSolrServer server) {
    this.server = server;
  }

  /**
   * Get all collections that include active and inactive
   */
  public Set<String> getCollections() {
    // TODO
    ClusterState clusterState=getClusterState();
    Set<String> collections = clusterState.getCollections();

    return collections;
  }

  ClusterState getClusterState(){
    CloudSolrServer solrServer = getSolrServer();
    solrServer.connect();
    ZkStateReader zkStateReader = solrServer.getZkStateReader();
    return zkStateReader.getClusterState();
  }

  public Set<String> getActiveCollections() {
    Set<String> collections = new HashSet<>();
    SolrQuery query = new SolrQuery();
    query.set("q", "*");
    query.set("rows", "0");

    for (String collectionName : getCollections()) {
      query.set("collection", collectionName);
      try {
        server.query(query);
        collections.add(collectionName);
      } catch (SolrServerException | SolrException e) {
        continue;
      }
    }
    return collections;
  }

  public CloudSolrServer getSolrServer() {
    return server;
  }
}

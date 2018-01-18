package com.hypers.search.utils.collection;

import com.hypers.search.utils.http.BaseHttpRequest;
import com.hypers.search.utils.http.HttpGetRequest;
import com.hypers.search.utils.http.HttpUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.cloud.ClusterState;

/**
 * @author long
 * @since 17-12-18
 */
public abstract class AbstractHttpFinder {

  private CloudSolrServer solrServer;

  protected AbstractHttpFinder(CloudSolrServer solrServer) {
    this.solrServer = solrServer;
  }

  protected String getActiveHost() {
    ClusterState clusterState = getClusterState(this.solrServer);
    Set<String> nodes = clusterState.getLiveNodes();
    Set<String> host = new HashSet<String>(nodes.size());

    for (String node : nodes) {
      host.add(node.substring(0, node.indexOf('_')));
    }
    return host.iterator().next();
  }

  ClusterState getClusterState(CloudSolrServer server) {
    server.connect();
    return server.getZkStateReader().getClusterState();
  }

  protected String execute(String url) {
    String host = getActiveHost();
    URI executeUri;
    try {
      executeUri = HttpUtils
          .URIBuilder("http", host, url).build();
    } catch (URISyntaxException e) {
      throw new SecurityException(
          "Uri builder error before executing. e: " + e + "e.message: " + e.getMessage());
    }

    HttpUtils.validURI(executeUri);

    try {
      BaseHttpRequest httpRequest = new HttpGetRequest();
      return httpRequest.process(executeUri, true);
    } catch (IOException e) {
      throw new SecurityException("Http request transmit error while executing, check it. e: " + e
          + "e.message: " + e.getMessage());
    }
  }
}

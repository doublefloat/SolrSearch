package com.hypers.search.service;

import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.search.CommonData;
import com.hypers.search.dao.CustomSolrRepository;
import com.hypers.search.model.request.SolrDocRequest;
import java.net.MalformedURLException;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-11-22
 */
public class SolrSearchServiceImplV2Test {

  static SolrSearchServiceImplV2 searchService = new SolrSearchServiceImplV2();

  static {
    try {
      searchService.setSolrServer(CommonData.getSolrServer());
    } catch (MalformedURLException e) {
    }
  }

  @Test
  public void testQuery() {
    SolrDocRequest request = CommonData.solrDocRequest();
    ResponseWrapper result = searchService.query(request);
  }
}

package com.hypers.search.utils.collection;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.CommonData;
import com.hypers.search.model.FacetParas;
import java.net.MalformedURLException;
import java.util.Map;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-8-25
 */
public class FieldFacetFinderTest {

  @DataProvider(name = "facetFieldFinder")
  public Object[][] getFacetFieldFinder() throws MalformedURLException {
    CloudSolrServer server = CommonData.getSolrServer();
    FieldFacetFinder finder = new FieldFacetFinder(server);

    return new Object[][]{{finder}};
  }
//
//  @Test(groups="facetFieldFinder",dataProvider = "facetFieldFinder")
//  public void testAssembleQuery(FacetFieldFinder finder){
//    SolrQuery query=finder.assembleQuery(CommonData.collection(),CommonData.aggregateField(),"count",1);
//
//    Assert.assertNotNull(query);
//  }

  @Test(groups = "facetFieldFinder", dataProvider = "facetFieldFinder")
  public void testGetFacetFields(FieldFacetFinder finder) throws ParameterErrorException {
    FacetParas paras=CommonData.facetParas();
    Map<String, Long> result = finder.getFacedFields(paras);
    Assert.assertTrue(result.size() > 0);
  }

  @Test(groups = "facetFieldFinder", dataProvider = "facetFieldFinder",
      expectedExceptions = ParameterErrorException.class)
  public void testGetFacetFieldsException(FieldFacetFinder finder) throws ParameterErrorException {
    FacetParas paras=CommonData.facetParas();
    paras.setCollection("collection-missed");
    finder.getFacedFields(paras);
  }

  @Test(groups = "facetFieldFinder", dataProvider = "facetFieldFinder")
  public void testGetFacetFields2(FieldFacetFinder finder) throws ParameterErrorException{
    //two new parameters to test
    FacetParas paras=CommonData.facetParas();
    paras.setLimit(10);
    paras.setQuery("costTimeA:*");
    paras.setFilter("channelId:5800");

    Map<String,Long> result=finder.getFacedFields(paras);
    Assert.assertTrue(result.size()<=10);
  }
}

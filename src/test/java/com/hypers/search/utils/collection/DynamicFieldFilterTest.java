package com.hypers.search.utils.collection;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.CommonData;
import java.net.MalformedURLException;
import java.util.Set;
import org.apache.solr.client.solrj.SolrServer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-10-10
 */
public class DynamicFieldFilterTest {
  @DataProvider(name="dynamicFieldFilter")
  public static Object[][] dynamicFieldFilter()throws MalformedURLException{
    SolrServer solrServer= CommonData.getSolrServer();
     return new Object[][]{{new DynamicFieldFilter(solrServer)}};
  }

  @Test(dataProvider="dynamicFieldFilter")
  public void testRetrieveDynamicFields(DynamicFieldFilter filter)throws ParameterErrorException {
    Set<String> flds=filter.retrieveDynamicFields(CommonData.collection(),CommonData.dynamicFields());
    Assert.assertTrue(flds.size()>0);
  }
}

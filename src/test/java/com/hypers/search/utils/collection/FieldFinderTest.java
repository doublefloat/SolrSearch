package com.hypers.search.utils.collection;

import com.hypers.search.CommonData;
import com.hypers.search.model.SolrField;
import com.hypers.common.exception.ParameterErrorException;
import java.net.MalformedURLException;
import java.util.List;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-8-25
 */
public class FieldFinderTest {

  @DataProvider(name = "fieldFinderProvider")
  public Object[][] getFieldFinder() throws MalformedURLException {
    CloudSolrServer server = CommonData.getSolrServer();

    return new Object[][]{{new FieldFinder(server)}};
  }

  @Test(dataProvider = "fieldFinderProvider")
  public void testGetFields(FieldFinder finder) throws ParameterErrorException {
    List<SolrField> fields = finder.getFields(CommonData.collection(), "fields", SolrField.class);

    for (SolrField field : fields) {
      Assert.assertTrue(CommonData.fields().contains(field.getName()));
    }
  }

  @Test(dataProvider = "fieldFinderProvider", expectedExceptions = {ParameterErrorException.class,
      SecurityException.class})
  public void testGetFieldsException(FieldFinder finder) throws ParameterErrorException {
    finder.getFields("collection-missed", "fields", SolrField.class);
  }
}

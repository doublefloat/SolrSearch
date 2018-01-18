package com.hypers.search.utils.collection;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.CommonData;
import com.hypers.search.model.SolrFieldType;

import java.net.MalformedURLException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 17-12-19
 */
public class FieldTypeFinderTest {

  @DataProvider(name = "fieldTypeFinder")
  public static Object[][] fieldTypeFinder() throws MalformedURLException {
    return new Object[][]{{new FieldTypeFinder(CommonData.getSolrServer())}};
  }

  @Test(dataProvider = "fieldTypeFinder")
  public void testGetSolrType(FieldTypeFinder typeFinder) throws ParameterErrorException{
    SolrFieldType type = typeFinder.getSolrType(CommonData.collection(), CommonData.type());
    Assert.assertNotNull(type);
  }

  @Test(dataProvider = "fieldTypeFinder")
  public void testGetSolrTypes(FieldTypeFinder typeFinder) throws ParameterErrorException {
    List<SolrFieldType> types = typeFinder.getSolrTypes(CommonData.collection());
    Set<String> set = new HashSet<>();
    for (SolrFieldType type : types) {
      set.add(type.getClassName());
    }
    for (String type : set) {
      System.out.println(type);
    }
    Assert.assertNotNull(types);
  }
}

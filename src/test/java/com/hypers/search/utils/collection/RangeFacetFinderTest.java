package com.hypers.search.utils.collection;

import com.hypers.search.CommonData;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.AggregationParas;
import com.hypers.search.model.Count;
import java.net.MalformedURLException;
import java.util.List;
import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * @author long
 * @since 2017/9/19
 */
public class RangeFacetFinderTest {

  private static RangeFacetFinder finder = new RangeFacetFinder();

  static {
    try {
      finder.setSolrServer(CommonData.getSolrServer());
    } catch (MalformedURLException e) {
    }
  }

  @Test
  public void testAggregation() throws Exception {
    AggregationParas paras = new AggregationParas();
    paras.setOuterFieldCategory(SolrFieldCategory.DATE);
    assembleDateParameters(paras);

    List<Count> result = finder.aggregate(paras);
    Assert.assertNotNull(result);

  }

  @Test
  public void testAggregation2() throws Exception {
    AggregationParas paras = new AggregationParas();
    assembleDateParameters(paras);
    paras.setOuterFieldCategory(SolrFieldCategory.NUMBER);
    paras.setCollection(CommonData.collection());
    paras.setOuterField("_id");
    paras.setOuterStart("0");
    paras.setOuterEnd("10000");
    paras.setOuterGap("1000");
    paras.setQuery("*:*");

    List<Count> result = finder.aggregate(paras);

    Assert.assertNotNull(result);
  }

  private void assembleDateParameters(AggregationParas paras) {
    paras.setCollection(CommonData.collection());
    paras.setQuery("*:*");
    paras.setFilters("*");

    paras.setOuterField(CommonData.aggregateField());
//    paras.setRangeStart("2017-02-02T18:22:01.000Z");
//    paras.setRangeEnd("2018-02-02T18:22:01.000Z");
    paras.setOuterStart("2017-02-02T18:22:01.001Z");
    paras.setOuterEnd("2018-02-02T18:22:01.001Z");
    paras.setOuterGap("+4MONTHS");
  }

}

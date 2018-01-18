package com.hypers.search.utils;

import com.hypers.search.model.AggregationParas;
import com.hypers.search.model.FacetParas;
import com.hypers.search.model.ParasMediator;

/**
 * @author long
 * @since 17-11-7
 */
public class FacetParasMapper implements Mapper<AggregationParas, FacetParas> {

  @Override
  public FacetParas mapping(AggregationParas input) {
    FacetParas paras = new FacetParas();
    paras.setCollection(input.getCollection());
    paras.setQuery(input.getQuery());
    paras.setFilter(input.getFilters());
    fillFacetField(paras, input);
//    paras.setLimit(-1);
    paras.setCount(1);
    paras.setSort("count");
    return paras;
  }

  protected void fillFacetField(FacetParas facetParas, AggregationParas paras) {
    facetParas.setField(paras.getOuterField());
    facetParas.setLimit(paras.getOuterLimit());
  }
}

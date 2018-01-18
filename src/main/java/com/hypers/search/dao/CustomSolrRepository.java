package com.hypers.search.dao;

import com.hypers.common.exception.OutLimitException;
import com.hypers.search.model.ParasMediator;
import com.hypers.search.model.SolrField;
import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.model.FacetParas;
import com.hypers.search.model.Count;
import com.hypers.search.model.AggregationParas;
import com.hypers.search.model.SolrFieldType;
import java.util.List;
import java.util.Map;

/**
 * @author long
 * @since 17-8-10
 */
public interface CustomSolrRepository {

  List<String> getCollections();

  List<SolrField> getDynamicFields(String collection) throws ParameterErrorException;

  Map<String, Long> getFieldFacetInfo(FacetParas paras, boolean asc) throws ParameterErrorException;

  List<Count> aggregate(AggregationParas paras) throws ParameterErrorException, OutLimitException;

  List<SolrField> getSolrFields(String collection) throws ParameterErrorException;

  List<SolrFieldType> getSolrTypes(String collection) throws ParameterErrorException;

  SolrFieldType getSolrType(String collection, String name) throws ParameterErrorException;
}

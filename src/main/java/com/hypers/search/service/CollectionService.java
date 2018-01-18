package com.hypers.search.service;

import com.hypers.search.model.request.FacetInfoRequest;

/**
 * @author long
 * @since 17-8-15
 */
public interface CollectionService {

  <T> T getCollections();

  <T> T getFields(String collection, boolean asc);

  <T> T getFacetFields(FacetInfoRequest request);

  <T> T getAggregateFields(String collection);

  <T> T getCategorizedFields(String collection);

  <T> T getStoredFields(String collection);

}
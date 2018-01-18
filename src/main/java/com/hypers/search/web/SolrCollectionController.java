package com.hypers.search.web;

import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.search.model.request.FacetInfoRequest;
import com.hypers.search.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author long
 * @since 2017/9/25
 */
@RestController
public class SolrCollectionController {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(SolrCollectionController.class);

  private CollectionService collectionService;

  @Autowired
  public void setCollectionService(CollectionService collectionService) {
    this.collectionService = collectionService;
  }

  @RequestMapping("/solr/search/collection")
  @SuppressWarnings("rawtypes")
  public ResponseWrapper searchCollections() {
    log.info("Trace: SolrCollectionController.searchCollections");
    return collectionService.getCollections();
  }

  @RequestMapping("/solr/search/collection/field")
  @SuppressWarnings("rawtypes")
  public ResponseWrapper getFieldsOfCollection(@RequestParam String collection,
      @RequestParam(defaultValue = "true") boolean asc) {
    log.info("Trace: SolrCollectionController.getFieldsOfCollection, collection: + " + collection
        + "; asc: " + asc);
    return collectionService.getFields(collection, asc);
  }

  @RequestMapping("/solr/search/collection/field/detail")
  @SuppressWarnings("rawtypes")
  public ResponseWrapper getFacetFieldOfCollection(
      @RequestParam String collection,
      @RequestParam String field,
      @RequestParam(required = false) Integer count,
      @RequestParam(required = false) Integer num,    //-1 to request all
      @RequestParam(required = false) Boolean asc) {
    log.info("Trace: SolrCollectionController.getFacetFieldOfCollection, collection: + "
        + collection + "; asc: " + asc + "; count: " + count + "; num: " + num);
    FacetInfoRequest request = new FacetInfoRequest();
    request.setCollection(collection);
    request.setField(field);
    request.setCount(count);
    request.setLimit(num);
    request.setAsc(asc);
    return collectionService.getFacetFields(request);
  }

  @RequestMapping("/solr/search/collection/field/aggregate")
  public ResponseWrapper getAggregateField(
      @RequestParam String collection) {
    log.info("SolrCollectionController.getAggregateField,collection:" + collection);
    return collectionService.getAggregateFields(collection);
  }

  @RequestMapping("/solr/search/collection/field/categorized")
  public ResponseWrapper getCategorizedFields(
      @RequestParam String collection) {
    log.info("SolrCollectionController.getCategorizedFields,collection:" + collection);
    return collectionService.getCategorizedFields(collection);
  }

  @RequestMapping("/solr/search/collection/field/stored")
  public ResponseWrapper getStoredFields(@RequestParam String collection) {
    log.info("SolrCollectionController.getStoredFields,collection:" + collection);
    return collectionService.getStoredFields(collection);
  }
}
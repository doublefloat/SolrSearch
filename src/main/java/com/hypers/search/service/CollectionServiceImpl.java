package com.hypers.search.service;

import com.hypers.common.SimpleTransfer;
import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.common.wrapper.ResultWrapper;
import com.hypers.search.config.FieldFilterProperties;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.Count;
import com.hypers.search.model.FacetParas;
import com.hypers.search.model.SolrField;
import com.hypers.search.model.SolrFieldType;
import com.hypers.search.model.request.FacetInfoRequest;
import com.hypers.search.utils.CountListMapper;
import com.hypers.search.utils.Mapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypers.common.StatusCode;
import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.dao.CustomSolrRepository;

/**
 * @author long
 * @since 17-8-15
 */
@Service
public class CollectionServiceImpl implements CollectionService {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(CollectionServiceImpl.class);

  private CustomSolrRepository customSolrRepository;

  private static final int DEFAULT_MIN_COUNT = 1;

  private static final int DEFAULT_LIMIT = 100;

  private static final String DEFAULT_SORT = "count";

  private static final Mapper<Map<String, Long>, List<Count>> mapper = new CountListMapper();

  private SolrTypeMapper typeMapper = new SolrTypeMapper();

  private SolrFieldFilter fieldFilter;

  @Autowired
  public void setFieldFilterProperties(FieldFilterProperties properties) {
    this.fieldFilter = new SolrFieldFilter(properties.getFilteredItems());
    System.out.println(properties.getFilteredItems());
  }

  @Autowired
  public void setCustomSolrRepository(CustomSolrRepository customSolrRepository) {
    this.customSolrRepository = customSolrRepository;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ResponseWrapper getCollections() {
    return new ResultWrapper()
        .wrapResult(StatusCode.SUCCESS, customSolrRepository.getCollections());
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ResponseWrapper getFields(String collection, boolean asc) {
    List<SolrField> solrFields;
    try {
      solrFields = customSolrRepository.getSolrFields(collection);
    } catch (SecurityException | ParameterErrorException e) {
      return new MessageWrapper()
          .wrapMessage(StatusCode.ERROR, "Collection: " + collection + " doesn't exist",
              SimpleTransfer.toMap("collection", collection, "asc", asc));
    }

    List<String> fields = filterToStringList(solrFields);
    if (!asc) {
      fields.sort((s1, s2) -> (s2.compareTo(s1)));
    }

    return new ResultWrapper().wrapResult(StatusCode.SUCCESS, fields,
        SimpleTransfer.toMap("collection", collection, "asc", asc));
  }

  private List<String> filterToStringList(List<SolrField> solrFieldList) {
    List<String> result = new ArrayList<>();
    for (SolrField solrField : solrFieldList) {
      if (solrField.getStored() && solrField.getIndexed()) {
        if (solrField.getUniqueKey() == null || !solrField.getUniqueKey()) {
          if (!solrField.getName().startsWith("_")) {
            result.add(solrField.getName());
          }
        }
      }
    }
    return result;
  }

  private FacetParas wrapFacetParas(FacetInfoRequest request) {
    FacetParas paras = new FacetParas();
    paras.setCollection(request.getCollection());
    paras.setField(request.getField());
    if (request.getSortType() == null) {
      paras.setSort(DEFAULT_SORT);
    } else {
      paras.setSort(request.getSortType());
    }

    if (request.getCount() == null) {
      paras.setCount(DEFAULT_MIN_COUNT);
    } else {
      paras.setCount(request.getCount());
    }

    if (request.getLimit() == null) {
      paras.setLimit(DEFAULT_LIMIT);
    } else {
      paras.setLimit(request.getLimit());
    }

    return paras;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ResponseWrapper getFacetFields(FacetInfoRequest request) {
    log.info("Trace: CollectionServiceImpl.getFacetFields; collection: " + request.toString());
    Map<String, Long> fields;
    try {
      fields = customSolrRepository.getFieldFacetInfo(wrapFacetParas(request),
          request.getAsc() == null ? false : request.getAsc());
    } catch (ParameterErrorException e) {
      log.error("Collection: " + request.getCollection() + " or field: " + request.getField()
          + " don't exist");
      return new MessageWrapper().wrapMessage(StatusCode.ERROR,
          ("Collection: " + request.getCollection() + " or field: " + request.getField()
              + " don't exist"), SimpleTransfer.toMap(request));
    }
    log.trace("CollectionServiceImpl.getFacetFields; FacetFieldInfo: " + fields);

    return new ResultWrapper()
        .wrapResult(StatusCode.SUCCESS, toCountList(fields), SimpleTransfer.toMap(request));
  }

  private List<Count> toCountList(Map<String, Long> pairs) {
    return mapper.mapping(pairs);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ResponseWrapper getAggregateFields(String collection) {
    List<SolrField> fieldList;
    try {
      fieldList = toCategorizedFields(customSolrRepository.getSolrFields(collection),
          customSolrRepository.getSolrTypes(collection));
    } catch (ParameterErrorException e) {
      return new MessageWrapper()
          .wrapMessage(StatusCode.ERROR, "Collection: " + collection + "doesn't exist",
              SimpleTransfer.toMap("collection", collection));
    }
    List<SolrField> result = new ArrayList<>();
    for (SolrField field : fieldList) {
      if (field.getType().equals(SolrFieldCategory.DATE.toString())) {
        result.add(field);
      }
    }

    return new ResultWrapper().wrapResult(StatusCode.SUCCESS, result,
        SimpleTransfer.toMap("collection", collection));
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ResponseWrapper getCategorizedFields(String collection) {
    List<SolrField> solrFieldList;
    List<SolrFieldType> fieldTypes;
    try {
      solrFieldList = customSolrRepository.getSolrFields(collection);
      fieldTypes = customSolrRepository.getSolrTypes(collection);
    } catch (ParameterErrorException e) {
      return new MessageWrapper()
          .wrapMessage(StatusCode.ERROR, "Collection: " + collection + "doesn't exist",
              SimpleTransfer.toMap("collection", collection));
    }

    List<SolrField> result = toCategorizedFields(solrFieldList, fieldTypes);
    result = doFilter(collection, result);
    return new ResultWrapper()
        .wrapResult(StatusCode.SUCCESS, result, SimpleTransfer.toMap("collection", collection));
  }

  private List<SolrField> toCategorizedFields(List<SolrField> solrFieldList,
      List<SolrFieldType> types) {
    for (SolrField field : solrFieldList) {

      SolrFieldCategory category = getCategory(types, field);
      field.setType(category == null ? null : category.toString());
    }
    return solrFieldList;
  }

  private List<SolrField> doFilter(String collection, List<SolrField> fieldList) {
    Set<String> filterItems = fieldFilter.getCrabSet(collection);
    List<SolrField> result = new ArrayList<>();
    for (SolrField field : fieldList) {
      if (!filterItems.contains(field.getName())) {
        result.add(field);
      }
    }
    return result;
  }

  private SolrFieldCategory getCategory(List<SolrFieldType> types, SolrField field) {
    for (SolrFieldType type : types) {
      if (!type.getName().equals(field.getType())) {
        continue;
      }
      return typeMapper.getFieldCategory(type.getClassName());
    }
    return null;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public ResponseWrapper getStoredFields(String collection) {
    List<SolrField> solrFieldList;
    try {
      solrFieldList = toCategorizedFields(customSolrRepository.getSolrFields(collection),
          customSolrRepository.getSolrTypes(collection));
    } catch (ParameterErrorException e) {
      return new MessageWrapper()
          .wrapMessage(StatusCode.ERROR, "can't find collection: " + collection,
              SimpleTransfer.toMap("collection", collection));
    }

    List<String> result = new ArrayList<>();
    for (SolrField solrField : solrFieldList) {
      if (solrField.getStored()) {
        result.add(solrField.getName());
      }
    }

    return new ResultWrapper()
        .wrapResult(StatusCode.SUCCESS, result, SimpleTransfer.toMap("collection", collection));
  }

  private static final class SolrTypeMapper {

    private static Map<String, SolrFieldCategory> pairs;

    static {
      pairs = new HashMap<>();
      pairs.put("solr.TextField", SolrFieldCategory.TEXT);
      pairs.put("solr.LatLonType", SolrFieldCategory.NUMBER);
      pairs.put("solr.TrieLongField", SolrFieldCategory.NUMBER);
      pairs.put("solr.DateField", SolrFieldCategory.DATE);
      pairs.put("solr.TrieDateField", SolrFieldCategory.DATE);
      pairs.put("solr.IntField", SolrFieldCategory.NUMBER);
      pairs.put("solr.DoubleField", SolrFieldCategory.NUMBER);
      pairs.put("solr.SpatialRecursivePrefixTreeFieldType", SolrFieldCategory.GEO);
      pairs.put("solr.FloatField", SolrFieldCategory.NUMBER);
      pairs.put("solr.RandomSortField", SolrFieldCategory.NONE);
      pairs.put("solr.LongField", SolrFieldCategory.NUMBER); pairs.put("solr.StrField", SolrFieldCategory.TEXT);
      pairs.put("solr.BinaryField", SolrFieldCategory.BINARY);
      pairs.put("solr.BoolField", SolrFieldCategory.BOOL);
      pairs.put("solr.CurrencyField", SolrFieldCategory.CURRENCY);
      pairs.put("solr.TrieIntField", SolrFieldCategory.NUMBER);
      pairs.put("solr.PointType", SolrFieldCategory.GEO);
      pairs.put("solr.TrieFloatField", SolrFieldCategory.NUMBER);
    }

    SolrFieldCategory getFieldCategory(String solrClass) {
      return pairs.get(solrClass);
    }
  }

  private final class SolrFieldFilter {

    private Map<String, String> items;
    private String separator = ",";

    public SolrFieldFilter(Map<String, String> filteredItems) {
      this.items = filteredItems;
    }

    void setSeparator(String separator) {
      this.separator = separator;
    }

    Set<String> getCrabSet(String collection) {
      String value = items.get(collection);

      if (value == null) {
        return Collections.emptySet();
      }
      String[] items = value.split(separator);
      Set<String> result = new HashSet<>(items.length);
      result.addAll(Arrays.asList(items));
      return result;
    }
  }
}

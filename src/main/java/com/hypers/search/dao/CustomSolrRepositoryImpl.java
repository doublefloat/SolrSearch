package com.hypers.search.dao;

import com.hypers.common.exception.OutLimitException;
import com.hypers.search.enums.AggregationType;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.FacetParas;
import com.hypers.search.model.ParasMediator;
import com.hypers.search.model.SolrFieldType;
import com.hypers.search.utils.CommonTask;
import com.hypers.search.utils.CountListMapper;
import com.hypers.search.utils.FacetParasMapper;
import com.hypers.search.utils.FilterResolver;
import com.hypers.search.utils.Mapper;
import com.hypers.search.utils.collection.DynamicFieldFilter;
import com.hypers.search.utils.collection.FieldTypeFinder;
import com.hypers.search.utils.collection.InnerRangeFacetFinder;
import com.hypers.search.utils.date.SolrDateUnit;
import com.hypers.search.utils.date.SolrDateUtil;
import com.hypers.search.utils.date.TimeTransfer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.model.SolrField;
import com.hypers.search.utils.collection.CollectionFinder;
import com.hypers.search.utils.collection.FieldFacetFinder;
import com.hypers.search.utils.collection.FieldFinder;
import com.hypers.search.model.Count;
import com.hypers.search.utils.collection.RangeFacetFinder;
import com.hypers.search.model.AggregationParas;

import static com.hypers.search.utils.date.SolrDateUtil.formatAsUTC;
import static com.hypers.search.utils.date.SolrDateUtil.escapeColons;

/**
 * @author long
 * @since 17-8-10
 */
@Repository
public class CustomSolrRepositoryImpl implements CustomSolrRepository {

  private FieldFinder fieldFinder;

  private FieldFacetFinder facetFieldFinder;

  private CollectionFinder collectionFinder;

  private RangeFacetFinder rangeFacetFinder;

  private RangeFacetFinder innerRangeFacetFinder;

  private DynamicFieldFilter dynamicFieldFilter;

  private FieldTypeFinder fieldTypeFinder;

  private Mapper<Map<String, Long>, List<Count>> countListMapper = new CountListMapper();

  private Mapper<AggregationParas, FacetParas> outerFacetParasMapper = new FacetParasMapper();

  private Mapper<AggregationParas, FacetParas> innerFacetParasMapper = new FacetParasMapper() {
    @Override
    protected void fillFacetField(FacetParas facetParas, AggregationParas paras) {
      ParasMediator mediator = (ParasMediator) paras;
      facetParas.setField(mediator.getInnerField());
      facetParas.setLimit(mediator.getInnerLimit());
    }
  };

  @Autowired
  public void setFieldTypeFinder(FieldTypeFinder fieldTypeFinder) {
    this.fieldTypeFinder = fieldTypeFinder;
  }

  @Autowired
  public void setDynamicFieldFilter(
      DynamicFieldFilter dynamicFieldFilter) {
    this.dynamicFieldFilter = dynamicFieldFilter;
  }

  @Autowired
  public void setRangeFacetFinder(RangeFacetFinder rangeFacetFinder) {
    this.rangeFacetFinder = rangeFacetFinder;
  }

  @Autowired
  public void setInnerRangeFacetFinder(InnerRangeFacetFinder innerRangeFacetFinder) {
    this.innerRangeFacetFinder = innerRangeFacetFinder;
  }

  @Autowired
  public void setFieldFinder(FieldFinder fieldFinder) {
    this.fieldFinder = fieldFinder;
  }

  @Autowired
  public void setFacetFieldFinder(FieldFacetFinder facetFieldFinder) {
    this.facetFieldFinder = facetFieldFinder;
  }

  @Autowired
  public void setCollectionFinder(CollectionFinder collectionFinder) {
    this.collectionFinder = collectionFinder;
  }

  @Override
  public List<String> getCollections() {
    Set<String> collections = collectionFinder.getCollections();
    return new ArrayList<>(collections);
  }

  /**
   * @param fieldType Using 'fields' to get information in label '<field>' and 'dynamicfields' to
   * get information in label '<dynamicfields>', which is defined in schema.xml
   */
  private List<String> getFieldNames(String collection, String fieldType)
      throws ParameterErrorException {
    List<SolrField> fields = fieldFinder.getFields(collection, fieldType, SolrField.class);

    List<String> fs = new ArrayList<>(fields.size());
    for (SolrField field : fields) {
      fs.add(field.getName());
    }

    return fs;
  }

  @Override
  public Map<String, Long> getFieldFacetInfo(FacetParas paras, boolean asc)
      throws ParameterErrorException {
    Map<String, Long> result = facetFieldFinder.getFacedFields(paras);

    if (paras.getSort().equals("index") && !asc) {
      // TODO invert the order of result
    }

    return result;
  }

  @Override
  public List<Count> aggregate(AggregationParas paras)
      throws ParameterErrorException, OutLimitException {
    switch (paras.getType()) {
      case VALUE:
        return aggregateByValue(paras);
      case GROUP:
        return aggregateByGroup(paras);
      case GROUP_VALUE:
        return aggregateByGroupValue((ParasMediator) paras);
      case GROUP_GROUP:
        return aggregateByGroupGroup((ParasMediator) paras);
      case VALUE_GROUP:
        return aggregateByValueGroup((ParasMediator) paras);
      case VALUE_VALUE:
        return aggregateByValueValue((ParasMediator) paras);
      default:
        throw new UnsupportedOperationException();
    }
  }

  protected List<Count> aggregateByValue(AggregationParas paras) throws ParameterErrorException {
    Map<String, Long> pairs = facetFieldFinder.getFacedFields(outerFacetParasMapper.mapping(paras));
    return countListMapper.mapping(pairs);
  }

  protected List<Count> aggregateByGroup(AggregationParas paras)
      throws ParameterErrorException, OutLimitException {
    verifyOuterGroupLimit(paras);
//    paras.setFilters(paras.getFilters() + " AND " + paras.getInnerField() + ":*");
    if (paras.getOuterFieldCategory() == SolrFieldCategory.DATE) {
      paras.setOuterStart(SolrDateUtil.
          formatAsUTC(new Date(Long.valueOf(paras.getOuterStart()))));
      paras.setOuterEnd(SolrDateUtil.
          formatAsUTC(new Date(Long.valueOf(paras.getOuterEnd()))));
      paras.setOuterGap("+" + Long.valueOf(paras.getOuterGap()) / TimeTransfer.SECOND_MILI
          + SolrDateUnit.SECONDS);
    }
    return rangeFacetFinder.aggregate(paras);
  }

  protected List<Count> aggregateByValueValue(ParasMediator paras)
      throws ParameterErrorException, OutLimitException {
    List<String> fieldGroups = groupOuterFieldAsFilter(paras);
    List<Count> countList = new ArrayList<>(fieldGroups.size());
    String filters = paras.getFilters();
    FacetParas facetParas = innerFacetParasMapper.mapping(paras);
    for (String fieldGroup : fieldGroups) {
      facetParas.setFilter(filters + " AND " + paras.getOuterField()
          + ":" + fieldGroup);
      List<Count> counts = countListMapper.mapping(facetFieldFinder.getFacedFields(facetParas));
      Count count = new Count();
      count.setName(fieldGroup);
      count.setCountList(counts);
      countList.add(count);
    }
    return countList;
  }

  protected List<Count> aggregateByValueGroup(ParasMediator paras)
      throws ParameterErrorException, OutLimitException {
    verifyInnerGroupLimit(paras);

    List<String> fieldGroups = groupOuterFieldAsFilter(paras);
    List<Count> countList = new ArrayList<>(fieldGroups.size());

    reviseInnerAggregatePara(paras);

    String filters = paras.getFilters();
    for (String fieldGroup : fieldGroups) {
      paras.setFilters(filters + " AND " + paras.getOuterField() + ":" +
          CommonTask
              .escape(fieldGroup, FilterResolver.particularCharacters, 0, fieldGroup.length()));
      List<Count> counts = innerRangeFacetFinder.aggregate(paras);
      Count count = new Count();
      count.setName(fieldGroup);
      count.setCountList(counts);
      countList.add(count);
    }
    return countList;
  }

  protected List<Count> aggregateByGroupValue(ParasMediator paras)
      throws ParameterErrorException, OutLimitException {
    verifyOuterGroupLimit(paras);

    List<String> fieldGroups = groupOuterFieldAsFilter(paras);
    List<String> names = toNameOfCount(paras);
    List<Count> countList = new ArrayList<>(fieldGroups.size());
    int i;
    String filters = paras.getFilters();
    FacetParas facetParas = innerFacetParasMapper.mapping(paras);
    for (i = 0; i < fieldGroups.size(); i++) {
      facetParas.setFilter(filters + " AND " + paras.getOuterField()
          + ":" + fieldGroups.get(i));
      List<Count> counts = countListMapper.mapping(facetFieldFinder.getFacedFields(facetParas));
      Count count = new Count();
      count.setName(names.get(i));
      count.setCountList(counts);
      countList.add(count);
    }
    countList.add(new Count(names.get(i), -1l));
    return countList;
  }

  protected List<Count> aggregateByGroupGroup(ParasMediator paras)
      throws ParameterErrorException, OutLimitException {
    verifyOuterGroupLimit(paras);
    verifyInnerGroupLimit(paras);

    List<String> fieldGroups = groupOuterFieldAsFilter(paras);
    List<String> names = toNameOfCount(paras);
    List<Count> countList = new ArrayList<>(fieldGroups.size());

    reviseInnerAggregatePara(paras);
    String filters = paras.getFilters();
    int i;
    for (i = 0; i < fieldGroups.size(); i++) {
      paras.setFilters(filters + " AND " + paras.getOuterField()
          + ":" + fieldGroups.get(i));
      List<Count> counts = innerRangeFacetFinder.aggregate(paras);
      Count count = new Count();
      count.setName(names.get(i));
      count.setCountList(counts);
      countList.add(count);
    }
    countList.add(new Count(names.get(i), -1l));
    return countList;
  }

  private void verifyInnerGroupLimit(ParasMediator mediator)
      throws ParameterErrorException, OutLimitException {
    try {
      Long start = Long.valueOf(mediator.getInnerStart());
      Long end = Long.valueOf(mediator.getInnerEnd());
      Long gap = Long.valueOf(mediator.getInnerGap());
      int limit = Integer.valueOf(mediator.getInnerLimit());
      verifyLimit(start, end, gap, limit);
    } catch (NumberFormatException e) {
      throw new ParameterErrorException(e.getMessage(), e);
    }
  }

  private void verifyOuterGroupLimit(AggregationParas mediator)
      throws ParameterErrorException, OutLimitException {
    try {
      int limit = Integer.valueOf(mediator.getOuterLimit());
      Long start = Long.valueOf(mediator.getOuterStart());
      Long end = Long.valueOf(mediator.getOuterEnd());
      Long gap = Long.valueOf(mediator.getOuterGap());
      verifyLimit(start, end, gap, limit);
    } catch (NumberFormatException e) {
      throw new ParameterErrorException(e.getMessage(), e);
    }
  }

  private void reviseInnerAggregatePara(ParasMediator paras) {
    switch (paras.getInnerFieldCategory()) {
      case NUMBER:
        //do nothing
        break;
      case DATE:
        paras.setInnerStart(SolrDateUtil.
            formatAsUTC(new Date(Long.valueOf(paras.getInnerStart()))));
        paras.setInnerEnd(SolrDateUtil.
            formatAsUTC(new Date(Long.valueOf(paras.getInnerEnd()))));
        //TODO the largest error distance is 999 milliseconds
        paras.setInnerGap("+" + Long.valueOf(paras.getInnerGap()) / TimeTransfer.SECOND_MILI
            + SolrDateUnit.SECONDS);
        break;
    }
  }

  private List<String> toNameOfCount(ParasMediator paras) {
    Long start = Long.valueOf(paras.getOuterStart());
    Long end = Long.valueOf(paras.getOuterEnd());
    Long gap = Long.valueOf(paras.getOuterGap());
    Long i;
    List<String> names = new ArrayList<>();
    for (i = start; i + gap < end; i += gap) {
      names.add(String.valueOf(i));
    }
    names.add(String.valueOf(i));
    names.add(String.valueOf(end));
    return names;
  }

  private List<String> groupOuterFieldAsFilter(ParasMediator paras)
      throws ParameterErrorException {
    List<String> result = null;
    AggregationType type = paras.getType();
    if (type == AggregationType.GROUP_VALUE || type == AggregationType.GROUP_GROUP) {
      Long start = Long.valueOf(paras.getOuterStart());
      Long end = Long.valueOf(paras.getOuterEnd());
      Long gap = Long.valueOf(paras.getOuterGap());
      //TODO handle parameter limit
      switch (paras.getOuterFieldCategory()) {
        case DATE:
          result = groupOuterDateField(start, end, gap);
          break;
        case NUMBER:
          result = groupOuterNumberField(start, end, gap);
          break;
        default:
          //never reach here
      }
    } else if (type == AggregationType.VALUE_GROUP || type == AggregationType.VALUE_VALUE) {
      result = groupOuterFieldInValue(paras);
    }
    return result;
  }

  private void verifyLimit(long start, long end, long gap, int limit) throws OutLimitException {
    if (limit != -1 && Math.ceil(new Float(end - start) / gap) > limit) {
      throw new OutLimitException("intervals is out of limit");
    }
  }

  protected List<String> groupOuterFieldInValue(ParasMediator paras)
      throws ParameterErrorException {
    FacetParas facetParas = outerFacetParasMapper.mapping(paras);
    Map<String, Long> pairs = facetFieldFinder.getFacedFields(facetParas);

    return new ArrayList<>(pairs.keySet());
  }

  protected List<String> groupOuterNumberField(long start, long end, long gap) {
    List<String> numberGroups = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    long i;
    for (i = start; i + gap < end; i += gap) {
      sb.append("[");
      sb.append(i);
      sb.append(" TO ");
      sb.append(i + gap);
      sb.append("]");

      numberGroups.add(sb.toString());
      sb.setLength(0);
    }
    sb.append("[");
    sb.append(i);
    sb.append(" TO ");
    sb.append(end);
    sb.append("]");

    numberGroups.add(sb.toString());
    return numberGroups;
  }

  protected List<String> groupOuterDateField(long start, long end, long gap) {
    StringBuilder sb = new StringBuilder();
    List<String> dateGroups = new ArrayList<>();
    long i;
    for (i = start; i + gap < end; i += gap) {
      sb.append("[");
      sb.append(escapeColons(formatAsUTC(new Date(i))));
      sb.append(" TO ");
      sb.append(escapeColons(formatAsUTC(new Date(i + gap))));
      sb.append("]");

      dateGroups.add(sb.toString());
      sb.setLength(0);
    }
    sb.append("[");
    sb.append(escapeColons(formatAsUTC(new Date(i))));
    sb.append(" TO ");
    sb.append(escapeColons(formatAsUTC(new Date(end))));
    sb.append("]");
    dateGroups.add(sb.toString());
    return dateGroups;
  }

  @Override
  public List<SolrField> getDynamicFields(String collection) throws ParameterErrorException {
    List<String> dynamicFieldNames = Collections.emptyList();
    List<String> wildDynamicFields = getFieldNames(collection, FieldFinder.DYNAMIC_FIELD_TYPE);
    if (wildDynamicFields.size() > 0) {
      dynamicFieldNames = new ArrayList<>(
          dynamicFieldFilter.retrieveDynamicFields(collection, wildDynamicFields));
    }
    List<SolrField> dynamicFieldList = fieldFinder
        .getFields(collection, FieldFinder.DYNAMIC_FIELD_TYPE, SolrField.class);
    List<SolrField> realUsedDF = new ArrayList<>(dynamicFieldNames.size());

    for (String fieldName : dynamicFieldNames) {
      String tail = fieldName.substring(fieldName.lastIndexOf("_"), fieldName.length());
      for (SolrField field : dynamicFieldList) {
        if (field.getName().endsWith(tail)) {
          SolrField solrField = new SolrField(field);
          solrField.setName(fieldName);
          realUsedDF.add(solrField);
          break;
        }
      }
    }
    return realUsedDF;
  }

  @Override
  public List<SolrField> getSolrFields(String collection) throws ParameterErrorException {
    List<SolrField> fieldList = fieldFinder
        .getFields(collection, FieldFinder.FIELD_TYPE, SolrField.class);
    List<SolrField> dynamicFieldList = getDynamicFields(collection);
    fieldList.addAll(dynamicFieldList);
    return fieldList;
  }

  @Override
  public SolrFieldType getSolrType(String collection, String name) throws ParameterErrorException {
    return fieldTypeFinder.getSolrType(collection, name);
  }

  @Override
  public List<SolrFieldType> getSolrTypes(String collection) throws ParameterErrorException {
    return fieldTypeFinder.getSolrTypes(collection);
  }
}

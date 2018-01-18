package com.hypers.search.service;

import com.hypers.search.model.request.NestedAggregationReq;
import com.hypers.search.model.request.FlatAggregationReq;
import com.hypers.search.utils.FilterResolver;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypers.common.exception.FormatErrorException;
import com.hypers.common.exception.OutLimitException;
import com.hypers.common.wrapper.ResultWrapper;
import com.hypers.search.dao.CustomSolrRepository;
import com.hypers.search.enums.AggregationType;
import com.hypers.search.enums.SolrFieldCategory;
import com.hypers.search.model.ParasMediator;
import com.hypers.search.utils.date.DateGapMapper;
import com.hypers.common.StatusCode;
import com.hypers.common.exception.ParameterErrorException;
import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.search.model.Count;
import com.hypers.search.model.request.AggregationRequest;
import com.hypers.search.model.request.SolrDocRequest;
import com.hypers.search.model.AggregationParas;
import com.hypers.search.utils.date.TimeRange;
import com.hypers.search.utils.date.SolrDateUtil;

import static com.hypers.search.utils.date.DateGapMapper.mappingToGranularity;
import static com.hypers.search.utils.date.TimeTransfer.parseDateString;
import static com.hypers.common.SimpleTransfer.toMap;

/**
 * @author long
 * @since 17-8-10
 */
@Service("solrSearchService")
public class SolrSearchServiceImpl extends AbstractSolrSearchServiceSkeleton {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(SolrSearchServiceImpl.class);

  private CustomSolrRepository customSolrRepository;

  private final static int DEFAULT_LIMIT = 100;

  @Autowired
  public void setCustomSolrRepository(CustomSolrRepository customSolrRepository) {
    this.customSolrRepository = customSolrRepository;
  }

  protected List<String> resolveFields(String fields) {
    if (fields == null) {
      List<String> rs = new ArrayList<>(1);
      rs.add("*");
      return rs;
    }

    List<String> fls = new ArrayList<>();
    // format of fields is f1,f2,f3
    fls.addAll(Arrays.asList(fields.split(",")));
    return fls;
  }

  public ResponseWrapper query(SolrDocRequest request) {
    throw new UnsupportedOperationException();
  }

  protected ParasMediator resolveValueGroupAggregation(NestedAggregationReq request)
      throws ParameterErrorException {
    assert request.getType() == AggregationType.VALUE_GROUP;

    ParasMediator paras = new ParasMediator();
    fillCommonParas(paras, request);
    paras.setType(request.getType());
    paras.setOuterLimit(request.getLimit());
    paras.setInnerLimit(-1);
    paras.setOuterField(request.getOuterField());
    paras.setOuterFieldCategory(request.getOuterFieldCategory());
    paras.setInnerField(request.getInnerField());
    paras.setInnerFieldCategory(request.getInnerFieldCategory());

    if (paras.getInnerFieldCategory() == SolrFieldCategory.DATE) {
      try {
        paras.setInnerStart(String.valueOf(
            parseDateString(request.getInnerStart() + ERROR_DISTANCE).getTime()));
        paras.setInnerEnd(String.valueOf(
            parseDateString(request.getInnerEnd() + ERROR_DISTANCE).getTime()));
//        paras.setInnerGap(String.valueOf(DateGapMapper.toMilliSeconds(request.getInnerGap())));
        //calculate gap
        Long milliSeconds = Long.valueOf(paras.getInnerEnd()) - Long.valueOf(paras.getInnerStart());
        paras.setInnerGap(String.valueOf(DateGapMapper
            .toMilliSeconds(DateGapMapper.toGranularity(milliSeconds))));
      } catch (ParseException | FormatErrorException e) {
        throw new ParameterErrorException("range field is wrong", e);
      }
    } else {
      paras.setInnerStart(request.getInnerStart());
      paras.setInnerEnd(request.getInnerEnd());
      paras.setInnerGap(request.getInnerGap());
    }
    return paras;
  }


  private void fillCommonParas(AggregationParas paras, AggregationRequest request)
      throws ParameterErrorException {
    paras.setCollection(request.getCollection());

    if (request.getQuery() == null) {
      paras.setQuery("*:*");
    } else {
      paras.setQuery(request.getQuery());
    }

    TimeRange timeRange = mappingToTimeRange(request.getTimeRange());
    if (request.getFilters() == null) {
      paras.setFilters("*:*" + " AND " + request.getAggregateFiled() + ":"
          + SolrDateUtil.solrTimeRangeString(timeRange));
    } else {
      paras.setFilters(FilterResolver.joinParameters(resolveFilters(request.getFilters()), "AND")
          + " AND " + request.getAggregateFiled() + ":" + SolrDateUtil
          .solrTimeRangeString(timeRange));
    }
  }

  private AggregationParas resolveDateAggregation(AggregationRequest request)
      throws ParameterErrorException {
    AggregationParas paras = new AggregationParas();
    fillCommonParas(paras, request);
    paras.setOuterField(request.getAggregateFiled());
    paras.setType(AggregationType.GROUP);
    paras.setOuterFieldCategory(SolrFieldCategory.DATE);
    paras.setOuterLimit(-1);

    TimeRange timeRange = mappingToTimeRange(request.getTimeRange());
    paras.setOuterStart(String.valueOf(timeRange.getStart().getTime()));
    paras.setOuterEnd(String.valueOf(timeRange.getEnd().getTime()));
    // calculate granularity;
    String granularity = null;
    Long gap;
    try {
      if (request.getGranularity() != null) {
        granularity = request.getGranularity();
        gap = DateGapMapper.toMilliSeconds(granularity);
      } else {
        granularity = mappingToGranularity(request.getTimeRange());
        if (granularity == null) {
          granularity = DateGapMapper.toGranularity(timeRange.getEnd().getTime()
              - timeRange.getStart().getTime());
        }
        gap = DateGapMapper.toMilliSeconds(granularity);
      }
    } catch (FormatErrorException e) {
      throw new ParameterErrorException("can't parse granularity:" + granularity, e);
    }

    paras.setOuterGap(gap.toString());
    return paras;
  }

  protected AggregationParas resolveGroupAggregation(FlatAggregationReq request)
      throws ParameterErrorException {
    assert request.getType() == AggregationType.GROUP;

    AggregationParas paras = new AggregationParas();
    fillCommonParas(paras, request);
    paras.setType(request.getType());
    paras.setOuterField(request.getOuterField());
    paras.setOuterFieldCategory(request.getOuterFieldCategory());
    paras.setOuterLimit(request.getLimit());

    if (request.getTagField() != null) {
      paras.setFilters(paras.getFilters() + " AND " + request.getTagField() +
          ":" + "*");
    }
    if (paras.getOuterFieldCategory() == SolrFieldCategory.DATE) {
      try {
        paras.setOuterStart(String.valueOf(
            parseDateString(request.getOuterStart() + ERROR_DISTANCE).getTime()));
        paras.setOuterEnd(String.valueOf(
            parseDateString(request.getOuterEnd() + ERROR_DISTANCE).getTime()));
        paras.setOuterGap(String.valueOf(DateGapMapper.toMilliSeconds(request.getOuterGap())));
      } catch (ParseException | FormatErrorException e) {
        throw new ParameterErrorException("range field is wrong", e);
      }
    } else {
      paras.setOuterStart(request.getOuterStart());
      paras.setOuterEnd(request.getOuterEnd());
      paras.setOuterGap(request.getOuterGap());
    }
    return paras;
  }

  protected AggregationParas resolveValueAggregationRequest(FlatAggregationReq request)
      throws ParameterErrorException {
    AggregationParas paras = new ParasMediator();
    fillCommonParas(paras, request);
    if (request.getType() != AggregationType.VALUE) {
      throw new ParameterErrorException("type must be 'VALUE'");
    }
    paras.setType(request.getType());
    paras.setOuterField(request.getOuterField());
    paras.setOuterFieldCategory(request.getOuterFieldCategory());
    paras.setOuterLimit(request.getLimit());
    return paras;
  }

  @Override
  public ResponseWrapper aggregateByValue(FlatAggregationReq request) {
    List<Count> result;
    try {
      result = customSolrRepository.aggregate(resolveValueAggregationRequest(request));
    } catch (ParameterErrorException | OutLimitException e) {
      return new MessageWrapper().wrapMessage(StatusCode.ERROR, e.getMessage(), toMap(request));
    }
    return new ResultWrapper().wrapResult(StatusCode.SUCCESS, result, toMap(request));
  }

  @Override
  public ResponseWrapper aggregateByDate(AggregationRequest request) {
    AggregationParas paras;
    List<Count> result;

    try {
      paras = resolveDateAggregation(request);
      result = customSolrRepository.aggregate(paras);
    } catch (ParameterErrorException | OutLimitException e) {
      log.error(
          "Trace: SolrSearchServiceImpl.aggregateByDate() | ParameterErrorException exception:  "
              + e.getMessage() + ";request: " + request);
      return new MessageWrapper().wrapMessage(StatusCode.INVALID, e.getMessage(),
          toMap(request));
    }
    return new ResultWrapper().wrapResult(StatusCode.SUCCESS, result, toMap(request));
  }

  @Override
  public ResponseWrapper aggregateByValueGroup(NestedAggregationReq request) {
    ParasMediator paras;
    List<Count> result;
    try {
      paras = resolveValueGroupAggregation(request);
      result = customSolrRepository.aggregate(paras);
    } catch (ParameterErrorException | OutLimitException e) {
      log.error(
          "Trace: SolrSearchServiceImpl.aggregateByDate() | ParameterErrorException exception:  "
              + e.getMessage() + ";request: " + request);
      return new MessageWrapper().wrapMessage(StatusCode.ERROR, e.getMessage(), toMap(request));
    }
    return new ResultWrapper().wrapResult(StatusCode.SUCCESS, result, toMap(request));
  }

  @Override
  public ResponseWrapper aggregateByGroup(FlatAggregationReq request) {
    List<Count> result;
    try {
      result = customSolrRepository.aggregate(resolveGroupAggregation(request));
    } catch (ParameterErrorException | OutLimitException e) {
      return new MessageWrapper().wrapMessage(StatusCode.ERROR, e.getMessage(), toMap(request));
    }
    return new ResultWrapper().wrapResult(StatusCode.SUCCESS, result, toMap(request));
  }
}
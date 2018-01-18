package com.hypers.search.service;

import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.utils.FilterResolver;
import com.hypers.search.utils.Resolver;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.search.model.request.AggregationRequest;
import com.hypers.search.model.request.SolrDocRequest;
import com.hypers.search.utils.date.TimeRange;
import com.hypers.search.utils.date.TimeTransfer;
import com.hypers.search.utils.date.SolrDateUtil;

/**
 * @author Ivan
 *
 */
public abstract class AbstractSolrSearchServiceSkeleton implements SolrSearchService {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(AbstractSolrSearchServiceSkeleton.class);

  protected TimeZone timeZone = TimeZone.getDefault();
  protected Locale locale = Locale.getDefault();
  protected final String ERROR_DISTANCE = ".001";
  protected final static int DEFAULT_PAGE = 1;
  protected final static int DEFAULT_PAGE_SIZE = 30;
  protected final static String RESPONSE_KEY_TOTAL = "total";

  private Resolver<String,List<String>> filterResolver=new FilterResolver();

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }

  protected void implementParameters(SolrDocRequest request) {

    if (request.getDirection() == null) {
      request.setDirection("asc");
    }

    if (request.getIgnoreCase() == null) {
      request.setIgnoreCase(true);
    }

    if (request.getPage() == null) {
      request.setPage(DEFAULT_PAGE);
    }

    if (request.getPageSize() == null) {
      request.setPageSize(DEFAULT_PAGE_SIZE);
    }

  }

  List<String> resolveFilters(String filters) throws ParameterErrorException{
    return filterResolver.resolve(filters);
  }

  protected String resolveTimeRange(String timeString) throws ParameterErrorException {
    TimeRange timeRange = mappingToTimeRange(timeString);

    if (timeRange.getStart() == null || timeRange.getEnd() == null) {
      throw new ParameterErrorException("can't resolve timeRange: " + timeString);
    }

    return formUtcTimeRangeString(timeRange);
  }

  private String formUtcTimeRangeString(TimeRange timeRange) {
    if (timeRange == null)
      return "[* TO *]";

    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(SolrDateUtil.formatAsUTC(timeZone, locale, timeRange.getStart()));
    sb.append(" TO ");
    sb.append(SolrDateUtil.formatAsUTC(timeZone, locale, timeRange.getEnd()));
    sb.append("]");

    SolrDateUtil.escapeColons(sb);
    return sb.toString();
  }

  protected TimeRange mappingToTimeRange(String timeString) throws ParameterErrorException{
    TimeRange timeRange = null;
    if (StringUtils.isBlank(timeString)) {
      timeString = "15 TO NOW min";
    }

    if (timeString.contains("TO NOW")) {
      int offset = Integer.valueOf(timeString.substring(0, timeString.indexOf(" ")));
      String unit = timeString.substring(timeString.lastIndexOf(" "), timeString.length());
      timeRange = TimeTransfer.generateTimeRange(new Date(), -offset, unit);
    } else if (timeString.contains("TO")) {
      timeRange = new TimeRange();
      String[] timeStrs = timeString.split(" TO ");
      try {
        timeRange.setStart(TimeTransfer.parseDateString(timeStrs[0] + ERROR_DISTANCE));
        timeRange.setEnd(TimeTransfer.parseDateString(timeStrs[1] + ERROR_DISTANCE));
      } catch (ParseException e) {
        throw new ParameterErrorException(e.getMessage(), e);
      }
    } else {
      timeRange = TimeTransfer.generateTimeRange(new Date(), timeString);
    }

    log.info(
        "Trace: AbstractSolrSearchServiceSkeleton.mappingToTimeRange; timeRange: " + timeRange);

    if (timeRange == null) {
      throw new ParameterErrorException("error in timeRange: " + timeRange);
    }
    return timeRange;
  }

  public abstract ResponseWrapper query(SolrDocRequest request);
}

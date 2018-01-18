package com.hypers.search.service;

import com.hypers.common.SimpleTransfer;
import com.hypers.common.StatusCode;
import com.hypers.common.wrapper.MessageWrapper;
import com.hypers.common.wrapper.ResponseWrapper;
import com.hypers.common.wrapper.ResultWrapper;
import com.hypers.search.utils.date.DateGapMapper;
import com.hypers.search.utils.date.TimeTransfer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author long
 * @since 17-11-22
 */
@Service
public class UtilityServiceImpl implements UtilityService {

  private static final String DATE_ERROR_DISTANCE = ".001";

  @Override
  public ResponseWrapper calculateGap(String start, String end) {
    Long startMilli, endMilli;
    try {
      startMilli = TimeTransfer.parseDateString(start + DATE_ERROR_DISTANCE).getTime() - 1;
      endMilli = TimeTransfer.parseDateString(end + DATE_ERROR_DISTANCE).getTime() - 1;
    } catch (ParseException e) {
      return new MessageWrapper().wrapMessage(StatusCode.ERROR, e.getMessage(),
          SimpleTransfer.toMap("start", start, "end", end));
    }
    List<String> result = new ArrayList<>(1);
    result.add(DateGapMapper.toGranularity(endMilli - startMilli));
    return new ResultWrapper().wrapResult(StatusCode.SUCCESS, result,
        SimpleTransfer.toMap("start", start, "end", end));
  }
}

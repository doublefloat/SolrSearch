package com.hypers.common.wrapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author long
 * @since 17-8-31
 */
public class ResultWrapper implements ResponseWrapper {
  private String code;
  private InnerWrapper result;

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }


  public void setResult(Map<String, Object> params, List<?> result) {
    this.setResult(params, result, null);
  }

  public void setResult(Map<String, Object> params, List<?> result, Map<String, Object> supplies) {
    if (this.result == null) {
      this.result = new InnerWrapper();
    }

    if (supplies != null && supplies.size() > 0) {
      this.result.setSupplies(supplies);
    }

    this.result.setParams(params);
    this.result.setItems(result);
  }

  public InnerWrapper getResult() {
    return result;
  }

  public ResultWrapper wrapResult(String code, List<?> results) {
    wrapResult(code, results, null);
    return this;
  }

  public ResultWrapper wrapResult(String code, List<?> results, Map<String, Object> params) {
    return this.wrapResult(code, results, params, null);
  }

  public ResultWrapper wrapResult(String code, List<?> results, Map<String, Object> params,
      Map<String, Object> supplies) {

    if (params == null) {
      params = Collections.emptyMap();
    }

    if (results == null) {
      results = Collections.emptyList();
    }

    setCode(code);
    setResult(params, results, supplies);

    return this;

  }

}

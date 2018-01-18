package com.hypers.common.wrapper;

import java.util.Collections;
import java.util.Map;

/**
 * @author long
 * @since 17-8-31
 */
public class MessageWrapper implements ResponseWrapper {

  private String code;
  private Map<String, Object> params;
  private String message;

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setParams(Map<String, Object> params) {
    this.params = params;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public MessageWrapper wrapMessage(String code, String message) {
    wrapMessage(code, message, null);
    return this;
  }

  public MessageWrapper wrapMessage(String code, String message, Map<String, Object> params) {
    setCode(code);
    setMessage(message);
    if (params == null) {
      setParams(Collections.emptyMap());
    } else {
      setParams(params);
    }
    return this;
  }

}
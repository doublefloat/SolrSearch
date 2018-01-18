package com.hypers.common.wrapper;

import java.util.List;
import java.util.Map;

/**
 * @author long
 * @since 2017/9/13
 */
public class InnerWrapper {

  Map<String, Object> params;
  Map<String, Object> supplies;
  List<?> items;

  public InnerWrapper() {}

  public InnerWrapper(Map<String, Object> params, List<?> items) {
    this.params = params;
    this.items = items;
  }

  public InnerWrapper(Map<String, Object> params, List<?> items, Map<String, Object> supplies) {
    this.params = params;
    this.items = items;
    this.supplies = supplies;
  }

  public void setItems(List<?> items) {
    this.items = items;
  }

  public List<?> getItems() {
    return items;
  }

  public void setParams(Map<String, Object> params) {
    this.params = params;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public Map<String, Object> getSupplies() {
    return supplies;
  }

  public void setSupplies(Map<String, Object> supplies) {
    this.supplies = supplies;
  }

}
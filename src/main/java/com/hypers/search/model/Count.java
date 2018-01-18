package com.hypers.search.model;

import java.util.List;

/**
 * @author Long
 * @since 2017/9/19
 */
public class Count {
  private String name;
  private Long count;
  List<Count> countList;

  public Count() {
  }

  public Count(String name, Long count) {
    this.name = name;
    this.count = count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public Long getCount() {
    return count;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setCountList(List<Count> countList) {
    this.countList = countList;
  }

  public List<Count> getCountList() {
    return countList;
  }

  @Override
  public String toString() {
    return "Count [name=" + name + ", count=" + count + "]";
  }

}

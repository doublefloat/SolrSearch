package com.hypers.search.utils.date;

import java.util.Date;

/**
 * @author long
 * @since 2017/9/15
 */
public class TimeRange {
  private Date start;
  private Date end;

  public TimeRange() {}

  public TimeRange(Date start, Date end) {
    this.start = start;
    this.end = end;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  @Override
  public String toString() {
    return "TimeRange [start=" + start + ", end=" + end + "]";
  }

}
